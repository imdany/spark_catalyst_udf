name := "spark_catalyst_udf"

version := "0.1"

scalaVersion := "2.12.12"
val HadoopVersion = "2.7.3"

run in Compile := Defaults
  .runTask(
    fullClasspath in Compile,
    mainClass in (Compile, run),
    runner in (Compile, run)
  )
  .evaluated

runMain in Compile := Defaults
  .runMainTask(fullClasspath in Compile, runner in (Compile, run))
  .evaluated

val SparkVersion = "3.0.0"

val scalacheck = "org.scalacheck" %% "scalacheck" % "1.14.3"
val scalatest = "org.scalatest" %% "scalatest" % "3.0.5"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % SparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % SparkVersion % "provided",
  // Testing
  scalacheck % Test,
  scalatest % Test
)
