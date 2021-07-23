package com.imdany


import org.apache.spark.sql.CustomFunctions.UUID_CUSTOM
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.expr
import org.apache.spark.sql.functions.udf

import java.util.concurrent.TimeUnit.NANOSECONDS

object Main {

  // Spark Session
  val spark = SparkSession.builder().appName("ImDany").master("local[*]").getOrCreate()

  // Registering UDF
  def uuid = udf(() => java.util.UUID.randomUUID().toString)
  spark.sqlContext.udf.register("uuid_udf", uuid)


  // For time calculation
  def average(x: Array[Long]): Long = x.sum / x.length

  def time[T](f: => T): Long = {
    val start = System.nanoTime()
    val ret = f
    val end = System.nanoTime()

    println(s"Time taken: ${NANOSECONDS.toMillis(end - start)} ms")
    NANOSECONDS.toMillis(end - start)
  }


  // Run UDFS
  def runUDF(nRows: Long, runID: String): Long = {
    println("[+] UDF:")
    time({
      val data = spark.range(nRows).toDF("ID").withColumn("uuid_udf", expr("uuid_udf()"))
      data.write.format("parquet").mode("overwrite").save(s"/tmp/test/UDF/${runID}")
    })
  }

  //Run Catalyst
  def runCatalyst(nRows: Long, runID: String): Long = {
    println("[+] Catalyst Expression:")
    time({
      val data = spark.range(nRows).toDF("ID").withColumn("uuid", UUID_CUSTOM())
      data.write.format("parquet").mode("overwrite").save(s"/tmp/test/catalyst/${runID}")
    })
  }



  def main(args: Array[String]): Unit = {

    val nRows: Long = args(0).toLong
    val nJobs: Int = args(1).toInt

    var udfRuns: Array[Long] = Array()
    var catalystRuns: Array[Long] = Array()

    for (i <- 1 to nJobs) {

      var runID: String = java.util.UUID.randomUUID().toString()

      udfRuns = udfRuns :+ runUDF(nRows, runID)
      catalystRuns = catalystRuns :+ runCatalyst(nRows, runID)
    }

    println(s"---------------------------")
    println(s"---------------------------")
    println(s"Runs:  ${nJobs}")
    println(s"Rows:  ${nRows}")
    println(s"---------------------------")
    println("Mode - Average - Max - Min")
    println(s"UDF: ${average(udfRuns)} ms, ${udfRuns.max} ms, ${udfRuns.min} ms")
    println(
      s"Catalyst: ${average(catalystRuns)} ms, ${catalystRuns.max} ms, ${catalystRuns.min} ms"
    )

  }

}
