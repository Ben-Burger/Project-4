package project4;

import java.io.Serializable;

/**********************************************************************
 * Creates DNodes with a generic type T that will be used in a 
 * double linked list
 * 
 * @author Jarod Collier and Ben Burger
 * @version 7/16/18
 *********************************************************************/
public class DNode <T> implements Serializable{
	
	/** Saves a DVD object as a binary file */
	private static final long serialVersionUID = 1L;

	/** Sets the data for each DNode to be generic */
	private T data;
	
	/** Generic DNode used for the next node */
	private DNode<T> nextNode;
	
	/** Generic DNode used for the previous node */
	private DNode<T> previousNode;	

	/******************************************************************
	 * Constructor that initializes the nextNode and PreviousNode
	 * to null
	 *****************************************************************/
	public DNode() {
		nextNode = null;
		previousNode = null;
	}
	
	/******************************************************************
	 * Constructor that creates a generic DNode with t data and then
	 * sets what the new DNode's previous and next node are
	 * @param data - the generic data to be put in the DNode
	 * @param previousNode - the previous node of the new DNode
	 * @param nextNode - the next node of the new DNode
	 *****************************************************************/
	public DNode(T data, DNode<T> previousNode, DNode<T> nextNode) {
		this.data = data;
		this.nextNode = nextNode;
		this.previousNode = previousNode;
	}
	
	/******************************************************************
	 * Constructor that creates a DNode with generic data in it and 
	 * sets the next and previous nodes to null
	 * @param data - generic data to be put in the DNode
	 *****************************************************************/
	public DNode(T data) {
		this.data = data;
		nextNode = null;
		previousNode = null;
	}

	/******************************************************************
	 * Gets the generic data of a specific DNode
	 * @return generic data T that is in a DNode
	 *****************************************************************/
	public T getData() {
		return data;
	}

	/******************************************************************
	 * Sets the DNode's data with generic t data
	 * @param data - generic data to set the DNode with
	 *****************************************************************/
	public void setData(T data) {
		this.data = data;
	}

	/******************************************************************
	 * Gets the next node in the list
	 * @return the next node in the list
	 *****************************************************************/
	public DNode<T> getNextNode() {
		return nextNode;
	}

	/******************************************************************
	 * Sets the next node as the inputted DNode
	 * @param nextNode - the DNode that will be the next node 
	 *****************************************************************/
	public void setNextNode(DNode<T> nextNode) {
		this.nextNode = nextNode;
	}
	
	/******************************************************************
	 * Gets the previous node in the list
	 * @return The DNode that is the previous DNode
	 *****************************************************************/
	public DNode<T> getPreviousNode() {
		return previousNode;
	}

	/******************************************************************
	 * Sets the previous node to the inputted DNode
	 * @param previousNode - the DNode that will be the previous node
	 *****************************************************************/
	public void setPreviousNode(DNode<T> previousNode) {
		this.previousNode = previousNode;
	}

	@Override
	/******************************************************************
	 * Returns the data that is in a DNode in string format
	 * @return A string of the data in the DNode
	 *****************************************************************/
	public String toString() {
		return "Node{" + "data=" + data + '}';
	}
}
