package com.dalton;

import java.util.Random;
import java.util.Scanner;

/**
 * The neural network simulator object, generates neuron objects and treats them as a network
 * The simulation is for a very simple neural network for recognizing text, the lab assignment strongly suggested using this
 * approach over more efficient approaches like matrices because we were being graded on the concepts, not on performance.
 * @author Dalton Herrewynen
 * @version 1
 */
class neuralNetSimulator{
	/** Number of neurons in hidden layer, had good luck with using 2 hidden layer neurons per input layer neuron */
	public static final int hiddenLSize=70;
	public static final int randomSeedVal=20;//for some reason this worked and 10 did not
	public static final double lowAlph=2, highAlph=5;
	/**
	 * Arrays of neurons for Input, Hidden, or Output Layers
	 * 35 input neurons for 35 pixel images, 26 output neurons because training data has 26 examples (one per english letter)
	 */
	Neuron[] inputL,hiddenL,outputL;

	/** Weights for the hidden layer neurons */ double[][] hiddenW;
	/** Weights for the output layer neurons */ double[][] outputW;

	 /** The example images, 26 loaded from the file, 35 "pixels" in the image */
	double[][] exampleImage;
	/** The example target output values, one per line as read from training data, each is 26 wide because there are 26 output neurons in the training data*/
	double[][] targetOutput;
	/** The symbols found on each line of the training data, used to print which lines is which for troubleshooting purposes */
	String[] trainSymbols;//26 data lines means 26 symbols

	double alpha=2;
	boolean hasRun;

	/**
	 * Maps weight values, so they are always between -1 and 1
	 * @param input Any double to be mapped
	 * @return A double with max and min values capped between -1 and 1
	 */
	public double weightMap(double input){
		if(input>1) return 1;//if over 1 stay at 1
		if(input<-1) return -1;//if under -1 stay at -1
		return input;//otherwise we are ok
	}

	/**
	 * Constructs a new simulator object, loads training data from a Scanner object
	 * @param trainData The Scanner object to load training data from
	 */
	public neuralNetSimulator(Scanner trainData){
		String line=trainData.nextLine();//scan first line for lengths
		int linePos=0,inputs=0,outputs=0;//use these to find the sizes
		//probably better to use indexOf() instead but this worked well enough for the lab and time was tight
		while(linePos<line.length() && line.charAt(linePos)!=','){
			++linePos;//find first comma
		}
		++linePos;//move over first comma
		while(linePos<line.length() && line.charAt(linePos)!=','){
			++linePos;//find first comma
			++inputs;//count inputs
		}
		++linePos;//move over second comma
		while(linePos<line.length() && line.charAt(linePos)!=','){
			++linePos;//find first comma
			++outputs;//count outputs
		}
		System.out.println("Inputs: "+inputs+" Outputs: "+outputs);//print the input and output structure
		inputL=new Neuron[inputs];//input layer - 35 "pixels" most likely
		hiddenL=new Neuron[hiddenLSize];//hidden layer
		outputL=new Neuron[outputs];//output layer - count the example outputs
		hiddenW=new double[hiddenL.length][inputL.length];//weights between hidden and input layer
		outputW=new double[outputL.length][hiddenL.length];//weights output and hidden layer
		//these are the target data samples
		exampleImage=new double[outputL.length][inputL.length];//number of data lines in the file, 35 "pixels"
		targetOutput=new double[outputL.length][outputL.length];//number of data lines and 26 output neurons
		trainSymbols=new String[outputL.length];//number of data lines means 26 symbols

		for(int i=0; i<targetOutput.length && trainData.hasNextLine(); ++i){//go until end of file or end of network capacity, whichever is first
			if(i!=0) line=trainData.nextLine();//skip scanning the first line as we did that earlier to figure out how many neurons we need
			linePos=line.indexOf(',');//end of the symbol
			trainSymbols[i]=line.substring(0,linePos);//get the letter(s)
			++linePos;//mode past the comma
			for(int j=0; j<exampleImage[i].length; ++j){
				if(line.charAt(linePos)=='1') exampleImage[i][j]=1;//extract 1 or 0
				else exampleImage[i][j]=0;
				++linePos;//move to next char
			}
			linePos+=1;//move past the comma
			for(int j=0; j<targetOutput[i].length; ++j){//set the outputs +1 to -1
				if(line.charAt(linePos)=='1') targetOutput[i][j]=1;//extract 1 or 0
				else targetOutput[i][j]=0;
				++linePos;//move to next char
			}
		}
		makeNewNeurons();
		makeRandomWeights(randomSeedVal);
	}

