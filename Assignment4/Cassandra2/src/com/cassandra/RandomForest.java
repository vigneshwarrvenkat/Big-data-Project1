package com.cassandra;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * Random Forest
 * 
 */
public class RandomForest {
	
	private static final int NUM_THREADS=Runtime.getRuntime().availableProcessors();
	//private static final int NUM_THREADS=2;
	public static int C ;
	public static int M;
	
	public static int Ms;
	
	private ArrayList<GenerateTree> trees;
	
	private long time_o;
	
	private int numTrees;
	
	private double update;
	private double progress;
	private int[] importances;
	private HashMap<int[],int[]> estimateOOB;
	private ArrayList<ArrayList<Integer>> Prediction;
	private double error;
	private ExecutorService treePool;
	private ArrayList<int[]> data;
	private ArrayList<int[]> testdata;
	/**
	 * Random forest generation
	 * 
	 * @param numTrees			the number of trees in the forest
	 * @param data				the training data used to generate the forest
	 * @param buildProgress		records the progress of the random forest creation
	 */
	public RandomForest(int numTrees, ArrayList<int[]> data, ArrayList<int[]> t_data ){
		this.numTrees=numTrees;
		this.data=data;
		this.testdata=t_data;
		trees=new ArrayList<GenerateTree>(numTrees);
		update=100/((double)numTrees);
		progress=0;
		StartTimer();
		System.out.println("generating "+numTrees+" trees in a random Forest. . . ");
		System.out.println("total data size is "+data.size());
		System.out.println("number of attributes "+(data.get(0).length-1));
		System.out.println("number of selected attributes "+((int)Math.round(Math.log(data.get(0).length-1)/Math.log(2)+1)));
		//System.out.println("number of selected attributes "+((int)Math.sqrt(data.get(0).length-1)));
//		ArrayList<Datum> master=AssignClassesAndGetAllData(data);
		estimateOOB=new HashMap<int[],int[]>(data.size());
		Prediction = new ArrayList<ArrayList<Integer>>();
	}
	/**
	 * random forest creation is start
	 */
	public String Start() {
		//System.out.println("Number of threads started : "+NUM_THREADS);
		System.out.print("Random Forest under progress..");
		treePool=Executors.newFixedThreadPool(NUM_THREADS);
		for (int t=0;t<numTrees;t++){
			treePool.execute(new CreateTree(data,this,t+1));
			System.out.print(".");
		}treePool.shutdown();
		try {	         
			treePool.awaitTermination(Long.MAX_VALUE,TimeUnit.SECONDS); //effectively infinity
	    } catch (InterruptedException ignored){
	    	System.out.println("interrupted exception in Random Forests");
	    }

		System.out.println("");
		System.out.println("Constructed Trees");
	float accuracy=	TestForest(trees,testdata);
	    
	    System.out.println("Done in "+TimeElapsed(time_o));
	    
	    String result = accuracy+ ","+ time_o;
	    
	    return result;
	}
	
