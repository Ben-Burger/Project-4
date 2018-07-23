package project4;


public class DNode <T> {

	private T data;
	
	private DNode<T> nextNode;
	private DNode<T> previousNode;
	
	//Hi ben
	
	

	public DNode() {
	}
	
	public DNode(T data, DNode<T> previousNode, DNode<T> nextNode) {
		this.data = data;
		this.nextNode = nextNode;
		this.previousNode = previousNode;
	}
	
	public DNode(T data) {
		this.data = data;
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
