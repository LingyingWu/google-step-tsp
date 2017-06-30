# Description of solution programs

program: [TravellingSalesmanProblem.java](https://github.com/LingyingWu/google-step-tsp/blob/gh-pages/TravellingSalesmanProblem.java)

***
### Solver 1: solverBrute  
Idea: find __all possible path__ and choose the one with the shortest total distance  
Remarks: only work for small size (<=8) due to large computational complexity O(N!)

|number of city (N)|total distance|
|:-----:|:-----:|
|5|__3291.62__|
|8|__3778.71__|

***
### Solver 2: solverGreedy
Idea: fix the starting point to city 0, and choose the next city which has the __shortest distance__ from current city  

|number of city (N)|total distance|
|:-----:|:-----:|
|5|3418.10|
|8|3832.29|
|16|5499.44|
|64|10159.16|
|512|12684.06|
|1028|25331.84|
|2048|49892.05|

***
### Solver 3: solverGreedyShortestFirst
Idea: similar to Solver 2, but starting with the shortest path

|number of city (N)|total distance|
|:-----:|:-----:|
|5|3518.53|
|8|4415.54|
|16|5568.56|
|64|10663.23|
|512|12858.61|
|1028|25759.40|
|2048|48870.85|

***
### Solver 4: solverTwoOpt
Idea: reorder the route which crosses over itself

Algorithm:  
(1) take route[1] to route[i-1] and add them in order to new_route  
(2) take route[i] to route[k] and add them in reverse order to new_route  
(3) take route[k+1] to end and add them in order to new_route  
(4) return the new_route;

|number of city (N)|total distance|
|:-----:|:-----:|
|5|3418.10|
|8|3832.29|
|16|4994.89|
|64|9326.92|
|512|11637.48|
|1028|21980.58|
|2048|runtime too long|

***
### Solver 5: solverThreeOpt
Idea: similar to Solver 4, but change reconnect three edges instead of two

|number of city (N)|total distance|
|:-----:|:-----:|
|5|3418.10|
|8|3832.29|
|16|__4931.44__|
|64|__8415.08__|
|512|__10835.85__|
|1028|runtime too long|
|2048|runtime too long|
