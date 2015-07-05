package luoweifu.image.transformation;

public class TransformationTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(Math.pow(16,3));
		System.out.println(256/8);
		Transformation tf = new FFT();		
		//tf.processing("F:\\image processing\\transformation\\lingxing.jpg", "F:\\image processing\\transformation\\FFT.jpg", "jpg");
		//¶þÎ¬ComplexÐÍ
		/*Complex[][] complexs = {
				{new Complex(0, 0),new Complex(1, 0),new Complex(0, 0),new Complex(2, 0),},
				{new Complex(0, 0),new Complex(3, 0),new Complex(0, 0),new Complex(4, 0),},
				{new Complex(0, 0),new Complex(5, 0),new Complex(0, 0),new Complex(6, 0),},
				{new Complex(0, 0),new Complex(7, 0),new Complex(0, 0),new Complex(8, 0),}
		};
		Complex[][] comp = ((FFT) tf).fft(complexs, 4, 4);
				
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				System.out.print(comp[i][j] + "\t");
				//System.out.print(comp[i*4 + j] + "\t");
			}
			System.out.println();
		}*/
		
		
	}

}
