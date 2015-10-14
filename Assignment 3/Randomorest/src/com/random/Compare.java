package com.random;

import java.util.Comparator;

public class Compare implements Comparator{		
	
	private int m;
	
	public Compare(int m){
		this.m=m;
	}
	/**
	 * Compare the two data records. They must be of type int[].
	 * @return			-1 if A[m] < B[m], 1 if A[m] > B[m], 0 if equal
	 */
	public int compare(Object o1, Object o2){
		int a=((int[])o1)[m];
		int b=((int[])o2)[m];
		if (a < b)
			return -1;
		if (a > b)
			return 1;
		else
			return 0;
	}		
}