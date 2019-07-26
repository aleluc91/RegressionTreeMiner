package tree;

import java.io.Serializable;

public class NodeInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String label;
	private int width;
	private int height;
	
	public NodeInfo(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}

	int getWidth() {
		return width;
	}

	int getHeight() {
		return height;
	}
	
}
