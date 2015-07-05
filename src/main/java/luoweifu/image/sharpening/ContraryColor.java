package luoweifu.image.sharpening;
/**
 * 反色处理，即如果是黑白图片，则将黑变成白，白变成黑
 * @author Administrator
 *
 */
public class ContraryColor extends Sharpening {

	@Override
	public int[] processing(int[] pix, int w, int h) {
		for(int i=0; i<w*h; i++) {
			pix[i] = 255-pix[i];
		}
		return super.processing(pix, w, h);
	}

}
