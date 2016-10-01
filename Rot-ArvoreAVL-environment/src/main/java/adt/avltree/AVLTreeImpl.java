package adt.avltree;

import adt.bst.BSTImpl;
import adt.bst.BSTNode;
import adt.bt.Util;

/**
 * 
 * Performs consistency validations within a AVL Tree instance
 * 
 * @author Claudio Campelo
 *
 * @param <T>
 */
public class AVLTreeImpl<T extends Comparable<T>> extends BSTImpl<T> implements AVLTree<T> {

	private static final int MAXIMUM_BALANCE = 1;
	private static final int IS_UNBALANCED_LEFT = 1;
	private static final int CHILD_IS_LEFT_PENDING = 1;
	private static final int CHILD_IS_RIGHT_PENDING = -1;
	
	protected int calculateBalance(BSTNode<T> node) {
		if ((node == null) || (node.isEmpty()))
			return -1;

		int heightDifference = calculateHeightDifference((BSTNode<T>) node.getLeft(),
				(BSTNode<T>) node.getRight());
		
		return Math.abs(heightDifference);
	}

	protected void rebalance(BSTNode<T> node) {
		if ((node == null) || (node.isEmpty()))
			return;

		// chama o rotation pra ajustar a arvore
		BSTNode<T> rotatedNode = rotation(node);
		
		if (rotatedNode.getParent() == null) {
			root = rotatedNode;
		}
	}

	protected void rebalanceUp(BSTNode<T> node) {
		if (node == null)
			return;
		
		if (calculateBalance(node) > MAXIMUM_BALANCE) {
			rebalance(node);
		}
		
		// vai chamando recursivamente para o pai do no para ir balanceando
		rebalanceUp((BSTNode<T>) node.getParent());
	}

	protected void leftRotation(BSTNode<T> node) {
		Util.leftRotation(node);
	}

	protected void rightRotation(BSTNode<T> node) {
		Util.rightRotation(node);
	}

	@Override
	public void insert(T element) {
		if (element == null)
			return;

		if (isEmpty()) {
			BSTNode<T> leftNil = new BSTNode<>();
			BSTNode<T> rightNil = new BSTNode<>();

			root.setData(element);

			root.setLeft(leftNil);
			leftNil.setParent(root);

			root.setRight(rightNil);
			rightNil.setParent(root);

		} else {
			insert(element, root);
		}
	}

	private void insert(T element, BSTNode<T> node) {
		if (node.isEmpty()) {
			BSTNode<T> leftNil = new BSTNode<>();
			BSTNode<T> rightNil = new BSTNode<>();

			node.setData(element);

			node.setLeft(leftNil);
			leftNil.setParent(node);

			node.setRight(rightNil);
			rightNil.setParent(node);

			// como o no inserido pode ter desbalanceado a arvore
			// chamamos o rebalanceUp para ir rotacionando a arvore
			// para menter balanceada
			rebalanceUp(node);
		} else {
			if (element.compareTo(node.getData()) > 0) {
				insert(element, (BSTNode<T>) node.getRight());
			} else {
				insert(element, (BSTNode<T>) node.getLeft());
			}
		}
	}

	// mesmo remove da BST so que sobrescrito para usar o rebalance
	@Override
	public void remove(T element) {
		if (element == null)
			return;

		BSTNode<T> node = search(element);

		if (node.isEmpty())
			return;

		remove(node);
	}

	private void remove(BSTNode<T> node) {
		if (node.isEmpty())
			return;

		switch (getDegreeNode(node)) {
			case ZERO_DEGREE:
				removeLeaf(node);
				break;
			case ONE_DEGREE:
				removeNodeOneDegree(node);
				break;
			case TWO_DEGREE:
				removeNodeTwoDegree(node);
				break;
			default:
				break;
		}
	}

	private void removeNodeTwoDegree(BSTNode<T> node) {
		BSTNode<T> sucessor = sucessor(node.getData());

		T data = node.getData();

		node.setData(sucessor.getData());
		sucessor.setData(data);

		remove(sucessor);
	}

	private void removeNodeOneDegree(BSTNode<T> node) {
		BSTNode<T> aux = null;

		if (!(node.getLeft().isEmpty())) {
			aux = (BSTNode<T>) node.getLeft();
		} else {
			aux = (BSTNode<T>) node.getRight();
		}

		/* if node is the root */
		if (node.getParent() == null) {
			aux.setParent(null);
			root = aux;

		/* if node not is the root */
		} else {
			aux.setParent(node.getParent());

			if (node.getParent().getLeft().equals(node)) {
				node.getParent().setLeft(aux);
			} else {
				node.getParent().setRight(aux);
			}
		}
		
		// chama o rebalance aqui pois a remocao de um no 
		// pode desbalacear a arvore
		rebalanceUp(aux);
	}

	private void removeLeaf(BSTNode<T> node) {
		node.setData(null);
		
		// chama o rebalance aqui pois a remocao de um no 
		// pode desbalacear a arvore
		rebalanceUp(node);
	}

	/**
	 * This auxiliary method calculate and return the height difference 
	 * but does not use the module.  
	 * 
	 * @param leftNode
	 * @param rightNode
	 * @return height
	 */
	private int calculateHeightDifference(BSTNode<T> leftNode, BSTNode<T> rightNode) {
		int heightLeftNode = height(leftNode);
		int heightRightNode = height(rightNode);
		
		// se o retorno for positivo a altura a esquerda ta sendo maior e vice versa		
		return (heightLeftNode - heightRightNode);
	}
	
	/**
	 * This auxiliary method apply the rotations in the nodes.
	 * 
	 * @param node
	 * @return node
	 */
	private BSTNode<T> rotation(BSTNode<T> node) {
		int heightDifference = calculateHeightDifference((BSTNode<T>) node.getLeft(),
				(BSTNode<T>) node.getRight());

		// se o no estiver desbalanceado para a esquerda (mais nos a esquerda)
		if (heightDifference >= IS_UNBALANCED_LEFT) {
			int differenceChildren = calculateHeightDifference((BSTNode<T>) node.getLeft().getLeft(),
					(BSTNode<T>) node.getLeft().getRight());
			
			// se o filho do no passado estiver pendendo para direita
			// (se colocar mais um no a direita desse filho ele fica desbalanceado)
			if (differenceChildren <= CHILD_IS_RIGHT_PENDING) { /* zigue-zague */
				Util.leftRotation((BSTNode<T>) node.getLeft());
				return Util.rightRotation(node);
			} else {
				return Util.rightRotation(node);
			}
			
		// se o no estiver desbalanceado para a direita (mais nos a direita)
		} else { /* IS_UNBALANCED_RIGHT */
			int differenceChildren = calculateHeightDifference((BSTNode<T>) node.getRight().getLeft(),
					(BSTNode<T>) node.getRight().getRight());
			
			// se o filho do no passado estiver pendendo para esquerda
			// (se colocar mais um no a esquerda desse filho ele fica desbalanceado)
			if (differenceChildren >= CHILD_IS_LEFT_PENDING) { /* zigue-zague */
				Util.rightRotation((BSTNode<T>) node.getRight());
				return Util.leftRotation(node);
			} else {
				return Util.leftRotation(node);
			}
		}
	}
}
