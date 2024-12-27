package cnc;

import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

public class Prop {
	private double width;
	private double length;

	public Prop(double length) {
		this.width = length / 8;
		this.length = length;
		double propThickness = length / 16;

		Vector<Vector<XYZ>> curves = getXYZPathSegBottom( propThickness);
		//Vector<Vector<XYZ>> curves = getXYZPathSegTop(length, propThickness);

		String gcode = getGCodeXYSwap( curves);
		try {
			RandomAccessFile ran = new RandomAccessFile("propBottom"+length+".ngc", "rw");
			ran.write(gcode.getBytes());
			ran.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private String noSci(double num){
		NumberFormat formatter = new DecimalFormat("###.######");
		return formatter.format(num);
	}

//	public String getGCode(Vector<Vector<XYZ>> curves){
//		int count=15;
//		String gcode = "N5 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  prop)\r\n";
//		gcode += "N" + count + " G00 Z1 \r\n";
//		count += 5;
//		
//		for(Vector<XYZ> curve: curves){
//			XYZ pt0=curve.get(0);
//			gcode += "N" + count + " G01 X" + noSci(pt0.x)+ " Y" + noSci(pt0.y)  + " Z"+noSci(pt0.z)+"  F6 \r\n";
//			//
//			count += 5;
//			for(int i=1;i<curve.size();i++){
//				XYZ pt1=curve.get(i);
//				gcode += "N" + count + " G01 X" + noSci(pt1.x)+ " Y" + noSci(pt1.y)  + " Z"+noSci(pt1.z)+"  F6 \r\n";
//			}
//		}
//		gcode += "N" + count + " G00 Z1 \r\n";
//		count += 5;
//		gcode += "N" + (count+5) + " M30";
//		
//		return gcode;
//	}//
	
	
	/**
	 * After each curve path, lift up before next curve.
	 * @param curves
	 * @return
	 */
	public String getGCodeLift(Vector<Vector<XYZ>> curves){
		int count=15;
		String gcode = "N5 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  prop)\r\n";
		gcode += "N" + count + " G00 Z1 \r\n";
		count += 5;
		
		for(Vector<XYZ> curve: curves){
			XYZ pt0=curve.get(0);
			//move to xy,
			gcode += "N" + count + " G00 X" + noSci(pt0.x)+ " Y" + noSci(pt0.y) +"  F6 \r\n";
			//move down
			gcode += "N" + count + " G01 X" + noSci(pt0.x)+ " Y" + noSci(pt0.y)  + " Z"+noSci(pt0.z)+"  F6 \r\n";
			count += 5;
			for(int i=1;i<curve.size();i++){
				XYZ pt1=curve.get(i);
				gcode += "N" + count + " G01 X" + noSci(pt1.x)+ " Y" + noSci(pt1.y)  + " Z"+noSci(pt1.z)+"  F6 \r\n";
			}
			//lift up
			gcode += "N" + count + " G01  Z1  F6 \r\n";
			count += 5;
		}

		gcode += "N" + (count+5) + " M30";
		
		return gcode;
	}//
	
	/**
	 * After each curve path, lift up before next curve.
	 * @param curves
	 * @return
	 */
	public String getGCodeXYSwap(Vector<Vector<XYZ>> curves){
		int count=15;
		String gcode = "N5 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  prop)\r\n";
		gcode += "N" + count + " G00 Z1 \r\n";
		count += 5;
		
		for(Vector<XYZ> curve: curves){
			XYZ pt0=curve.get(0);
			//move to xy,
			//gcode += "N" + count + " G00 X" + noSci(pt0.y)+ " Y" + noSci(pt0.x) +"  F6 \r\n";
			//move down
			gcode += "N" + count + " G01 X" + noSci(pt0.y)+ " Y" + noSci(pt0.x)  + " Z"+noSci(pt0.z)+"  F6 \r\n";
			count += 5;
			for(int i=1;i<curve.size();i++){
				XYZ pt1=curve.get(i);
				gcode += "N" + count + " G01 X" + noSci(pt1.y)+ " Y" + noSci(pt1.x)  + " Z"+noSci(pt1.z)+"  F6 \r\n";
				count += 5;
			}
			//lift up
			//gcode += "N" + count + " G01  Z1  F6 \r\n";
			//count += 5;
		}

		gcode += "N" + (count+5) + " M30";
		
		return gcode;
	}//
	

	/**
	 * This returns all the segments going from left of prop to right. these are
	 * the under side of the prop
	 * 
	 * @return
	 */
	public Vector<Vector<XYZ>> getXYZPathSegBottom(double propThickness) {
		Vector<Vector<XYZ>> allSegs = new Vector<Vector<XYZ>>();
		double per = 0.0;
		double step = .0625; // 1/16
		int steps = (int) (this.length / step);
		for (int i = 0; i <= steps; i++) {
			// given step or percent of length of wing, compute curve segment
			// and add to curve list.
			per = (double) i / steps;
			Vector<XYZ> curve = getCurveSegBottom(per, propThickness);
			
			System.out.println("" + i + "   " + per + "    curve="
					+ curve.get(0) + ",            " + curve.get(1));
			allSegs.add(curve);
		}
		return allSegs;
	}//
	
	
	public Vector<Vector<XYZ>> getXYZPathSegBottom2(double propThickness) {
		double len = this.length;
		double wid = this.width;
		//drill hole in center
		
		return null;
	}
	
	
	public Vector<Vector<XYZ>> getXYZPathSegTop(double propLength,
			double propThickness) {
		Vector<Vector<XYZ>> allSegs = new Vector<Vector<XYZ>>();
		double per = 0.0;
		double step = .0625; // 1/16
		int steps = (int) (propLength / step);
		for (int i = 1; i <= steps; i++) {
			// given step or percent of length of wing, compute curve segment
			// and add to curve list.
			per = (double) i / steps;
			Vector<XYZ> curve = getCurveSegTop(per, propThickness);
			allSegs.add(curve);
		}
		return allSegs;
	}//

	public Vector<XYZ> getCurveSegBottom(double percent, double depth) {
		double y = (length * percent)-length/2;

		Vector<XYZ> curv = new Vector<XYZ>();
		if (percent < .45) {
			if(percent<.04){
				percent=.04;
			}
			//.04 to .45
			curv = getTwoPt(y, width, depth, 45 * (percent * 2));
		} else if (percent >= .45 && percent <= .55) {
			curv = getTwoPt(y, width, 0, 0);
		} else if (percent > .55) {
			if(percent >.96){
				percent=.96;
			}
			percent=percent-.51;
			percent=.49-percent;
			//.55 to .96
			curv = getTwoPtFlip(y, width, depth, 45 * (percent * 2));
		}

		return curv;
	}
	
	
	public Vector<XYZ> getCurveSegTop(double percent, double depth) {
		double y = (length * percent)-length/2;

		Vector<XYZ> curv = new Vector<XYZ>();
		if (percent < .45) {
			curv = get20Pt(y, width, depth, 45 * (percent * 2));
		} else if (percent >= .45 && percent <= .55) {
			curv = getTwoPt(y, width, 0, 0);
		} else if (percent > .55) {
			curv = get20PtFlip(y, width, depth, 45 * (percent * 2));
		}

		return curv;
	}
	

	/**
	 * This can be used for strait lines. The under part of wing or prop.
	 * 
	 * @param y
	 * @param width
	 * @param depth
	 * @param angle
	 * @return
	 */
	private Vector<XYZ> getTwoPt(double y, double width, double depth,
			double angle) {
		double mid=(depth/2) * Math.cos(Math.toRadians(angle * 2));
		XYZ pt1 = new XYZ();
		pt1.x = -width/2;
		pt1.y = y;
		pt1.z = 0 - mid;

		XYZ pt2 = new XYZ();
		pt2.x = width/2;
		pt2.y = y;
		pt2.z = -depth * Math.sin(Math.toRadians(angle * 2))  - mid;

		Vector<XYZ> vec = new Vector<XYZ>();
		vec.add(pt1);
		vec.add(pt2);

		return vec;
	}

	private Vector<XYZ> getTwoPtFlip(double y, double width, double depth,
			double angle) {

		System.out.println("angle="+angle);
		double mid=(depth/2) * Math.cos(Math.toRadians(angle * 2));
		XYZ pt1 = new XYZ();
		pt1.x = -width/2;
		pt1.y = y;
		pt1.z = -depth * Math.sin(Math.toRadians(angle * 2)) -mid;

		XYZ pt2 = new XYZ();
		pt2.x = width/2;
		pt2.y = y;
		pt2.z = 0-mid;

		Vector<XYZ> vec = new Vector<XYZ>();
		vec.add(pt1);
		vec.add(pt2);

		return vec;
	}
	
	
	
	
	
	private Vector<XYZ> get20Pt(double y, double width, double depth,
			double angle) {
		XYZ pt1 = new XYZ();
		pt1.x = -width/2;
		pt1.y = y;
		pt1.z = 0;
		

		XYZ pt2 = new XYZ();
		pt2.x = width/2;
		pt2.y = y;
		pt2.z = -depth * Math.sin(Math.toRadians(angle * 2));
		
		
		
		
		
		double medX = 0;
		double medZ = pt1.z;
		if(pt2.z>pt1.z){
			medZ=pt2.z;
		}
		medZ=medZ-.1;
		
		QuadCurve2D.Double curve = new QuadCurve2D.Double(pt1.x,pt1.z, medX,medZ,  pt2.x,pt2.z);
		Point2D.Double[] XZs = Curve.getPoints(curve, 20);


		Vector<XYZ> vec = new Vector<XYZ>();
		for(Point2D.Double pt : XZs){
			XYZ xyz= new XYZ();
			xyz.x=pt.x;
			xyz.y=y;
			xyz.z=pt.y;
			vec.add(xyz);
		}
		
		return vec;
	}

	private Vector<XYZ> get20PtFlip(double y, double width, double depth,
			double angle) {

		XYZ pt1 = new XYZ();
		pt1.x = -width/2;
		pt1.y = y;
		pt1.z = -depth * Math.sin(Math.toRadians(angle * 2));

		XYZ pt2 = new XYZ();
		pt2.x = width/2;
		pt2.y = y;
		pt2.z = 0;
		
		
		double medX = 0;
		double medZ = pt1.z;
		if(pt2.z>pt1.z){
			medZ=pt2.z;
		}
		medZ=medZ-.1;
		
		QuadCurve2D.Double curve = new QuadCurve2D.Double(pt1.x,pt1.z, medX,medZ,  pt2.x,pt2.z);
		Point2D.Double[] XZs = Curve.getPoints(curve, 20);


		Vector<XYZ> vec = new Vector<XYZ>();
		for(Point2D.Double pt : XZs){
			XYZ xyz= new XYZ();
			xyz.x=pt.x;
			xyz.y=y;
			xyz.z=pt.y;
			vec.add(xyz);
		}
		

		return vec;
	}
	
	
	

	public class XYZ {
		public double x;
		public double y;
		public double z;

		public String toString() {
			return "x=" + x + "  y=" + y + "  z=" + z;
		}
	}

        /**
         * interpolate between v0 and v1, given t.
         * @param v0
         * @param v1
         * @param t .5 is the middle. 1 is at v1.
         * @return 
         */
	public static double lerp(double v0, double v1, double t) {
		return (1 - t) * v0 + t * v1;
	}

	public static void main(String[] args) {

		new Prop(7);
	}

}
