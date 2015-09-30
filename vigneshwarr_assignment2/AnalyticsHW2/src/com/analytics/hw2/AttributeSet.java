package com.analytics.hw2;

import java.util.*;

public class AttributeSet {
	private String name;
	private ArrayList<Features> attributes;
	private double entropy;
	private boolean isUsed;
	
	public AttributeSet() {
		attributes = new ArrayList<Features>();
		entropy = -1;
		isUsed = false;
	}
	
	public void setAttributes(ArrayList<Features> attributes) {
		this.attributes= attributes;
	}

	public ArrayList<Features>getAttributes() {
		return attributes;
		}

	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	public double getEntropy() {
		return entropy;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