	/**
	 * Sets all the alpha values for all neurons in this simulation
	 * @param newAlpha The new alpha value
	 */
	public void overrideAlpha(double newAlpha){
		alpha=newAlpha;//store new alpha
		//set each neuron's new alpha
		for(int i=0; i<inputL.length; ++i){
			inputL[i].setAlpha(alpha);
		}
		for(int i=0; i<hiddenL.length; ++i){
			hiddenL[i].setAlpha(alpha);
		}
		for(int i=0; i<outputL.length; ++i){
			outputL[i].setAlpha(alpha);
		}
		hasRun=false;//have not run yet with new values
	}
	/**
	 * Sets the weights according to a given random seed
	 * @param seed The random seed
	 */
	public void makeRandomWeights(int seed){
		Random random=new Random(seed);//an arbitrary seed for predictable results
		//generate random weights
		makeNewNeurons();
		for(int i=0; i<hiddenW.length; ++i){
			for(int j=0; j<hiddenW[i].length; ++j){//set the initial weights to "random" values
				hiddenW[i][j]=0.5*(random.nextDouble()-0.5);//want random values in range -0.25 to +0.25
			}
		}
		//pass first layer on through, debug only, makes it into a simpler perceptron
		for(int i=0; i<hiddenW.length; ++i){
			for(int j=0; j<hiddenW[i].length; ++j){//set the initial weights to "random" values
				//if(i==j) hiddenW[i][j]=1;//only passing through the input directly, temporarily a 2 layer network
				//else hiddenW[i][j]=0;
			}
		}
		for(int i=0; i<outputW.length; ++i){
			for(int j=0; j<outputW[i].length; ++j){
				outputW[i][j]=0.5*(random.nextDouble()-0.5);
			}
		}
		loadWeights();
		hasRun=false;//have not run yet with new values
	}

	/** Generates neurons and populates the network */
	public void makeNewNeurons(){
		//make new neurons, load their weights
		for(int i=0; i<inputL.length; ++i){
			inputL[i]=new Neuron(alpha);
		}
		for(int i=0; i<hiddenL.length; ++i){
			hiddenL[i]=new Neuron(alpha);
		}
		for(int i=0; i<outputL.length; ++i){
			outputL[i]=new Neuron(alpha);
		}
		hasRun=false;//have not run yet
	}
	/** Loads the weights from the simulation into each neuron's list of weights */
	public void loadWeights(){
		for(int i=0; i<inputL.length;++i){
			inputL[i].loadWeight(1.0);//set input weights to 1, they just sense the "image"
		}
		for(int i=0; i<hiddenW.length; ++i){
			hiddenL[i].loadWeight(hiddenW[i]);
		}
		for(int i=0; i<outputW.length; ++i){
			outputL[i].loadWeight(outputW[i]);
		}
	}
	/**
	 * Prints all outputs for a given "image"
	 * @param inputs The "image" data to load into the inputs
	 */
	public void displayResults(double[] inputs){
		displayResults(inputs,0, outputL.length);//the full output
	}
	/**
	 * Prints the outputs for a given image
	 * @param inputs The input data or "image"
	 * @param start  Which index to start printing at
	 * @param end    Which index to stop printing at
	 */
	public void displayResults(double[] inputs, int start, int end){
		//overrideAlpha(highAlph);//want sharp transitions
		overrideAlpha(lowAlph);
		run(inputs);//run the network
		//show output
		if(end>outputL.length) end=outputL.length;//don't overflow
		for(int i=start; i<end; ++i){//show just the neurons we want
			System.out.printf("\""+trainSymbols[i]+"\"=%.6f  ",outputL[i].getOutput());//set the last arg to + number to set the precision
			//System.out.println("Output \""+trainSymbols[i]+"\"="+outputL[i].getOutput());
		}
		System.out.print('\n');
	}

