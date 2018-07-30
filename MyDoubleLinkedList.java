package project4;

import java.io.Serializable;

import project4.DNode;

/**********************************************************************
 * Creates a double linked list of generic T DNodes
 * 
 * @author Jarod Collier and Ben Burger
 * @version 7/16/18
 *********************************************************************/
public class MyDoubleLinkedList<T> implements Serializable {

	/** Saves a DVD object as a binary file */
	private static final long serialVersionUID = 1L;

	/** DNode for the top of the list */
	private DNode<T> top;

	/** DNode for the bottom of the list */
	private DNode<T> tail; 

	/******************************************************************
	 * Constructor that initializes the top and tail nodes to null
	 *****************************************************************/
	public MyDoubleLinkedList () {
		top = null;
		tail = null;
	}

	/******************************************************************
	 * Gets the current size of the double linked list
	 * @return integer of the size of the list
	 *****************************************************************/
	public int getSize() {

		int size = 0;

		DNode<T> currentNode = top;

		// loop to go through entire list until the next node is null
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
	 * Adds a new object, t, at the top of the double linked list
	 * @param t - generic object that the user wants to be added
	 *****************************************************************/
	public void addFirst (T t) {

		// If list is empty, new node is head
		if (top == null) {
			top = new DNode<T> (t);
			tail = null;
		}

		// if tail is empty, new node is tail
		else if (tail == null) {
			tail = new DNode<T> (t);
			top.setNextNode(tail);
			tail.setPreviousNode(top);
		}

		// Otherwise, new node is the top
		else {
			DNode<T> previousHead = top;
			DNode<T> newHead = new DNode<T> (t);

			newHead.setNextNode(previousHead);
			previousHead.setPreviousNode(newHead);

			top = newHead;
		}
	}

	/******************************************************************
	 * Adds a new object, t, at the tail of the double linked list
	 * @param t - generic object that the user wants to be added
	 *****************************************************************/
	public void add(T t) {

		// if no list, new node is first element
		if (top == null) {
			DNode<T> insertNode = new DNode<T>(t);
			top = insertNode;
			tail = insertNode;
		}

		// otherwise new node goes after the last node
		else {
			DNode<T> previousTail = tail;
			DNode<T> newTail = new DNode<T> (t);

			newTail.setPreviousNode(previousTail);
			previousTail.setNextNode(newTail);
			tail = newTail;
		}
	}

	/******************************************************************
	 * Adds a new node before the given index with data
	 * @param index - the index before the new node will be placed
	 * @param data - the data to fill the node
	 * @throws IllegalArgumentException when parameters break method
	 *****************************************************************/
	public void addBefore(int index, T data) {
		// case 0 not list and index = 0
		if (top == null && index == 0) {
			DNode<T> temp = new DNode<T> (data, null, null);
			top = temp;
			tail = temp;
			return;
		}

		// case 1 no list and index != 0
		if (top == null && index != 0) {
			throw new IllegalArgumentException();
		}

		// case 2 the list exists and index == 0 
		if (index == 0) {
			DNode<T> temp = new DNode<T> (data, null, top.getNextNode());
			top = temp;
			return;
		}

		// case 3 the list exist and index is inbounds
		if (index > 0 && index < getSize()) {
			DNode<T> extra = top; 
			for (int i = 0; i < index-1; i++) {
				extra = extra.getNextNode(); 
			}
			DNode<T> temp = new DNode<T> (data, extra, extra.getNextNode());
			extra.getNextNode().setPreviousNode(temp);
			extra.setNextNode(temp);
			return;	
		}

		throw new IllegalArgumentException();
	}

	/******************************************************************
	 * Removes data at a specific index
	 * @param index of the list that the user chooses
	 * @return Generic object T that is removed
	 * @throws RuntimeException when parameters break method
	 *****************************************************************/
	public T remove (int index) {

		removeFromFront(index);

		removeFromBack(index);

		removeFromMiddle(index); 

		return null;
	}

	/******************************************************************
	 * Removes the DNode at the specific index in the middle of the 
	 * list and returns the generic data that was in that DNode
	 * @param index of the DNode to be removed
	 * @return Generic data T of what was in the DNode removed
	 *****************************************************************/
	private T removeFromMiddle(int index) {
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
		return null;
	}

	/******************************************************************
	 * Removes the DNode at the back of the list and returns the 
	 * generic data that was in that DNode
	 * @param index of the DNode to be removed
	 * @return Generic data T of what was in the DNode removed
	 *****************************************************************/
	private T removeFromBack(int index) {
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
		return null;
	}

	/******************************************************************
	 * Removes the DNode at the front of the list and returns the 
	 * generic data that was in that DNode
	 * @param index of the DNode to be removed
	 * @return Generic data T of what was in the DNode removed
	 *****************************************************************/
	private T removeFromFront(int index) {
		// Removing from the front
		if (index == 0 && top != null) {

			DNode<T> nextNode = top.getNextNode();

			if (nextNode != null) {
				nextNode.setPreviousNode(null);
			}
			DNode<T> previousTop = top;

			top = null;
			top = nextNode;

			return previousTop.getData();
		}
		return null;
	}

	/******************************************************************
	 * Returns true if at least one item removed
	 * @param t - generic object to be removed from the list
	 * @return true or false if something was removed
	 *****************************************************************/
	public boolean removeAll (T t) {

		DNode<T> currentNode = top;

		int index;

		int beforeSize = getSize();

		// Searching for DNodes to remove
		while (currentNode != null) {
			T currentData = currentNode.getData();
			index = 0;

			// Removes DNode if its data equals t
			if (currentData.equals(t)) {
				remove(index);
			}
			currentNode = currentNode.getNextNode();
			index++;
		}

		// if something was removed return true
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
	 * @throws Exception if parameter breaks method
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

	/******************************************************************
	 * Creates a toString of the list forwards and backwards to see
	 * the contents of the list
	 * @return String of what the list contains
	 *****************************************************************/
	public String toString() {

		// Show the linked list forward
		String string = "Forward: ";
		DNode<T> currentNode = top;
		while (currentNode != null){
			string += currentNode.toString() + " ";
			currentNode = currentNode.getNextNode();
		}

		// Show the linked list backward
		string += "\nBackward: ";
		currentNode = tail;
		while (currentNode != null) {
			string += currentNode.toString() + " ";
			currentNode = currentNode.getPreviousNode();
		}
		return string;
	}
}
