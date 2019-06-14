import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/* this class deals with the PPM photo reading, writing, translate the photo  
 * to grayscale area, get sub image.
 */
public class PPMReadWrite {

	String magicNumber; // A "magic number" for identifying the file type
	int width;  // Width of the image
	int height;  // Height of the image
	int maxColorVal;  // Maximum color value
	int[][][] rgb;  // matrix that contains the RGB value for each pixel 
	int[][] grayScale;  // matrix that contains the gray value for each pixel

	public int [][] PPMRead(String fileName) throws IOException{
		File file = new File(fileName);     	  
		BufferedReader br = new BufferedReader(new FileReader(file));// read the file 

		magicNumber = br.readLine(); // ppm3

		String[] dimensions = br.readLine().split("\\s+");
		width = Integer.parseInt(dimensions[0]); //512
		height = Integer.parseInt(dimensions[1]); // 512 

		maxColorVal = Integer.parseInt(br.readLine()); //255 

		rgb = new int[height][width][3]; // empty arr containing the rgb data
		grayScale = new int[height][width]; // we will take the grey value of every pixel 

		String st; 
		int i = 0, row = 0, column = 0;
		while ((st = br.readLine()) != null && i < height * width *3) { // while in the picture and with the right value 
			String[] valuesArr = st.trim().split("\\s+"); // store the values in string 
			List<Integer> values = new ArrayList<>(); // the values array list 
			for (String v : valuesArr) { 
				int x = Integer.parseInt(v);// we will convert the values to integers
				values.add(x); // add them to the values array list 
			}
			for (int j = 0; j < values.size(); j++) {
				rgb[row][column][i%3] = values.get(j); // we will get the values which are numbers and we will put them in the rgb array that was empty until that point 
				i++;
				if(i % 3 == 0) {
					column++;
				}
				if(column >= width) {
					row++;
					column = 0;
				}
			}
		} 
		br.close();
		// until this point we got the rgb value of eeach pixel 
		// now we want to convert it into a greyscale values
		for (i = 0; i < rgb.length; i++) {
			for (int j = 0; j < rgb[0].length; j++) {
				grayScale[i][j]  = (int)( (rgb[i][j][0] * 0.299) + (rgb[i][j][1] * 0.587) + (rgb[i][j][2] * 0.114));
			}

		}
		System.out.println("done reading now i have the grescale arr which contains....obiously the greyscale values of the big 512x512 pic");
		return grayScale;
	}


	public void writeGrayScale(String filename,int[][] grayValues) throws IOException{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
		// write header
		int rowdimension = grayValues.length;
		int columndimension = grayValues[0].length;

		writer.write("P3");
		writer.newLine();
		writer.write(grayValues.length+" "+ grayValues.length);
		writer.newLine();
		writer.write(Integer.toString(maxColorVal));
		writer.newLine();
		for(int row=0;row<rowdimension;row++){
			for(int column=0;column<columndimension;column++){
				writer.write(grayValues[row][column]+" ");
				writer.write(grayValues[row][column]+" ");
				writer.write(grayValues[row][column]+"");
				if(column < columndimension - 1)writer.write(" ");
			}
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}

	public double [] getSubImageGrey(int i, int j, int width, int height){
		if (i + height >= this.height) {
			i = this.height - height;
		}
		if (j + width >= this.width) {
			j = this.width - width;
		}
		double  ans [] = new double[height * width];
		//int ans [][] = new int[16][16];
		int rowIndex = 0;
		//int colIndex = 0;
		for (int k = i; k < i + height;k++) {
			for (int l = j; l < j + width;l++){				
				ans[rowIndex] = grayScale[k][l];
				rowIndex++;

			}
		}
		return ans;
	}




	public int[][] matrixTurner(double [] source){
		int [][] target = new int [source.length/16][source.length/16];
		int sourceCounter = 0;
		for (int i = 0; i < target.length; i++) {
			for (int j = 0; j < target[0].length; j++) {
				target[i][j] = (int) (source[sourceCounter]);
				sourceCounter++;
			}
		}
		return target; 
	}

	public int[] intTurner(double [] source){
		int [] target = new int [512];
		int sourceCounter = 0;
		for (int i = 0; i < 256; i++) {
			target[i] = (int) (source[sourceCounter]);
			sourceCounter++;

		}
		return target; 
	}

	public static int[][] convertToFullPic(ArrayList<int[][]> source){

		int[][] target = new int[512][512];
		int row = 0;
		int col = 0;
		int picRow = 16; // picture row tracking
		int picCol = 16; // picture col tracking
		// the first for loop is to run on the array list and extract each 16x16 matrice
		for (int k = 0; k <source.size(); k++) {

			int [][] mat = source.get(k); // extracting the matrix 		
	
			//these 2 loops are for copying each matrices data 	
			for (int i = 0; i < mat.length; i++) {
				for (int j = 0; j < mat[0].length; j++) {
					target[row][col] = mat[i][j];
					col++;
		
				}
				row++;
				if(row != picRow) col = picCol-16;
			}
		//	System.out.println("row" + row);
		//	System.out.println("col" + col);
			// this means i finished the first pixel
			if(col == picCol && row == picRow) {

				if(picCol <= 496) {
					picCol+=16;
					row = picRow-16;
					col = picCol-16;

				}

				else {
					if(picRow <= 496) {
					picRow += 16;
					picCol = 16;
					row = picRow-16;
					col = picCol-16;
					}
				}
			} 


		}



		return target;
	}


}

