package luoweifu.image.transformation;

import luoweifu.image.Matrix;

/**
 * 离散余弦变换
 * @author Administrator
 *
 */
public class DCT extends Transformation {
	@Override
	public int[] processing(int[] pix, int w, int h) {
		//System.out.println("dct processing");
		if(w != h) {
			System.err.println("the width should be eqeual to height!");
			return null;
		} 
		double[][] iMatrix = translateTo2D(pix, w, w);	
		double[][] quotient = coefficient(w); // 求系数矩阵
		double[][] quotientT = Matrix.transposingMatrix(quotient, w); // 转置系数矩阵
	
		double[][] temp = new double[w][w];
		temp = Matrix.matrixMultiply(quotient, iMatrix, w);
		iMatrix = Matrix.matrixMultiply(temp, quotientT, w);
		
		int newpix[] = new int[w * w];
		int r = 0;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < w; j++) {				
				newpix[i * w + j] = (int) iMatrix[i][j];
			}
		}
		
		int d = findMaxInt(newpix)-findMinInt(newpix);
		for(int i=0; i<w*w; i++) {
			newpix[i] = (int)(255.0*newpix[i]/d);
		}
		return super.processing(newpix, w, h);
	}
	
	/**
	 * 将一维的整型数组转换成二维的double型数组
	 * 
	 * @param pix
	 * @param w
	 * @param h
	 * @return
	 */
	private double[][] translateTo2D(int[] pix, int w, int h) {
		double[][] iMatrix = new double[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				iMatrix[i][j] = (double) (pix[i * w + j]);
			}
		}
		return iMatrix;
	}

	/**
	 * 求离散余弦变换的系数矩阵
	 * 
	 * @param n
	 *            n*n矩阵的大小
	 * @return 系数矩阵
	 */
	private double[][] coefficient(int n) {
		double[][] coeff = new double[n][n];
		double sqrt = 1.0 / Math.sqrt(n);
		for (int i = 0; i < n; i++) {
			coeff[0][i] = sqrt;
		}
		for (int i = 1; i < n; i++) {
			for (int j = 0; j < n; j++) {
				coeff[i][j] = Math.sqrt(2.0 / n)
						* Math.cos(i * Math.PI * (j + 0.5) / (double) n);
			}
		}
		return coeff;
	}

}
