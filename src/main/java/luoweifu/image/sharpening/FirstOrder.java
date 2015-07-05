package luoweifu.image.sharpening;

/**
 * 一阶微分
 * @author luoweifu
 *
 */
public abstract class FirstOrder extends Sharpening {
	/**
	 * N*N矩阵的模板
	 */
	private static final int N = 3;
	
	protected int template[] = new int[N*N];
	
	/*public FirstOrder(int[] template) {
		if(template.length != N*N) {
			System.err.println("the template is not correct, the element number is wrong!");
		}
		for(int i=0; i<N*N; i++) {
			template[i] = 0;
		}
	}*/

	@Override
	public int[] processing(int[] pix, int w, int h) {
		if(template.length != N*N) {
			System.err.println("the template is not correct, the element number is wrong!");
		}
		int[] newpix = new int[w * h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (x != 0 && x != w - 1 && y != 0 && y != h - 1) {
					// G=f(x-1,y-1) + 2f(x,y-1) + f(x+1,y-1) – f(x-1,y+1) –
					// 2f(x,y+1) – f(x+1,y+1)
					newpix[x + y * w] = template[0]*pix[(y-1)*w + x-1] + template[1]*pix[(y-1)*w + x] + template[2]*pix[(y-1)*w + x+1]
					  + template[3]*pix[y*w + x-1] + template[4]*pix[y*w + x] + template[5]*pix[y*w + x+1]
					  + template[6]*pix[(y+1)*w + x-1] + template[7]*pix[(y+1)*w + x] + template[8]*pix[(y+1)*w + x+1];
					//newpix[x + y * w] = 255 << 24 | r << 16 | r << 8 | r;
				}
			}
		}
		/*int temp = findMinInt(newpix);
		System.out.println("minPix:" + temp);
		int t = -temp;
		System.out.println("minPix:" + t);
		for (int i = 0; i < newpix.length; i++) {
			newpix[i] = newpix[i] + (-temp);
		}*/
		return super.processing(newpix, w, h);
	}
	
}
