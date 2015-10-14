package com.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GenerateTree {


	/** Instead of checking each index we'll skip every INDEX_SKIP indices unless there's less than MIN_SIZE_TO_CHECK_EACH*/
	private static final int INDEX_SKIP=3;
	/** If there's less than MIN_SIZE_TO_CHECK_EACH points, we'll check each one */
	private static final int MIN_SIZE_TO_CHECK_EACH=10;
	/** If the number of data points is less than MIN_NODE_SIZE, we won't continue splitting, we'll take the majority vote */
	private static final int MIN_NODE_SIZE=5;
	/** the number of data records */
	private int N;
	/** the number of samples left out of the boostrap of all N to test error rate 
	 */
	private int testN;
	/** Of the testN, the number that were correctly identified */
	private int correct;
	/** an estimate of the importance of each attribute in the data record
	*/
	private int[] importances;
	/** This is the root of the Decision Tree */
	private TreeNode root;
	/** This is a pointer to the Random Forest this decision tree belongs to */
	private RandomForest forest;
	/** This keeps track of all the predictions done by this tree */
	public ArrayList<Integer> predictions;

	/**
	 * This constructs a decision tree from a data matrix.
	 * It first creates a bootstrap sample, the train data matrix, as well as the left out records, 
	 * the test data matrix. Then it creates the tree, then calculates the variable importances (not essential)
	 * and then removes the links to the actual data (to save memory)
	 */
	public GenerateTree(ArrayList<int[]> data,RandomForest forest,int num){
		this.forest=forest;
		N=data.size();
		importances=new int[RandomForest.M];
		predictions = new ArrayList<Integer>();
	
		
		ArrayList<int[]> train=new ArrayList<int[]>(N); 
		ArrayList<int[]> test=new ArrayList<int[]>();
		BootStrapSample(data,train,test,num);
		testN=test.size();
		correct=0;	
		
		root=CreateTree(train,num);//creating tree using training data
		FlushData(root);
	}
	/**
	 * Responsible for gauging the error rate of this tree and 
	 * calculating the importance values of each attribute
	 */
	private void CalcTreeVariableImportanceAndError(ArrayList<int[]> test,int nv) {
		correct=CalcTreeErrorRate(test,nv);
		CalculateClasses(test, nv);
		
		for (int m=0;m<RandomForest.M;m++){
			ArrayList<int[]> data=RandomlyPermuteAttribute(CloneData(test),m);
			int correctAfterPermute=0;
			for (int[] arr:data){
				int prediction=Evaluate(arr);
				if (prediction == GetClass(arr))
					correctAfterPermute++;
			}
			importances[m]+=(correct-correctAfterPermute);
		}		

	}

	/**
	 * Calculates the tree error rate,
	 * displays the error rate to console,
	 * and updates the total forest error rate
	 */
	public int CalcTreeErrorRate(ArrayList<int[]> test,int nu){		
		int correct=0;		
		for (int[] record:test){
			int Class=Evaluate(record);
			forest.UpdateOOBEstimate(record,Class);
			int k = record[record.length-1];

				if (Evaluate(record) == k)
				correct++;
		}
		
		double err=1-correct/((double)test.size());
//		System.out.print("\n");
		System.out.println("Number of correct  = "+correct+", out of :"+test.size());
		System.out.println("Test-Data error rate of tree "+nu+"  is: "+(err*100)+" %");
		
		return correct;
	}
	/**
	 * This method will get the classes and will return the updates
	 * 
	 */
	public ArrayList<Integer> CalculateClasses(ArrayList<int[]> test,int nu){
		ArrayList<Integer> corest = new ArrayList<Integer>();int k=0;int korect = 0;
		for(int[] record : test){
			int kls = Evaluate(record);
			corest.add(kls);
			int k1 = record[record.length-1];
			if (kls==k1)
				korect++;
		}
		predictions= corest;
		return corest;
		
	}
	/**
	 * This will classify a new data record by using tree
	 * recursion and testing the relevant variable at each node.
	 */
	public int Evaluate(int[] record){//need to write this 
		TreeNode evalNode=root;
		
		while (true){
			if (evalNode.isLeaf)
				return evalNode.Class;
			if (record[evalNode.splitAttributeM] <= evalNode.splitValue)
				evalNode=evalNode.left;
			else
				evalNode=evalNode.right;			
		}
	}
	/**
	 *
	 */
	private ArrayList<int[]> RandomlyPermuteAttribute(ArrayList<int[]> test,int m){
		int num=test.size()*2;
		for (int i=0;i<num;i++){
			int a=(int)Math.floor(Math.random()*test.size());
			int b=(int)Math.floor(Math.random()*test.size());
			int[] arrA=test.get(a);
			int[] arrB=test.get(b);
			int temp=arrA[m];
			arrA[m]=arrB[m];
			arrB[m]=temp;
		}
		return test;
	}
	/**
	 * Creates a copy of the data matrix
	 */
	private ArrayList<int[]> CloneData(ArrayList<int[]> data){
		ArrayList<int[]> clone=new ArrayList<int[]>(data.size());
		int M=data.get(0).length;
		for (int i=0;i<data.size();i++){
			int[] arr=data.get(i);
			int[] arrClone=new int[M];
			for (int j=0;j<M;j++){
				arrClone[j]=arr[j];
			}
			clone.add(arrClone);
		}
		return clone;
	}
	/**
	 * This creates the decision tree according to the specifications of random forest trees. 
	 * @return: the TreeNode object that stores information about the parent node of the created tree
	 */
	private TreeNode CreateTree(ArrayList<int[]> train, int ntree){
		TreeNode root=new TreeNode();
		root.data=train;
		//System.out.println("creating ");
		RecursiveSplit(root,ntree);
		return root;
	}
	
	/**
	 * Step A
	 * Check if this node is a leaf, if so, it will mark isLeaf true
	 * and mark Class with the leaf's class. The function will not
	 * recurse past this point.
	 * Step B
	 * Create a left and right node and keep their references in
	 * this node's left and right fields. For debugging purposes,
	 * the generation number is also recorded. The attributes are
	 * now chosen by the function...
	 */
	private void RecursiveSplit(TreeNode parent, int Ntreenum){
		
		
		if (!parent.isLeaf){
			
		
			Integer Class=CheckIfLeaf(parent.data);
			if (Class != null){
				parent.isLeaf=true;
				parent.Class=Class;
				return;
			}
			
			
			int Nsub=parent.data.size();
			
			parent.left=new TreeNode();
			parent.left.generation=parent.generation+1;
			parent.right=new TreeNode();
			parent.right.generation=parent.generation+1;
			ArrayList<Integer> vars=GetVarsToInclude();
			DoubleWrap lowestE=new DoubleWrap(Double.MAX_VALUE);

			
			for (int m:vars){
				
				SortAtAttribute(parent.data,m);
				
				ArrayList<Integer> indicesToCheck=new ArrayList<Integer>();
				for (int n=1;n<Nsub;n++){
					int classA=GetClass(parent.data.get(n-1));
					int classB=GetClass(parent.data.get(n));
					if (classA != classB)
						indicesToCheck.add(n);
				}
				
				if (indicesToCheck.size() == 0){
					parent.isLeaf=true;
					parent.Class=GetClass(parent.data.get(0));
					continue;
				}

				if (indicesToCheck.size() > MIN_SIZE_TO_CHECK_EACH){
					for (int i=0;i<indicesToCheck.size();i+=INDEX_SKIP){
	//					
						
						CheckPosition(m,indicesToCheck.get(i),Nsub,lowestE,parent,Ntreenum);
						if (lowestE.d == 0)
							break;
					}
				}
				else {
					for (int n:indicesToCheck){
						CheckPosition(m,n,Nsub,lowestE,parent,Ntreenum);
						if (lowestE.d == 0)
							break;
					}
				}

				if (lowestE.d == 0)
					break;
			}
//			
			if (parent.left.data.size() == 1){
				parent.left.isLeaf=true;
				parent.left.Class=GetClass(parent.left.data.get(0));							
			}
			else if (parent.left.data.size() < MIN_NODE_SIZE){
				parent.left.isLeaf=true;
				
				parent.left.Class=GetMajorityClass(parent.left.data);	
			}
			else {
				Class=CheckIfLeaf(parent.left.data);
				if (Class == null){
					parent.left.isLeaf=false;
					parent.left.Class=null;
//					System.out.println("make branch left: m:"+m);
				}
				else {
					parent.left.isLeaf=true;
					parent.left.Class=Class;
				}
			}
				//------------Right Child
			if (parent.right.data.size() == 1){
				parent.right.isLeaf=true;
				parent.right.Class=GetClass(parent.right.data.get(0));								
			}
			else if (parent.right.data.size() < MIN_NODE_SIZE){
				parent.right.isLeaf=true;
				parent.right.Class=GetMajorityClass(parent.right.data);	
			}
			else {
				Class=CheckIfLeaf(parent.right.data);
				if (Class == null){
//					System.out.println("make branch right: m:"+m);
					parent.right.isLeaf=false;
					parent.right.Class=null;
				}
				else {
					parent.right.isLeaf=true;
					parent.right.Class=Class;
				}
			}
			
			if (!parent.left.isLeaf)
				RecursiveSplit(parent.left,Ntreenum);
//			else {				
//				System.out.print("left leaf! Class:"+parent.left.Class+"  ");
//				PrintOutClasses(parent.left.data);
//			}
			if (!parent.right.isLeaf)
				RecursiveSplit(parent.right,Ntreenum);
//			else {				
//				System.out.print("leaf right! Class:"+parent.right.Class+"  ");
//				PrintOutClasses(parent.right.data);
//			}
		}
	}
	/**
	 * Given a data matrix, return the most popular Y value (the class)
	*/
	private int GetMajorityClass(List<int[]> data){
		int[] counts=new int[RandomForest.C];
		for (int[] record:data){
			int Class=record[record.length-1];
			
			try{
			counts[Class-1]++;
			}
			catch(ArrayIndexOutOfBoundsException exception) {
			    
			}
		}
		int index=-99;
		int max=Integer.MIN_VALUE;
		for (int i=0;i<counts.length;i++){
			if (counts[i] > max){
				max=counts[i];
				index=i+1;
			}				
		}
		return index;
	}

	/** the entropy is calculated. The lower sub-entropy
	 * and upper sub-entropy are then weight averaged to obtain the total entropy. 
	 * 
	 * @param m				the attribute to split on
	 * @param n				the index to check
	 * @param Nsub			the number of records in the data matrix
	 * @param lowestE		the minimum entropy to date
	 * @param parent		the parent node
	 * @return				the entropy of this split
	 */
	private double CheckPosition(int m,int n,int Nsub,DoubleWrap lowestE,TreeNode parent, int nTre){
		
		if (n < 1) //exit conditions
			return 0;
		if (n > Nsub)
			return 0;
		
		List<int[]> lower=GetLower(parent.data,n);
		List<int[]> upper=GetUpper(parent.data,n);
		if (lower == null)
			System.out.println("lower list null");	
		if (upper == null)
			System.out.println("upper list null");
		double[] pl=GetClassProbs(lower);
		double[] pu=GetClassProbs(upper);
		double eL=CalcEntropy(pl);
		double eU=CalcEntropy(pu);
	
		double e=(eL*lower.size()+eU*upper.size())/((double)Nsub);

		if (e < lowestE.d){			
			lowestE.d=e;
//			System.out.print("-");
			parent.splitAttributeM=m;
			parent.splitValue=parent.data.get(n)[m];
			parent.left.data=lower;	
			parent.right.data=upper;
		}
		return e;//entropy
	}
	/**
	 * Given a data record, return the Y value - take the last index
	 * 
	 * @param record		the data record
	 * @return				its y value (class)
	 */
	public static int GetClass(int[] record){
		return record[RandomForest.M];
	}
	/**
	 * Given a data matrix, check if all the y values are the same. If so,
	 * return that y value, null if not 
	 */
	private Integer CheckIfLeaf(List<int[]> data){
//		System.out.println("checkIfLeaf");
		boolean isLeaf=true;
		int ClassA=GetClass(data.get(0));
		for (int i=1;i<data.size();i++){			
			int[] recordB=data.get(i);
			if (ClassA != GetClass(recordB)){
				isLeaf=false;
				break;
			}
		}
		if (isLeaf)
			return GetClass(data.get(0));
		else
			return null;
	}
	/**
	 * Split a data matrix and return the upper portion.
	 */
	private List<int[]> GetUpper(List<int[]> data,int nSplit){
		int N=data.size();
		List<int[]> upper=new ArrayList<int[]>(N-nSplit);
		for (int n=nSplit;n<N;n++)
			upper.add(data.get(n));
		return upper;
	}
	/**
	 * Split a data matrix and return the lower portion
	 */
	private List<int[]> GetLower(List<int[]> data,int nSplit){
		List<int[]> lower=new ArrayList<int[]>(nSplit);
		for (int n=0;n<nSplit;n++)
			lower.add(data.get(n));
		return lower;
	}
	
	/**
	 * Sorts a data matrix by an attribute from lowest record to highest record
	 */
	@SuppressWarnings("unchecked")
	private void SortAtAttribute(List<int[]> data,int m){
		Collections.sort(data,new Compare(m));
	}
	/**
	 * Given a data matrix, return a probabilty mass function representing 
	 * the frequencies of a class in the matrix (the y values)
	 */
	private double[] GetClassProbs(List<int[]> records){
		
		double N=records.size();
		
		int[] counts=new int[RandomForest.C];
		
		for (int[] record:records){
			
			//System.out.println("getclass: " + GetClass(record)+ RandomForest.C);
			try{
			counts[GetClass(record)-1]++;
			}
			catch(ArrayIndexOutOfBoundsException exception) {
			    
			}
			
		}
		double[] ps=new double[RandomForest.C];
		for (int c=0;c<RandomForest.C;c++)
			ps[c]=counts[c]/N;
		return ps;
	}
	/** ln(2) */
	private static final double logoftwo=Math.log(2);
	/**
	 *Calculating Entropy
	 */
	private double CalcEntropy(double[] ps){
		double e=0;		
		for (double p:ps){
			if (p != 0) //otherwise it will divide by zero - see TSK p159
				e+=p*Math.log(p)/logoftwo;
		}
		return -e; //according to TSK p158
	}
	/**
	 * Of the M attributes, select {@link RandomForest#Ms Ms} at random.
	 * 
	 * @return		The list of the Ms attributes' indices
	 */
	private ArrayList<Integer> GetVarsToInclude() {
		boolean[] whichVarsToInclude=new boolean[RandomForest.M];

		for (int i=0;i<RandomForest.M;i++)
			whichVarsToInclude[i]=false;
		
		while (true){
			int a=(int)Math.floor(Math.random()*RandomForest.M);
			whichVarsToInclude[a]=true;
			int N=0;
			for (int i=0;i<RandomForest.M;i++)
				if (whichVarsToInclude[i])
					N++;
			if (N == RandomForest.Ms)
				break;
		}
		
		ArrayList<Integer> shortRecord=new ArrayList<Integer>(RandomForest.Ms);
		
		for (int i=0;i<RandomForest.M;i++)
			if (whichVarsToInclude[i])
				shortRecord.add(i);
		return shortRecord;
	}

	/**
	 * Create a boostrap sample of a data matrix
	 */
	private void BootStrapSample(ArrayList<int[]> data,ArrayList<int[]> train,ArrayList<int[]> test,int numb){
		ArrayList<Integer> indices=new ArrayList<Integer>(N);
		for (int n=0;n<N;n++)
			indices.add((int)Math.floor(Math.random()*N));
		ArrayList<Boolean> in=new ArrayList<Boolean>(N);
		for (int n=0;n<N;n++)
			in.add(false); //have to initialize it first
		for (int num:indices){
			train.add((data.get(num)).clone());
			in.set(num,true);
		}//System.out.println("created training-data for tree : "+numb);
		for (int i=0;i<N;i++)
			if (!in.get(i))
				test.add((data.get(i)).clone());//System.out.println("created testing-data for tree : "+numb);//everywhere its set to false we get those to test data
		

	}
	
	private void FlushData(TreeNode node){
		node.data=null;
		if (node.left != null)
			FlushData(node.left);
		if (node.right != null)
			FlushData(node.right);
	}

	
	public int getNumCorrect(){
		return correct;
	}
	
	public int getTotalNumInTestSet(){
		return testN;
	}
	
	public int getImportanceLevel(int m){
		return importances[m];
	}

}

