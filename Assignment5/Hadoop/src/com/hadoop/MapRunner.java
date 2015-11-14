package com.hadoop;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

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

public class MapRunner extends Mapper {
	
	
	public void map(Object key, Text value, Context context, ArrayList<int[]> train, int ntree
            ) throws IOException, InterruptedException   {
		
   	GenerateTree create = new GenerateTree();
		
	create.CreateTree(train,ntree);

	
	Gson gson = new Gson();	
	String json = gson.toJson(create.CreateTree(train,ntree));
	
	context.write(1, json);
	
}

	
	public static class IntSumReducer
    extends Reducer<Text,IntWritable,Text,IntWritable> {
 private IntWritable result = new IntWritable();

 public void reduce(Text key, Iterable<IntWritable> values,
                    Context context
                    ) throws IOException, InterruptedException {
   
	 String S="";
	 
	 for (IntWritable val : values) {
	         S += val.get();
	      }
	 
	//Writing to the database 
	 MainDriver.loadData(S);
	     
	 
 }
}
	
	
}
