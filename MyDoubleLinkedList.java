package project4;

import java.io.Serializable;

public class MyDoubleLinkedList<T> implements Serializable{

	/** Saves a DVD object as a binary file */
	private static final long serialVersionUID = 1L;

	private DNode<T> top;
	private DNode<T> tail;

	private int size; //FIXME meant to be public or private??? 

	public MyDoubleLinkedList () {
		top = null;
		tail = null;
	}

	public int getSize() {
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
			top = new DNode(t);
			tail = null;
		}
		else if (tail == null) {
			tail = new DNode(t);
			top.setNextNode(tail);
			tail.setPreviousNode(top);
		}
		else {
			DNode<T> previousHead = top;
			DNode<T> newHead = new DNode(t);
			
			newHead.setNextNode(previousHead);
			previousHead.setPreviousNode(newHead);
			
			top = newHead;
		}
		
		incrementSize();
		
	}

	/******************************************************************
	 * Add at the tail
	 * @param t
	 *****************************************************************/
	public void addAtTail(T t) {

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
	public boolean remove (int index) {
		return false;
	}

	/******************************************************************
	 * Removes first occurrence
	 * @param index
	 * @return
	 *****************************************************************/
	public T removeFirstOccurence (T t) {

		return t;
	}

	/******************************************************************
	 * return true if at least one item removed
	 * @param t
	 * @return
	 *****************************************************************/
	public boolean removeAll (T t) {

		return false;
	}

	/******************************************************************
	 * 
	 * @param index
	 * @return
	 *****************************************************************/
	public T get(int index) {
		return T;
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

	/******************************************************************
	 * Increments size of list variable by 1
	 *****************************************************************/
	public void incrementSize() {
		size++;
	}


	/******************************************************************
	 * Decrements the size of the list variable by 1
	 *****************************************************************/
	public void decrementSize() {
		size--;
	}
}
