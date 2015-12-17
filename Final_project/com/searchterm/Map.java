package com.searchterm;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.StringUtils;

public class Map extends Mapper {

	    @Override
	    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	    	TfIdf tf = new TfIdf("C:/Users/Vignesh/workspace/SearchTerm/Big_Data");
			
		    private IntWritable ONE = new IntWritable(1);
		    
		    int neighbors = context.getConfiguration().getInt("neighbors", 2);
	        String[] tokens = value.toString().split("\\s+");
	        if (tokens.length > 1) {
	          for (int i = 0; i < tokens.length; i++) {
	              Tf.setWord(tokens[i]);

	             int start = (i - neighbors < 0) ? 0 : i - neighbors;
	             int end = (i + neighbors >= tokens.length) ? tokens.length - 1 : i + neighbors;
	              for (int j = start; j <= end; j++) {
	                  if (j == i) continue;
	                  tf.setNeighbor(tokens[j]);
	                   context.write(tf, ONE);
	              }
	          }
	      }
	  }
		    
		    
	    	
	    }
	        
	
	
	
}

	
