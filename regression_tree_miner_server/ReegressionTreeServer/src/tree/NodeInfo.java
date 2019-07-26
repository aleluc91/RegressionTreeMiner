package tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String label;
	private int width;
	private int height;
	
	public NodeInfo(String label , int width , int height){
		this.label = label;
		this.width = width;
		this.height = height;
	}
	
	public String getLabel(){
		return label;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
}
