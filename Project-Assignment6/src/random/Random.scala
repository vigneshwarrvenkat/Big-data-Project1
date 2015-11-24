package random;
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import datastax.spark.connector._
import org.apache.spark.mllib.regression.LabeledPoint



object Random
{

  def main(args: Array[String]) 

  {

   

    val randomforest = new SparkConf()

    randomforest.setMaster("local[*]") 

    randomforest.set("spark.cassandra.connection.host", "localhost")

    

   //Creating spark Context...

    val spark = new SparkContext(randomforest)

    val sqlContext = new SQLContext(spark)

    // Load training data in RDD 

    val train = spark.cassandraTable("Classification", "train")

    val training : RDD[LabeledPoint] = train.map 

    { 

      row  => 

        {  (LabeledPoint(row.getInt("label").toDouble, 

            Vectors.sparse(7, Array(0, 1, 2, 3, 4, 5, 6), Array

                (

                  row.getInt("minBid"),  row.getInt("maxBid"), row.getInt("minAsk"),row.getInt("maxAsk"), 
row.getInt("AvgAsk"), row.getInt("AvgBid"), row.getInt("Spread"), ))))

        }

    }

   val trainings = new sqlContext.createDataFrame(training) 

    //Load testing data in RDD 

    val test = spark.cassandraTable("Classification", "test")

    val testing : RDD[LabeledPoint] = test.map 

    {

      row  => 

        { 

            (LabeledPoint(row.getInt("label").toDouble, 

            Vectors.sparse(7, Array(0, 1, 2, 3, 4, 5, 6), Array(

                   row.getInt("minBid"),  row.getInt("maxBid"), row.getInt("minAsk"),row.getInt("maxAsk"), 
row.getInt("AvgAsk"), row.getInt("AvgBid"), row.getInt("Spread"), ))))

        }

    }

  val testings = new sqlContext.createDataFrame(testing)   

    
val valuelabel = new StringIndexer().setInputCol("label").setOutputCol("outlabel") 
    

    //Train the RandomForest.

    val numClasses = 2

    val categoricalFeaturesInfo = Map[Int, Int]()

    val numTrees = 10 

    val featureSubsetStrategy = "auto" 

    val impurity = "gini"

    val maxDepth = 4

    val maxBins = 32

    
//Random forest model
    
    val forestmodel = RandomForest.trainClassifier(training, numClasses,categoricalFeaturesInfo,numTrees, 
        featureSubsetStrategy,impurity,maxDepth,maxBins)
                                            
   //Creating pipeline                                         
                                            
     val pipe = new Pipeline()
  .setStages(Array(valuelabel,forestmodel))
  
  val pipemodel = pipe.fit(training)
  
  
  val predict = pipemodel.transform(testing)
  
  val check = new MulticlassClassificationEvaluator()
  
  val accuracy = check.evaluate(predict)
  
  println("accurate:" + (1.0 - accuracy))

    
//Saving to Cassandra
  val output = spark.parallelize(accuracy,Date.getTime())
  
  output.saveToCassandra ("Classification", "Classify",SomeColumns("accuracy","time"))
  
  }
}
  
  
   
   

  