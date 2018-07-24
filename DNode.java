package project4;

import java.io.Serializable;

public class DNode <T> implements Serializable{
	
	/** Saves a DVD object as a binary file */
	private static final long serialVersionUID = 1L;

	private T data;
	
	private DNode<T> nextNode;
	private DNode<T> previousNode;	

	public DNode() {
		nextNode = null;
		previousNode = null;
	}
	
	public DNode(T data, DNode<T> previousNode, DNode<T> nextNode) {
		this.data = data;
		this.nextNode = nextNode;
		this.previousNode = previousNode;
	}
	
	public DNode(T data) {
		this.data = data;
		nextNode = null;
		previousNode = null;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public DNode<T> getNextNode() {
		return nextNode;
	}

	public void setNextNode(DNode<T> nextNode) {
		this.nextNode = nextNode;
	}
	
	public DNode<T> getPreviousNode() {
		return previousNode;
	}

	public void setPreviousNode(DNode<T> previousNode) {
		this.previousNode = previousNode;
	}
	
	@Override
	public String toString() {
		return "Node{" + "data=" + data + '}';
	}
	
	
}
