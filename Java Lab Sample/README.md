# The Java lab example

A slightly modified version of my lab solution that we will be working from.

I have provided a basic shell script to automate the building and running processes, I did not bother with a more proper build tool like ant or gradle because this is not our project, merely our source.

Our first phase of this project is to convert this Java program into a C++ program or a C++ library. Then we will create our own version with some upgrades to make it much faster and more efficient.

The original lab only had one source file `Main.java` which contained all three Java classes, it's generally best to put each class in its own file, but the lab marking was done with a tool which only took one single source file. For our purposes, I moved everything to its own file and also added some Javadoc.

I assume the single file requirement was because the professor used a build file or a shell script which would compile a file named `Main.java` into some program he had written to test the labs automatically. We were never shown how this works to prevent us from being able to cheat on the marking process. Our instructions were to put all code into a single source file and to submit our compiled class files as one archive.

## Training data format

The training data file is formatted as follows:

- One entry per line, that is one image and expected output per line
- Each line has 3 parts, separated by commas
  - A single character symbol to name each line of data
  - A set of inputs (0 and 1 characters) which represent a 35 pixel monochrome image
    - It could be any size, my program can tolerate incorrect sizes because it was easier to code it to detect sizes than to count them myself
  - A set of expected output values
    - Again, my program counts their length, so it can tolerate sizes other than 26

## Manual build and run process

To run without a build file or script use teh following:

```shell
javac -d . Main.java neuralNetSimulator.java Neuron.java
java com.dalton.Main train.txt
```

The `train.txt` file is the file containing the training data borrowed from my lab solution. Any arbitrary list of inputs and outputs would work if they follow the same format. The professor informed us that his marking system would use totally different training data, so our programs had to work with any data that fit the same format. The example used English uppercase letters, but any monochrome bitmap would work after conversion into a list of bits.