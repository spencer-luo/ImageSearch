package luoweifu.image;
/**
 *  双立方插值
 * @author Administrator
 *
 */
public class BiCubicInterpolationScale {

	private static double a00, a01, a02, a03;
	private static double a10, a11, a12, a13;
	private static double a20, a21, a22, a23;
	private static double a30, a31, a32, a33;
	private static int srcWidth;
	private static int srcHeight;
	
	/**
	 * 双立方插值
	 * @param inPixelsData 像素矩阵数组
	 * @param srcW 原图像的宽
	 * @param srcH 原图像的高
	 * @param destW 目标图像的宽
	 * @param destH 目标图像的高
	 * @return 处理后的推三矩阵数组
	 */
	public static int[] imgScale(int[] inPixelsData, int srcW, int srcH, int destW, int destH) {
		double[][][] input3DData = processOneToThreeDeminsion(inPixelsData, srcH, srcW);
		int[][][] outputThreeDeminsionData = new int[destH][destW][4];
		double[][] tempPixels = new double[4][4];
		float rowRatio = ((float)srcH)/((float)destH);
		float colRatio = ((float)srcW)/((float)destW);
		srcWidth = srcW;
		srcHeight = srcH;
		for(int row=0; row<destH; row++) {
			// convert to three dimension data
			double srcRow = ((float)row)*rowRatio;
			double j = Math.floor(srcRow);
			double t = srcRow - j;
			for(int col=0; col<destW; col++) {
				double srcCol = ((float)col)*colRatio;
				double k = Math.floor(srcCol);
				double u = srcCol - k;
				for(int i=0; i<4; i++) {
					tempPixels[0][0] = getRGBValue(input3DData,j-1, k-1,i);
					tempPixels[0][1] = getRGBValue(input3DData,j-1, k, i);
					tempPixels[0][2] = getRGBValue(input3DData, j-1,k+1, i);
					tempPixels[0][3] = getRGBValue(input3DData, j-1, k+2,i);
					
					tempPixels[1][0] = getRGBValue(input3DData, j, k-1, i);
					tempPixels[1][1] = getRGBValue(input3DData, j, k, i);
					tempPixels[1][2] = getRGBValue(input3DData, j, k+1, i);
					tempPixels[1][3] = getRGBValue(input3DData, j, k+2, i);
					
					tempPixels[2][0] = getRGBValue(input3DData, j+1,k-1,i);
					tempPixels[2][1] = getRGBValue(input3DData, j+1, k, i);
					tempPixels[2][2] = getRGBValue(input3DData, j+1, k+1, i);
					tempPixels[2][3] = getRGBValue(input3DData, j+1, k+2, i);
					
					tempPixels[3][0] = getRGBValue(input3DData, j+2, k-1, i);
					tempPixels[3][1] = getRGBValue(input3DData, j+2, k, i);
					tempPixels[3][2] = getRGBValue(input3DData, j+2, k+1, i);
					tempPixels[3][3] = getRGBValue(input3DData, j+2, k+2, i);
					
					// update coefficients
					updateCoefficients(tempPixels);
					outputThreeDeminsionData[row][col][i] = getPixelValue(getValue(t, u));
				}

			}
		}
		
		return convertToOneDim(outputThreeDeminsionData, destW, destH);
	}
	
	private static double getRGBValue(double[][][] input3DData, double row, double col, int index) {
		if(col >= srcWidth) {
			col = srcWidth - 1;
		}
		
		if(col < 0) {
			col = 0;
		}
		
		if(row >= srcHeight) {
			row = srcHeight - 1;
		}
		
		if(row < 0) {
			row = 0;
		}
		return input3DData[(int)row][(int)col][index];
	}
	
	private static int getPixelValue(double pixelValue) {
		return pixelValue < 0 ? 0: pixelValue >255.0d ?255:(int)pixelValue;
	}
	
