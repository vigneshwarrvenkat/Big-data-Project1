package com.plankton;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadInput {
//method to take the txt fle as input and pass those values to random forests
  BufferedReader BR = null;
	String path;
	public ReadInput(String path){
		this.path=path;
	}
	public ArrayList<int[]> CreateInput(String path){

		ArrayList<int[]> DataInput = new ArrayList<int[]>();
		
		try {
		 
		String sCurrentLine;
		BR = new BufferedReader(new FileReader(path));

		while ((sCurrentLine = BR.readLine()) != null) {
			ArrayList<Integer> Sp=new ArrayList<Integer>();int i;
			if(sCurrentLine!=null){
				
				String S = sCurrentLine.replace(',',' ');
				
				S = S.replace('.','0');
				
				
				S =" "+S+" ";
			//	System.out.println("data2 : " + sCurrentLine);
				for(i=0;i<S.length();i++){
					if(Character.isWhitespace(S.charAt(i)))
						Sp.add(i);
				}int[] DataPoint = new int[Sp.size()-1];
				for(i=0;i<Sp.size()-1;i++){
					try{
					DataPoint[i]=Integer.parseInt(S.substring(Sp.get(i)+1, Sp.get(i+1)));
					}
					catch(NumberFormatException ex){
						
					}
					//System.out.println("Data :"+ DataPoint[i]);
				}DataInput.add(DataPoint);//for(int t=0;t<DataInput.get(0).length;t++){System.out.print(DataInput.get(0)[t]+",");}System.out.println("");
			}System.out.println("size :" +DataInput.size());
			
			
			
		}

		for(int j=0;j<DataInput.size();j++){
			//System.out.println(" from arraylist : " + DataInput.get(0)[0]);
		}
		
		
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		try {
			if (BR != null)BR.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	return DataInput;
}
}


