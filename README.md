# ParallelSystemsExercises

This repository contains all exercises of the lecture "Parallel Systems Laboratory" at the University of Applied 
Sciences Karlsruhe in the winter semester of 2022 / 2023.

## Exercise 1
A car wash park consists of 5 automatic car wash lines and 4 boxes for interior cleaning. In the first hour (afternoon),
3 to 5 cars randomly enter the wash park every 5 minutes. In the second hour (rush hour), 4 to 7 cars randomly enter the
wash park every 5 minutes and in the third hour (evening), 3 to 5 cars randomly enter the wash park every 5 minutes. 
Each car wants to go through a car wash, and it takes 5 to 10 minutes (i.e. randomly 5, 6, 7, 8, 9 or 10 minutes) 
depending on the car wash program (randomly). In the afternoon, every third car also wants to have its interior cleaned,
in rush hour only every fourth car, and in the evening every car. An interior cleaning (with chips 5 minutes) takes 
randomly 5, 10 or 15 minutes. For the owners:inside, who also do an interior cleaning, it doesn't matter if first 
interior cleaning and then car wash or vice versa.
Implement this situation with threads, synchronized, wait and notify. Scale minutes to seconds in the simulation.

## Exercise 2
Convert your wash park scenario to the executor framework and lock/condition. Test different ExecutorServices and see 
if you can make the code more compact with lambda expressions.

## Exercise 3
You have to review the code of one of your fellow students. 
Therefore, this exercise isn't included in this repository.

## Exercise 4
Convert your car wash scenario to semaphores for the coordination of car wash lanes and cleaning boxes. 