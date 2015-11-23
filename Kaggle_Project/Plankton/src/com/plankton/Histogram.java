package com.plankton;


import processing.core.*;

import org.opencv.core.Mat;

/*
 * Class to calculate the histogram
 * of the matrix created by the gray scale image.
 * Uses Applet to provide the output.
 */

public class Histogram {
	private Mat mat;
	private PApplet parent;
	
	public Histogram(PApplet parent, Mat mat){
		this.mat = mat;
		this.parent = parent;
	}
	
	
	/*
	 * Draws the Histogram based on the input
	 * matrix values. 
	 * Translates the x and Y co-ordinates into
	 * histogram co-ordinates and finds the maximum height
	 * of the height and width of the histogram.
	 * Initially a rectangular frame is created for 
	 * the histogram
	 */
	public void draw(int x, int y, int w, int h) {
		parent.pushMatrix();
		parent.translate(x, y);
		int numBins = mat.height();
		float binWidth = w/(float)numBins;

		for (int i  = 0; i < numBins; i++) {
			float v = (float)mat.get(i, 0)[0];
			parent.rect(i*binWidth, h, binWidth, -h*v);
		}
		parent.popMatrix();
	}
	
	public Mat getMat(){
		return mat;
	}
}

