package luoweifu.image.transformation;

/**
 * 傅丽叶变换
 * @author Administrator
 *
 */
public class FFT extends Transformation {
	
	@Override
	public int[] processing(int[] pix, int w, int h) {
		Complex matrix[][] = translateToComplex(pix, w, h);
		Complex[] fftPix = new Complex[w*h];
		matrix = fft(matrix, w, h);
		for(int i=0; i<h; i++) {
			for(int j=0; j<w; j++) {
				fftPix[i*w + j] = matrix[i][j];
			}
			
		}	
		int newpix[] = new int[w * h];
		double a = 0;
		double b = 0;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < h; j++) {
				a = matrix[i][j].getA();
				b = matrix[i][j].getB();
				 newpix[i*w + j] = (int) (Math.sqrt(a*a +
				 b*b)/100);
				//System.out.print(a + "\t");
			}
		}
		return super.processing(newpix, w, h);
	}

	/**
	 * 一维快速傅里叶变换
	 * 
	 * @param matrix
	 *            二维复数集数组
	 * @param w
	 *            图像的宽
	 * @param h
	 *            图像的高
	 * @return 傅里叶变换后的数组集
	 */
	public Complex[][] fft(Complex matrix[][], int w, int h) {
		double r1 = Math.log10(w) / Math.log10(2.0)
				- (int) (Math.log10(w) / Math.log10(2.0));
		double r2 = Math.log10(h) / Math.log10(2.0)
				- (int) (Math.log10(h) / Math.log10(2.0));
		if (r1 != 0.0 || r2 != 0.0) {
			System.err.println("输入的参数w或h不是2的n次幂！");
			return null;
		}
		int r = 0;
		r = (int) (Math.log10(w) / Math.log10(2));
		// 进行行傅里叶变换
		for (int i = 0; i < h; i++) {
			matrix[i] = fft(matrix[i]);
		}
		// 进行列傅里叶变换
		r = (int) (Math.log10(h) / Math.log10(2)); // 求迭代次数r
		Complex tempCom[] = new Complex[h];
		for (int j = 0; j < w; j++) {
			for (int i = 0; i < h; i++) {
				tempCom[i] = matrix[i][j];
			}
			tempCom = fft(tempCom);
			for (int i = 0; i < h; i++) {
				matrix[i][j] = tempCom[i];
			}
		}
		return matrix;
	}

	/**
	 * 一维快速傅里叶变换
	 * 
	 * @param values
	 *            一维复数集数组
	 * @return 傅里叶变换后的数组集
	 */
	public Complex[] fft(Complex[] values) {
		int n = values.length;
		int r = (int) (Math.log10(n) / Math.log10(2)); // 求迭代次数r
		Complex[][] temp = new Complex[r + 1][n]; // 计算过程的临时矩阵
		Complex w = new Complex(); // 权系数
		temp[0] = values;
		int x1, x2; // 一对对偶结点的下标值
		int d, p; // p表示加权系数Wpn的p值, t是重新排序后对应的序数值
		for (int l = 1; l <= r; l++) {
			if (l != r) {
				d = (int) (n / Math.pow(2, l));
				for(int i=0; i<Math.pow(2, l-1); i++) {
					for (int k = 0; k < d; k++) {
						x1 = (int) (i*Math.pow(2, r-(l-1)) + k);
						x2 = x1 + d;
						p = getWeight(k, l, r);
						w.setA(Math.cos(-2 * Math.PI * p / n));
						w.setB(Math.sin(-2 * Math.PI * p / n));
						temp[l][x1] = Complex.add(temp[l - 1][x1], Complex.multiply(
								w, temp[l - 1][x2]));
		
						w.setA(-Math.cos(-2 * Math.PI * p / n));
						w.setB(-Math.sin(-2 * Math.PI * p / n));
						temp[l][x2] = Complex.add(temp[l - 1][x1], Complex
								.multiply(w, temp[l - 1][x2]));
					}
				}
			} else {
				for (int k = 0; k < n / 2; k++) {
					x1 = 2 * k;
					x2 = 2 * k +1;
					// System.out.println("x1:" + x1 + "  x2:" + x2);
					p = reverseRatio(2 * k, r);
					w.setA(Math.cos(-2 * Math.PI * p / n));
					w.setB(Math.sin(-2 * Math.PI * p / n));
					temp[l][p] = Complex.add(temp[l - 1][x1], Complex.multiply(
							w, temp[l - 1][x2]));
					p = reverseRatio(2 * k + 1, r);
					w.setA(Math.cos(-2 * Math.PI * p / n));
					w.setB(Math.sin(-2 * Math.PI * p / n));
					temp[l][p] = Complex.add(temp[l - 1][x1], Complex.multiply(
							w, temp[l - 1][x2]));
				}
			}
		}
		return temp[r];
	}
	/*
	public int[] toPix(Complex[] fftData, int iw, int ih) {
		int[] pix = new int[iw * ih];

		int u, v, r;
		for (int j = 0; j < ih; j++) {
			for (int i = 0; i < iw; i++) {
				double tem = fftData[i + j * iw].getA() * fftData[i + j * iw].getA()
						+ fftData[i + j * iw].getB() * fftData[i + j * iw].getB();
				r = (int) (Math.sqrt(tem) / 100);
				if (r > 255)
					r = 255;

				if (i < iw / 2)
					u = i + iw / 2;
				else
					u = i - iw / 2;
				if (j < ih / 2)
					v = j + ih / 2;
				else
					v = j - ih / 2;

				pix[u + v * iw] = r;//255 << 24 | r << 16 | r << 8 | r;
			}
		}
		return pix;
	}*/
	/**
	 * 求加权系数 1.将数k写成r位的二进制数;2.将该二进制数向右移r-l位;3.将r位的二进制数比特倒转;4.求出倒置后的二进制数代表的十进制数;
	 * 
	 * @param k
	 *            要倒转的十进制数
	 * @param l
	 *            下标值
	 * @param r
	 *            二进制的位数
	 * @return 加权系数
	 */
	private int getWeight(int k, int l, int r) {
		int d = r - l; // 位移量
		k = k >> d;
		return reverseRatio(k, r);
	}

	/**
	 * 将数进行二进制倒转， 如0101倒转至1010
	 * 1.将数k写成r位的二进制数;2.将r位的二进制数比特倒转;3.求出倒置后的二进制数代表的十进制数;
	 * 
	 * @param k
	 *            要倒转的十进制数
	 * @param r
	 *            二进制的位数
	 * @return 倒转后的十进制数
	 */
	private int reverseRatio(int k, int r) {
		int n = 0;
		StringBuilder sb = new StringBuilder(Integer.toBinaryString(k));
		StringBuilder sb2 = new StringBuilder("");
		if (sb.length() < r) {
			n = r - sb.length();
			for (int i = 0; i < n; i++) {
				sb.insert(0, "0");
			}
		}
	
		for (int i = 0; i < sb.length(); i++) {
			sb2.append(sb.charAt(sb.length() - i - 1));
		}
		return Integer.parseInt(sb2.toString(), 2);
	}

	private Complex[][] translateToComplex(int[] pix, int w, int h) {
		Complex[][] complexs = new Complex[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				complexs[i][j] = new Complex(pix[i * w + j], 0.0);
			}
		}
		return complexs;
	}
	
}
