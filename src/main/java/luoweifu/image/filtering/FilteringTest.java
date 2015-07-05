package luoweifu.image.filtering;


import luoweifu.image.ImageProcessing;

public class FilteringTest {
	public static void main(String args[]) {
		int[] pix = {
					1,2,1,4,3,
					1,10,2,3,4,
					5,2,6,8,8,
					5,5,7,0,8,
					5,6,7,8,9,
		};
		int pix2[] = {
				1,3,2,3,2,1,2,
				1,2,1,4,3,3,2,
				1,10,2,3,4,4,2,
				5,2,6,18,8,7,3,
				5,5,7,0,8,8,5,
				5,6,7,8,9,9,8,
				4,5,6,8,8,6,7,
		};
		
		ImageProcessing imgPro = new ImageProcessing();
		Filtering ft = new AverageFiltering();
		ft.decorate(imgPro);
		ft.processing("F:\\image processing\\ШЅды\\CC000038_gray.jpg", "F:\\image processing\\ШЅды\\CC000038_grayQZ.jpg", "jpg");
		/*pix = ft.processing(pix, 5, 5);
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				System.out.print(pix[i*5 + j] + "\t");
			}
			System.out.println();
		}*/
	}
}

/*
1	2	1	4	3	
1	3	4	4	4	
5	4	4	5	8	
5	5	5	6	8	
5	6	7	8	9	
 
 
 1	2	1	4	3	
1	7	7	7	4	
5	8	8	9	8	
5	9	9	10	8	
5	6	7	8	9	

*/
 