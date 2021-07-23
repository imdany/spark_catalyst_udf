## Comparing performance of Catalyst Expressions vs UDFs

This code is part of a medium post that compares the performance between using Catalyst expressions vs using UDFs.

### How to use:

Compile the code doing 

```
sbt compile
```

Run the code with:

```
sbt "run X Y"
```

Where: 
- x = Number of rows of the dataframe
- y = Number of runs

Ex: This will run the code with a dataframe with 10 rows 5 times, and get the average of times.
```
sbt "run 10 5"
```

This will return an output like this:

```
---------------------------
Runs:  5
Rows:  10
---------------------------
Mode - Average - Max - Min
UDF: 903 ms, 3274 ms, 232 ms
Catalyst: 303 ms, 442 ms, 188 ms
```