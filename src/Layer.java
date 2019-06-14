import java.util.Arrays;
import java.util.Random;


/* this class represent the layer of the network if it input/hidden/output layer
 * so i'd one array for each layer, weights  dweights, and random for random init of weights 
 * run function for the feed forward 
 * train function for the "learniung of the layer
 * 
 * @authors Yair Ivgi, Neil Michaeli
 * 
 */




public class Layer {


	private double[] output;
	private double[] input;
	private double[] weights; // weight of the edges 
	private double[] dweights;// weight of edges in the previous iter
	private Random random; // randon edges init 

	/* init the layer with the sizes of each layer and random init 
	 * of weights
	 */

	public Layer(int inputSize, int outputSize) {
		this.output= new double [outputSize];
		this.input = new double  [inputSize +1];
		this.weights = new double [(inputSize+1) * outputSize]; // adding 1 cause of we have a bias node 
		this.dweights = new double [weights.length];
		this.random = new Random();
		initWeights();// this function will init the weights randomly;		
	}

	public void initWeights() {

		for (int i = 0; i < weights.length; i++) {
			weights[i] = (random.nextFloat() - 0.5f) * 4; // [-2,2]
		}
	}

	/* this function gets the training input and return the output 
	 * that's the feed forward part of the algorithm for a specific layer
	 * first we are copying the input then we are sum every node's weight and input
	 * and then applying the activation function for the "overall" output of the node
	 /*
	  * 
	  */
	public double[] run(double[] inputArray) {

		System.arraycopy(inputArray,0, input, 0,inputArray.length); // we copy values of the input
		input[input.length-1] = 1; // the bias node  
		int offset = 0; // we need a offset for edges tracking 

		for (int i = 0; i < output.length; i++) {
			for (int j = 0; j < input.length; j++) {
				output[i] += weights[offset+j] * input[j];// we calculate every node in the network 
			}
			output[i] = ActivationFunc.sigmoid(output[i]);// after all the specific node calculations has been made we applying the activation function
			offset += input.length;// update the offset 
		}
		return Arrays.copyOf(output, output.length); // this is the output after the training 

	}

	public double[] train(double[] error, double learningRate, double momentum) {

		int offset = 0;
		double [] nextError = new double[input.length];

		for (int i = 0; i < output.length; i++) {

			double delta = error[i] * ActivationFunc.dersigmoid(output[i]); // this is the output delta 

			for (int j = 0; j < input.length; j++) {

				int previousWeightIndex = offset +j;
				nextError[j] = nextError[j] + weights[previousWeightIndex] * delta;
				double dw = input[j] * delta * learningRate; //thats the gradient 
				weights[previousWeightIndex] += dweights[previousWeightIndex] * momentum + dw;  // previous change time the momentum + gradient
				dweights[previousWeightIndex] = dw; // for the next iteration 

			}
			offset += input.length;
		}
		return nextError;	


	}
}




