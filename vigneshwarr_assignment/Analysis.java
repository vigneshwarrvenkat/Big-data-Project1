 /*Big Data Analysis - Assignment1
  * 
  * Predicting the currency fluctuations- USD
  * 
  * 
  * */
package com.analysis;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Analysis {
	
	 /*Function which writes the file*/

  private static void callWrite(float f1,float f2,float f3,float f4, int cnt) throws IOException
  {
    BufferedWriter bw = null;
    String csvFile1 = "resources/TestData1.csv";
    BufferedReader br = null;
    String csvFile = "resources/Test.csv";
    String line = "";
    String cvsSplitBy = ",";
        
        br = new BufferedReader(new FileReader(csvFile));
        bw = new BufferedWriter(new FileWriter(csvFile1));
        
        /*Reading the file*/
        
        while ((line = br.readLine()) != null) {

                // use comma as separator
          String[] country = line.split(cvsSplitBy);
         // for(int i=0;i<=cnt;i++)
          {
        	 // System.out.println("max: "+f3+"min:"+f4);
           bw.write(line+","+Float.toString(f1)+","+Float.toString(f2)+","+Float.toString(f3)+","+Float.toString(f4));
           bw.append('\n');
            //System.out.println(Float.toString(f1)+" - "+Float.toString(f2));
          }
          
        }
        
    br.close();
    bw.close();
  }
  
  /*Main Function*/
  
  public static void main(String[] args) throws IOException {

  Analysis obj = new Analysis();
  obj.run();

  }
  
  /*Function which reads the file*/

  public void run() throws IOException {

  String csvFile = "resources/Test.csv";
  BufferedReader br = null;
  
  String line = "";
  String cvsSplitBy = ",";

  
int cnt=0; 
float current=0.0f,previous=0.0f,max=0.0f,min=0.0f;
float sum1=0.0f,sum2=0.0f;
    br = new BufferedReader(new FileReader(csvFile));
    float[] Max_value = new float[2];
    
    /*reading the file*/
    
    while ((line = br.readLine()) != null) {

            // use comma as separator
      String[] country = line.split(cvsSplitBy);
      
      
      current= Float.parseFloat(country[1].substring(12,14));    
      
      
      if(previous!=0.0 && current==previous )
      {
    	  
    	  /*Calculating the Sum*/
    	  
        sum1+=Float.parseFloat(country[2]);
        sum2+=Float.parseFloat(country[3]);
        cnt++;
        //System.out.println(country[1]);
        
        if(max<Float.parseFloat(country[3])){
        	
        	System.out.println("hi: "+max);
      	  
      	  max=Float.parseFloat(country[3]);
         }
        
        else{
        	min=Float.parseFloat(country[3]);
        }
        
        
        /*Finding the minimum and maximum value*/
        
       
        
        if(min==0){
      	  min=Float.parseFloat(country[3]);
        }
        else if(min>Float.parseFloat(country[3])){
        	
        	min=Float.parseFloat(country[3]);
        }
                
      }
      
      else{
    	  
    	  /* Calling to write the file*/
    	  
    	  callWrite(sum1/cnt,sum2/cnt,max,min,cnt);
    	  max=0;
    	  min=0;
    	  
      }
      
      previous=current;
           
    }
    
      
   // System.out.println("Sum1: "+sum1+"\tSum1 Avg:"+(sum1/cnt));
    //System.out.println("Sum2: "+sum2+"\tSum2 Avg:"+(sum2/cnt));
    
  
    br.close();
    
  System.out.println("Done");
  }

}