	/**
	 * 
	 */
	private float TestForest(ArrayList<GenerateTree> collec_tree,ArrayList<int[]> test_data ) {
		int correstness = 0 ;int k=0;
		ArrayList<Integer> ActualValues = new ArrayList<Integer>();
		for(int[] rec:test_data){
			ActualValues.add(rec[rec.length-1]);
		}int treee=1;
		for(GenerateTree dt:collec_tree){
			dt.CalculateClasses(test_data,treee);
			Prediction.add(dt.predictions);
			
		}
		for(int i = 0;i<test_data.size();i++){
			ArrayList<Integer> Val = new ArrayList<Integer>();
			for(int j =0;j<collec_tree.size();j++){
				Val.add(Prediction.get(j).get(i));
			}
			int pred = ModeOf(Val);
			if(pred == ActualValues.get(i)){
				correstness=correstness+1;
			}
		}
		System.out.println("Accuracy of Forest is : "+(100*correstness/test_data.size())+"%");
		
		return (100*correstness/test_data.size());
	}
	private int ModeOf(ArrayList<Integer> treePredict) {
		// TODO Auto-generated method stub
		int max=0,maxclass=-1;
		for(int i=0; i<treePredict.size();i++){
			int count = 0;
			for(int j=0;j<treePredict.size();j++){
				if(treePredict.get(j)==treePredict.get(i)){
					count++;
				}
			if(count>max){
				maxclass = treePredict.get(i);
				max = count;
			}
			}
		}
		return maxclass;
	}
	/**
	 * This calculates the forest-wide error rate. 
	 * For each "left out" 
	 * data record, if the class with the maximum count is equal to its actual
	 * class, then increment the number of correct. One minus the number correct 
	 * over the total number is the error rate.
	 */
	private void CalcErrorRate(){
		double N=0;
		int correct=0;
		for (int[] record:estimateOOB.keySet()){
			N++;
			int[] map=estimateOOB.get(record);
			int Class=FindMaxIndex(map);
			if (Class == GenerateTree.GetClass(record))
				correct++;
		}
		error=1-correct/N;
		System.out.println("correctly mapped "+correct);
		System.out.println("Forest error rate % is: "+(error*100));		
	}
	
	
	public void UpdateOOBEstimate(int[] record,int Class){
		if (estimateOOB.get(record) == null){
			int[] map=new int[C];
			//System.out.println("class of record : "+Class);map[Class-1]++;
			estimateOOB.put(record,map);
		}
		else {
			int[] map=estimateOOB.get(record);
			map[Class-1]++;
		}
	}
	/**
	 * This calculates the forest-wide importance levels for all attributes.
	 *
	 */
	private void CalcImportances() {
		importances=new int[M];
		for (GenerateTree tree:trees){
			for (int i=0;i<M;i++)
				importances[i]+=tree.getImportanceLevel(i);
		}
		for (int i=0;i<M;i++)
			importances[i]/=numTrees;


	}
	/** to check the time duration*/
	private void StartTimer(){
		time_o=System.currentTimeMillis();
	}
	/**
	 *  generation  of one 
	 *  decision tree in a thread pool environment.
	 *
	 *
	 */
	private class CreateTree implements Runnable{
		
		private ArrayList<int[]> data;
		
		private RandomForest forest;
		
		private int treenum;
		
		public CreateTree(ArrayList<int[]> data,RandomForest forest,int num){
			this.data=data;
			this.forest=forest;
			this.treenum=num;
		}
		/**
		 * Creates the decision tree
		 */
		public void run() {
			
			trees.add(new GenerateTree(data,forest,treenum));
			
			progress+=update;
		}		
	}
	

	
	/**
	 * Evaluates an incoming data record.
	 * 
	 * 
	 * @param record		the data record to be classified
	 */
	public int Evaluate(int[] record){
		int[] counts=new int[C];
		for (int t=0;t<numTrees;t++){
			int Class=(trees.get(t)).Evaluate(record);
			counts[Class]++;
		}
		return FindMaxIndex(counts);
	}
	/**
	 * return the index that provides the maximum value
	 * 
	 * 
	 */
	public static int FindMaxIndex(int[] arr){
		int index=0;
		int max=Integer.MIN_VALUE;
		for (int i=0;i<arr.length;i++){
			if (arr[i] > max){
				max=arr[i];
				index=i;
			}				
		}
		return index;
	}


	
	public void Stop() {
		treePool.shutdownNow();
	}
	
	
	private static String TimeElapsed(long timeinms){
		int s=(int)(System.currentTimeMillis()-timeinms)/1000;
		int h=(int)Math.floor(s/((double)3600));
		s-=(h*3600);
		int m=(int)Math.floor(s/((double)60));
		s-=(m*60);
		return ""+h+"hr "+m+"m "+s+"s";
	}
}
