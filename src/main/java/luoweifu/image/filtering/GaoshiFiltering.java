package luoweifu.image.filtering;

/**
 *  ¸ßË¹ÂË²¨
 * @author luoweifu
 *
 */
public class GaoshiFiltering extends Filtering{

	
	@Override
	public int[] processing(int pix[], int w, int h) {
		int newpix[] = new int[w * h];
		//ColorModel cm = ColorModel.getRGBdefault();
		//int r = 0;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (x != 0 && x != w - 1 && y != 0 && y != h - 1) {
					// g = (f(x-1,y-1) + 2f(x,y-1)+ f(x+1,y-1)
					// + 2f(x-1,y) + 4f(x,y) + 2f(x+1,y)
					// + f(x-1,y+1) + 2f(x,y+1) + f(x+1,y+1))/9
					newpix[y * w + x] = Math.round((pix[x - 1 + (y - 1) * w]
							+ 2*pix[x + (y - 1) * w]
							+ pix[x + 1 + (y - 1) * w]
							+ 2*pix[x - 1 + (y) * w]
							+ 4*pix[x + (y) * w]
							+ 2*pix[x + 1 + (y) * w]
							+ pix[x - 1 + (y + 1) * w]
							+ 2*pix[x + (y + 1) * w]
							+ pix[x + 1 + (y + 1) * w]) / 9);
					
					//newpix[y * w + x] = 255 << 24 | r << 16 | r << 8 | r;	
				} else {
					newpix[y * w + x] = pix[y * w + x];
				}
			}
		}		
		return super.processing(newpix, w, h);
	}

}
