package luoweifu.image.identification;

import luoweifu.image.AmplificatingShrinking;
import luoweifu.image.ImageDigital;

/**
 * 均值hash算法
 * @author 罗伟富
 *
 */
public class AverageHash extends FingerIdentification{

	@Override
	public long getCharacteristicValue(int[] pix, int w, int h) {
		pix = AmplificatingShrinking.shrink(pix, w, h, FWIDTH, FHEIGHT);
		int[] newpix = ImageDigital.grayImage(pix, FWIDTH, FHEIGHT);
		int avrPix = averageGray(newpix, FWIDTH, FHEIGHT);
		//int hist[] = new int[FWIDTH*FHEIGHT];
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<FWIDTH*FHEIGHT; i++) {
			if(newpix[i] >= avrPix) {
				sb.append("1");	
			} else {
				sb.append("0");	
			}			
		}
		//StringBuilder sb = new StringBuilder(getCharacteristicValue(pix, w, h));
		long result = 0;
		if(sb.charAt(0) == '0') {
			result = Long.parseLong(sb.toString(), 2);
		} else {
			//如果第一个字符是1，则表示负数，不能直接转换成long，
			result = 0x8000000000000000l ^ Long.parseLong(sb.substring(1), 2);
		}
		return result;
	}
	
	/**
	 * 求灰度图像的均值
	 * @param pix 图像的像素矩阵
	 * @param w 图像的宽
	 * @param h 图像的高
	 * @return 灰度均值
	 */
	protected int averageGray(int[] pix, int w, int h) {
		long sum = 0;
		for(int i=0; i<h; i++) {
			for(int j=0; j<w; j++) {
				sum = sum+pix[i*w + j];
			}	
		}
		return (int)(sum/(w*h));
	}
}
