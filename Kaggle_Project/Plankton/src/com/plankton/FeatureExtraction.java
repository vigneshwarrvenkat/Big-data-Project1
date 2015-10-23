package com.plankton;


import ij.process.ColorProcessor;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;

public class FeatureExtraction {
	
	public static float[][] R;
	public static float[][] G;
	public static float[][] B;
	public static float[][] H;
	public static float[][] S;
	public static float[][] V;
	
	public static int[][] bin;
	static int height;
	static int width;
	public static double[][] allFeatures=new double[1000][66];
	public static double[] features=new double[1000];
	public static Calendar cal=Calendar.getInstance();
	public static PrintStream outFile;
	
	 static String feature1;
	
	static ArrayList<String> ft= new ArrayList<String>();
	
	
	public static double[] extractSingleImage(String name){
		
		
		
		double[] out=new double[66];
		System.out.println("file :" + name);
		URL url = FeatureExtraction.class.getResource(name);
		System.out.println("url = " + url);
		BufferedImage image = null;
		try {
			image = ImageIO.read(url);
		} catch(IOException e) {
			System.out.println("read error: " + e.getMessage());
		}
		
		ColorProcessor cp=new ColorProcessor(image);
		
		/////////////// Color Extraction /////////////////
		
		ColorExtraction ce=new ColorExtraction(cp);
		ce.extract();
		double[] colors=ce.getColorFeature();


		////////////// Edge Detection ///////////////        

		CannyEdgeDetector ced=new CannyEdgeDetector();
		ced.setSourceImage(image);
		ced.process();
		BufferedImage edge=ced.getEdgesImage();
		rgb2bin(edge);
		

		////////////// Texture Extraction ///////////////        

		TextureExtraction te=new TextureExtraction(bin, height, width);
		te.extract();
		double[] textures=te.getCoocFeatures();
		
		
		/////////////////Number of edges//////////////////
		HarrisTest he = new HarrisTest();
	   // int output =	he.getEdges(s);
		
		

		//		////////// Join //////////////
		for (int i=0;i<18;i++){
			out[i]=colors[i];
		}

		for (int i=0;i<48;i++){
        	out[i+18]=textures[i];        	
        }
		
		return out;
	}
	
	
    public static void extractAll(String filename,boolean flag) throws IOException {
    	
    	
    	
    	double Area=0;
    	float ratio=0.0f;
    	int output=0;
    	String feature="";
    	
    	System.out.println("Flag: " +flag);
    	
    	
    	try{
    		outFile=new PrintStream("Features.txt");
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    	long start=cal.getTimeInMillis();
    	
    	System.out.println("Features");
    	
    	
    	
    	
            File dir2 = new File(filename);
    	    File dir=new File(filename);
    	    File files[]=dir.listFiles();//files array stores the list of files

    	 for(int l=0;l<files.length;l++)
    		 
    		 
    	    {
    		 
    		 
    		 
    		        
    	        if(files[l].isFile()) //check whether files[i] is file or directory
    	        {
    	           // System.out.println("File:"+files[l].getParentFile().getName()+"/"+files[l].getName());
    	           // System.out.println();
    	            
    	        	String s="";
    	        	
    	        	System.out.println("Flag2: " +flag);   	
    	        	
    	        	if(flag==true){
    	    		    
    	        		  s = "image2.orig/"+"train/"+files[l].getParentFile().getName()+"/"+files[l].getName();
    	        		  
    	        		  System.out.println("first" + s + flag);
    	        		 
    	        		  
    	    		    
    	    		    }
    	    		    else{
    	    		    	 s = "image2.orig/"+"test/"+files[l].getParentFile().getName()+"/"+files[l].getName();
    	    		    	 System.out.println("second");
    	    		    	 System.out.println(filename);
    	    		    }
    	            
    	        
    	             
    	            
    	         //   System.out.println("string" + s);
    	            
    		    	URL url = FeatureExtraction.class.getResource(s);
    		    	
    		    //	String str =  url;
    		                                        
    		    	
    		    	System.out.println("URL: " + url);
    		    	
    		        // where are we looking for this image
    		        BufferedImage image = null;
    		        try {
    		            image = ImageIO.read(url);
    		        } catch(IOException e) {
    		            System.out.println("read error: " + e.getMessage());
    		        }
    		
    		        ColorProcessor cp=new ColorProcessor(image);
    		        
    		         /////////////// Color Extraction /////////////////
    		        
    		        ColorExtraction ce=new ColorExtraction(cp);
    		        ce.extract();
    		        double[] colors=ce.getColorFeature();
    		        
    		        
    		        ////////////// Edge Detection ///////////////        
    		        
    		        CannyEdgeDetector ced=new CannyEdgeDetector();
    		        ced.setSourceImage(image);
    		        ced.process();
    		        BufferedImage edge=ced.getEdgesImage();
    		        rgb2bin(edge);
    		        
    		        
    		        ////////////// Texture Extraction ///////////////        
    		        
    		        TextureExtraction te=new TextureExtraction(bin, height, width);
    		        te.extract();
    		        double[] textures=te.getCoocFeatures();
    		        
    		        ///////////////number of edges////////////
    		        HarrisTest he = new HarrisTest();
    			    output =	he.getEdges(s);
    		        
    		        
    		        //////////// Join //////////////
    		        
    		         Area = height*width;
    		         ratio = (float) height/width;
    		         float f = (float) (10.0/9.0);
    		     System.out.println("ratio: "+  ratio + "height:" +height +"width:" +width + "f:" + f);   
    		        
    		        //System.out.println("time: "+(finish-start));
    		        //System.out.println("*************");
    		        
    		        for (int i=0;i<18;i++){
    		        	features[i]=colors[i];
    		        	//outFile.print(colors[i]+ "\t");
    		        }
    		        
    		        //System.out.println("*************");
    		        
    		        for (int i=0;i<48;i++){
    		        	features[i+18]=textures[i];
    		        	//outFile.print(textures[i]+ "\t");
    		     //   System.out.println(textures[i]);
    		        }
    		        
    		        for(int i=0;i<output;i++){
    		        	features[i+66]=output;
    		        	//outFile.print(output+ "\t");	
    		        	
    		        }
    		        
    		        outFile.println();
    		   //     allFeatures[ind]=features;
    		        
    		        BlobDemo find = new BlobDemo();
        			//int blob = find.FindBlob(s);
    		       int blob = 0;
        		//	System.out.println("number of blobs: " + blob);
    		        
    		        System.out.println("ration" + width+  " hi " + height);
    		        
    		        try{
        	        
        	        feature1 = String.valueOf(Area) + ","  + String.valueOf(ratio)+ "," + Integer.toString(output)+ "," + Integer.toString(height)+ "," + Integer.toString(width) + "," + Integer.toString(blob)+","+files[l].getParentFile().getName();  
    		        }
    		        catch(NumberFormatException ex){
    		        	
    		        }
        	        FeatureExtraction.ft.add(feature1);
        	        
        	        
        	        
        	        
        	        
    		        
    		    }
    	        else if(files[l].isDirectory())
    	        {
    	          //  System.out.println("Directory:"+files[l].getName());
    	            System.out.println();
    	            
    	            if(flag==true){
    	            
    	            extractAll(files[l].getAbsolutePath(),true);
    	            }
    	            else{
    	            	
    	            	extractAll(files[l].getAbsolutePath(),false);
    	            	
    	            }
    	            
    	           
    	        }
    	        
    	        
    	        
    	        
    	    	}
    		    
    		    long finish=cal.getTimeInMillis();
    		  //  System.out.println("time: "+(finish-start));
    		    
    		    String csvFile1 = "";
    		    
    		    if(flag==true){
    		    
    		    csvFile1 = "TestData1.csv";
    		    
    		    }
    		    else{
    		    	csvFile1 = "TestData2.csv";
    		    }
    		    
    		    BufferedWriter bw = null;
    	    	
    	    	try {
    				bw = new BufferedWriter(new FileWriter(csvFile1));
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    	    	
    	    	for(int k=0;k<ft.size();k++){
    	    	
    	    	bw.append(ft.get(k));
    	    	bw.append('\n');
    	    	
    	    	}
    
                bw.close();
    	        }
    	        
    	   

    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
	    
	    
	    	
    
    
    public static void RGB_HSV_Decompose(ColorProcessor cp){
    	
    	R=new float[cp.getHeight()][cp.getWidth()];
    	G=new float[cp.getHeight()][cp.getWidth()];
    	B=new float[cp.getHeight()][cp.getWidth()];
    	H=new float[cp.getHeight()][cp.getWidth()];
    	S=new float[cp.getHeight()][cp.getWidth()];
    	V=new float[cp.getHeight()][cp.getWidth()];
    	int rgb[] =new int[3];
    	float hsv[]=new float[3];
    	for (int i=0;i<cp.getHeight();i++){
    		for (int j=0;j<cp.getWidth();j++){
    			rgb=cp.getPixel(i, j, rgb);
    			R[i][j]=rgb[0];
    			G[i][j]=rgb[1];
    			B[i][j]=rgb[2];
    			hsv=java.awt.Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsv);
    			H[i][j]=hsv[0];
    			S[i][j]=hsv[1];
    			V[i][j]=hsv[2];
    		}
    	}
    }
    
    public static double[] meanStdSkew ( float[][] data, int h, int w )
    {
	    double mean = 0;
	    double[] out=new double[3];
	    
	    for (int i=0;i<h;i++){
        	for (int j=0;j<w;j++){
        		mean += data[i][j];
        	}
	    }
	    mean /= (h*w);
	    out[0]=mean;
	    double sum = 0;
	    for (int i=0;i<h;i++){
        	for (int j=0;j<w;j++){
        		final double v = data[i][j] - mean;
        		sum += v * v;
        	}
	    }
	    out[1]=Math.sqrt( sum / ( h*w - 1 ) );
	    
	    sum = 0;
	    for (int i=0;i<h;i++){
        	for (int j=0;j<w;j++){
        		final double v = (data[i][j] - mean)/out[1];
        		sum += v * v * v;        		
        	}
	    }
	    
	    out[2]=Math.pow(sum/(h*w-1),1./3);
	    return out;
    }


    
    

    
    public static void rgb2bin(BufferedImage in){
    	int h=in.getHeight();
    	int w=in.getWidth();
    	height=h;
    	width=w;
    	int[][] out=new int[h][w];
    	long rgb;
    	int r,g,b;
    	ColorProcessor cp=new ColorProcessor(in);
    	for (int i=0;i<h;i++){
    		for (int j=0;j<w;j++){
    			rgb=cp.getPixel(i, j);
    	        r   = (int)(rgb % 0x1000000 / 0x10000);  
    	        g = (int)(rgb % 0x10000 / 0x100);  
    	        b = (int)(rgb % 0x100);  
    	        if (r==0 && g==0 && b==0){
    	        	out[i][j]=0;
    	        }
    	        else{
    	        	out[i][j]=1;
    	        }
    		}
    	}
    	
    	bin= out;
    	
    }
}