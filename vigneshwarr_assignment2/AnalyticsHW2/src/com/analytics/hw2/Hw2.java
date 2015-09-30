package com.analytics.hw2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/*
 * 
 * 
 * The main class which acts as the
 * driver for reading the data from the 
 * file and tree building
 */



public class Hw2 {
	public static int NUM_ATTRS = 7;
	public static ArrayList<String> attrMap;
	public static ArrayList<Integer> usedAttributes = new ArrayList<Integer>();

	public static void main(String[] args) {
		populateAttrMap();

		Tree t = new Tree();
		ArrayList<Record> records;
		LearningSet learningSet = new LearningSet();
		
		// read in all our data
		records = FileReader.buildRecords();
		
	//	System.out.println("Records: " + records.get(4).getAttributes().get(6).getValueLabel());
		
	//	System.out.println("Records" + records);
		
		Node root = new Node();
		
		for(Record record : records) {
			root.getData().add(record);
		}
		
		t.buildTree(records, root, learningSet);
		System.out.print("Prediction of Currency : ");
		if(traverseTree(records.get(5), root) == 0) {
		System.out.println("Decrease");
		} else {
		System.out.println("Increase");
		}
		
		
		print(root);
		System.out.println("Hi");
		
		//return;
	}
	
	
		
		public static float traverseTree(Record r, Node root) {
			if(root.children == null)
			return root.getTestAttribute().getValue();
			double nodeValue = 0;
			for(int i = 0; i < r.getAttributes().size(); i++) {
			if(r.getAttributes().get(i).getName().equalsIgnoreCase(root.getTestAttribute().getName())) {
			nodeValue = r.getAttributes().get(i).getValue();
			break;
			}
			}
			for(int i = 0; i < root.getChildren().length; i++) {
			if(nodeValue == root.children[i].getTestAttribute().getValue()) {
			traverseTree(r, root.children[i]);
			}
			}
			return root.getTestAttribute().getValue();
			}
	
	
	public static boolean isAttributeUsed(int attribute) {
		if(usedAttributes.contains(attribute)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static int setSize(String set) {
		
			return 2;
			
	}
		
		
	
	public static String getLeafNames(int attributeNum, int valueNum) {
		if(attributeNum == 0) {
			if(valueNum == 0) {
				return "Decrease";
			}
			else if(valueNum == 1) {
				return "Increase";
			}
			
		}
		
		return null;
	}
	
	
	
	
	
	/**
	 * print the tree to console
	 * @param root : root of the tree
	 */
	private static void print(Node root) {
		int count = 0;
		if (root == null) {
			return;
		}
	
		if (root.getChildren() != null) {
			for (Node child : root.getChildren()) {
				System.out.println("Leaf: " + count);	
				count = count+1;
				print(child);
			}
		}
	}
	
	
	
	
	
	/**
	 * 
	 * Print decision tree to file
	 * @param root : root of the tree
	 * @param bw : buffered Writter for the file
	 */
	
	/*
	private void printToFile(Node root, BufferedWriter bw) {
		if (root == null) {
			return;
		}
		try {
			bw.newLine();
			bw.write(root.getDisplayPrefix());
			if (root.getChildren() != null) {
				for (TreeNode child : root.getChildren().values()) {
					printToFile(child,bw);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	/**
	 * Print the whole tree to console
	 */
	
	
	/*
	public void print() {
		this.print(root);
	}
	
	
	
	*/
	
	/**
	 * Print the whole tree to given file
	 * @param bw: bufferedWritter for given file
	 */
	
	
	/*
	public void printToFile(BufferedWriter bw) {
		try {
			printToFile(root,bw);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	*/
	
	public static void populateAttrMap() {
		attrMap = new ArrayList<String>();
		attrMap.add("AverageBid");
		attrMap.add("AverageAsk");
		attrMap.add("MaxBid");
		attrMap.add("MinBid");
		attrMap.add("MaxAsk");
		attrMap.add("MinAsk");
		attrMap.add("Label");
	}
}