package luoweifu.image.identification;
/**
 * 图像识别  比较两张图片的相似度
 * @author Administrator
 *
 */
public interface Identification {
	/**
	 * 返回的小数匹配值的小数点的位数
	 */
	public static final int DECIMAL_PALACE = 1000;
	/**
	 * 从本地图片中获得图像的特征值
	 * @param srcPath
	 * @return
	 */
	public String getCharacteristic(String srcPath);
	/**
	 * 从数据库中获得图像的特征值 
	 * @param target
	 * @returnhlhl
	 */
	//public String getCharacteristic2(String target);
	
	/**
	 * 通过两个特征值比较两个图像的相似度
	 * @param charac1
	 * @param charac2
	 * @return 一个0-1的float值，值越大则表示越相似
	 */
	public float identification(String charac1, String charac2);
}
