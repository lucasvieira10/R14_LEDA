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

	protected int calculateBalance(BSTNode<T> node) {
		if (node == null || node.isEmpty())
			return -1;

		int heightLeftNode = height((BSTNode<T>) node.getLeft());
		int heightRightNode = height((BSTNode<T>) node.getRight());

		return Math.abs(heightLeftNode - heightRightNode);
	}

	protected void rebalance(BSTNode<T> node) {
		if (node == null || node.isEmpty())
			return;

		BSTNode<T> rotatedNode = rotation(node);
		
		if (rotatedNode.getParent() == null) {
			root = rotatedNode;
		}
	}

	protected void rebalanceUp(BSTNode<T> node) {
		if (node == null)
			return;
		
		if (calculateBalance(node) > 1) {
			rebalance(node);
		}
		
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

			rebalanceUp(node);
		} else {
			if (element.compareTo(node.getData()) > 0) {
				insert(element, (BSTNode<T>) node.getRight());
			} else {
				insert(element, (BSTNode<T>) node.getLeft());
			}
		}
	}

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
		
		rebalanceUp(aux);
	}

	private void removeLeaf(BSTNode<T> node) {
		node.setData(null);
		rebalanceUp(node);
	}

	private BSTNode<T> rotation(BSTNode<T> node) {
		int heightLeftNode = height((BSTNode<T>) node.getLeft());
		int heightRightNode = height((BSTNode<T>) node.getRight());

		int diference = heightLeftNode - heightRightNode;

		if (diference > 0) {
			if (node.getLeft().getLeft().isEmpty()) {
				Util.leftRotation((BSTNode<T>) node.getLeft());
			}
			
			return Util.rightRotation(node);
		} else {
			if (node.getRight().getRight().isEmpty()) {
				Util.rightRotation((BSTNode<T>) node.getRight());
			}
			
			return Util.leftRotation(node);
		}
	}
}
