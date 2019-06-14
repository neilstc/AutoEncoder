

/* this class represent the algorithm 
 *it contains 3 layers by default input - hidden - output 
 *basically it making sure that every layer running the feed forward 
 *and after that the back propagation 
 *@authors Yair Ivgi, Neil Michaeli
 */



public class Backpropagation {


	private Layer [] layers;// as the name suggest this represent the layers of the network

	public Backpropagation(int inputSize, int hiddenSize, int outputSize) {
		// we are building network with 3 layers- input - hidden - output
		this.layers = new Layer[2];
		this.layers[0] = new Layer(inputSize, hiddenSize);
		this.layers[1] = new Layer(hiddenSize, outputSize);

	}

	//in case  getter for specific layer
	public Layer getLayer(int index) {

		return layers[index];

	}
	
	/*

/* this is the feed forward it consumes an input and starting to apply the activation function
 *to each layer -> so basically u can say this function applies the feed forward to the entire network
 */
	public double[] run(double [] input) {
		double[] activations = input;

		for (int i = 0; i < layers.length; i++) {
			activations = layers[i].run(activations); // output!! 
		//	System.out.println("layer" + i + " feed forward completed ");


		}
		return activations;

	}
/* this is the main function of the backpropagation 
 *it gets an input, target output learning rate that generally 0.3 ~ and momentun 0.6 ~ 
 */
	public void train (double[] calculatedOutput, double[] targetOutput, double learningRate, double momentum) {

		//float[] calculatedOutput = run(input); // this array contains the calculated output !!  
		double[] error = new double [calculatedOutput.length];

		for (int i = 0; i < error.length; i++) {
			error[i] = targetOutput[i] - calculatedOutput[i]; // the target is the original input! 
		}
		//thats the actual backpropagation here we will change the weighta and all...

		for (int i = layers.length - 1; i >= 0; i--) {
			
			error = layers[i].train(error,learningRate, momentum); // here we training the layers !!!
		//	System.out.println("layer" + i + " backpropagation completed ");
			

		}			
	}
	
}

