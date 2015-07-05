package luoweifu.image.filtering;

import java.util.Arrays;
/**
 * ÖÐÖµÂË²¨
 * @author luoweifu
 *
 */
public class MedianFiltering extends Filtering{

	@Override
	public int[] processing(int[] pix, int w, int h) {
		int newpix[] = new int[w * h];
		int[] temp = new int[9];
		//ColorModel cm = ColorModel.getRGBdefault();
		//int r = 0;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (x != 0 && x != w - 1 && y != 0 && y != h - 1) {
					// g = median[(x-1,y-1) + f(x,y-1)+ f(x+1,y-1)
					// + f(x-1,y) + f(x,y) + f(x+1,y)
					// + f(x-1,y+1) + f(x,y+1) + f(x+1,y+1)]
					temp[0] = pix[x - 1 + (y - 1) * w];
					temp[1] = pix[x + (y - 1) * w];
					temp[2] = pix[x + 1 + (y - 1) * w];
					temp[3] = pix[x - 1 + (y) * w];
					temp[4] = pix[x + (y) * w];
					temp[5] = pix[x + 1 + (y) * w];
					temp[6] = pix[x - 1 + (y + 1) * w];
					temp[7] = pix[x + (y + 1) * w];
					temp[8] = pix[x + 1 + (y + 1) * w];
					Arrays.sort(temp);
					newpix[y * w + x] = temp[4];
					//newpix[y * w + x] = 255 << 24 | r << 16 | r << 8 | r;
				} else {
					newpix[y * w + x] = pix[y * w + x];
				}
			}
		}
		return super.processing(newpix, w, h);
	}

}
