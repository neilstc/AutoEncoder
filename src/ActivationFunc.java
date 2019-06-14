
public class ActivationFunc {
/* activation function - sigmoid and the deriviation 
 * we also used this class for general math/matrix things 
 * */	
	
	
	// this is the the activation function we will use for the hidden network calc
	public static double sigmoid(double x) {
		
		return (double) (1/ (1 + Math.exp(-x)));
	}
	//this is for the output we wont thr value that we will get already calculated with sih thus we do not need to calc again 
	public static double dersigmoid(double x) {
		return x*(1-x);
	}

	
	public static double[] normalize(double [] source){
	   double [] target = new double [source.length];
	   for (int i = 0; i < target.length; i++) {
		
		   
		   target[i] = source[i]/256;
	}
	   return target;
		
	}
	
	public static double[] dinormalize(double [] source){
		   double [] target = new double [source.length];
		   for (int i = 0; i < target.length; i++) {
			
			   
			   target[i] = source[i]*256;
		}
		   return target;
			
		}
}


