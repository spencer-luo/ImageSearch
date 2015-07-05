package luoweifu.image.sharpening;
/**
 * ´¹Ö±Ò»½×Î¢·Ö
 * @author luoweifu
 *
 */
public class FirstMomentY extends FirstOrder{

	FirstMomentY() {
		int coefficient[] = {
				1,  0, -1, 
				2,  0, -2, 
				1,  0, -1};
		this.template = coefficient;
	}

	@Override
	public int[] processing(int[] pix, int w, int h) {
		return super.processing(pix, w, h);
	}
}
