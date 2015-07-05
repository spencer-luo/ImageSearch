package luoweifu.image.transformation;

/**
 * 用y = a+bi;模拟复数
 * 
 * @author luoweifu
 * 
 */
public class Complex {
	private double a;
	private double b;
	private static Complex complex = new Complex();
	/**
	 * 构造函数
	 */
	public Complex() {
		a = 0.0;
		b = 0.0;
	}

	/**
	 * 构造函数
	 * 
	 * @param a
	 *            实部
	 * @param b
	 *            虚部
	 */
	public Complex(double a, double b) {
		this.a = a;
		this.b = b;
	}

	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}

	public void setA(double a) {
		this.a = a;
	}

	public void setB(double b) {
		this.b = b;
	}

	/**
	 * 求一个复数和一个实型数据的和；
	 * 
	 * @param a
	 *            实数
	 * @return 结果（复数）
	 */
	public Complex add(double a) {
		this.a = this.a + a;
		return this;
	}

	/**
	 * 用于求解两复数的和 z1=a1+b1i, z2=a2+b2i z1+z2 = (a1+a2) + (b1+b2)i
	 * 
	 * @param c
	 * @return
	 */
	public Complex add(Complex c) {
		a = a + c.getA();
		b = b + c.getB();
		return this;
	}
	
	public static  Complex add(Complex c1, Complex c2) {
		complex = new Complex();
		complex.setA(c1.getA() + c2.getA());
		complex.setB(c1.getB() + c2.getB());
		return complex;
	}

	/**
	 * 用于求一个复数和一个实型数据的差；
	 * 
	 * @param x
	 * @return
	 */
	public Complex minus(double x) {
		a = a - x;
		return this;
	}

	/**
	 * 用于求解两复数的差
	 * 
	 * @param c
	 * @return
	 */
	public Complex minus(Complex c) {
		a = a - c.getA();
		b = b - c.getB();
		return this;
	}

	/**
	 * 用于求一个复数和一个实型数据的积；
	 * 
	 * @param r
	 * @return
	 */
	public Complex multiply(double r) {
		a = a * r;
		b = b * r;
		return this;
	}

	/**
	 * 复数的乘法 z1=a1+b1i, z2=a2+b2i z1*z2 = (a1+b1i)*(a2+b2i)
	 *             = (a1a2-b1b2) + (a1b2+a2b1)i
	 * 
	 * @param c
	 * @return
	 */
	public Complex multiply(Complex c) {
		double a1 = this.a, b1 = this.b;
		double a2 = c.getA(), b2 = c.getB();
		a = a1 * a2 - b1 * b2;
		b = a1 * b2 + a2 * b1;
		return this;
	}

	/**
	 * 复数的乘法
	 *  z1=a1+b1i, z2=a2+b2i
	 * z1*z2 = (a1a2-b1b2) + (a1b2+a2b1)i
	 * @param c1
	 *            复数1
	 * @param c2
	 *            复数2
	 * @return 乘得的结果
	 */
	public static Complex multiply(Complex c1, Complex c2) {
		complex = new Complex();
		double a1 = c1.getA(), b1 = c1.getB();
		double a2 = c2.getA();
		double b2 = c2.getB();
		complex.setA(a1 * a2 - b1 * b2);
		complex.setB(a1 * b2 + a2 * b1);
		return complex;
	}

	/**
	 * 复数的除法 z1=a1+b1i, z2=a2+b2i z1/z2 = [(a1a2 + b1b1) + (a2b1-a1b2)i]/(a2a2 +
	 * b2b2)
	 * 
	 * @param c
	 * @return
	 */
	public Complex division(Complex c) {
		double a1 = this.a, b1 = this.b;
		double a2 = c.getA(), b2 = c.getB();
		a = (a1 * a2 + b1 * b2) / (a2 * a2 + b2 * b2);
		b = (a2 * b1 - a1 * b2) / (a2 * a2 + b2 * b2);
		return this;
	}

	@Override
	public String toString() {
		if (b >= 0) {
			return a + "+" + b + "i";
		} else {
			return a + "" + b + "i";
		}
	}

	/**
	 * 求复数的模
	 * 
	 * @return
	 */
	public double model() {
		return Math.sqrt(a * a + b * b);
	}

	/**
	 * 复数的n次幂
	 * 
	 * @param n
	 * @return
	 */
	public Complex pow(int n) {
		double r = model();
		double o = Math.atan2(b, a);
		a = Math.pow(r, n) * Math.cos(n * o);
		b = Math.pow(r, n) * Math.sin(n * o);
		return this;
	}
	/*
	 * public double sqrt(int n) { double r = model(); double o = Math.atan2(b,
	 * a); //Math. return 0; }
	 */
	/*
	 * public static void main(String[] args) { Complex c1 = new Complex(5, 3);
	 * Complex c2 = new Complex(1, 2);
	 * 
	 * c1.add(4); System.out.println("复数(5+3i)与实数4的和为："+c1);
	 * 
	 * c1.add(c2); System.out.println("复数(5+3i)与复数（1+2i）的和为："+c1);
	 * 
	 * c1.minus(4); System.out.println("复数(5+3i)与实数4的差为："+c1);
	 * 
	 * c1.minus(c2); System.out.println("复数(5+3i)与复数（1+2i）的差为："+c1);
	 * 
	 * c1.multiply(7); System.out.println("复数(5+3i)与实数7的积为："+c1);
	 * 
	 * c1.multiply(c2); System.out.println("复数(5+3i)与复数（1+2i）的积为："+c1);
	 * 
	 * c1.division(c2); System.out.println("复数(5+3i)与复数（1+2i）的商为："+c1);
	 * //System.out.println(Math.pow(81, 0.25)); }
	 */

}