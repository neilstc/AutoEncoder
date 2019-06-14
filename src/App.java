import java.util.ArrayList;
import java.util.Random;
/* General explanation about the algorithm 
 * so we are getting a greyscale photo and our objective is train our neural network model 
 * with backpropagation. we doing so by cutting the photo to 16x16 pieces these pieces will be used as our datasets 
 * and we store them in arraylist. now every time we run bp.run we will feedforward our network and eventually we will store it
 * in an training results arraylist, after we feed forward our network we want to train the model with the error so we send 
 * the results and the original output to to bp.train
 * after enough iteration we will send our output to a function that will print it to a photo 
 * @authors neil michaeli & yair ivgi
 */


public class App {

	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub


		PPMReadWrite rw = new PPMReadWrite();
		//int grayScale[][] = rw.PPMRead("Lana.ppm");
		int grayScale[][] = rw.PPMRead("ali.ppm");
		ActivationFunc af = new ActivationFunc();
		System.out.println("now we have a grayscale value arr of the image");
		ArrayList<double[]> datasets = new ArrayList<>();
		ArrayList<double[]> trainingResults = new ArrayList<>();
		int outPut [][] = new int[512][512]; //this will be the final; output
		ArrayList <int[][]> finalOutput = new ArrayList<>(); // this graph will contain the final out;



		for (int i = 0; i <= grayScale.length-16; i+=16) {
			for (int j = 0; j <= grayScale.length-16; j+=16) {
				datasets.add(rw.getSubImageGrey(i, j, 16, 16)); 

			}	
		}


		//we will normalize the data to fit the algo;

		for (int i = 0; i < datasets.size(); i++) {
			datasets.set(i,af.normalize(datasets.get(i)));
		}
		//now the data sets normalized 

		// the object 256 inputs 64 hidden 256 outputs 
		Backpropagation bp = new Backpropagation(256,64,256);
	
		for (int iterations = 0; iterations < Constants.ITERATIONS; iterations++) {

			for (int i = 0; i < datasets.size(); i++) {
				//	trainingResults.add(bp.run(datasets.get(i)));// i'm sending every pixel original input i'll get back the output after compression 
				if(trainingResults.size()<=1023) {
					trainingResults.add(i, bp.run(datasets.get(i)));
				}
				else {
					trainingResults.set(i,bp.run(datasets.get(i)));
				}
				bp.train(trainingResults.get(i),datasets.get(i), Constants.LEARNING_RATE,Constants.MOMENTUM);// i'll send that output and the original input for correction 

			}
			if(iterations%100 ==0)
			System.out.println(iterations);

		}	
		//System.out.println("WALAK TRAINED ");
		// converting into integers and multiplying by 256 
		for (int i = 0; i < trainingResults.size(); i++) {
			trainingResults.set(i, af.dinormalize(trainingResults.get(i)));
		}

		//second approach
		// first ill turn the the training set from double to int

		for (int i = 0; i < trainingResults.size(); i++) {   
			finalOutput.add(i,rw.matrixTurner(trainingResults.get(i)));

		}


	     outPut = rw.convertToFullPic(finalOutput);

		int[][] check = rw.matrixTurner(trainingResults.get(0));
		rw.writeGrayScale("check.ppm", check);
		rw.writeGrayScale("Final Output 1000 iterations.ppm", outPut);
		System.out.println("finished writing EVERYTHING");
		 
		
	}
}
