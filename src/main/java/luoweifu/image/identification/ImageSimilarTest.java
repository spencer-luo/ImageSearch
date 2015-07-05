package luoweifu.image.identification;

import luoweifu.image.ImageDecorator;
import luoweifu.image.ImageDigital;
import luoweifu.image.ImageProcessing;
import luoweifu.image.sharpening.EdgeDetection;
import luoweifu.image.sharpening.Sobel;
import luoweifu.image.transformation.DCT;

import java.awt.image.BufferedImage;

public class ImageSimilarTest {
	/**
	 * 搜索图片，求图片的相似度
	 * @param n 图片的数量
	 * @param str1 原图片的地址
	 * @param str2 要比较图片的地址
	 * @param idents Identification接口的实例化对像，可以传入多个对像
	 */
	public static void searchImage(int n, String str1, String str2, Identification... idents) {
		int characNum = idents.length;
		String charact1[] = new String[characNum];
		String charact2[] = new String[characNum];
		Element[] element = new Element[n];
		//获得str1中图片的特征值
		for(int i=0; i<characNum; i++) {
			charact1[i] = idents[i].getCharacteristic(str1);
		}
		
		for(int j=0; j<n; j++) {
			float f = 0f;
			for(int i=0; i<characNum; i++) {
				charact2[i] = idents[i].getCharacteristic(str2 + (j+1) + ".jpg");
				f += idents[i].identification(charact1[i], charact2[i]);
			}
			//f = f/characNum;
			f = (int)((f/characNum)*1000)/1000f;
			//System.out.println(f);
			element[j] = new Element((j+1), 1000*f);
		}
		Element.sort1(element);
		BufferedImage img = null;
		for(int i=0; i<n; i++) {
			img = ImageDigital.readImg(str2 + element[i].index + ".jpg");
			ImageDigital.writeImg(img, "jpg", str2 + "T" + element[i].similar + "_" + element[i].index + ".jpg");
		}
	}
	
	public static void processingTest(String str1, String str2) {
		int DCTW = 40;
		int DCTH = 40;
		BufferedImage image = ImageDigital.readImg(str1);
		int w = image.getWidth();
		int h = image.getHeight();
		int[] pix = new int[w*h];
		pix = image.getRGB(0, 0, w, h, pix, 0, w);
		int[] newpix= new int[DCTW*DCTH];
		//newpix = AmplificatingShrinking.shrink(pix, w, h, DCTW, DCTH);
		pix = ImageDigital.grayImage(pix, w, h);
		ImageProcessing imgpro = new ImageProcessing();
		ImageDecorator dct = new DCT();
		ImageDecorator ed = new EdgeDetection();
		ImageDecorator sboel = new Sobel();
		dct.decorate(imgpro);
		ed.decorate(dct);
		sboel.decorate(ed);
		pix = sboel.processing(pix, w, h);	//顺序：先处理sboel再处理dct
	//System.out.println("dctPix:" + pix + "len:" + pix.length);
		/*int sum = 0;
		for(int i=0; i<w*h; i++) {	
			sum += pix[i];
		}
		int avgPix = (int)(sum/(w*h));
		System.out.println("sum:" + sum + "  avgPix:" + avgPix);
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<w*h; i++) {	
			if(pix[i] >= avgPix) {
				pix[i] = 1;
			} else {
				pix[i] = 0;
			}
		}
		long result = 0;
		if(sb.charAt(0) == '0') {
			result = Long.parseLong(sb.toString(), 2);
		} else {
			//如果第一个字符是1，则表示负数，不能直接转换成long，
			result = 0x8000000000000000l ^ Long.parseLong(sb.substring(1), 2);
		}*/
		//BufferedImage img = new BufferedImage(DCTW, DCTH, BufferedImage.TYPE_BYTE_INDEXED);
		
		for(int i=0; i<w*h; i++) {
			pix[i] = 255<<24 | pix[i] <<16 | pix[i]<<8 | pix[i];
		}
		image.setRGB(0, 0, w, h, pix, 0, w);
		//image.flush();
		//image.
		//ImageDigital.writeImg(newpix, DCTW, DCTH, "jpg", str2);
		ImageDigital.writeImg(image, "jpg", str2);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str1 = "F:\\image processing\\测试图片素材\\测试图片7\\Timages.jpg";
		String str2 = "F:\\image processing\\测试图片素材\\测试图片7\\images";
		int n = 91;
		Identification ident1 = new HistogramIdentification();
		Identification ident2 = new PHash();
		searchImage(n, str1, str2, ident1, ident2);//ident1,    , ident2
		
		/*String str1 = "F:\\image processing\\测试图片素材\\测试图片3.2\\Baboo.jpg";
		String str2 = "F:\\image processing\\测试图片素材\\测试图片3.2\\imagesTest.jpg";
		processingTest(str1, str2);
		*/
	}

}

class Element implements Comparable{
	int index;
	float similar;
	
	Element(int index, float similar) {
		this.index = index;
		this.similar = similar;
	}

	public int compareTo(Object o) {
		float s = ((Element)o).similar;
		if(this.similar < s) {
			return -1;
		}else if(this.similar == s) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * 将数组中的i和j位置互换
	 * @param obj
	 * @param i
	 * @param j
	 */
	public static void swap(Object[] obj, int i, int j) {
		if(i>=0 && i<obj.length && j>=0 && j<obj.length) {
			Object temp = obj[i];
			obj[i] = obj[j];
			obj[j] = temp;
		}
	}

	/**
	 * 从小到大排序
	 * @param data
	 */
	public static void sort1(Comparable data[]) {
		int n = data.length;
		Comparable temp;
		for(int i=0; i<n; i++) {
			for(int j=0; j<n-i-1; j++) {
				if(data[j].compareTo(data[j+1]) > 0) {
					swap(data, j, j+1);
				}
			}
		}
	}
	/**
	 * 从大到小排序
	 * @param data
	 */
	public static void sort2(Comparable data[]) {
		int n = data.length;
		Comparable temp;
		for(int i=0; i<n; i++) {
			for(int j=0; j<n-i-1; j++) {
				if(data[j].compareTo(data[j+1]) < 0) {
					swap(data, j, j+1);
				}
			}
		}
	}
}