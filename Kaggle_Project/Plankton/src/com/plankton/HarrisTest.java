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
 * @author PERRAUDIN Steven <unseen07@gmail.com>
 * @date 3 mai 2010
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
		
		int width     = img.getWidth(); 	// largeur de l'image
		int height    = img.getHeight(); 	// hauteur de l'image
		int[][] input =  new int[width][height];		// tableau 2D [x][y] contenant l'image en niveau de gris (0-255)
		
		
		for(i = 0 ; i < width - 1; i++){
			for(j = 0 ; j < height - 1 ; j++){
				input[i][j] = rgb2gray(img.getRGB(i, j));
			}
		}
		
		//////////////////////
		 
		double sigma   = 1.2;  // parametre du filtre gaussien
		double k       = 0.06; // parametre de la formule de la mesure
		int spacing = 8;    // distance minimum entre 2 coins
		 
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
	
	
	
	static int rgb2gray(int srgb) {
		int r = (srgb >>16 ) & 0xFF;
		int g = (srgb >> 8 ) & 0xFF;
		int b = srgb & 0xFF;
		return (int)(0.299*r + 0.587*g + 0.114*b);
	}
	
	

}
