package cnc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Dots {

	
	
	private static Image img = null;

	public static void main(String[] arg) {
		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		final JFrame frame = new JFrame();
		final JPanel show = new JPanel() {
			public void paint(Graphics g) {
				g.setColor(Color.black);
				g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				
				g.setColor(Color.white);
				int start=2;
				for(int y=0;y<dim.height;y=y+2){
				  for(int x=start;x<dim.width;x=x+4){
					g2.drawRect(x, y, 1, 1);
				  }
				  if(start==0){
					  start=2;
				  }else{
					  start=0;
				  }
				}
				
				
			}
		};
		frame.add(show);
		
		frame.setSize(dim);
		frame.validate();
		frame.setVisible(true);
	}
}
