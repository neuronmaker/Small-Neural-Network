package com.dalton;

/**
 * A single neuron object. Not efficient, but easy to demonstrate the correct concepts of a neural network
 * @author Dalton Herrewynen
 * @version 1
 */
class Neuron{
	/** The e constant */
	private static final double e=2.718281828459045;
	private double[] weight;
	private double[] input;
	private double output;
	private double alpha;

	/**
	 * Calculates this neuron's result with current loaded weights and inputs
	 * @return Double from 0 to 1
	 */
	public double getOutput(){
		output=0;
		for(int i=0; i<input.length; ++i){
			output+=input[i]*weight[i];
		}
		return logFunc(output);
	}
	/**
	 * The log function used by many early neural networks
	 * @param I input double value
	 * @return double value ranging from 0 though 1
	 */
	public double logFunc(double I){
		I=I*2-1;//map from -1 -> 1 to 0 -> 1
		return 1/(1+Math.pow(e,(-1*alpha*I)));// 1/(1+e^(-aI))
	}
	/**
	 * Loads an input value into one of the neuron's input slots
	 * @param inVal Value to load
	 * @param addr  Which input slot to write to
	 */
	public void setInput(double inVal, int addr){
		input[addr]=inVal;
	}
	/**
	 * Loads the weights into this neuron, each weight corresponds to one input slot
	 * Additionally, sets the number of input slots to match the number of weights
	 * @param inWeight Array of weights to load
	 */
	public void loadWeight(double []inWeight){
		weight=new double[inWeight.length];//set the lengths
		input=new double[inWeight.length];
		for(int i=0; i<inWeight.length; ++i){
			weight[i]=inWeight[i];
		}
	}
	/**
	 * Loads a single weight for neurons with a single input slot
	 * @param inWeight the weight to load
	 */
	public void loadWeight(double inWeight){
		weight=new double[1];//set the lengths to 1
		input=new double[1];
		weight[0]=inWeight;
	}
	/**
	 * Sets the Alpha value of this neuron after it has been created
	 * @param alph The new alpha value
	 */
	public void setAlpha(double alph){
		alpha=alph;
	}
	/**
	 * Reads the alpha value for this neuron
	 * @return the alpha value
	 */
	public double getAlpha(){
		return alpha;
	}
	/**
	 * Constructs a new instance of a Neuron with a preset alpha value
	 * @param alph the alpha value to set
	 */
	public Neuron(double alph){
		this.alpha=alph;
	}

	/** Default constructor, loads a reasonable alpha value */
	public Neuron(){
		this.alpha=0.5;
	}
}