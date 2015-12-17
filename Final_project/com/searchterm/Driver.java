
package com.searchterm;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

public class Driver extends Configured implements Tool {
	 static String input;
  public static void main(String args[]) throws Exception {
    int res = ToolRunner.run(new TfIdf(), args);
    
    Scanner scan = new Scanner(System.in);
    System.out.println("Please provide the input term");
    input = scan.nextLine();
    
    System.exit(res);
  }

public int run(String[] arg0) throws Exception {
	// TODO Auto-generated method stub
	
	String word;
	Double[] corpusdata;
	
	Configuration conf = getConf();
   
	Job job = new Job(conf, this.getClass().toString());
	job.setInputFormatClass(TextInputFormat.class);
	 job.setMapperClass(Map.class);
	    job.setCombinerClass(Reduce.class);
	    job.setReducerClass(Reduce.class);
	

	    return job.waitForCompletion(true) ? 0 : 1;
}



}
