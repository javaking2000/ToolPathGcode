package cnc;

import java.awt.Graphics;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ToothGear {
	
	Vector<Double> xx= new Vector<Double>();
	Vector<Double> yy= new Vector<Double>();
	
	
	//offset for drawing picture.
	int off=60;
	
	public ToothGear(float inner, float outer, int numTeeth, float plusRadius){
		//inner+=plusRadius;
		//outer+=plusRadius;
		
		double deltaDeg = 360.0/(numTeeth);
		
		double x=.04;
		for(int i=0;i<numTeeth;i++){
			double angle=i*deltaDeg;
			//System.out.println("angle="+angle);
			double xL = (inner+plusRadius*2)*Math.cos(Math.toRadians(angle));
			double yL = (inner+plusRadius*2)*Math.sin(Math.toRadians(angle));
			
			double xH = (outer+plusRadius*3)*Math.cos(Math.toRadians(angle+deltaDeg*.5));
			double yH = (outer+plusRadius*3)*Math.sin(Math.toRadians(angle+deltaDeg*.5));

			double xL2 = (inner+plusRadius*2)*Math.cos(Math.toRadians(angle+deltaDeg*.99));
			double yL2 = (inner+plusRadius*2)*Math.sin(Math.toRadians(angle+deltaDeg*.99));
			
			xx.add(xL);
			yy.add(yL);
			
			xx.add(xH);
			yy.add(yH);
			
			
			xx.add(xL2);
			yy.add(yL2);
//			System.out.println("  x="+xL);
		}
		
		xx.add(xx.firstElement());
		yy.add(yy.firstElement());
		

	}
	
	
	private Vector<XY> getCurve(XY xy1, XY xy2, double plusRadius){
		Vector<XY> curve = new Vector<XY>();
		double radius=Math.sqrt( Math.pow(xy1.x-xy2.x, 2) + Math.pow(xy1.y-xy2.y, 2) )/2.0 +plusRadius;
		double midx=(xy1.x+xy2.x)/2;
		double midy=(xy1.y+xy2.y)/2;
		XY center=new XY();
		center.x=midx;
		center.y=midy;
		
		double start = getAngleRadian(center, xy2);
		int num=7;
		double deltaAng=Math.PI/num;
		// clockwise?
		for(int i=0;i<=num;i++){
			double currAng=(i*deltaAng)+start;
			XY xy=new XY();
			xy.x=Math.cos(currAng)*radius + midx;
			xy.y=Math.sin(currAng)*radius +midy;
			curve.add(xy);
		}
		return curve;
	}
	private Vector<XY> getCurve2(XY xy1, XY xy2, double plusRadius){
		Vector<XY> curve = new Vector<XY>();
		double radius=Math.sqrt( Math.pow(xy1.x-xy2.x, 2) + Math.pow(xy1.y-xy2.y, 2) )/2.0 +plusRadius;
		double midx=(xy1.x+xy2.x)/2;
		double midy=(xy1.y+xy2.y)/2;
		XY center=new XY();
		center.x=midx;
		center.y=midy;
		
		double start = getAngleRadian(center, xy2);
		int num=7;
		double deltaAng=-Math.PI/num;
		// clockwise?
		for(int i=0;i<=num;i++){
			double currAng=(i*deltaAng)+start;
			XY xy=new XY();
			xy.x=Math.cos(currAng)*radius + midx;
			xy.y=Math.sin(currAng)*radius +midy;
			curve.add(xy);
		}
		return curve;
	}
	
	private static double getAngleRadian(XY center, XY pt){
		double x=pt.x-center.x;
		double y=pt.y-center.y;
		
		return Math.atan2(y, x);
	}
	
	public class XY{ 
		public XY(){
		}
		public XY(double x, double y){
			this.x=x;
			this.y=y;
		}
		public double x;
		public double y;
	}
	
	DecimalFormat dc = new DecimalFormat("###.####");
	public String getGCode(){
		String prevLine="";
		double dStep=-.05;
		double z=dStep;
		double pixelsPerInch=56.8;  //equal to picture on screen.
		String gcode= "N5 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
		gcode += "N1"  + " G00 Z0.05 F9\r\n";
		double x = xx.get(0)/pixelsPerInch;
		double y = yy.get(0)/pixelsPerInch;
		gcode+="N2" + " G00 X" + dc.format(x)+ " Y" + dc.format(y)  +" \r\n";
		gcode += "N3"  + " G01 Z"+z+" F9\r\n";
		gcode+="N4" + " G01 X" + dc.format(x)+ " Y" + dc.format(y)  +" \r\n";
		int count=1;
		for(int i=0;i<xx.size();i++){
			x = xx.get(i)/pixelsPerInch;
			y = yy.get(i)/pixelsPerInch;
			gcode+="N" + (count++*5) + " G01 X" + dc.format(x)+ " Y" + dc.format(y)  +" \r\n";
			prevLine="N" + (count++*5) + " G01 X" + dc.format(x)+ " Y" + dc.format(y)  +" \r\n";
		}
		
		z=dStep*2;
		gcode += "N"  + (count++*5)+ " G01 Z"+z+" F9\r\n";
		gcode+=prevLine;
		for(int i=0;i<xx.size();i++){
			x = xx.get(i)/pixelsPerInch;
			y = yy.get(i)/pixelsPerInch;
			gcode+="N" + (count++*5) + " G01 X" + dc.format(x)+ " Y" + dc.format(y)  +" \r\n";
		}
		
		z=dStep*3;
		gcode += "N"  + (count++*5)+ " G01 Z"+z+" F9\r\n";
		gcode+=prevLine;
		for(int i=0;i<xx.size();i++){
			x = xx.get(i)/pixelsPerInch;
			y = yy.get(i)/pixelsPerInch;
			gcode+="N" + (count++*5) + " G01 X" + dc.format(x)+ " Y" + dc.format(y)  +" \r\n";
		}
		
		z=dStep*4;
		gcode += "N"  + (count++*5)+ " G01 Z"+z+" F9\r\n";
		gcode+=prevLine;
		for(int i=0;i<xx.size();i++){
			x = xx.get(i)/pixelsPerInch;
			y = yy.get(i)/pixelsPerInch;
			gcode+="N" + (count++*5) + " G01 X" + dc.format(x)+ " Y" + dc.format(y)  +" \r\n";
		}
		
		z=dStep*5;
		gcode += "N"  + (count++*5)+ " G01 Z"+z+" F9\r\n";
		gcode+=prevLine;
		for(int i=0;i<xx.size();i++){
			x = xx.get(i)/pixelsPerInch;
			y = yy.get(i)/pixelsPerInch;
			gcode+="N" + (count++*5) + " G01 X" + dc.format(x)+ " Y" + dc.format(y)  +" \r\n";
		}
		
		z=dStep*6;
		gcode += "N"  + (count++*5)+ " G01 Z"+z+" F9\r\n";
		gcode+=prevLine;
		for(int i=0;i<xx.size();i++){
			x = xx.get(i)/pixelsPerInch;
			y = yy.get(i)/pixelsPerInch;
			gcode+="N" + (count++*5) + " G01 X" + dc.format(x)+ " Y" + dc.format(y)  +" \r\n";
		}
		
		gcode+="M30";
		return gcode;
	}
	
	public class Renderer extends JPanel{
		private Vector<ToothGear> list = new Vector<ToothGear>();
		private double mult=2;
		public void add(ToothGear s){
			list.add(s);
		}

			public void paint(Graphics g){
				super.paint(g);
				int count=0;
				for(ToothGear s:list){
					final int[] copyX=copy(s.xx, mult);
					final int[] copyY=copy(s.yy, mult);
					g.drawPolyline(copyX, copyY, copyX.length);
				}
			}

	
	 private int[] copy(Vector<Double> orig, double mult){
		 int ofset=250;
		int[] copy=new int[orig.size()];
		for(int i=0;i<orig.size();i++){
			copy[i]=(int)(orig.get(i)*mult)+ofset;
		}
		return copy;
	 }
	}///
	
	public static void writeFile(String gcode, String filename){
		try {
			RandomAccessFile ran = new RandomAccessFile(filename+".ngc", "rw");
			ran.write(gcode.getBytes());
			ran.close();
			
			//System.exit(1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String args[]){

		ToothGear sp = new ToothGear(75, 80, 60, 0);
		ToothGear sp2 = new ToothGear(75, 80, 60, 3.6f);

		//Sprocket sp = new Sprocket(40, 60, 7);
		writeFile(sp2.getGCode(), "sproket11");
		
		JFrame frame = new JFrame();
		Renderer ren = sp2.new Renderer();
		ren.add(sp);
		//ren.add(sp2);

		frame.add(ren);
		frame.setSize(640,480);
		frame.setVisible(true);
		

	}
}
