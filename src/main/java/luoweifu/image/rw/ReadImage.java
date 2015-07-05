package luoweifu.image.rw;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * 读取图片
 * @author luoweifu
 *
 */
public abstract class ReadImage {
	protected int width; // BMP图像的宽度
	protected int height; // BMP图像的高度
	protected DataInputStream in = null;
	protected BufferedImage img = null;
	
	public ReadImage(String srcPath) {
		try {
			in = new DataInputStream(new FileInputStream(srcPath));
			img = ImageIO.read(new File(srcPath));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public abstract BufferedImage readImage();
	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @param args
	 */
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub

	}*/

}
