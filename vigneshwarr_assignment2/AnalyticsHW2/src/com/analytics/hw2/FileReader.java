package com.analytics.hw2;

import java.io.*;
import java.util.ArrayList;

/*FileReader Class
 * Used to read the data from the sample file 
 * to the ArrayList.
 * 
 */

public class FileReader {
	public static final String DATA_FILE = "TestData2.csv";

    public static ArrayList<Record> buildRecords() {
		BufferedReader reader = null;
		FileInputStream fis = null;
	//DataInputStream dis = null;
		ArrayList<Record> records = new ArrayList<Record>();

        try { 
           File f = new File(DATA_FILE);
           fis = new FileInputStream(f); 
           reader = new BufferedReader(new InputStreamReader(fis));;
           
           // read the first record of the file
           String line;
           Record r = null;
           ArrayList<Features> attributes;
           line= reader.readLine();
           while ((line = reader.readLine()) != null) {
        	   
        	   line.trim();
				String[] res = line.split(",");
				
				System.out.println("String " + Integer.parseInt(res[6]));
        	   
          //    StringTokenizer st = new StringTokenizer(line, ",");
              attributes = new ArrayList<Features>();
              r = new Record();
              
              
			 		  
				  attributes.add(new Features("Average of Bid", Float.parseFloat(res[0])));
			 
				
				  
			  
				  attributes.add(new Features("Average of Bid", Float.parseFloat(res[1])));
			 
			  
				  attributes.add(new Features("Max of Bid", Float.parseFloat(res[2])));
			 
			  
			  
				  attributes.add(new Features("Min of Bid", Float.parseFloat(res[3])));
			  
			  
				  attributes.add(new Features("Max of Ask", Float.parseFloat(res[4])));
			 
			  
				  attributes.add(new Features("Min of Ask", Float.parseFloat(res[5])));
			 
			  
			  
				  attributes.add(new Features("Label", Integer.parseInt(res[6])));
				  
				  System.out.println(" hi" + attributes.get(6).getValue());
			 
			  
			    		    
			  r.setAttributes(attributes);
			  records.add(r);
			 
			  
			    }
           System.out.println(" values fromrecord : " + records.get(3).getAttributes().get(6).getValueLabel());
           
          
           

        } 
        catch (IOException e) { 
           System.out.println("Uh oh, got an IOException error: " + e.getMessage()); 
        } 
        catch (Exception e) {
            System.out.println("Uh oh, got an Exception error: " + e.getMessage()); 
        }
        finally { 
           if (fis != null) {
              try {
                 fis.close();
              } catch (IOException ioe) {
                 System.out.println("IOException error trying to close the file: " + ioe.getMessage()); 
              }
           }
        }
		return records;
	}
}