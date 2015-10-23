package com.plankton;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;





class Retrieval{
	
	public static double[][] allFeatures=new double[1000][66];
	public static double[][] distances = new double[1000][2];
	
	public static void retrieve(String fileName){
		
		System.out.println("HI");
		
		double[] featureVector=FeatureExtraction.extractSingleImage(fileName);
		
		System.out.println("fine :" + fileName);
		
		double dis;
		
		for (int i=0;i<1000;i++){
			distances[i][0]=-1;
			distances[i][1]=100;
		}
		
		int k;
		double D;
		
		
		
		for (int i=0;i<1000;i++){
			k=0;
			D=euclideanDistance(featureVector, allFeatures[i]);
			
		    while (distances[k][1]<D){
		        k=k+1;        
		    }
		    for (int n=999;n>=k+1;n--){
		        distances[n][0]=distances[n-1][0];
		        distances[n][1]=distances[n-1][1];
		    }
		    distances[k][0]=i;
		    distances[k][1]=D;
		}
		
	}
	
	public static double euclideanDistance(double[] a, double[] b){
		double S=0;
		for (int i=0;i<18;i++){
			S+= Math.pow(a[i]-b[i],2);
		}
		for (int i=18;i<66;i++){
			S+= 10*Math.pow(a[i]-b[i],2);
		}
		return S;
		
	}
	
	public static void main(String args[]) throws IOException
	{
	     
		 String str="C:/Users/Vignesh/workspace/Plankton/src/com/plankton/image2.orig/train"; 
		 FeatureExtraction.extractAll(str,true);
		 
		 String st="C:/Users/Vignesh/workspace/Plankton/src/com/plankton/image2.orig/test"; 
		 
		 System.out.println("change in system");
		 
		 FeatureExtraction.extractAll(st,false);
		   
		
		 String traindata="TestData1.csv";
			String testdata="TestData2.csv";
			int numTrees=25;
			
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
	             
	
		 
		 
		 
		 
		 
		try{
	    	  FileInputStream fstream = new FileInputStream("Features.txt");
	    	  DataInputStream in = new DataInputStream(fstream);
	    	  BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    	  String strLine;
	    	  Scanner scan;
	    	  int imgInd=0;
	    	  while ((strLine = br.readLine()) != null){
	    		  //System.out.println(imgInd);
	    		  scan=new Scanner(strLine);
	    		  int ftrInd=0;
	    		  while(scan.hasNext()){
	    			  allFeatures[imgInd][ftrInd]=scan.nextDouble();
	    			  ftrInd++;
	    		  }
	    		  imgInd++;	    		  	
	    	  }
	    	  in.close();
	      }catch (Exception e){
	    	  System.err.println("Error: " + e.getMessage());
	      }
	      
	     
	      
	      System.out.println("Hi");
	      
	    
	      
	    //  String inputNumber = JOptionPane.showInputDialog("Enter the input number (0 - 999) : ");
	      
	     // retrieve ("image.orig/"+inputNumber+".jpg");
	      
	    //  FindContours find = new FindContours();
	      //find.setup();
	      
//	      for (int i=0;i<10;i++){
//	    	  System.out.println(distances[i][0]+"\t"+distances[i][1]);
//	      }
	      String path;
	      Retrieval test;
	      

	      for (int i=0;i<10;i++){
	    	  System.out.println(distances[i][0]);		      
	              	  
	      }
	      
	      
	      
	      
	      
	      
	     
	    	    // Load the library

	    	    System.loadLibrary("opencv_java249");

	    	    // Consider the image for processing
	    	    Mat image1 = Highgui.imread("C:/Users/Vignesh/workspace/Plankton/src/com/plankton/0.jpg", Imgproc.COLOR_BGR2GRAY);
	    	    Mat imageHSV = new Mat(image1.size(), Core.DEPTH_MASK_8U);
	    	    Mat imageBlurr = new Mat(image1.size(), Core.DEPTH_MASK_8U);
	    	    Mat imageA = new Mat(image1.size(), Core.DEPTH_MASK_ALL);
	    	   Imgproc.cvtColor(image1, imageHSV, Imgproc.COLOR_BGR2GRAY);
	    	 Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
	    	 Imgproc.adaptiveThreshold(imageBlurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);

	    	   // Highgui.imwrite("C:/Users/Vignesh/workspace/KaggleExtraction/src/com/hello/0.jpg",imageBlurr);

	    	    ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();    
	    	    Imgproc.findContours(imageA, (List<org.opencv.core.MatOfPoint>) contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
	    	    //Imgproc.drawContours(imageBlurr, contours, 1, new Scalar(0,0,255));
	    	    System.out.println("" + contours.size());
	    	    
	    	    for(int i=0; i<contours.size();i++){
	    	    	
	    	    //	System.out.println("countour" + contours.get(i) );
	    	    }
	    	    
	    	   // Highgui.imwrite("C:/Users/patiprad/Desktop/test2.png",image);
	    	
	      
	      
	      
	      
	      
	      
	      
	      
	      
	      
	      
	      
	      
	}
	
	
	
	
	BufferedImage image;
    Dimension size = new Dimension();

    public Retrieval(BufferedImage image) {
        this.image = image;
        size.setSize(image.getWidth(), image.getHeight());
    }
}
