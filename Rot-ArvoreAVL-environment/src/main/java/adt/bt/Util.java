package adt.bt;

import adt.bst.BSTNode;

public class Util {

	/**
	 * A rotacao a esquerda em node deve subir o seu filho a direita e retorna-lo em seguida
	 * @param node
	 * @return
	 */
	public static <T extends Comparable<T>> BSTNode<T> leftRotation(BSTNode<T> node) {
		BSTNode<T> rightNode = (BSTNode<T>) node.getRight();
		rightNode.setParent(node.getParent());
		
		BSTNode<T> rightsParent = (BSTNode<T>) rightNode.getParent();
		
		if (rightsParent != null) {
			if (rightsParent.getLeft().equals(node)) {
				rightsParent.setLeft(rightNode);
			} else {
				rightsParent.setRight(rightNode);
			}
		}
		
		node.setRight(rightNode.getLeft());
		rightNode.getLeft().setParent(node);
		rightNode.setLeft(node);
		node.setParent(rightNode);
		
		return rightNode;
	}

	/**
	 * A rotacao a direita em node deve subir seu filho a esquerda s retorna-lo em seguida
	 * @param node
	 * @return
	 */
	public static <T extends Comparable<T>> BSTNode<T> rightRotation(BSTNode<T> node) {
		BSTNode<T> leftNode = (BSTNode<T>) node.getLeft();
		leftNode.setParent(node.getParent());

		BSTNode<T> leftsParent = (BSTNode<T>) leftNode.getParent();

		if (leftsParent != null) {
			if (leftsParent.getLeft().equals(node)) {
				leftsParent.setLeft(leftNode);
			} else {
				leftsParent.setRight(leftNode);
			}
		}
		
		node.setLeft(leftNode.getRight());
		leftNode.getRight().setParent(node);
		leftNode.setRight(node);
		node.setParent(leftNode);
		
		return leftNode;
	}
}
