package luoweifu.image.sharpening;

import luoweifu.image.ImageProcessing;

public class DifferentialOperator {
	private Sharpening sharp = null;
	/*public static final int HORIZON = 1;
	public static final int VERTICAL = 2;
	public static final int ROBERTS = 3;
	public static final int SOBEL = 4;
	public static final int LAPLACIAN = 5;
*/
	public DifferentialOperator(int type) {
		/*switch (type) {
		case HORIZON:
			int[] template = {1,2,1,  0,0,0,  -1,-2,-1};
			sharp = new FirstOrder(template);
			break;
		case VERTICAL:
			int[] template2 = {1,2,1,  0,0,0,  -1,-2,-1};
			sharp = new FirstOrder(template2);
			break;
		case ROBERTS:
			break;
		case SOBEL:
			break;
		case LAPLACIAN:
			break;
		}*/
	}

	public int[] sharpening(int[] pix, int w, int h) {
		return sharp.processing(pix, w, h);
	}
	
	public static void main(String args[]) {
		String s1 = "F:\\image processing\\sharpening\\images.jpg";
		String s2 = "F:\\image processing\\sharpening\\imagesGrayS.jpg";
		Sharpening sharp = null;
		sharp = new Sobel();
		ImageProcessing imgPro = new ImageProcessing();
		sharp.decorate(imgPro);
		sharp.processing(s1, s2, "jpg");
		//ImageDigital.grayImage(s1, "jpg", s2);
		//System.out.println(0x800b & 0xff);
	}
}
