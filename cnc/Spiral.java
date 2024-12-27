package cnc;

import java.util.Vector;
import static cnc.XY.*;

import javax.swing.JFrame;

public class Spiral {
	double centX=0;
	double centY=0;
	
	/**
	 * Distance is in inches, angles are in radians.
	 * @param innerRadius
	 * @param outterRadius
	 * @param radiansTravel
	 * @param startRadian
	 * @return
	 */
	public static Vector<XY> getSpiralCW(double innerRadius, double outterRadius, double radiansTravel, double startRadian){
		Vector<XY> xyVec=new Vector<XY>();
		int numPts=100;
		double dRadian=radiansTravel/numPts;
		double dRadius=(outterRadius-innerRadius)/numPts;
		for(int i=0;i<numPts;i++){
			double x=Math.cos(startRadian+(i*dRadian))*(innerRadius+(i*dRadius));
			double y=Math.sin(startRadian+(i*dRadian))*(innerRadius+(i*dRadius));
			xyVec.add(new XY(x,y));
		}
		return xyVec;
	}
	
	public static void main(String[] args){
		
		double radianTravel=Math.PI*.9;
		double radianOffset=.09;
		double innerRadius=.25;
		double outterRadius=1.5;
                radianTravel=Math.PI*.9;
		radianOffset=.9;
		innerRadius=.25;
		outterRadius=1.5;
		
		JFrame frame = new JFrame();
		Renderer2 ren = new Renderer2();
		
		Vector<XY> spiral1 =getSpiralCW(innerRadius, outterRadius, radianTravel, 0);
		ren.add(spiral1);
		Vector<XY> spiral12 =reverse(getSpiralCW(innerRadius, outterRadius, radianTravel, 0+radianOffset));
		join(spiral1, spiral12);spiral1.add(spiral1.firstElement());
		
		Vector<XY> spiral2 =getSpiralCW(innerRadius, outterRadius, radianTravel, Math.PI/2);
		ren.add(spiral2);
		Vector<XY> spiral22 =reverse(getSpiralCW(innerRadius, outterRadius, radianTravel, Math.PI/2+radianOffset));
		join(spiral2, spiral22); spiral2.add(spiral2.firstElement());
//		
		Vector<XY> spiral3 =getSpiralCW(innerRadius, outterRadius, radianTravel, Math.PI);
		ren.add(spiral3);
		Vector<XY> spiral32 =reverse(getSpiralCW(innerRadius, outterRadius, radianTravel, Math.PI+radianOffset));
		join(spiral3, spiral32);spiral3.add(spiral3.firstElement());
//		
		Vector<XY> spiral4 =getSpiralCW(innerRadius, outterRadius, radianTravel, Math.PI*3/2);
		ren.add(spiral4);
		Vector<XY> spiral42 =reverse(getSpiralCW(innerRadius, outterRadius, radianTravel, Math.PI*3/2+radianOffset));
		join(spiral4, spiral42);spiral4.add(spiral4.firstElement());
		
		Vector<Vector<XY>> shapes = new Vector<Vector<XY>>();
		shapes.add(spiral1);
		shapes.add(spiral2);
		shapes.add(spiral3);
		shapes.add(spiral4);
		
		//pnpoly(Vector<XY> poly, XY p)
		for(int y=0;y<120;y++){
			for(int x=0;x<120;x++){
				if(pnpolys(shapes, new XY(x*.3,y*.3))){
					//draw black circle
                                        //done in Renderer
				}else{
					//draw red circle
				}
			}
		}

		frame.add(ren);
		frame.setSize(640, 480);
		frame.setVisible(true);
	}
}
