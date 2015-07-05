package luoweifu.image.sharpening;

/**
 * Ò»½×Î¢·ÖRobertsËã×Ó
 * @author luoweifu
 *
 */
public class Roberts extends Sharpening {

	@Override
	public int[] processing(int[] pix, int w, int h) {
		//ColorModel cm = ColorModel.getRGBdefault();
		//int r;
		int[] newpix = new int[w * h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (x != w - 1 && y != h - 1) {
					// G=|f(x+1,y+1) - f(x,y)| + |f(x,y+1)-f(x+1,y)|
					newpix[x + y * w] = Math.abs(pix[x + 1 + (y + 1) * w]
							- pix[x + y * w])
							+ Math.abs(pix[x + (y + 1) * w]
									- pix[x + 1 + y * w]);
					//newpix[x + y * w] = 255 << 24 | r << 16 | r << 8 | r;
				} /*else {
					if (x == w - 1) {
						newpix[x + y * w] = pix[x - 1 + y * w];
					}
					if (y == h - 1) {
						newpix[x + y * w] = pix[x + (y - 1) * w];
					}
				}*/
			}
		}
		return super.processing(newpix, w, h);
	}
	
}
