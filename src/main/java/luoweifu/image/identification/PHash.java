package luoweifu.image.identification;


import luoweifu.image.AmplificatingShrinking;
import luoweifu.image.ImageDecorator;
import luoweifu.image.ImageDigital;
import luoweifu.image.ImageProcessing;
import luoweifu.image.sharpening.EdgeDetection;
import luoweifu.image.sharpening.Sobel;
import luoweifu.image.transformation.DCT;

/**
 * 指纹识别图像
 * @author Administrator
 *
 */
public class PHash extends FingerIdentification{

	@Override
	public long getCharacteristicValue(int[] pix, int w, int h) {
		pix = AmplificatingShrinking.shrink(pix, w, h, DCTW, DCTH);
		pix = ImageDigital.grayImage(pix, DCTW, DCTH);
		//System.out.println("pix length:" + pix.length);
		ImageProcessing imgpro = new ImageProcessing();
		ImageDecorator dct = new DCT();
		ImageDecorator ed = new EdgeDetection();
		ImageDecorator sboel = new Sobel();
		dct.decorate(imgpro);
		ed.decorate(dct);
		sboel.decorate(ed);
		int[] dctPix = sboel.processing(pix, DCTW, DCTW);	//顺序：先处理sboel再处理dct
	//System.out.println("dctPix:" + dctPix + "len:" + dctPix.length);
		int avrPix = averageGray(dctPix);
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<FHEIGHT; i++) {	
			for(int j=0; j<FWIDTH; j++) {		
				if(dctPix[i*FWIDTH + j] >= avrPix) {
					sb.append("1");	
				} else {
					sb.append("0");	
				}
			}
		}
		long result = 0;
		if(sb.charAt(0) == '0') {
			result = Long.parseLong(sb.toString(), 2);
		} else {
			//如果第一个字符是1，则表示负数，不能直接转换成long，
			result = 0x8000000000000000l ^ Long.parseLong(sb.substring(1), 2);
		}
		return result;	
	}
	
	public int[] getGrey(int[] pix, int w, int h) {
		return pix;
	}

	/**
	 * 求灰度图像的均值
	 * @param pix 图像的像素矩阵
	 * @param w 图像的宽
	 * @param h 图像的高
	 * @return 灰度均值
	 */
	protected int averageGray(int[] pix) {
		long sum = 0;
		for(int j=0; j<FHEIGHT; j++) {
			for(int i=0; i<FWIDTH; i++) {
				sum = sum+pix[j*DCTW + i];
			}	
		}
		return (int)(sum/(FWIDTH*FHEIGHT));
	}

}
