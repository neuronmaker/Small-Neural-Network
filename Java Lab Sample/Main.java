/*
-----BEGIN PGP SIGNED MESSAGE-----
Hash: SHA512

This file was imported from my lab assignment solution on November 12, 2023.
It is the sample we will be using as the template for our first version of our library.
See the C++ documentation for more details.
Some parts of this file have been altered but the file more-or-less represents what the lab assignment was expecting.
These files will not necessarily be retained once the conversion to C++ is complete, though Git will retain them in its tree.

Message signed with PGP key: 1F32 5206 DE34 CFCF 0022  D045 48A7 3B8C 8920 7930
Download the key from my personal repo, Keybase.io, or keys.openpgp.org
-----BEGIN PGP SIGNATURE-----

iHUEARYKAB0WIQTdEHZzdRj88+sPHs2DGTsp04R9UwUCZVFmRAAKCRCDGTsp04R9
U0hKAP0Xp+ZSFAOH0NgxCBcW4/dPvS0seh7SnFBQT1mXUCbaMwEAvwFGeQcUnGPF
Yj9yAUVuqP7LdiTG/3FzzJSbgL/RQAk=
=sOvq
-----END PGP SIGNATURE-----
*/
package com.dalton;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The Main class of Lab assignment
 * This runs both the training and execution of the neural network simulator object
 * @author Dalton Herrewynen
 * @version 1
 */
public class Main{
	/**
	 * Converts a String into an array of double values used by the neural network as its inputs
	 * @param image A string as read from input or file
	 * @return Array of doubles as encoded by the String
	 */
	public static double[] inputFromImg(String image){
		double[] inputSource=new double[35];//The "image" the network sees
		for(int i=0; i<inputSource.length; ++i){
			if(image.charAt(i)=='1') inputSource[i]=1;//get the 1's and 0's
			else inputSource[i]=0;
		}
		return inputSource;
	}

	/**
	 * Main function, creates and starts the simulation, loads training data, and prints some test executions
	 * @param args Needs a command-line argument telling it where the training data is
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException{
		Scanner input=new Scanner(System.in);
		String path,image;//some of these are unused in the final lab submission, which is what this came from
		File trainFile;
		Scanner trainData;
		if(args.length>=1){//if training file path supplied by command line
			path=args[0];
		}else{//if not then get it
			System.out.print("No training file provided\nFile path: ");
			path=input.nextLine();
		}
        /*if(args.length>=2){//if input "image" supplied by command line
            image=args[1];
        }else{//if not then get it from user input
            System.out.print("No image provided, need 35 1's and 0's\nEnter data: ");
            image=input.nextLine();
        }*/
		trainFile=new File(path);//set training data file
		trainData=new Scanner(trainFile);//make the scanner the constructor needs
		neuralNetSimulator simulator=new neuralNetSimulator(trainData);

		String[] DAT={
				"00100010101000110001111111000110001",// A
				"11110100011000111110100011000111110",// B
				"01110100011000010000100001000101110",// C
				//"11100100101000110001100011001011100",// D
				//"11111100001000011100100001000011111",// E
				//"11111100001000011100100001000010000",// F
		};
		String[] Names={"A","B","C","D","E","F"};//matches above
		//to do: take first line, test it, train against it, then test it again.
		int start=0,end=3;
		//System.out.print("A on Pattern A, B on Pattern A, C on Pattern A, ");
		//System.out.print("A on Pattern B, B on Pattern B, C on Pattern B, ");
		//System.out.print("A on Pattern C, B on Pattern C, C on Pattern C, ");
		for(int i=0; i<30; ++i){
			//System.out.println("iteration:"+i+" COST: "+simulator.calcCost());
			for(int letter=0; letter<DAT.length; ++letter){
				//System.out.print(Names[letter]+": ");
				//simulator.displayResults(inputFromImg(DAT[letter]), start, DAT.length);
				simulator.run(inputFromImg(DAT[letter]));//run and print
				printOutput(simulator.getOutput(),3);
			}
			System.out.print("\n");
			simulator.train(simulator.getImage(0), simulator.getTarget(0), 0.1);
			simulator.train(simulator.getImage(1), simulator.getTarget(1), 0.1);
			simulator.train(simulator.getImage(2), simulator.getTarget(2), 0.1);
		}
		for(int letter=0; letter<DAT.length; ++letter){
			//System.out.print(Names[letter]+": ");
			//simulator.displayResults(inputFromImg(DAT[letter]), start, DAT.length);
			simulator.run(inputFromImg(DAT[letter]));//run and print
			printOutput(simulator.getOutput(),3);
		}
		System.out.print("\n");//nicer terminal
	}

	/**
	 * Prints the output, formatted for easier reading
	 * @param output array of doubles to print
	 * @param width  How many of the doubles to print
	 */
	public static void printOutput(double[] output,int width){
		for(int i=0; i<output.length&&i<width;++i){
			System.out.printf("%.6f, ",output[i]);
		}
	}
}
