package com.plankton;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.plankton.HarrisFast.Corner;


//import com.harris.HarrisFast.Corner;



//import com.harris.HarrisFast.Corner;

/**
 * 
 * HarrisTest.java
 * unseen.labs.face.harris
 * 
 * This class is referred from unseen labs ...UK
 *
 */
public class HarrisTest {

	/**
	 * @param args
	 */
	public int getEdges(String S) {
		
		BufferedImage img			= null;
		BufferedImage img_output	= null;
		FileReader in				= null;
		FileWriter out				= null;
		
		String filepath				= "C:/Users/Vignesh/workspace/Plankton/bin/com/plankton/";
		String filename				= "64.jpg";
		int i,j;
		
		//////////////////////
		
		try {
			img = ImageIO.read(new File(filepath+S));
		} catch (IOException e) {
			e.printStackTrace();
		}		 
		
		int width     = img.getWidth(); 	// width of 'image
		int height    = img.getHeight(); 	// height of l'image
		int[][] input =  new int[width][height];		//  Two dimension of the image
		
		
		for(i = 0 ; i < width - 1; i++){
			for(j = 0 ; j < height - 1 ; j++){
				input[i][j] = rgb2gray(img.getRGB(i, j));
			}
		}
		
		//////////////////////
		 
		double sigma   = 1.2;  // Gaussion sigma
		double k       = 0.06; // Parameter(constant to gaussian)
		int spacing = 8;    // distance minimum 
		 
		HarrisFast hf = new HarrisFast(input,width,height);
		int output = hf.filter(sigma,k,spacing);
		
		 
		//////////////////////
		
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(Color.RED);
		
		for(Corner corner : hf.corners){
				g2d.fill(new Rectangle2D.Float(corner.x - 5, corner.y - 5, 10, 10));
		}
		
		g2d.dispose();
		
		//////////////////////
	/*	
        FileOutputStream fos;
		try {
			fos = new FileOutputStream(filepath + "out_" + filename);
			ImageIO.write(img, "png", fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
*/		return output;
 
	}
	
	/*
	 * Returns the gray scale points
	 * of the image.
	 * Uses Bit manipulation to retrieve the 
	 * R,G,B values
	 * 
	 */
	
	static int rgb2gray(int srgb) {
		int r = (srgb >>16 ) & 0xFF;
		int g = (srgb >> 8 ) & 0xFF;
		int b = srgb & 0xFF;
		return (int)(0.299*r + 0.587*g + 0.114*b);
	}
	
	

}
