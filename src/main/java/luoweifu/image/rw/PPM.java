package luoweifu.image.rw;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;

public class PPM extends ReadImage{
	private char ch0, ch1;
	//private int width, height;
	private int maxpix;
	private DataInputStream in = null;
	private BufferedImage img = null;
	
	public PPM(String srcPath) {
		super(srcPath);
		readPPMHeader();
	}

	@Override
	public BufferedImage readImage() {
		int[] pixels = new int[width * height];
		System.out.println(ch1 + "  " + pixels.length);
		try {
			for (int i = 0; i < width * height; i++) {
				int r;
				
					r = in.readByte();
				
				if (r < 0)
					r = r + 256;
				int g = in.readByte();
				if (g < 0)
					g = g + 256;
				int b = in.readByte();
				//System.out.println("b:" + b);
				if (b < 0)
					b = b + 256;
				pixels[i] = (255 << 24) | (r << 16) | (g << 8) | b;
				/*if(pixels[i] <= 0) {
					pixels[i] = pixels[0];
				}*/
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(img == null) {
			img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);
		}
		//System.out.println(img + "  " +  pixels.length + "  " + pixels[500]);
		img.setRGB(0, 0, width, height, pixels, 0, width);
		return img;
	}

	public void readPPMHeader() {
		try {
			ch0 = (char) in.readByte();
			ch1 = (char) in.readByte();
			if (ch0 != 'P' || ch1 != '6') {
				System.out.print("Not a ppm image!" + " [0]=" + ch0 + ", [1]="
						+ ch1);
				System.exit(0);
			}
			in.readByte(); // 读空格
	
			// 读出宽度
			char c = (char) in.readByte();
			if (c < '0' || c > '9') {
				System.out.print("Errow!");
				System.exit(1);
			}
	
			int k = 0;
			do {
				k = k * 10 + c - '0';
				c = (char) in.readByte();
			} while (c >= '0' && c <= '9');
			width = k;
	
			// 读出高度
			c = (char) in.readByte();
			if (c < '0' || c > '9') {
				System.out.print("Errow!");
				System.exit(1);
			}
	
			k = 0;
			do {
				k = k * 10 + c - '0';
				c = (char) in.readByte();
			} while (c >= '0' && c <= '9');
			height = k;
	
			// 读出灰度最大值(尚未使用)
			c = (char) in.readByte();
			if (c < '0' || c > '9') {
				System.out.print("Errow!");
				System.exit(1);
			}
	
			k = 0;
			do {
				k = k * 10 + c - '0';
				c = (char) in.readByte();
			} while (c >= '0' && c <= '9');
			maxpix = k;
		} catch (IOException e1) {
			System.out.println("Exception!");
		}
	}

}
