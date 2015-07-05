package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import luoweifu.image.ImageDigital;

/**
 * This is a test class, use to test image processing.
 * 测试类，用于测试图像的处理过程
 */
public class Test {
	public static void main(String[] args) {
		//System.out.println(Integer.parseInt("c762", 16));
		System.out.println(Integer.parseInt("18", 16));
		String s1 = "D:\\image\\person.jpg";
		String s2 = "D:\\image\\Test.jpg";
		BufferedImage img = ImageDigital.readImg(s1);
		ImageDigital.writeImg(img, "bmp", s2);
		/*
		BufferedImage image = null;
        try {

              //you can either use URL or File for reading image using ImageIO
        	File imagefile = new File("F:\\image processing\\wuzhangshi.png");
            String fn = imagefile.getPath();
            String ext = fn.substring(fn.length()-3, fn.length());
            System.out.println(ext);
            image = ImageIO.read(imagefile);
            System.out.println(imagefile.getPath());
            System.out.println(imagefile.getName());
            System.out.println(imagefile);
            //ImageIO Image write Example in Java
            ImageIO.write(image, "jpg",new File("F:\\image processing\\wuzhangshiJPG.jpg"));
            ImageIO.write(image, "bmp",new File("F:\\image processing\\wuzhangshiBMP.bmp"));
            ImageIO.write(image, "gif",new File("F:\\image processing\\wuzhangshiGIF.gif"));
            ImageIO.write(image, "png",new File("F:\\image processing\\wuzhangshiPNG.png"));

        	File imagefile = new File("F:\\image processing\\商务小人2.png");
            image = ImageIO.read(imagefile);
            int w = image.getWidth();
            System.out.println(w);
            int h = image.getHeight();
            int pix[] = new int[w*h];
            image.getRGB(0, 0, w, h, pix, 0, w);
            int a, r, g, b;
            for(int i=0; i<w*h; i++) {
            	a = pix[i]>>24 & 0xff;
            	r = pix[i]>>16 & 0xff;
            	g = pix[i]>>8 & 0xff;
            	b = pix[i] & 0xff;
            	System.out.println("r1: " + r);
            	pix[i] = 32<<24 | r<<16 | g<<8 | b;
            	r = pix[i]>>16 & 0xff;
            	System.out.println("r2: " + r);
            }
            image.setRGB(0, 0, w, h, pix, 0, w);
            ImageIO.write(image, "png", new File("F:\\image processing\\商务小人7.png"));
        } catch (IOException e) {
              e.printStackTrace();
        }*/
       //System.out.println(1<<8);


        //Read more: http://javarevisited.blogspot.com/2011/12/read-write-image-in-java-example.html#ixzz2PNi9ppnt
	}
}
