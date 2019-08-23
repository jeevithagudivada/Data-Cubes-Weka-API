# Data-Cubes-Weka-API
Java program that uses the Weka API and takes the dimension of the cuboid and print out the corresponding cuboids.

Steps: 
--------------------------------------------------------------------------------------------
1. Preprocessing the bank data in the following manner:
a) Delete all attributes except age, sex, region, and income
b) Discretize age using 3 equal width bins and name the attribute values as

 YOUNG (representing young people)

 MIDDLE (representing middle aged people)

 OLD (representing old people)


2. Computing all data cuboids for the facts count and avg_ income , where avg_income
represents the average income for a cell. The program should take a command line parameter n
representing the dimension of the cuboid and print out the corresponding cuboids.

For example:

DataCube 0 will print out the apex cubo id

DataCube n will print out all level n cuboids (n <= 3 in this case)


-----------------------------------------------------------------------------------------------
