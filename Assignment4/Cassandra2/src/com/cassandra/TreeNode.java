package com.cassandra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class TreeNode implements Cloneable{
	public boolean isLeaf;
	public TreeNode left;
	public TreeNode right;
	public int splitAttributeM;
	public Integer Class;
	public List<int[]> data;
	public int splitValue;
	public int generation;
	
	public TreeNode(){
		splitAttributeM=-99;
		splitValue=-99;
		generation=1;
	}
	public TreeNode clone(){ //"data" element always null in clone
		TreeNode copy=new TreeNode();
		copy.isLeaf=isLeaf;
		if (left != null) //otherwise null
			copy.left=left.clone();
		if (right != null) //otherwise null
			copy.right=right.clone();
		copy.splitAttributeM=splitAttributeM;
		copy.Class=Class;
		copy.splitValue=splitValue;
		return copy;
	}
}
 class DoubleWrap{
	public double d;
	public DoubleWrap(double d){
		this.d=d;
	}		
}