	private static void updateCoefficients (double[][] p) {
		a00 = p[1][1];
		a01 = -.5*p[1][0] + .5*p[1][2];
		a02 = p[1][0] - 2.5*p[1][1] + 2*p[1][2] - .5*p[1][3];
		a03 = -.5*p[1][0] + 1.5*p[1][1] - 1.5*p[1][2] + .5*p[1][3];
		a10 = -.5*p[0][1] + .5*p[2][1];
		a11 = .25*p[0][0] - .25*p[0][2] - .25*p[2][0] + .25*p[2][2];
		a12 = -.5*p[0][0] + 1.25*p[0][1] - p[0][2] + .25*p[0][3] + .5*p[2][0] - 1.25*p[2][1] + p[2][2] - .25*p[2][3];
		a13 = .25*p[0][0] - .75*p[0][1] + .75*p[0][2] - .25*p[0][3] - .25*p[2][0] + .75*p[2][1] - .75*p[2][2] + .25*p[2][3];
		a20 = p[0][1] - 2.5*p[1][1] + 2*p[2][1] - .5*p[3][1];
		a21 = -.5*p[0][0] + .5*p[0][2] + 1.25*p[1][0] - 1.25*p[1][2] - p[2][0] + p[2][2] + .25*p[3][0] - .25*p[3][2];
		a22 = p[0][0] - 2.5*p[0][1] + 2*p[0][2] - .5*p[0][3] - 2.5*p[1][0] + 6.25*p[1][1] - 5*p[1][2] + 1.25*p[1][3] + 2*p[2][0] - 5*p[2][1] + 4*p[2][2] - p[2][3] - .5*p[3][0] + 1.25*p[3][1] - p[3][2] + .25*p[3][3];
		a23 = -.5*p[0][0] + 1.5*p[0][1] - 1.5*p[0][2] + .5*p[0][3] + 1.25*p[1][0] - 3.75*p[1][1] + 3.75*p[1][2] - 1.25*p[1][3] - p[2][0] + 3*p[2][1] - 3*p[2][2] + p[2][3] + .25*p[3][0] - .75*p[3][1] + .75*p[3][2] - .25*p[3][3];
		a30 = -.5*p[0][1] + 1.5*p[1][1] - 1.5*p[2][1] + .5*p[3][1];
		a31 = .25*p[0][0] - .25*p[0][2] - .75*p[1][0] + .75*p[1][2] + .75*p[2][0] - .75*p[2][2] - .25*p[3][0] + .25*p[3][2];
		a32 = -.5*p[0][0] + 1.25*p[0][1] - p[0][2] + .25*p[0][3] + 1.5*p[1][0] - 3.75*p[1][1] + 3*p[1][2] - .75*p[1][3] - 1.5*p[2][0] + 3.75*p[2][1] - 3*p[2][2] + .75*p[2][3] + .5*p[3][0] - 1.25*p[3][1] + p[3][2] - .25*p[3][3];
		a33 = .25*p[0][0] - .75*p[0][1] + .75*p[0][2] - .25*p[0][3] - .75*p[1][0] + 2.25*p[1][1] - 2.25*p[1][2] + .75*p[1][3] + .75*p[2][0] - 2.25*p[2][1] + 2.25*p[2][2] - .75*p[2][3] - .25*p[3][0] + .75*p[3][1] - .75*p[3][2] + .25*p[3][3];
	}
	
	private static double getValue (double x, double y) {
		double x2 = x * x;
		double x3 = x2 * x;
		double y2 = y * y;
		double y3 = y2 * y;

		return (a00 + a01 * y + a02 * y2 + a03 * y3) +
		       (a10 + a11 * y + a12 * y2 + a13 * y3) * x +
		       (a20 + a21 * y + a22 * y2 + a23 * y3) * x2 +
		       (a30 + a31 * y + a32 * y2 + a33 * y3) * x3;
	}

	/* <p> The purpose of this method is to convert the data in the 3D array of ints back into </p>
	 * <p> the 1d array of type int. </p>
	 * 
	 */
	private static int[] convertToOneDim(int[][][] data, int imgCols, int imgRows) {
		// Create the 1D array of type int to be populated with pixel data
		int[] oneDPix = new int[imgCols * imgRows * 4];

		// Move the data into the 1D array. Note the
		// use of the bitwise OR operator and the
		// bitwise left-shift operators to put the
		// four 8-bit bytes into each int.
		for (int row = 0, cnt = 0; row < imgRows; row++) {
			for (int col = 0; col < imgCols; col++) {
				oneDPix[cnt] = ((data[row][col][0] << 24) & 0xFF000000)
						| ((data[row][col][1] << 16) & 0x00FF0000)
						| ((data[row][col][2] << 8) & 0x0000FF00)
						| ((data[row][col][3]) & 0x000000FF);
				cnt++;
			}// end for loop on col

		}// end for loop on row

		return oneDPix;
	}// end convertToOneDim
	
	private static double [][][] processOneToThreeDeminsion(int[] oneDPix2, int imgRows, int imgCols) {
		double[][][] tempData = new double[imgRows][imgCols][4];
		for(int row=0; row<imgRows; row++) {
			
			// per row processing
			int[] aRow = new int[imgCols];
			for (int col = 0; col < imgCols; col++) {
				int element = row * imgCols + col;
				aRow[col] = oneDPix2[element];
			}
			
			// convert to three dimension data
			for(int col=0; col<imgCols; col++) {
				tempData[row][col][0] = (aRow[col] >> 24) & 0xFF; // alpha
				tempData[row][col][1] = (aRow[col] >> 16) & 0xFF; // red
				tempData[row][col][2] = (aRow[col] >> 8) & 0xFF;  // green
				tempData[row][col][3] = (aRow[col]) & 0xFF;       // blue
			}
		}
		return tempData;
	}	
	/*public static void main(String args[]) {
		
	}*/
}