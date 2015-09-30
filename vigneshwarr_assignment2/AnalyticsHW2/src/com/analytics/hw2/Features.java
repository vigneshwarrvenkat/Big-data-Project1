package com.analytics.hw2;

public class Features {
	
	private String name;
	private float value;
	private int valueLabel;
	private boolean isUnknown;
	private double surrogate;
	
	public Features(String name, float value) {
		this.name = name;
		this.value = value;
		
		isUnknown = false;
	}
	
	public Features(String name, int value) {
		this.name = name;
		this.valueLabel = value;
		
		isUnknown = false;
	}
	
	public Features(String name, String value) {
		this.name = name;
		
			this.value = Float.valueOf(value);
			this.isUnknown = false;
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

	

	
	

}
