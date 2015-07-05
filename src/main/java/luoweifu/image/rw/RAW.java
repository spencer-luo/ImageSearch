/*
 * @RAW.java
 * @Version 1.0 2010.02.26
 * @Author Xie-Hua Sun
 */

package luoweifu.image.rw;

import luoweifu.image.ImageDigital;

import java.awt.image.BufferedImage;
import java.io.*;


public class RAW {
	// 将image编码为RAW图像,返回byte[][]
	public byte[][] readRAW_DAT(String name, int iw, int ih) {
		byte[][] pix = new byte[iw][ih];
		try {
			FileInputStream fin = new FileInputStream(name);
			DataInputStream in = new DataInputStream(fin);

			// 磁盘文件读入数据
			for (int j = 0; j < ih; j++) {
				for (int i = 0; i < iw; i++) {
					byte c = in.readByte();
					pix[i][j] = c;
				}
			}
		} catch (IOException e1) {
		}

		return pix;
	}

	// 将image编码为RAW图像, 返回int[][]
	public int[] readRAW(String name, int iw, int ih) {
		int c;
		int[] pix = new int[iw*ih];

		try {
			FileInputStream fin = new FileInputStream(name);
			DataInputStream in = new DataInputStream(fin);

			// 磁盘文件读入数据
			for (int j = 0; j < ih; j++) {
				for (int i = 0; i < iw; i++) {
					c = in.readByte();
					if (c < 0)
						c += 256;
					pix[i + j * iw] = (255 << 24) | (c << 16) | (c << 8) | c;
				}
			}
		} catch (IOException e1) {
			System.out.println("Exception!");
		}
		return pix;
	}

	// 读入.raw文件
	public int[] readRAW1D(String name, int iw, int ih) {
		int[] pix = new int[iw * ih];
		try {
			FileInputStream fin = new FileInputStream(name);
			DataInputStream in = new DataInputStream(fin);

			for (int i = 0; i < iw * ih; i++) { // 磁盘文件读入数据
				int c = in.readByte();
				if (c < 0)
					c = c + 256;
				pix[i] = c;
			}
		} catch (IOException e1) {
			System.out.println("Exception!");
		}
		return pix;
	}

	// 在当前目录写入.raw文件
	public void writeRAW(String name, int[] pix, int iw, int ih) {
		try {
			RandomAccessFile rf = new RandomAccessFile(name, "rw");
			for (int i = 0; i < iw * ih; i++) {
				rf.writeByte((byte) (pix[i] & 0xff));
			}
			rf.close();
		} catch (FileNotFoundException e1) {
		} catch (IOException e2) {
		}
	}

	public static void main(String args[]) {
		String s1 = "F:\\image processing\\ch1\\raw\\cat.raw";
		String s2 = "F:\\image processing\\ch1\\raw\\imagesTest1.jpg";
		//String s3 = "F:\\image processing\\BMPImages\\images1.jpg";
		RAW br = new RAW();
		int[] pix = br.readRAW(s1,256 , 256);
		
		BufferedImage img = new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_INDEXED);
		img.setRGB(0, 0, 256, 256, pix, 0, 256);
		
		ImageDigital.writeImg(img, "jpg", s2);
	}
}