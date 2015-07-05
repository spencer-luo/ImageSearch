package luoweifu.image.sharpening;

/**
 * 二阶微分Laplacian算子
 * @author luoweifu
 *
 */
public class Laplacian extends Sharpening {
	/**
	 * N*N矩阵的模板
	 */
	private static final int N = 3;
	private int[] template;
	public Laplacian(int type) {
		switch(type) {
		case 0:
			int[] template0 = {
					0, -1, 0,
					-1, 4,-1,
					0, -1, 0,
			};
			template = template0;
			break;
		case 1:
			int[] template1 = {
					-1, -1, -1,
					-1,  8, -1,
					-1, -1, -1,
			};
			template = template1;
			break;
		case 2:
			int[] template2 = {
					 1, -2, 1,
					-2,  4, -2,
					 1, -2, 1,
			};
			template = template2;
			break;
		case 3:
			int[] template3 = {
					0, -1, 0,
					-1, 5,-1,
					0, -1, 0,
			};
			template = template3;
			break;
		case 4:
			int[] template4 = {
					-1, -1, -1,
					-1,  9, -1,
					-1, -1, -1,
			};
			template = template4;
			break;
		default:
			System.out.println("the type is wrong! please set type from 0 to 4.");
		}
	}

	@Override
	public int[] processing(int[] pix, int w, int h) {
		if(template.length != N*N) {
			System.err.println("the template is not correct, the element number is wrong!");
		}
		int[] newpix = new int[w * h];
		//ColorModel cm = ColorModel.getRGBdefault();
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (x != 0 && x != w - 1 && y != 0 && y != h - 1) {
					// G=f(x-1,y-1) + 2f(x,y-1) + f(x+1,y-1) – f(x-1,y+1) –
					// 2f(x,y+1) – f(x+1,y+1)
					newpix[x + y * w] = template[0]*pix[(y-1)*w + x-1] + template[1]*pix[(y-1)*w + x] + template[2]*pix[(y-1)*w + x+1]
					  + template[3]*pix[y*w + x-1] + template[4]*pix[y*w + x] + template[5]*pix[y*w + x+1]
					  + template[6]*pix[(y+1)*w + x-1] + template[7]*pix[(y+1)*w + x] + template[8]*pix[(y+1)*w + x+1];
				}
			}
		}
		return super.processing(newpix, w, h);
	}
	
}
