package com.analytics.hw2;

import java.util.*;

public class Record {
	private ArrayList<Features> attributes;

	public ArrayList<Features> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(ArrayList<Features> attributes) {
		this.attributes = attributes;
		
		for(int i=0; i<=6; i++){
		
		System.out.println("for :" + attributes.get(i).getValue());
		
		}
	}
}