package luoweifu.image.sharpening;

import luoweifu.image.ImageDecorator;
import luoweifu.image.ImageDigital;
import luoweifu.image.ImageProcessing;

import java.awt.image.BufferedImage;

/**
 * 边缘检测
 * @author Administrator
 *
 */
public class EdgeDetection extends ImageDecorator {

	@Override
	public int[] processing(int[] pix, int w, int h) {
		
		int threshold = iterationGetThreshold(pix);
		pix = threshold(pix, threshold);
		return super.processing(pix, w, h);
	}
	/**
	 * 双峰法求最佳阀值
	 * @param histo 灰度直方图
	 * @return 最佳阀值
	 */
	public int doublePeakGetThreshold(double[] histo) {
		for(int i=0; i<histo.length; i++) {
			double h = 0;
			if(i>=2)
				h += histo[i-2];
			if(i>=1)
				h += histo[i-1];
			h += histo[i];
			if(i<histo.length-1)
				h += histo[i+1];
			if(i<histo.length-2)
				h += histo[i+2];
			histo[i] = h/5;
		}
		double max1 = 0;	//最大的峰值
		double max2 = 0;	//次大的峰值
		int t1=0, t2=0;
		for(int i=0; i<histo.length; i++) {
			if(max1 < histo[i]) {
				max2 = max1;
				t2 = t1;	//第二个峰值点
				max1 = histo[i];
				t1 = i;		//第一个峰值点
			}
		}
		if(t1>t2) {
			int t =t1;
			t1 = t2;
			t2 = t;
		}
		//求峰谷的序号,即为阀值
		int min = t1;
		for(int i=t1; i<t2; i++) {
			if(histo[min] > histo[i]) {
				min = i;
			}
		}

		System.out.println("t1:" + t1 + "  t2:" + t2 + "  min:" + min);
		return min;
	}
	/**
	 * 用迭代法 求最佳阀值
	 * @param pix 灰度像素数组
	 * @return 最佳阀值
	 */
	public int iterationGetThreshold(int[] pix) {
		int min = pix[0], max = pix[0];
		for(int i=0; i<pix.length; i++) {
			if(pix[i] > 255) {
				pix[i] = 255;
			}
			if(pix[i] < 0) {
				pix[i] = 0;
			}
			if(min >pix[i])
				min = pix[i];
			if(max <pix[i])
				max = pix[i];
		}
		double histo[] = getHisto(pix);
		int threshold = 0;
		int newThreshold = (int) ((min+max)/2);;
		while(threshold != newThreshold) {
			double sum1=0, sum2=0, w1=0, w2=0 ;
			int avg1, avg2;
			for(int i=min; i<newThreshold; i++) {
				sum1 += histo[i]*i;
				w1 += histo[i];
			}
			avg1 = (int) (sum1/w1);
			for(int i=newThreshold; i<max; i++) {
				sum2 += histo[i]*i;
				w2 += histo[i];
			}
			avg2 = (int) (sum2/w2);
			//System.out.println("avg1:" + avg1 + "  avg2:" + avg2 + "  newThreshold:" + newThreshold);
			threshold = newThreshold;
			newThreshold = (avg1+avg2)/2;
		}
		return newThreshold;
		
		/*if(min==0 && max == 255) {
			return (min+max)/2;
		} else {
			int t = (min+max)/2;
			double sum1=0, sum2=0, w1=0, w2=0 ;
			int avg1, avg2;
			for(int i=min; i<t; i++) {
				sum1 += histo[i]*i;
				w1 += histo[i];
			}
			avg1 = (int) (sum1/w1);
			for(int i=t; i<max; i++) {
				sum2 += histo[i]*i;
				w2 += histo[i];
			}
			avg2 = (int) (sum2/w2);
			return (avg1+avg2)/2;
		} */
	}
	/**
	 * 求图像的灰度直方图
	 * @param pix 一维的灰度图像像素值
	 * @return 0-255的 像素值所占的比率
	 */
	public double[] getHisto(int pix[]) {
		double histo[] = new double[256];
		for(int i=0; i<pix.length; i++) {
			//System.out.println("pix[i]:" + pix[i]);
			if(pix[i] > 255) {
				pix[i] = 255;
			}
			if(pix[i] < 0) {
				pix[i] = 0;
			}
			histo[pix[i]] ++;
		}
		for(int i=0; i<255; i++) {
			histo[i] = (double)histo[i]/pix.length;
		}
		return histo;
	}
	/**
	 * 求二值图像
	 * @param pix 像素矩阵数组
	 * @param w 矩阵的宽
	 * @param h 矩阵的高
	 * @param threshold 阀值
	 * @return 处理后的数组
	 */
	public int[] threshold(int pix[], int threshold) {
		for(int i=0; i<pix.length; i++) {
			if(pix[i] <= threshold) {
				pix[i] = 0;		
			} else {
				pix[i] = 255;
			}
		}	
		return pix;
	}
	public static void main(String args[]) {
		String s1 = "F:\\image processing\\histo&threshold\\imagesT312.0_39.jpg";
		String s2 = "F:\\image processing\\histo&threshold\\TimagesTest.jpg";
		BufferedImage img = ImageDigital.readImg(s1);
		
		int w = img.getWidth();
		int h = img.getHeight();
		int[] pix = new int[w*h];
		img.getRGB(0, 0, w, h, pix, 0, w);
		pix = ImageDigital.grayImage(pix, w, h);
		
		ImageProcessing imgPro = new ImageProcessing();
		ImageDecorator sobel = new Sobel();
		EdgeDetection ed = new EdgeDetection();
		ed.decorate(imgPro);
		sobel.decorate(sobel);
		int newpix[] = ed.processing(pix, w, h);
		
		/*
		double[] histo = ed.getHisto(pix);
		int threshold = ed.iterationGetThreshold(pix);//ed.doublePeakGetThreshold(histo);
		System.out.println(threshold);
		*/
		for(int i=0; i<w*h; i++) {
			pix[i] = 255<<24 | pix[i]<<16 | pix[i]<<8 | pix[i];
		}
		img.setRGB(0, 0, w, h, pix, 0, w);
		ImageDigital.writeImg(img, "jpg", s2);
		//ed.getHisto(pix)
		
		
	}
}
