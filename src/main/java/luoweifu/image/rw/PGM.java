/**
 * @PGM.java
 * @Version 1.0, 2010.02.26
 * @Author Xie-Hua Sun
 */

package luoweifu.image.rw;

import luoweifu.image.ImageDigital;

import java.awt.image.*;
import java.io.*;

public class PGM extends ReadImage{
	private char ch0, ch1;
	//private int width, height;
	private int maxpix;
	/*private DataInputStream in = null;
	private BufferedImage img = null;
	*/
	public PGM(String srcPath) {
		super(srcPath);
		readPGMHeader();
	}
	
	public void readPGMHeader() {
		try {
			ch0 = (char) in.readByte();
			ch1 = (char) in.readByte();
			if (ch0 != 'P' || ch1 != '5') {
				System.out.print("Not a pgm image!" + " [0]=" + ch0 + ", [1]="
						+ ch1);
				System.exit(0);
			}
			in.readByte(); // 读空格
			char c = (char) in.readByte();

			if (c == '#') // 读注释行
			{
				do {
					c = (char) in.readByte();
				} while ((c != '\n') && (c != '\r'));
				c = (char) in.readByte();
			}

			// 读出宽度
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

	/***************************************************************
	 * 读入.pgm或.ppm文件 type 5:pgm, 6:ppm
	 ***************************************************************/
	public BufferedImage readImage() {
		int[] pixels = new int[width * height];
		System.out.println(ch1 + "  " + pixels.length);
		try {
			for (int i = 0; i < width * height; i++) {
				int b = in.readByte();
				if (b < 0)
					b = b + 256;
				pixels[i] = (255 << 24) | (b << 16) | (b << 8) | b;
			}
		} catch (IOException e1) {
			System.out.println("Exception!");
			e1.printStackTrace();
		}
		if(img == null) {
			img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);
		}
		//System.out.println(img + "  " +  pixels.length + "  " + pixels[500]);
		img.setRGB(0, 0, width, height, pixels, 0, width);
		return img;
	}

	public char getCh0() {
		return ch0;
	}

	public char getCh1() {
		return ch1;
	}

	public int getWidth() {
		return width;
	}

	public static void main(String args[]) {
	
		String s1 = "F:\\image processing\\ch1\\pgm\\1.pgm";
		String s2 = "F:\\image processing\\ch1\\pgm\\imagesTest1.jpg";
		//String s3 = "F:\\image processing\\BMPImages\\images1.jpg";
		PGM br = new PGM(s1);
		
		BufferedImage img = br.readImage();
		
		ImageDigital.writeImg(img, "jpg", s2);
	}
}