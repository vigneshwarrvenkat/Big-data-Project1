
/*
package com.analytics.hw2;

 public class Attribute {
	private String name;
	private float value;
	private int valueLabel;
	private boolean isUnknown;
	private double surrogate;
	
	public Attribute(String name, float value) {
		this.name = name;
		this.value = value;
		surrogate = -1;
		isUnknown = false;
	}
	
	public Attribute(String name, int value) {
		this.name = name;
		this.value = value;
		surrogate = -1;
		isUnknown = false;
	}
	
	public Attribute(String name, String value) {
		this.name = name;
		try {
			this.value = Float.valueOf(value);
			this.isUnknown = false;

		}
		catch(NumberFormatException nfe) {
			this.value = -1;
			this.isUnknown = true;
		}
		
		surrogate = -1;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void setValue(Float value) {
		this.value = value;
	}

	public float getValue() {
		return value;
	}
	
	public int getValueLabel() {
		return valueLabel;
	}

	public void setSurrogate(double surrogate) {
		this.surrogate = surrogate;
	}

	public double getSurrogate() {
		return surrogate;
	}

	public void setUnknown(boolean isUnknown) {
		this.isUnknown = isUnknown;
	}

	public boolean isUnknown() {
		return isUnknown;
	}
}

*/