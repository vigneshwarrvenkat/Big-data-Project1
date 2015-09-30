package com.analytics.hw2;

import java.util.ArrayList;


public class Entropy {	
	
	
	public static double calculateEntropy(ArrayList<Record> data) {
		double entropy = 0;
		System.out.println("data size: " + data.size());
		if(data.size() == 0) {
			// nothing to do
			return 0;
		}
		
	//	System.out.println("HW2 size: " + Hw2.setSize("PlayTennis") + data.size());
		
		for(float i = 0; i < Hw2.setSize("Label"); i++) {
			int count = 0;
			for(int j = 0; j < data.size(); j++) {
				//Record record = data.get(j);
				
			//	System.out.println(" value of record :  " + data.get(j).getAttributes().get(6).getValueLabel());
				
				if(data.get(j).getAttributes().get(6).getValueLabel() == i) {
					count++;
				}
			}
				
			double probability = count / (double)data.size();
			
			System.out.println("Probability: " + probability + "count" + count + "size" + data.size());
			
			if(count > 0) {
				entropy += -probability * (Math.log(probability) / Math.log(2));
			}
		}
		
		return entropy;
	}
	
	public static double calculateGain(double rootEntropy, ArrayList<Double> subEntropies, ArrayList<Integer> setSizes, int data) {
		double gain = rootEntropy; 
		
		for(int i = 0; i < subEntropies.size(); i++) {
			gain += -((setSizes.get(i) / (double)data) * subEntropies.get(i));
		}
		
		return gain;
	}
	
	
	
}