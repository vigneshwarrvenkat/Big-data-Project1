package com.cassandra;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/*This Main class reads the data from the Cassandra
 * and processes it to load into the random forest.
 * The forest loads wih the predetermined input 
 * and provides the accuracy and time period as the 
 * output. Later the output is written back to the 
 * cassandra.
 * 
*/
public class Main {

	public static void main(String args[]) {

		Cluster cluster;
		Session session;

		// Connect to the cluster and keyspace "demo"
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("classification");

		ArrayList<int[]> DataInput = new ArrayList<int[]>();

		ArrayList<int[]> DataInput2 = new ArrayList<int[]>();

		ResultSet results = session.execute("SELECT * FROM classify");
		for (Row row : results) {

			String Line = row.getDouble("Average_Bid") + " "
					+ row.getDouble("Average_ask") + " "
					+ row.getDouble("Max_Bid") + " " + row.getDouble("Max_Ask")
					+ " " + row.getDouble("Min_Bid") + " "
					+ row.getDouble("Min_Ask") + " " + row.getInt("Label");

			ArrayList<Integer> Sp = new ArrayList<Integer>();
			int i;
			if (Line != null) {

				// String S = sCurrentLine.replace(',',' ');

				String S = Line.replace('.', '0');

				S = " " + S + " ";
				// System.out.println("data2 : " + sCurrentLine);
				for (i = 0; i < S.length(); i++) {
					if (Character.isWhitespace(S.charAt(i)))
						Sp.add(i);
				}
				int[] DataPoint = new int[Sp.size() - 1];
				for (i = 0; i < Sp.size() - 1; i++) {
					DataPoint[i] = Integer.parseInt(S.substring(Sp.get(i) + 1,
							Sp.get(i + 1)));
					// System.out.println("Data :"+ DataPoint[i]);
				}
				DataInput.add(DataPoint);// for(int
			}
			System.out.println("size :" + DataInput.size());

			System.out.println("output :" + Line);

		}

		System.out.println("Training Data Input");

		// Reading from the test data table...
		ResultSet results2 = session.execute("SELECT * FROM classify");
		for (Row row2 : results2) {

			String Line2 = row2.getDouble("Average_Bid") + " "
					+ row2.getDouble("Average_ask") + " "
					+ row2.getDouble("Max_Bid") + " "
					+ row2.getDouble("Max_Ask") + " "
					+ row2.getDouble("Min_Bid") + " "
					+ row2.getDouble("Min_Ask") + " " + row2.getInt("Label");

			ArrayList<Integer> Sp = new ArrayList<Integer>();
			int i;
			if (Line2 != null) {

				// String S = sCurrentLine.replace(',',' ');

				String S = Line2.replace('.', '0');

				S = " " + S + " ";
				// System.out.println("data2 : " + sCurrentLine);
				for (i = 0; i < S.length(); i++) {
					if (Character.isWhitespace(S.charAt(i)))
						Sp.add(i);
				}
				int[] DataPoint = new int[Sp.size() - 1];
				for (i = 0; i < Sp.size() - 1; i++) {
					DataPoint[i] = Integer.parseInt(S.substring(Sp.get(i) + 1,
							Sp.get(i + 1)));
					// System.out.println("Data :"+ DataPoint[i]);
				}
				DataInput2.add(DataPoint);
				}
			System.out.println("size :" + DataInput.size());

			System.out.println("output2 :" + Line2);

		}

		System.out.println("Training Data Input");

		int numTrees = 90;

		int categ = -1;

		for (int k = 0; k < DataInput.size(); k++) {
			if (DataInput.get(k)[DataInput.get(k).length - 1] < categ) {

				System.out.println(" inside: "
						+ DataInput.get(k)[DataInput.get(k).length - 1]);
				continue;
			} else {
				categ = DataInput.get(k)[DataInput.get(k).length - 1];
			}
		}

		RandomForest RaF = new RandomForest(numTrees, DataInput, DataInput2);
		RaF.C = 2;
		RaF.M = DataInput.get(0).length - 1;
		RaF.Ms = Integer.valueOf((int) (Math.sqrt(RaF.M)));
		String accuracy = RaF.Start();

		String[] result = accuracy.split(",");

		Main.loadData(numTrees, result[0], result[1]);

		cluster.close();
	}

	// Writing the data to the dtabase

	public static void loadData(int num_trees, String accuracy, String duration) {

		PreparedStatement statement = getSession().prepare(
				"INSERT INTO classification.results "
						+ "(id,Num_of_Trees, Accuracy, Time_period) "
						+ "VALUES (?, ?, ?);");

		BoundStatement boundStatement = new BoundStatement(statement);

		getSession()
				.execute(boundStatement.bind(num_trees, accuracy, duration));

	}

}
