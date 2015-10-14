package com.random;

import java.util.ArrayList;

public class Main {
	
	public static void main(String args[]){

		String traindata="TestDat.csv";
		String testdata="TestDat2.csv";
		int numTrees=1;
		
		ReadInput DT = new ReadInput(traindata);
		ArrayList<int[]> Input=DT.CreateInput(traindata);int categ=-1;
		
		ReadInput DTT = new ReadInput(traindata);
		ArrayList<int[]> Test=DTT.CreateInput(testdata);
		
		
		
		for(int k=0;k<Input.size();k++)
		{
			if(Input.get(k)[Input.get(k).length-1]<categ){
				
				System.out.println(" inside: " + Input.get(k)[Input.get(k).length-1]);
			continue;
			}
		else{
			categ=Input.get(k)[Input.get(k).length-1];
			}
		}
		
		RandomForest RaF =new RandomForest(numTrees, Input, Test);
		RaF.C=2;
		RaF.M=Input.get(0).length-1;
		RaF.Ms=Integer.valueOf((int) (Math.sqrt(RaF.M)));
		RaF.Start();
             
	}
	
	
}

