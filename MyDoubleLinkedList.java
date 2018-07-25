package project4;

import java.io.Serializable;

public class MyDoubleLinkedList<T> implements Serializable, Cloneable {

	/** Saves a DVD object as a binary file */
	private static final long serialVersionUID = 1L;

	private DNode<T> top;
	private DNode<T> tail; 

	public MyDoubleLinkedList () {
		top = null;
		tail = null;
	}

	public int getSize() {

		int size = 0;

		DNode<T> currentNode = top;

		while (currentNode != null) {
			size++;
			currentNode = currentNode.getNextNode();
		}

		return size;
	}

	/******************************************************************
	 * Remove all items
	 *****************************************************************/
	public void clear() {
		top = null;
		tail = null;
	}

	/******************************************************************
	 * Add at the top
	 * @param t
	 *****************************************************************/
	public void addFirst (T t) {

		// If list is empty, new node is head
		if (top == null) {
			top = new DNode<T> (t);
			tail = null;
		}
		else if (tail == null) {
			tail = new DNode<T> (t);
			top.setNextNode(tail);
			tail.setPreviousNode(top);
		}
		else {
			DNode<T> previousHead = top;
			DNode<T> newHead = new DNode<T> (t);

			newHead.setNextNode(previousHead);
			previousHead.setPreviousNode(newHead);

			top = newHead;
		}
	}

	/******************************************************************
	 * Add at the tail
	 * @param t
	 *****************************************************************/
	public void add(T t) {

		if (top == null) {
			DNode<T> insertNode = new DNode<T>(t);
			top = insertNode;
			tail = insertNode;
		}
		else {
			DNode<T> previousTail = tail;
			DNode<T> newTail = new DNode<T> (t);

			newTail.setPreviousNode(previousTail);
			previousTail.setNextNode(newTail);
			tail = newTail;
		}
//		System.out.println(toString());
	}

	/******************************************************************
	 * Removes data at a specific index
	 * @param index
	 * @return
	 * @throws
	 *****************************************************************/
	public T remove (int index) {

		// Removing from the front
		if (index == 0 && top!= null) {

			DNode<T> nextNode = top.getNextNode();

			if (nextNode != null) {
				nextNode.setPreviousNode(null);
			}
			DNode<T> previousTop = top;

			top = null;
			top = nextNode;

			return previousTop.getData();
		}

		// removing from the back
		if (index == getSize() - 1) {

			DNode<T> previousTail = tail;

			if (top != null) {
				if (tail != null) {
					DNode<T> newTail = tail.getPreviousNode();
					tail = null;
					newTail.setNextNode(null);
					tail = newTail;
				}
				else {
					top = null;
				}
			}
			return previousTail.getData();
		}

		// Removing from the middle
		if (index < getSize()) {
			DNode<T> nodeToRemove = top;
			for (int i = 0; i < index; i++) 
				nodeToRemove = nodeToRemove.getNextNode();

			DNode<T> prevNode = nodeToRemove.getPreviousNode();
			DNode<T> nextNode = nodeToRemove.getNextNode();

			// head node does not have a previous node
			if (prevNode == null) {
				top = null;
				top = nextNode;
				top.setPreviousNode(null);
				return nodeToRemove.getData();
			}

			// Tail does not have a next node
			else if (nextNode == null) {
				tail = null;
				tail = prevNode;
				tail.setNextNode(null);
				return nodeToRemove.getData();
			}

			// Somewhere in the middle of the linked list
			else {
				DNode<T> temp = nodeToRemove;

				nodeToRemove = null;
				prevNode.setNextNode(nextNode);
				nextNode.setPreviousNode(prevNode);

				return temp.getData();
			} 
		} 

		throw new RuntimeException();
	}

	/******************************************************************
	 * return true if at least one item removed
	 * @param t
	 * @return
	 *****************************************************************/
	public boolean removeAll (T t) {

		DNode<T> currentNode = top;

		int index;

		int beforeSize = getSize();

		while (currentNode != null) {
			T currentData = currentNode.getData();
			index = 0;

			if (currentData.equals(t)) {
				remove(index);
			}
			currentNode = currentNode.getNextNode();
			index++;
		}

		if (beforeSize > getSize())
			return true; 

		return false;
	}

	/******************************************************************
	 * Gets the node at the specified index
	 * @param index of the double linked list
	 * @return T - object stored within the node
	 * @throws IllegalArgumentException when index is less than 0
	 * @throws IllegalArgumentException when index is greater than 
	 * list size
	 *****************************************************************/
	public T get(int index) {

		if (index < 0)
			throw new IllegalArgumentException("Enter index" +
					" greater than 0");
		if (index > getSize() - 1)
			throw new IllegalArgumentException("Enter index" +
					" smaller than list size");

		if (index == 0)
			return top.getData();

		else {

			DNode<T> getNode = top;

			for (int i = 0; i < index; i++)
				getNode = getNode.getNextNode();

			return getNode.getData();
		}
	}

	/******************************************************************
	 * return index if found, -1 otherwise
	 * @param t - the type of object that is being searched for
	 * @return integer of the index the object is found or -1
	 *****************************************************************/
	public int find(T t) throws Exception {

		DNode<T> findNode = top;

		// Checks if head is the object being searched for
		if (findNode.getData().equals(t))
			return 0;
		
		// Checks if tail is the object being searched for
		else if (tail.equals(t))
			return getSize() - 1;
		
		// Checks if body has the object being searched for
		else if (!findNode.getData().equals(t)){
			
			int searching = 1;
			
			for (int i = 0; i < getSize() - 1; i++) {
				findNode = findNode.getNextNode();
				searching++;
				if (findNode.equals(t))
					return searching; 
			}
		}
		else
			return -1;
		
		throw new Exception("How did you get here");
	}
	
	public String toString() {
		
		// Show the linked list forward
		String string = "Forward: ";
		DNode<T> currentNode = top;
		while (currentNode != null){
			string += currentNode.toString() + " ";
			currentNode = currentNode.getNextNode();
		}
		
		// Show hte linked list backward
		string += "\nBackward: ";
		currentNode = tail;
		while (currentNode != null) {
			string += currentNode.toString() + " ";
			currentNode = currentNode.getPreviousNode();
		}
		
		return string;
		
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {

	    return super.clone();
	}
}
