#!/bin/bash
echo "Cleaning"
rm -r com
echo "Compiling"
javac -d . Main.java neuralNetSimulator.java Neuron.java
echo "running"
java com.dalton.Main train.txt