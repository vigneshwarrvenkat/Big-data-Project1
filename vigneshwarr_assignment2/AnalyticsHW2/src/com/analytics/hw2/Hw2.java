package com.analytics.hw2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;





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
		System.out.print("Prediction for Play Tennis: ");
		if(traverseTree(records.get(6), root) == 0) {
		System.out.println("No");
		} else {
		System.out.println("yes");
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
		if(set.equalsIgnoreCase("")) {
			return 3;
		}
		else if(set.equalsIgnoreCase("Wind")) {
			return 2;
		}
		else if(set.equalsIgnoreCase("Temperature")) {
			return 3;
		}
		else if(set.equalsIgnoreCase("Humidity")) {
			return 2;
		}
		else if(set.equalsIgnoreCase("Label")) {
			return 2;
		}
		return 0;
	}
	
	public static String getLeafNames(int attributeNum, int valueNum) {
		if(attributeNum == 0) {
			if(valueNum == 0) {
				return "Sunny";
			}
			else if(valueNum == 1) {
				return "Overcast";
			}
			else if(valueNum == 2) {
				return "Rain";
			}
		}
		else if(attributeNum == 1) {
			if(valueNum == 0) {
				return "Hot";
			}
			else if(valueNum == 1) {
				return "Mild";
			}
			else if(valueNum == 2) {
				return "Cool";
			}
		}
		else if(attributeNum == 2) {
			if(valueNum == 0) {
				return "High";
			}
			else if(valueNum == 1) {
				return "Normal";
			}
		}
		else if(attributeNum == 3) {
			if(valueNum == 0) {
				return "Weak";
			}
			else if(valueNum == 1) {
				return "Strong";
			}
		}
		
		return null;
	}
	
	
	
	
	
	/**
	 * print the tree to console
	 * @param root : root of the tree
	 */
	private static void print(Node root) {
		if (root == null) {
			return;
		}
	System.out.println("count");
		if (root.getChildren() != null) {
			for (Node child : root.getChildren()) {
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
		attrMap.add("Outlook");
		attrMap.add("Temperature");
		attrMap.add("Humidity");
		attrMap.add("Wind");
		attrMap.add("PlayTennis");
	}
}