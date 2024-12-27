package cnc;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Curve {

	// B(t)=(1-t)((1-t)(P0 +tP1) + t((1-t)P1 +tp2); //quad
	// quad
	private static double eval(double p1, double p2, double p3, double t) {
		double a = t * t;
		double b = 2 * t * (1 - t);
		double c = (1 - t) * (1 - t);
		return a * p1 + b * p2 + c * p3;

	}

	// cubic
	private static Point2D.Double point(double x1, double x2, double x3,
			double x4, double y1, double y2, double y3, double y4, double t) {
		// Set X and Y at time 't'
		double X = x1 + 3 * t * (x2 - x1) + 3 * (t * t) * (x1 + x3 - (2 * x2))
				+ (t * t * t) * (x4 - x1 + 3 * x2 - 3 * x3);
		double Y = y1 + 3 * t * (y2 - y1) + 3 * (t * t) * (y1 + y3 - (2 * y2))
				+ (t * t * t) * (y4 - y1 + 3 * y2 - 3 * y3);
		return new Point2D.Double(X, Y);
	}



	public static Point2D.Double[] getPoints(CubicCurve2D.Double curve) {
		System.out.println("cub");
		int size = 11;
		Point2D.Double[] array = new Point2D.Double[size];
		for (int t = 0; t < size; t++) {
			array[t] = point(curve.x1, curve.ctrlx1, curve.ctrly2, curve.x2,
					curve.y1, curve.ctrly1, curve.ctrly2, curve.y2, t);
		}
		return array;
	}// /

	public static Point2D.Double[] getPoints(QuadCurve2D.Double curve) {

		// QuadCurve2D.Double curve = new QuadCurve2D.Double(0,0, 1,1, 2,0);
		int size = 11;
		Point2D.Double[] array = new Point2D.Double[size];

		for (int t = 0; t < size; t++) {
			double ex = eval(curve.getX1(), curve.getCtrlX(), curve.getX2(),
					(1 - t * .1));
			double ey = eval(curve.getY1(), curve.getCtrlY(), curve.getY2(),
					(1 - t * .1));
			// System.out.println("      "+ex+", "+ey);
			array[t] = new Point2D.Double(ex, ey);
		}
		return array;
	}// /

	
	
	public static Point2D.Double[] getPoints(QuadCurve2D.Double curve, int numPts) {

		// QuadCurve2D.Double curve = new QuadCurve2D.Double(0,0, 1,1, 2,0);
		int size = numPts;
		Point2D.Double[] array = new Point2D.Double[size];

		for (int t = 0; t < size; t++) {
			double ex = eval(curve.getX1(), curve.getCtrlX(), curve.getX2(),
					(1 - t * .1));
			double ey = eval(curve.getY1(), curve.getCtrlY(), curve.getY2(),
					(1 - t * .1));
			// System.out.println("      "+ex+", "+ey);
			array[t] = new Point2D.Double(ex, ey);
		}
		return array;
	}///
	
	
	
	
	public static void main(String[] args) {
		// Book Antiqua Bold
		GraphicsEnvironment gv = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Font[] fonts = gv.getAllFonts();
		for (Font f : fonts) {
			System.out.println("" + f.getFontName());
		}

	}// /

}
