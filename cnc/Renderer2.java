package cnc;

import static cnc.XY.pnpolys;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;

import cnc.XY;

public class Renderer2 extends JPanel {
	private Vector<Vector<XY>> list = new Vector<Vector<XY>>();
	private double mult = 50;
	// offset for drawing picture.
	private int off = 350;

	public void add(Vector<XY> v) {
		list.add(v);
	}

	public void paint(Graphics g) {
		super.paint(g);

		for(Vector<XY> shape: list){
			draw(g, shape, mult);
		}
		
		for(int y=-120;y<120;y++){
                    double ry = y*.1;
			for(int x=-120;x<120;x++){
                            double rx = x*.1;
				if(pnpolys(list, new XY(rx,ry))){
//                                    if(ry>1.1 && ry<2.1){
//                                        g.setColor(Color.CYAN);
//                                    }else
					g.setColor(Color.black);
					g.fillOval((int)(rx*mult+off), (int)(ry*mult+off), 5, 5);
					
				}else{
					g.setColor(Color.red);
					g.fillOval((int)(rx*mult+off), (int)(ry*mult+off), 5, 5);
					
				}
			}
		}

	}//

	
        
        //TODO fill using the pnpolys
        //process a row, get line segs
        
        //TODO fill line segments.
	
	

	private void draw(Graphics g, Vector<XY> curve, double mult) {
		for (int i = 1; i < curve.size(); i++) {
			g.drawLine((int) (curve.get(i - 1).x*mult) + off,
					(int) (curve.get(i - 1).y*mult) + off, (int) (curve.get(i).x *mult)
							+ off, (int) (curve.get(i).y*mult) + off);
		}
	}

	

}// /