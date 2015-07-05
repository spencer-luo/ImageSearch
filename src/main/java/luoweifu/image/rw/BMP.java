//BMPReader.java, from Mark Wutka
//Revised by Xie-Hua Sun

package luoweifu.image.rw;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;

import luoweifu.image.ImageDigital;

/**
 *This class provides a public static method that tkes an InputStream to a
 * Windows .bmp file and converts it into an ImageProducer via a
 * MemoryImageSource. You can fetch a .bmp throough a URL with the following
 * code: URL url = new URL(<wherever your URL is>) Image img=
 * createImage(BMPReader.getBMPImage(url.openStream()));
 */
public class BMP extends ReadImage {
	// Constants indication how the data is stored
	public static final int BI_RGB = 0;
	public static final int BI_RLE8 = 1;
	public static final int BI_RLE4 = 2;
	

	private int bfSize; // BMP图像文件的大小
	private int imageSize;// BMP图像数据大小
	private int biBitCount; // BMP图像的色深，即一个像素用多少位表示
	private int colorTable[];// BMP图像的调色板
	//private DataInputStream in = null;
	//BufferedImage img = null;
	
	
	
	public BMP(String srcPath) {
		super(srcPath);
	}
	
	@Override
	public BufferedImage readImage() {
		
		// Verify that the header starts with 'BM'
		try {
			if (in.read() != 'B')
				throw new IOException("Not a .BMP file!");
			
			if (in.read() != 'M')
				throw new IOException("Not a .BMP file!");
			
			
			// Get the total file size
			bfSize = intelInt(in.readInt());

			// Skip the 2 16-bit reserved words
			in.readUnsignedShort();
			in.readUnsignedShort();

			int bitmapOffset = intelInt(in.readInt());

			int bitmapInfoSize = intelInt(in.readInt());

			width = intelInt(in.readInt());
			height = intelInt(in.readInt());

			// Skip the 16-bit bitplane size
			in.readUnsignedShort();

			biBitCount = intelShort(in.readUnsignedShort());

			int compressionType = intelInt(in.readInt());

			imageSize = intelInt(in.readInt());

			// Skip pixels per meter
			in.readInt();
			in.readInt();

			int colorsUsed = intelInt(in.readInt());
			int colorsImportant = intelInt(in.readInt());
			if (colorsUsed == 0)
				colorsUsed = 1 << biBitCount;
			
			if(biBitCount <= 8) {
				colorTable = new int[colorsUsed];
				// Read the bitmap's color table
				for (int i = 0; i < colorsUsed; i++)
					colorTable[i] = (intelInt(in.readInt()) & 0xffffff) + 0xff000000;

			}
			
			// Create space for the pixels
			int pixels[] = new int[width * height];
			
			if(img == null) {
				img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);
			}
			// Read the pixels from the stream based on the compression type
			if (compressionType == BI_RGB){
				/*if (bitCount == 24)
					readRGB24(in, width, height, pixels);
				else {
					//readRGB(width, height, colorTable, bitCount, pixels, in);
					//img = ImageIO.read(new File(file));
				}	*/	
			}else if (compressionType == BI_RLE8) {
				readRLE(width, height, colorTable, biBitCount, pixels, in, imageSize,
						8);
				img.setRGB(0, 0, width, height, pixels, 0, width);
			}else if (compressionType == BI_RLE4) {
				readRLE(width, height, colorTable, biBitCount, pixels, in, imageSize,
						4);
				img.setRGB(0, 0, width, height, pixels, 0, width);
			}
			// Create a memory image source from the pixels
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return img;
		// new MemoryImageSource(width,height,pixels,0,width);
	}

	// readRGB reads in pixels values that are stored uncompressed. The bits
	// represent
	// indices into the color table
	protected static void readRGB(int width, int height, int colorTable[],
			int bitCount, int pixels[], DataInputStream in) throws IOException {
		// How many pixels can be stored in a byte?
		int pixelsPerByte = 8 / bitCount;

		// A bit mask containing the number of bits in a pixel
		int bitMask = (1 << bitCount) - 1;

		// The shift values that will move each pixel to the far right
		int bitShifts[] = new int[pixelsPerByte];

		for (int i = 0; i < pixelsPerByte; i++)
			bitShifts[i] = 8 - ((i + 1) * bitCount);

		int whichBit = 0;

		// Read in the first byte
		int currByte = in.read();

		// Start at the bottom of the pixel array and work up
		for (int h = height - 1; h >= 0; h--) {
			int pos = h * width;
			for (int w = 0; w < width; w++) {
				// Get the next pixel from the current byte
				pixels[pos] = colorTable[(currByte >> bitShifts[whichBit])
						& bitMask];
				pos++;
				whichBit++;

				// If the current bit position is past the number of pixels in
				// a byte, you advance to the next byte
				if (whichBit >= pixelsPerByte) {
					whichBit = 0;
					currByte = in.read();
				}
			}
		}
	}

