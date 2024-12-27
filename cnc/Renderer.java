package cnc;

import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;

import cnc.XY;

public class Renderer extends JPanel {
	private Vector<RoundToothGear> list = new Vector<RoundToothGear>();
	private double mult = 2;
	// offset for drawing picture.
	private int off = 350;

	public void add(RoundToothGear s) {
		list.add(s);
	}

	public void paint(Graphics g) {
		super.paint(g);

		for (RoundToothGear s : list) {
			final int[] copyX = copy(s.sprocketX, mult);
			final int[] copyY = copy(s.sprocketY, mult);
			g.drawPolyline(copyX, copyY, copyX.length);


			Vector<Vector<XY>> list = s.getInnerShapes( s);
			for(Vector<XY> shape: list){
				draw(g, shape, mult);
			}
		}

	}//

	
	
	

	private void draw(Graphics g, Vector<XY> curve, double mult) {
		for (int i = 1; i < curve.size(); i++) {
			g.drawLine((int) (curve.get(i - 1).x*mult) + off,
					(int) (curve.get(i - 1).y*mult) + off, (int) (curve.get(i).x *mult)
							+ off, (int) (curve.get(i).y*mult) + off);
		}
	}

	

	private int[] copy(Vector<Double> orig, double mult) {

		int[] copy = new int[orig.size()];
		for (int i = 0; i < orig.size(); i++) {
			copy[i] = (int) (orig.get(i) * mult) + off;
		}
		return copy;
	}
}// /