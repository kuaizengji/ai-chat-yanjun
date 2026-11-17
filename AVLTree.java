/*
 * Name: AVLTree.java
 * Author: Li Yanjun
 * Version: 1.0
 * Copyright: Copyright (c) 2026 Li Yanjun. All rights reserved.
 * Description: AVL tree that rebalances a binary search tree after every insertion and deletion.
 */
public class AVLTree extends BST{
	Node root;

	// Build an empty AVL tree through the binary search tree constructor.
	public AVLTree() {
		super();
	}

    // Return the balance factor, computed as the left subtree height minus the right subtree height.
    private int getBalance(Node node) {
        return node == null ? 0 : Node.getHeight(node.getLeft()) - Node.getHeight(node.getRight());
    }
	
	// Rotate this subtree to the right and return the node that becomes the new subtree root.
	private Node rotateRight(Node y) {
        // Keep the left child and the subtree that must move during the rotation.
        Node x = y.getLeft();
        Node T2 = x.getRight();

        // Promote the left child and hang the saved subtree on the old root.
        x.setRight(y);
        y.setLeft(T2);

        // Update heights from the lower node upward after the links change.
        y.updateHeight();
        x.updateHeight();

        return x;
    }
	
	// Rotate this subtree to the left and return the node that becomes the new subtree root.
	private Node rotateLeft(Node x) {
        // Keep the right child and the subtree that must move during the rotation.
        Node y = x.getRight();
        Node T2 = y.getLeft();

        // Promote the right child and hang the saved subtree on the old root.
        y.setLeft(x);
        x.setRight(T2);

        // Update heights from the lower node upward after the links change.
        x.updateHeight();
        y.updateHeight();

        return y;
    }
	
	// Restore AVL balance at this node by applying the matching single or double rotation.
	private Node rebalance(Node root) {
		int balance = getBalance(root);
		
		// A balance factor between minus one and one already satisfies the AVL rule.
		if (-2 < balance && balance < 2) {
			return root;
		}
		
		// Choose a rotation from the sign of this node and the sign of its heavy child.
		if (balance > 1 && getBalance(root.getLeft()) >= 0) { // Left child is heavy and itself leans left, so one right rotation is enough.
            return rotateRight(root);
        }
        if (balance > 1 && getBalance(root.getLeft()) < 0) { // Left child is heavy but leans right, so rotate that child left before rotating right.
            root.setLeft(rotateLeft(root.getLeft()));
            root = rotateRight(root);
        }
        if (balance < -1 && getBalance(root.getRight()) <= 0) { // Right child is heavy and itself leans right, so one left rotation is enough.
            return rotateLeft(root);
        }
        if (balance < -1 && getBalance(root.getRight()) > 0) { // Right child is heavy but leans left, so rotate that child right before rotating left.
            root.setRight(rotateRight(root.getRight()));
            root = rotateLeft(root);
        }
		
		return root;
	}

	public Node insertNode(Node root, int x) {
		/**
		 * insertNode inserts a key "x" into a subtree whose root is "root" 
		 * input:	root - the root of the subtree
		 * 			x - the key to be inserted
		 * output: the (updated) root of the subtree
		 */
		root = super.insertNode(root, x);
		
		// Refresh this node height because the insertion may have made a subtree taller.
		root.updateHeight();
		
		// Repair any AVL violation created on the path back to the subtree root.
		root = rebalance(root);
		
		return root;
	}

	public Node deleteNode(Node root, int x) {
		/**
		 * deleteNode deletes the node whose key is "x" from a subtree whose root is "root"
		 * input: 	root - the root of the subtree 
		 * 			x - the key of the node to be deleted
		 * output: the (updated) root of the subtree
		 */
		root = super.deleteNode(root, x);
		
		// A missing node needs no repair; otherwise refresh its height and rebalance it.
		if (root != null) {
			root.updateHeight();
			root = rebalance(root);
		}
		
		return root;
	}

	
	public static void main(String[] args) {
		// Demonstrate insertion, search, and deletion on one AVL tree and print each shape.
		AVLTree avl = new AVLTree();
		System.out.println("The initial tree is empty:");
		BinaryTreePrinter.printTree(avl);

		// Insert the first two keys before any rotation is required.
		System.out.println("After inserting 10, 5:");
		avl.insertNode(10);
		avl.insertNode(5);
		BinaryTreePrinter.printTree(avl);

		// Insert a key that makes the left side too heavy and triggers a right rotation.
		System.out.println("\nAfter inserting 3: (case 1)");
		avl.insertNode(3);
		BinaryTreePrinter.printTree(avl);

		System.out.println("\nAfter inserting 13, 18: (case 4)");
		avl.insertNode(13);
		avl.insertNode(18);
		BinaryTreePrinter.printTree(avl);

		System.out.println("\nAfter inserting 12: (case 3)");
		avl.insertNode(12);
		BinaryTreePrinter.printTree(avl);
		System.out.println("\nAfter inserting 4: (case 2)");
		avl.insertNode(4);
		BinaryTreePrinter.printTree(avl);
		
		System.out.println("\nAfter inserting 11, 22, 16:");
		avl.insertNode(11);
		avl.insertNode(22);
		avl.insertNode(16);
		BinaryTreePrinter.printTree(avl);
		
		System.out.println("\nAfter inserting 20: (case 4)");
		avl.insertNode(20);
		BinaryTreePrinter.printTree(avl);

		System.out.println("\nAfter inserting 8:");
		avl.insertNode(8);
		BinaryTreePrinter.printTree(avl);

		// Confirm that stored keys are found and that a missing key is reported as absent.
		System.out.println("\nSearching for 10, 5, 13, 18, 21:");
		System.out.println(avl.findNode(10) != null && avl.findNode(10).getKey() == 10 ? "true" : "false");
		System.out.println(avl.findNode(5) != null && avl.findNode(5).getKey() == 5 ? "true" : "false");
		System.out.println(avl.findNode(13) != null && avl.findNode(13).getKey() == 13 ? "true" : "false");
		System.out.println(avl.findNode(18) != null && avl.findNode(18).getKey() == 18 ? "true" : "false");
		System.out.println(avl.findNode(21) == null ? "true" : "false");

		// Delete keys that force the tree to rebalance after the node is removed.
		System.out.println("\nAfter deleting 16 (case 3, case 1):");
		avl.deleteNode(16);
		BinaryTreePrinter.printTree(avl);
		
		System.out.println("\nAfter deleting 3 (case 4):");
		avl.deleteNode(3);
		BinaryTreePrinter.printTree(avl);
		
		System.out.println("\nAfter deleting 5, 8 (case 4):");
		avl.deleteNode(5);
		avl.deleteNode(8);
		BinaryTreePrinter.printTree(avl);
		
		System.out.println("\nAfter deleting 20, 18 (case 2):");
		avl.deleteNode(20);
		avl.deleteNode(18);
		BinaryTreePrinter.printTree(avl);
	}
}