	// readRLE reads run-length encoded data in either RLE4 or RLE8 format
	protected static void readRLE(int width, int height, int colorTable[],
			int bitCount, int pixels[], DataInputStream in, int imageSize,
			int pixelSize) throws IOException {
		int x = 0;
		int y = height - 1;

		// You already know how many bytes are in the image, so only go through
		// that many
		for (int i = 0; i < imageSize; i++) {
			// RLE encoding is defined by two bytes
			int byte1 = in.read();
			int byte2 = in.read();
			i += 2;

			// If byte0==0, this is an escape code
			if (byte1 == 0) {
				// If escaped, byte2==0 means you are at end of line
				if (byte2 == 0) {
					x = 0;
					y--;
					// If escaped, byte2==1 means end of bitmap
				} else if (byte2 == 1) {
					return;
					// if escaped, byte2==2 adjusts the current x and y by
					// an offset stored in the next two words
				} else if (byte2 == 2) {
					int xoff = (char) intelShort(in.readUnsignedShort());
					i += 2;
					int yoff = (char) intelShort(in.readUnsignedShort());
					i += 2;
					x += xoff;
					y -= yoff;
					// If escaped, any other value for byte 2 is the number of
					// bytes
					// that you should read as pixel values (these pixels are
					// not
					// run-length encoded)
				} else {
					int whichBit = 0;
					// Read in the next byte
					int currByte = in.read();
					i++;
					for (int j = 0; j < byte2; j++) {
						if (pixelSize == 4) {
							// The pixels are 4-bits,so half the time you shift
							// the current byte
							// to the right as the pixel value
							if (whichBit == 0) {
								pixels[y * width + x] = colorTable[(currByte >> 4) & 0xf];
							} else {
								// The rest of the time, you mask out the upper
								// 4 bits, save the
								// pixel value, then read in the next byte
								pixels[y * width + x] = colorTable[currByte & 0xf];
								currByte = in.read();
								i++;
							}
						} else {
							pixels[y * width + x] = colorTable[currByte];
							currByte = in.read();
							i++;
						}
						x++;
						if (x >= width) {
							x = 0;
							y--;
						}
					}
					// The pixels must be word-aligned, so if you read an unevel
					// number of
					// bytes, read and ignore a byte to get aligned again
					if ((byte2 & 1) == 1) {
						in.read();
						i++;
					}
				}
				// If the first byte was not 0, it is the number of pixels that
				// are encoded by byte 2
			} else {
				for (int j = 0; j < byte1; j++) {
					if (pixelSize == 4) {
						// If j is odd, use the upper 4 bits
						if ((j & 1) == 0)
							pixels[y * width + x] = colorTable[(byte2 >> 4) & 0xf];
						else
							pixels[y * width + x + 1] = colorTable[byte2 & 0xf];
					} else
						pixels[y * width + x + 1] = colorTable[byte2];

					x++;
					if (x >= width) {
						x = 0;
						y--;
					}
				}
			}
		}
	}

	// intelShort converts a 16-bit number stored in intel byte order into
	// the local host format
	protected static int intelShort(int i) {
		return ((i >> 8) & 0xff) + ((i << 8) & 0xff00);
	}

	// intelInt converts a 32-bit number stored in intel byte order into
	// the local host format
	protected static int intelInt(int i) {
		return ((i & 0xff) << 24) + ((i & 0xff00) << 8) + ((i & 0xff0000) >> 8)
				+ ((i >> 24) & 0xff);
	}

	protected void readRGB24(DataInputStream in, int w, int h, int[] pixs) {
		int r, g, b;
		for (int y = h - 1; y >= 0; y--) {
			for (int x = 0; x < w; x++) {
				try {
					b = in.read();
					// System.out.println(r);
					g = in.read();
					r = in.read();
					// pixs[(y)*w+x] = 0xff<<24 | (r&0xff)<<16 | (g&0xff)<<8 |
					// (b&0xff);
					pixs[(y) * w + x] = 0xff << 24 | r << 16 | g << 8 | b;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public int getBfSize() {
		return bfSize;
	}
	public int getImageSize() {
		return imageSize;
	}
	public int getBiBitCount() {
		return biBitCount;
	}
	public int[] getColorTable() {
		return colorTable;
	}
	public static void main(String args[]) {
		String s1 = "F:\\image processing\\BMPImages\\images_8.bmp";
		String s2 = "F:\\image processing\\BMPImages\\imagesTest1.jpg";
		//String s3 = "F:\\image processing\\BMPImages\\images1.jpg";
		BMP br = new BMP(s1);
		
		BufferedImage img = br.readImage();
		
		ImageDigital.writeImg(img, "jpg", s2);
	}
}
