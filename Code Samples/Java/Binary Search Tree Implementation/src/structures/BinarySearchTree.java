package structures;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.naming.NameAlreadyBoundException;

public class BinarySearchTree<T extends Comparable<T>> implements
		BSTInterface<T> {
	protected BSTNode<T> root;

	public boolean isEmpty() {
		return root == null;
	}

	public int getSize() {
		int size = 0;
		Iterator i = inorderIterator();

		while(i.hasNext()) {
			size++;
			i.next();
		}

		return size;
	}

	public boolean contains(T t) throws NullPointerException{
		if (t == null) {
			throw new NullPointerException("Input is null!");
		}
		boolean inTree = containsHelper(t, root);
		return inTree;
	}

	private boolean containsHelper(T t, BSTNode<T> cur) {
		if (cur == null) {
			return false;
		}
		else if (cur.getData().compareTo(t) == 0) {
			return true;
		}
		else if (cur.getData().compareTo(t) < 0) {
			return containsHelper(t, cur.getRight());
		}
		else {
			return containsHelper(t, cur.getLeft());
		}
	}

	public boolean removeElement(T t) throws NullPointerException {
		if (t == null) {
			throw new NullPointerException("Cannot remove null element!");
		}
		BSTNode<T> par = null;
		BSTNode<T> cur = root;
		while(cur != null) {
			if (cur.getData().equals(t)) { // node to be removed is found
				if (cur.getRight() == null && cur.getLeft() == null) { // leaf node or root node with no children
					if (par == null) { // cur is root
						root = null;
					}
					else if (par.getLeft() != null && par.getLeft().equals(cur)) { // leaf node on left of parent
						par.setLeft(null);
					}
					else { // leaf node on right of parent
						par.setRight(null); 
					}
				}
				else if (cur.getRight() == null) { // node with only one child (left != null)
					if (par == null) { // root node
						root = cur.getLeft();
					}
					else if (par.getLeft().equals(cur)) { // parent's left node
						par.setLeft(cur.getLeft());
					}
					else { // parent's right node
						par.setRight(cur.getLeft());
					}
				}
				else if (cur.getLeft() == null) { // node with only one child (right != null)
					if (par == null) { // root node
						root = cur.getRight();
					}
					else if (par.getLeft().equals(cur)) { // parent's left node
						par.setLeft(cur.getRight());
					}
					else { // parent's right node
						par.setRight(cur.getRight());
					}
				}
				else { // node has two children (find successor first)
					BSTNode<T> succ = cur.getRight();
					while (succ.getLeft() != null) {
						succ = succ.getLeft();
					}
					T succData = succ.getData();
					removeElement(succData);
					cur.setData(succData);
				}
				return true;
			}
			else {
				if (cur.getData().compareTo(t) < 0){ // search right
					par = cur;
					cur = cur.getRight();
				}
				else { // search left
					par = cur;
					cur = cur.getLeft();
				}
			}
		}
		return false;
	}

	public T getHighestValueFromSubtree(BSTNode<T> node) {
		// node must not be null
		if (node.getRight() == null) {
			return node.getData();
		} else {
			return getHighestValueFromSubtree(node.getRight());
		}
	}

	public T getLowestValueFromSubtree(BSTNode<T> node) {
		if (node.getLeft() == null) {
			return node.getData();
		}
		else {
			return getLowestValueFromSubtree(node.getLeft());
		}
	}

	private BSTNode<T> removeRightmostFromSubtree(BSTNode<T> node){
		// node must not be null
		if (node.getRight() == null) {
			return node.getLeft();
		} else {
			node.setRight(removeRightmostFromSubtree(node.getRight()));
			if (node.getRight() != null){
				node.getRight().setParent(node);
			}
			return node;
		}
	}

	public BSTNode<T> removeLeftmostFromSubtree(BSTNode<T> node){
		if (node.getLeft() == null) {
			return node.getRight();
		}
		else {
			node.setLeft(removeLeftmostFromSubtree(node.getLeft()));
			if (node.getLeft() != null) {
				node.getLeft().setParent(node);
			}
			return node; 
		}
	}

	public T getElement(T t) throws NullPointerException {
		if (t == null) {
			throw new NullPointerException();
		}
		else {
			BSTNode<T> cur = root;
			while(cur != null) {
				if (cur.getData().equals(t)) {
					return cur.getData();
				}
				else if (cur.getData().compareTo(t) < 0) {
					cur = cur.getRight();
				}
				else {
					cur = cur.getLeft();
				}
			}
			return null;
		}
	}

	public void addElement(T t) throws NullPointerException {
		if (t == null) {
			throw new NullPointerException("Element added is null!");
		}
		BSTNode<T> nodeAdded = new BSTNode<T>(t, null, null);
		boolean added = false;
		if (root == null) {
			root = nodeAdded;
		}
		else {
			BSTNode<T> cur = root;
			while (added == false) {
				if (cur.getData().compareTo(t) < 0 && cur.getRight() == null) {
					cur.setRight(nodeAdded);
					added = true;
				}
				else if (cur.getData().compareTo(t) >= 0 && cur.getLeft() == null) {
					cur.setLeft(nodeAdded);
					added = true;
				}
				else if (cur.getData().compareTo(t) >= 0 && cur.getLeft() != null) {
					cur = cur.getLeft();
				}
				else if(cur.getData().compareTo(t) < 0 && cur.getRight() != null) {
					cur = cur.getRight();
				}
			}
		}
	}
	@Override
	public T getMin() {
		if (isEmpty() == true) {
			return null;
		}
		else {
			BSTNode<T> cur = root;
			while (cur != null) {
				if (cur.getLeft() == null) {
					return cur.getData();
				}
				else {
					cur = cur.getLeft();
				}
			}
			return null;
		}
	}


	@Override
	public T getMax() {
		if (isEmpty() == true) {
			return null;
		}
		else {
			BSTNode<T> cur = root;
			while (cur != null) {
				if (cur.getRight() == null) {
					return cur.getData();
				}
				else {
					cur = cur.getRight();
				}
			}
			return null;
		}
	}

	@Override
	public int height() { // iterative solution using level-order traversal like that used in project 5
		if (isEmpty()) {
			return -1;
		}
		else {
			Queue<BSTNode<T>> nodesToVisit = new LinkedList<BSTNode<T>>();
			Queue<BSTNode<T>> childrenToVisit = new LinkedList<BSTNode<T>>();
			int height = 0;
			nodesToVisit.add(root);
			while (nodesToVisit.isEmpty() == false) {
				if (nodesToVisit.peek().getLeft() != null) {
					childrenToVisit.add(nodesToVisit.peek().getLeft());
				}
				if (nodesToVisit.peek().getRight() != null) {
					childrenToVisit.add(nodesToVisit.peek().getRight());
				}
				nodesToVisit.remove();
				if(nodesToVisit.isEmpty() && childrenToVisit.isEmpty() == false) {
					while(childrenToVisit.isEmpty() == false) {
						nodesToVisit.add(childrenToVisit.peek());
						childrenToVisit.remove();
					}
					height++;
				}
			}
			return height;
		}
	}

	public Iterator<T> preorderIterator() {
		Queue<T> queue = new LinkedList<T>();
		preorderTraverse(queue, root);
		return queue.iterator();
	}

	private void preorderTraverse(Queue<T> queue, BSTNode<T> node) {
		if (node != null) {
			queue.add(node.getData());
			preorderTraverse(queue, node.getLeft());
			preorderTraverse(queue, node.getRight());
		}
	}


	public Iterator<T> inorderIterator() {
		Queue<T> queue = new LinkedList<T>();
		inorderTraverse(queue, root);
		return queue.iterator();
	}

	private void inorderTraverse(Queue<T> queue, BSTNode<T> node) {
		if (node != null) {
			inorderTraverse(queue, node.getLeft());
			queue.add(node.getData());
			inorderTraverse(queue, node.getRight());
		}
	}

	public Iterator<T> postorderIterator() {
		Queue<T> queue = new LinkedList<T>();
		postorderTraverse(queue, root);
		return queue.iterator();
	}

	private void postorderTraverse(Queue<T> queue, BSTNode<T> node) {
		if (node != null) {
			postorderTraverse(queue, node.getLeft());
			postorderTraverse(queue, node.getRight());
			queue.add(node.getData());
		}
	}

	@Override
	public boolean equals(BSTInterface<T> other) throws NullPointerException {
		if (other == null) {
			throw new NullPointerException();
		}

		if (other.isEmpty() && isEmpty()) {
			return true;
		}
		else {
			BSTNode<T> otherRoot = other.getRoot();	
			return equalsHelper(root, otherRoot);
		}
	}

	private boolean equalsHelper(BSTNode<T> thisCur, BSTNode<T> otherCur) {
		if (thisCur == null && otherCur == null) {
			return true;
		}
		else {
			if (thisCur != null && otherCur != null) {
				if (thisCur.getData().equals(otherCur.getData())) {
					if ((equalsHelper(thisCur.getLeft(), otherCur.getLeft())) && (equalsHelper(thisCur.getRight(), otherCur.getRight()))) {
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
	}

	@Override
	public boolean sameValues(BSTInterface<T> other) throws NullPointerException {
		if (other == null) {
			throw new NullPointerException();
		}
		Iterator<T> thisIterator = inorderIterator();
		Iterator<T> otherIterator = other.inorderIterator();

		while(thisIterator.hasNext() && otherIterator.hasNext()) {
			if (thisIterator.next().compareTo(otherIterator.next()) != 0) {
				return false;
			}
		}
		if (thisIterator.hasNext()) {
			return false;
		}
		if (otherIterator.hasNext()) {
			return false;
		}

		return true;
	}
	
	@Override
	public int countRange(T min, T max) {
		Iterator<T> iterator = inorderIterator();
		int count = 0;
		while (iterator.hasNext()) {
			T curValue = iterator.next();
			if (curValue.compareTo(min) > 0 && curValue.compareTo(max) < 0) {
				count++;
			}
		}
		return count;
  }


	@Override
	public BSTNode<T> getRoot() {
		return root;
	}

	public static <T extends Comparable<T>> String toDotFormat(BSTNode<T> root) {
		// header
		int count = 0;
		String dot = "digraph G { \n";
		dot += "graph [ordering=\"out\"]; \n";
		// iterative traversal
		Queue<BSTNode<T>> queue = new LinkedList<BSTNode<T>>();
		queue.add(root);
		BSTNode<T> cursor;
		while (!queue.isEmpty()) {
			cursor = queue.remove();
			if (cursor.getLeft() != null) {
				// add edge from cursor to left child
				dot += cursor.getData().toString() + " -> "
						+ cursor.getLeft().getData().toString() + ";\n";
				queue.add(cursor.getLeft());
			} else {
				// add dummy node
				dot += "node" + count + " [shape=point];\n";
				dot += cursor.getData().toString() + " -> " + "node" + count
						+ ";\n";
				count++;
			}
			if (cursor.getRight() != null) {
				// add edge from cursor to right child
				dot += cursor.getData().toString() + " -> "
						+ cursor.getRight().getData().toString() + ";\n";
				queue.add(cursor.getRight());
			} else {
				// add dummy node
				dot += "node" + count + " [shape=point];\n";
				dot += cursor.getData().toString() + " -> " + "node" + count
						+ ";\n";
				count++;
			}

		}
		dot += "};";
		return dot;
	}

	public static void main(String[] args) {
		for (String r : new String[] { "a", "b", "c", "d", "e", "f", "g" }) {
			BSTInterface<String> tree = new BinarySearchTree<String>();
			for (String s : new String[] { "d", "b", "a", "c", "f", "e", "g" }) {
				tree.addElement(s);
			}
			Iterator<String> iterator = tree.inorderIterator();
			while (iterator.hasNext()) {
				System.out.print(iterator.next());
			}
			System.out.println();
			iterator = tree.preorderIterator();
			while (iterator.hasNext()) {
				System.out.print(iterator.next());
			}
			System.out.println();
			iterator = tree.postorderIterator();
			while (iterator.hasNext()) {
				System.out.print(iterator.next());
			}
			System.out.println();

			System.out.println(tree.removeElement(r));

			iterator = tree.inorderIterator();
			while (iterator.hasNext()) {
				System.out.print(iterator.next());
			}
			System.out.println();
		}

		BSTInterface<String> tree = new BinarySearchTree<String>();
		for (String r : new String[] { "a", "b", "c", "d", "e", "f", "g" }) {
			tree.addElement(r);
		}
		System.out.println(tree.getSize());
		System.out.println(tree.height());
		System.out.println(tree.countRange("a", "g"));
		System.out.println(tree.countRange("c", "f"));
	}
}