	/**
	 * Takes input data, feeds it to the network, and runs the neural network
	 * @param inputs The input data or "image"
	 */
	public void run(double[] inputs){
		//load inputs
		for(int i=0; i<inputs.length; ++i){
			inputL[i].setInput(inputs[i],0);
		}
		//pass to hidden layer
		for(int i=0; i<hiddenL.length; ++i){
			//load each input neuron into each hidden neuron
			for(int j=0; j<inputL.length; ++j){
				hiddenL[i].setInput(inputL[j].getOutput(),j);//load each input neuron's output into this hidden layer neuron
			}
		}
		//pass to output layer
		for(int i=0; i<outputL.length; ++i){
			//load each input neuron into each hidden neuron
			for(int j=0; j<hiddenL.length; ++j){
				outputL[i].setInput(hiddenL[j].getOutput(),j);//load each hidden neuron's output into this output layer neuron
			}
		}
		//done
		hasRun=true;//now there is data to pull
	}
	/**
	 * Returns the output from all the output layer neurons
	 * @return double array of output values
	 */
	public double[] getOutput(){
		double[] output=new double[outputL.length];
		for(int i=0; i<outputL.length; ++i){
			output[i]=outputL[i].getOutput();
		}
		return output;
	}

	/**
	 * Get the current weights as loaded into the output layer neurons
	 * @param neuron Which neuron to read
	 * @return An array of double values with the weights of the neuron being read
	 */
	public double[] dumpOutputWeights(int neuron){
		double[] weights=new double[outputW[neuron].length];
		for(int i=0; i<weights.length; ++i) weights[i]=outputW[neuron][i];
		return weights;
	}

	/**
	 * Calculates the cost of this network, a way to estimate training success
	 * @return A double value representing the error of this network
	 */
	public double calcCost(){
		double cost=0;
		overrideAlpha(lowAlph);//make logFunc less sharp
		for(int i=0; i<exampleImage.length; ++i){
			run(exampleImage[i]);//run an example
			for(int j=0; j<outputL.length;++j){
				cost+=Math.pow((outputL[j].getOutput()-targetOutput[i][j]),2);//add together the squares of the differences for each example on each output neuron
			}
		}
		cost=cost/exampleImage.length;//average of the images we have
		return cost;
	}
	/**
	 * Fetches an "image" (array of double values) from the table of images and outputs
	 * @param index Which image entry to read
	 * @return array of double values representing the image that was read
	 */
	public double[] getImage(int index){
		double[] newImage=new double[exampleImage[index].length];
		for(int i=0; i<exampleImage[index].length; ++i){
			newImage[i]=exampleImage[index][i];
		}
		return newImage;
	}
	/**
	 * Reads the expected output from the table of images and outputs
	 * @param index Which output entry to read
	 * @return array of double values for teh expected output for this table row
	 */
	public double[] getTarget(int index){
		double[] targetReturn=new double[targetOutput[index].length];
		for(int i=0; i<targetOutput[index].length; ++i){
			targetReturn[i]=targetOutput[index][i];
		}
		return targetReturn;
	}

	/**
	 * Runs one training cycle on one input image and target output
	 * @param image     The Image data for this training example
	 * @param target    The target output for this training example
	 * @param learnRate How fast to learn
	 */
	public void train(double[] image, double[] target, double learnRate){//train on a specific example
		double[][] nudgeWeight=new double[outputL.length][hiddenL.length];//storage for the nudges of the weights to each neuron
		overrideAlpha(lowAlph);//make logFunc less sharp
		//train output layer weights
		run(image);//run on example
		for(int neuron=0; neuron<outputL.length; ++neuron){//check each output neuron error
			//outputError[neuron]=target[neuron]-outputL[neuron].getOutput();//difference between output and target
			for(int weight=0; weight<nudgeWeight[neuron].length; ++weight){
				//for each weight, calculate whether it should be higher/lower and how much
				//nudge proportional to the error * how much weight (and direction) and how hard the other neuron pulled
				//Formula from the notes(page22)
				double thisNeuron=outputL[neuron].getOutput(), lastNeuron=hiddenL[weight].getOutput();
				double delta=-2*alpha*(target[neuron]-thisNeuron)*thisNeuron*(1.0-thisNeuron);
				nudgeWeight[neuron][weight]=(-learnRate*delta*lastNeuron);//change= -learn_rate * delta * last_neuron
			}
		}
		//change the weights for each output neuron for this example
		for(int neuron=0; neuron<outputW.length; ++neuron){//each neuron
			for(int weight=0; weight<outputW[neuron].length; ++weight){//each weight on the neuron
				outputW[neuron][weight]+=nudgeWeight[neuron][weight];//add the nudge to each weight proportional to learn rate
			}
		}
		loadWeights();//make changes work
	}
}