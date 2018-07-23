package project4;

import java.io.Serializable;

public class MyDoubleLinkedList<T> implements Serializable{

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

	}

	/******************************************************************
	 * Add at the top
	 * @param t
	 *****************************************************************/
	public void addAtTop (T t) {

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

//		incrementSize();

	}

	/******************************************************************
	 * Add at the tail
	 * @param t
	 *****************************************************************/
	public void addAtTail(T t) {

		if (top == null) {
			DNode<T> insertNode = new DNode<T>(t);
			top = insertNode;
			tail = null;
		}
		else if (tail == null) {

			tail = new DNode<T>(t);
			top.setNextNode(this.tail);
			tail.setPreviousNode(this.top);
		}
		else {
			DNode<T> previousTail = tail;
			DNode<T> newTail = new DNode<T> (t);

			newTail.setPreviousNode(previousTail);
			previousTail.setNextNode(newTail);
			tail = newTail;
		}

//		incrementSize();

	}

	/******************************************************************
	 * Adds anywhere in the list
	 * @param t
	 * @param index
	 *****************************************************************/
	public void add(T t, int index) {

	}

	/******************************************************************
	 * Removes data at a specific index
	 * @param index
	 * @return
	 *****************************************************************/
	public T remove (int index) {

		// Removing from the front
		if (index == 0 && top != null) {

			DNode<T> nextNode = top.getNextNode();

			if (nextNode != null) {
				nextNode.setPreviousNode(null);
			}
			DNode<T> previousTop = top;

			top = null;
			top = nextNode;
//			decrementSize();

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
//			decrementSize();
			return previousTail.getData();
		}
		
		// Removing from the middle
		if (index < getSize()) {
			DNode<T> nodeToRemove = top;
			for (int i = 0; i < index - 1; i++) 
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

		return top.getData(); //FIXME what to do here?
	}

	//	/******************************************************************
	//	 * Removes first occurrence
	//	 * @param index
	//	 * @return
	//	 *****************************************************************/
	//	public T removeFirstOccurence (T t) {
	//
	//		return t;
	//	}

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
	 * 
	 * @param index
	 * @return
	 *****************************************************************/
	public T get(int index) {
		
		return top.getData();
	}

	/******************************************************************
	 * return index if found, -1 otherwise
	 * @param t
	 * @return
	 *****************************************************************/
	public int find(T t) {
		return 0;
	}

	/******************************************************************
	 * Checks if list is empty
	 * @return true or false whether list is empty
	 *****************************************************************/
	public boolean isEmpty() {
		if (top == null && tail == null)
			return true;
		return false;
	}

//	/******************************************************************
//	 * Increments size of list variable by 1
//	 *****************************************************************/
//	public void incrementSize() {
//		size++;
//	}
//
//
//	/******************************************************************
//	 * Decrements the size of the list variable by 1
//	 *****************************************************************/
//	public void decrementSize() {
//		size--;
//	}
}
