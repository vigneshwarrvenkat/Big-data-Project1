package com.plankton;


import processing.core.*;
import java.awt.Rectangle;

import org.opencv.video.Video;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Core;
import org.opencv.core.Scalar;
/*
 * Library file to determine the different 
 * scales of the input image file.This class converts
 * input rawimage into the gray scale image.
 */
public class Flow {
	
	private Mat prev;
	private Mat flow;
	private boolean hasFlow = false;
	private double pyramidScale = 0.5;
	private int nLevels = 4;
	private int windowSize = 8;
	private int nIterations = 2;
	private int polyN = 7;
	private double polySigma = 1.5;
	private int runningFlags = Video.OPTFLOW_FARNEBACK_GAUSSIAN;
	private PApplet parent;
	
	
	public Flow(PApplet parent) {
		flow = new Mat();
		this.parent = parent;
	}
	  
	/*
	 * Get method for the width of the gray scale
	 */

	public int width(){
		return flow.width();
	}
	/*
	 * Get method for the height of the gray scale
	 */

	  
	public int height(){
		return flow.height();
	}
	/*
	 * Returns a boolean whether 
	 * the image has a grayscale.
	 */


	public boolean hasFlow(){
		return hasFlow;
	}

	public void calculateOpticalFlow(Mat m) {
		int flags = runningFlags;
	    if (!hasFlow) {
	      prev = m.clone();
	      flags = Video.OPTFLOW_USE_INITIAL_FLOW;
	      hasFlow = true;
	    }
	    Video.calcOpticalFlowFarneback(prev, m, flow, pyramidScale, nLevels, windowSize, nIterations, polyN, polySigma, flags);
	    prev = m.clone();
	}
	/*
	 * Total flow rate in a a particular
	 * region of gray scale
	 */

	public PVector getTotalFlowInRegion(int x, int y, int w, int h) {
		Mat region = flow.submat(y, y+h, x, x+w);
	    Scalar total = Core.sumElems(region);
	    return new PVector((float)total.val[0], (float)total.val[1]);
	}
	/*
	 *Average flow rate in a particular
	 *region of gray scale 
	 */

	public PVector getAverageFlowInRegion(int x, int y, int w, int h) {
	    PVector total = getTotalFlowInRegion(x, y, w, h);
	    return new PVector(total.x/(w*h), total.y/(w*h));
	}
	/*returns the total flow rate 
	 * of the gray sscale image
	 */


	public PVector getTotalFlow() {
	    return getTotalFlowInRegion(0, 0, flow.width(), flow.height());
	}
	/*
	 * Returns the Average flow of the scale 
	 * scale in the image
	 */

	public PVector getAverageFlow() {
	    return getAverageFlowInRegion(0, 0, flow.width(), flow.height());
	}
	/*
	 * Get the flow rate at a particular
	 * point of co-ordinate
	 */

	  
	public PVector getFlowAt(int x, int y){
	    return new PVector((float)flow.get(y, x)[0], (float)flow.get(y, x)[1]);
	}
	
	/*
	 * Draws the image based on the gray 
	 * scale points extracted.
	 */
	  
	public void draw() {
	    int stepSize = 4;

	    for (int y = 0; y < flow.height(); y+=stepSize) {
	      for (int x = 0; x < flow.width(); x+=stepSize) {
	        PVector flowVec = getFlowAt(x,y);
	        parent.line(x, y, x+flowVec.x, y+flowVec.y);
	      }
	    }
	}
	
	public void setPyramidScale(double v){
		pyramidScale = v;
	}
	
	public double getPyramidScale(){
		return pyramidScale;
	}
	
	public void setLevels(int n){
		nLevels = n;
	}
	
	public int getLevels(){
		return nLevels;
	}
	
	public void setWindowSize(int s){
		windowSize = s;
	}
	
	public int getWindowSize(){
		return windowSize;
	}

	public void setIterations(int i){
		nIterations = i;
	}
	
	public int getIterations(){
		return nIterations;
	}
	
	public void setPolyN(int n){
		polyN = n;
	}
	
	public int getPolyN(){
		return polyN;
	}

	public void setPolySigma(double s){
		polySigma = s;
	}
	
	public double getPolySigma(){
		return polySigma;
	}
}