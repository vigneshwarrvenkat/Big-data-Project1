/*Big Data Analysis - Assignment1
 * 
 * Predicting the currency fluctuations- USD
 * 
 * 
 * */
package com.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Analysis {

	/* Main Function */

	public static void main(String[] args) throws IOException {

		Analysis obj = new Analysis();
		obj.run();

	}

	/* Function which reads the file */

	public void run() throws IOException {

		String csvFile = "resources/USDCAD-2014-01.csv";
		String csvFile1 = "resources/TestData1.csv";
		BufferedReader br = null;
		BufferedWriter bw = null;
		String line = "";
		String cvsSplitBy = ",";

		int cnt = 0, label_bid, label_ask;
		float current = 0.0f, previous = 0.0f, max_bid = 0.0f, min_bid = 0.0f, max_ask = 0.0f, min_ask = 0.0f;
		float sum1 = 0.0f, sum2 = 0.0f;
		br = new BufferedReader(new FileReader(csvFile));
		bw = new BufferedWriter(new FileWriter(csvFile1));

		/* Array list to store the previous values for predicting the labels */
		ArrayList<Float> label = new ArrayList<Float>();
		label.add(0.0f);
		label.add(0.0f);
		label.add(0.0f);
		label.add(0.0f);
		label.add(0.0f);
		label.add(0.0f);

		bw.append("Average of Bid" + "," + "Average of Ask" + ","
				+ "Max of Bid" + "," + "Min of Bid" + "," + "Max of Ask" + ","
				+ "Min of Ask" + "," + "Label_Bid" + "," + "Label_Ask");
		bw.append('\n');

		/* reading the file */

		while ((line = br.readLine()) != null) {

			// use comma as separator
			String[] country = line.split(cvsSplitBy);

			country[1].replaceAll(" ", "");
			
			System.out.println("label: " + country[1]);

			current = Float.parseFloat(country[1].substring(12, 14));
			

			/* Condition to check one minute block ended */

			if (current != previous) {

				System.out.println("array0:" + label.get(0) + "aray1: "
						+ label.get(2) + "size:" + label.size());

				System.out.println("label: " + label.get(0));
				System.out.println("sum1: " + sum1 / cnt);
				System.out.println("labe2: " + label.get(2));
				System.out.println("sum2: " + max_bid);

				/*
				 * Condition for label prediction when the average value of bid
				 * for one minute is less than the next value of one minute,
				 * When the max value of bid for one minute is less than the
				 * next max value for one minute the LABEL_BID becomes true.
				 * else LABEL_BID becomes false
				 */
				if ((Float.compare(label.get(0), sum1 / cnt) < 0)
						&& (Float.compare(label.get(2), max_bid) < 0)) {

					label_bid = 1;

				}

				else {
					label_bid = 0;

				}

				/*
				 * Condition for label prediction when the average value of ask
				 * for one minute is less than the next value of one minute,
				 * When the max value of ask for one minute is less than the
				 * next max value for one minute the LABEL_ASK becomes true.
				 * else LABEL_ASK becomes false
				 */

				if ((Float.compare(label.get(1), sum2 / cnt) < 0)
						&& (Float.compare(label.get(4), max_ask) < 0)) {

					label_ask = 1;

				}

				else {
					label_ask = 0;
				}

				label.clear();

				label.add(sum1 / cnt);
				label.add(sum2 / cnt);
				label.add(max_bid);
				label.add(min_bid);
				label.add(max_ask);
				label.add(min_ask);

				/*
				 * Writing the features and the label to the file
				 */

				bw.append(Float.toString(sum1 / cnt) + ","
						+ Float.toString(sum2 / cnt) + ","
						+ Float.toString(max_bid) + ","
						+ Float.toString(min_bid) + ","
						+ Float.toString(max_ask) + ","
						+ Float.toString(min_ask) + ","
						+ Integer.toString(label_bid) + ","
						+ Integer.toString(label_ask));
				bw.append('\n');

				sum1 = 0;
				sum2 = 0;
				max_bid = 0;
				min_bid = 0;
				min_ask = 0;
				max_ask = 0;
				cnt = 0;

			}
			
			
			

			/*
			 * This block is used to sum up and find the average of one minute
			 * of data The average is also usd to calculate the max and min bid
			 * and ask price for that particular one minute of data
			 */

			sum1 += Float.parseFloat(country[2]);
			sum2 += Float.parseFloat(country[3]);
			cnt++;

			// System.out.println("bid:"+ Float.parseFloat(country[2])+"ask:"+
			// Float.parseFloat(country[3]));
			System.out
					.println("sum1:" + sum1 + "sum 2: " + sum2 + "cnt:" + cnt);

			if (max_bid < Float.parseFloat(country[2])) {

				max_bid = Float.parseFloat(country[2]);
				// System.out.println("max_bid:: "+max_bid);
			}

			else {
				min_bid = Float.parseFloat(country[2]);
			}

			if (min_bid == 0) {
				min_bid = Float.parseFloat(country[2]);
			} else if (min_bid > Float.parseFloat(country[2])) {

				min_bid = Float.parseFloat(country[2]);
			}

			// System.out.println("min_bid:: "+min_bid);

			if (max_ask < Float.parseFloat(country[3])) {

				max_ask = Float.parseFloat(country[3]);
				// System.out.println("max:: "+max_ask);
			}

			else {
				min_ask = Float.parseFloat(country[3]);
			}

			/* Finding the minimum and maximum value */

			if (min_ask == 0) {
				min_ask = Float.parseFloat(country[3]);
			} else if (min_ask > Float.parseFloat(country[3])) {

				min_ask = Float.parseFloat(country[3]);
			}

			// System.out.println("min_ask:: "+min_ask);

			// current= Float.parseFloat(country[1].substring(12,14));
			previous = current;

		}

		System.out.println("label last: " + label.get(0));
		System.out.println("sum1 last: " + sum1 / cnt);
		System.out.println("labe2 last: " + label.get(2));
		System.out.println("sum2 last: " + max_bid);

		if ((Float.compare(label.get(0), sum1 / cnt) < 0)
				&& (Float.compare(label.get(2), max_bid) < 0)) {

			label_bid = 1;

		}

		else {
			label_bid = 0;
		}

		if ((Float.compare(label.get(1), sum2 / cnt) < 0)
				&& (Float.compare(label.get(4), max_ask) < 0)) {

			label_ask = 1;

		}

		else {
			label_ask = 0;
		}

		bw.append(Float.toString(sum1 / cnt) + "," + Float.toString(sum2 / cnt)
				+ "," + Float.toString(max_bid) + "," + Float.toString(min_bid)
				+ "," + Float.toString(max_ask) + "," + Float.toString(min_ask)
				+ "," + Integer.toString(label_bid) + ","
				+ Integer.toString(label_ask));
		bw.append('\n');

		bw.close();
		br.close();
		
            
        

		System.out.println("Done");
	}

}