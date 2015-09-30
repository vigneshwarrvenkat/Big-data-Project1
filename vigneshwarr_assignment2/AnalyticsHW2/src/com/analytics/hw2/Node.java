package com.analytics.hw2;

import java.util.*;

public class Node {
	private Node parent;
	public Node[] children;
	private ArrayList<Record> data;
	private double entropy;
	private boolean isUsed;
	private Features testAttribute;

	public Node() {
		this.data = new ArrayList<Record>();
		setEntropy(0.0);
		setParent(null);
		setChildren(null);
		setUsed(false);
		setTestAttribute(new Features("", 0));
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getParent() {
		return parent;
	}

	public void setData(ArrayList<Record> data) {
		this.data = data;
	}

	public ArrayList<Record> getData() {
		return data;
	}

	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	public double getEntropy() {
		return entropy;
	}

	public void setChildren(Node[] children) {
		this.children = children;
	}

	public Node[] getChildren() {
		return children;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setTestAttribute(Features testAttribute) {
		this.testAttribute = testAttribute;
	}

	public Features getTestAttribute() {
		return testAttribute;
	}
}