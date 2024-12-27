package cnc;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Text2GCode {
	private static DecimalFormat dc = new DecimalFormat("###.####");
        
        public static Font singleLine;
        public static Font singleLinePro;
        public static Font cursivePro;
        
        public Text2GCode() {
            try {
                singleLine =Font.createFont(Font.TRUETYPE_FONT, new
                                 File("/home/dan2/Downloads/fonts/simplify-notation-single-line-font/SimplifyNotationSingleLine-GOPmm.ttf"));
                singleLinePro =Font.createFont(Font.TRUETYPE_FONT, new
			 File("/home/dan2/Downloads/fonts/znikoslsvginot-font/Znikoslsvginot8-GOB3y.ttf"));
                
                cursivePro =Font.createFont(Font.TRUETYPE_FONT, new
			 File("/home/dan2/Downloads/fonts/cursive/auditory-perception-single-line-font/AuditoryPerceptionSingleLine-RpELl.ttf"));
                 
            } catch (FontFormatException ex) {
                System.out.println("Cant load font files, "+ex.getMessage());
            } catch (IOException ex) {
                System.out.println("Cant load font files, "+ex.getMessage());
            }
        }
        
        public Font getCursive(){
            cursivePro = cursivePro.deriveFont(Font.ITALIC, 36);
            return cursivePro;
        }
        
        public Font getFont(){
            singleLine = singleLine.deriveFont(Font.ITALIC, 36);
            return singleLine;
        }
        
         public Font getFontPro(){
            singleLinePro = singleLinePro.deriveFont(Font.ITALIC, 22);
            return singleLinePro;
        }
         
         public Font getFontPro(int size){
            singleLinePro = singleLinePro.deriveFont(Font.ITALIC, size);
            return singleLinePro;
        }

	
	private void fillAllChar(Hashtable<Shape, Vector<Vector<Point.Double>>> shapeToPolys, Hashtable<Shape, String> shapeToChar, String gcode){
		//String gcode=gcodeOutline;
		Set<Shape> shapes = shapeToPolys.keySet();
		for(Shape shape: shapes){
			System.out.println("Processing Shape!");
			String strChar=shapeToChar.get(shape);
			Vector<Vector<Point.Double>> polys= shapeToPolys.get(shape);
			Vector<Line2D.Double> lineSegs = processOneShape(shape, strChar);
			System.out.println(""+strChar+"  lines="+lineSegs.size());
			for(Line2D.Double line : lineSegs){
				//add gcode for drill segment
				
				double x1=line.x1/3;
				double x2=line.x2/3;
				double y1=line.y1* -1 / 3;
				double y2=line.y2* -1 / 3;
				
				System.out.println("adding Line");
				gcode += "N" + count + " G00 Z1 \r\n";
				count += 5;
				
				//move to start, 
				gcode += "N" + count + " G00 X" + dc.format(x1)+ " Y" + dc.format(y1)  + " \r\n";
				count += 5;
				gcode += "N" + count + " G01 Z0 F9 \r\n";
				count += 5;
				//drill in
				gcode += "N" + count + " G01 Z-0.02 F6\r\n";
				count += 5;

				// drill the segment.
				gcode += "N" + count + " G00 X" + dc.format(x1)+ " Y" + dc.format(y1)  + " \r\n";
				count += 5;
				gcode += "N" + count + " G01 X" + dc.format(x2)+ " Y" + dc.format(y2)  + " F6 \r\n";
				count += 5;
				
				//pull out
				gcode += "N" + count + " G00 Z1 \r\n";
				count += 5;
			}
		}
	}
	
	
	/**
	 * Process one character and create a Vector of LineSegs for drilling!
	 * @param shape - shape of char
	 * @param c - the string char.
	 * @return
	 */
	private static Vector<Line2D.Double> processOneShape(Shape shape, String c){
		Vector<Line2D.Double> lineSegs = new Vector<Line2D.Double>();
		
		Rectangle area = shape.getBounds();
		Vector<Vector<Point.Double>> oneCharPolys = shapeToPolys.get(shape);
		Vector<Point.Double> mainPoly = oneCharPolys.get(0);
		
		double step=1.0/16.0; //inches
		int rows = (int)(area.getBounds2D().getHeight()/step);
		int stepsPerRow = (int)(area.getBounds2D().getWidth()/step);
		boolean alreadyIn=false;
		for(int row=0;row<rows;row++){
			double y = row*step;
			
			Double x1=null;
			Double y1=null;
			Double x2=null;
			Double y2=null;
			for(int s=0;s<stepsPerRow;s++){
				
				Point.Double leftPt= new Point.Double(-1, y);
				Point.Double testPt = new Point.Double(s*step, y);
				boolean inBounds = area.contains(testPt);
				
				if(inBounds && pointInPoly(leftPt, testPt, mainPoly)){
						//inside the char
						//AaBbDdge ij OoPpQqR
						if(oneCharPolys.size()>1 && c!="i" && c!="j" && pointInIsland(oneCharPolys,  leftPt,  testPt) ){
							// inside island and not i or j, DO NOTHING!
						}else if(!alreadyIn){
							//First point in, start the lineSegment!
							alreadyIn=true;
							x1=testPt.x;
							y1=testPt.y;
						}else{
							//update end seg
							x2=testPt.x;
							y2=testPt.y;
						}
				}else if(alreadyIn){
					//First point outside the char!  End the line seg, use prev pt.
					alreadyIn=false;
					Line2D.Double seg = new Line2D.Double(x1+step, y1, x2-step, y2);
					lineSegs.add(seg);
					System.out.println("adding line,  row="+row+"  width="+area.getBounds2D().getWidth()+"  step="+step);
				}
			}
		}
		//remove the very top and the very bottom lineSegments.
		if(lineSegs.size()>0){
			lineSegs.remove(0);
			lineSegs.remove(lineSegs.size()-1);
		}
		
		return lineSegs;
	}//
	
	private static boolean pointInIsland(Vector<Vector<Point.Double>> oneCharPolys, Point.Double leftPt, Point.Double testPt){
		if(pointInPoly(leftPt, testPt, oneCharPolys.get(1)) || pointInPoly(leftPt, testPt, oneCharPolys.get(oneCharPolys.size()-1))){
			return true;
		} 
		return false;
	}
	
	public static boolean pointInPoly(Point.Double left, Point.Double pt, Vector<Point.Double> poly){
		int intersectCount=0;
		Line2D.Double ray = new Line2D.Double(left.x, left.y,  pt.x, pt.y );
		
		Point.Double p1 = poly.firstElement();
		for(int i=1;i<poly.size();i++){
			Point.Double p2 = poly.get(i);
			Line2D.Double pline = new Line2D.Double(p1.x, p1.y, p2.x, p2.y);
			if(ray.intersectsLine(pline)){
				intersectCount++;
			}
			p1=p2;
		}
		if(intersectCount%2==1){
			return true;
		}
		return false;
	}
	
	
	
	//private DecimalFormat dc = new DecimalFormat("###.####");
	static Hashtable<Shape, Vector<Vector<Point.Double>>> shapeToPolys = new Hashtable<Shape, Vector<Vector<Point.Double>>>();
	static Hashtable<Shape, String> shapeToChar = new Hashtable<Shape, String>();
	int count = 10;
        public void setCount(int count){
            this.count=count;
        }
	
	public String getGCodeOutline(final String text, final Font font, double xoff, double yoff, double z, double feedIPM) {
		String gcode = "N5 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  " + text + ")\r\n";
		JFrame frame = new JFrame();
		frame.setSize(850, 350);
		frame.setVisible(true);
		frame.validate();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		Graphics2D g2 = (Graphics2D) frame.getContentPane().getGraphics();
		g2.setColor(Color.black);

		double[] prev = null;
		//double xoff=0;
			
		
		GlyphVector gv = font.createGlyphVector(g2.getFontRenderContext(), text);
		int numGlys = gv.getNumGlyphs();
		for(int i=0;i<numGlys;i++){
			
			Vector<Vector<Point.Double>> oneCharPolys = new Vector<Vector<Point.Double>>();
			
			Vector<Point.Double> poly = new Vector<Point.Double>();
			oneCharPolys.add(poly);
		
			
			Shape sp = gv.getGlyphOutline(i);
               
                        
			shapeToPolys.put(sp, oneCharPolys);
			String strChar=text.substring(i, i+1);
			System.out.println("char="+strChar);
			shapeToChar.put(sp, strChar);
			
			PathIterator pathIt = sp.getPathIterator(g2.getTransform());
                        
double div = 50.0;
			g2.setColor(Color.black);
			while (!pathIt.isDone()) {
				double[] coords = new double[6];
				int type = pathIt.currentSegment(coords);
				if (type == pathIt.SEG_MOVETO) {
					double x = coords[0]/div +xoff;
					double y = coords[1]*-1 / div+yoff;
					
					poly.add(new Point.Double(x, y));

					gcode += "N" + count + " G00 Z.25 \r\n";
					count += 5;
					gcode += "N" + count + " G00 X" + dc.format(x)+ " Y" + dc.format(y)  + " \r\n";
					count += 5;
					gcode += "N" + count + " G01 Z0 F"+feedIPM+" \r\n";
					count += 5;
					gcode += "N" + count + " G01 Z"+z+" F"+feedIPM+"\r\n";
					count += 5;
					//repeat xy coord for dumb cnc machine.
					gcode += "N" + count + " G00 X" + dc.format(x)+ " Y" + dc.format(y)  + " \r\n";
					count += 5;

					prev = coords;
				} else if (type == pathIt.SEG_CLOSE) {
					gcode += "N" + count + " G00 Z.25 \r\n";
					count += 5;
					g2.setColor(Color.red);
					
					poly = new Vector<Point.Double>();
					oneCharPolys.add(poly);
				} else if (type == pathIt.SEG_LINETO) {
					double x = coords[0]/div+xoff;
					double y = coords[1] * -1 / div+yoff;
					poly.add(new Point.Double(x, y));
					
					gcode += "N" + count + " G01 X" + dc.format(x) + " Y" + dc.format(y)  + " F9 \r\n";
					count += 5;

					g2.drawLine((int) prev[0] + 150,
							(int) prev[1] + 150, (int) (coords[0] + 150),
							(int) coords[1] + 150);
					
					prev = coords;
				} else if (type == pathIt.SEG_QUADTO) {
					QuadCurve2D.Double curve = new QuadCurve2D.Double(
							prev[0], prev[1], coords[0], coords[1],
							coords[2], coords[3]);
					Point2D.Double[] points = Curve.getPoints(curve);
					for (Point2D.Double p : points) {
						double x = (p.x / div +xoff);
						double y = p.y * -1 / div+yoff;
						poly.add(new Point.Double(x, y));
						
						gcode += "N" + count + " G01 X" + dc.format(x) + " Y" + dc.format(y) + " F"+feedIPM+" \r\n";
					
						count += 5;

						g2.drawLine((int) prev[0] + 150,
								(int) prev[1] + 150, (int) (p.x + 150),
								(int) p.y + 150);
						double[] f = new double[2];
						f[0] = p.x;
						f[1] = p.y;
						prev = f;
					}

				} else if (type == pathIt.SEG_CUBICTO) {
					CubicCurve2D.Double curve = new CubicCurve2D.Double(
							prev[0], prev[1], coords[0], coords[1],
							coords[2], coords[3], coords[4], coords[5]);
					Point2D.Double[] points = Curve.getPoints(curve);
					for (Point2D.Double p : points) {
						double x = (p.x / div +xoff);
						double y = p.y * -1 / div+yoff;
						
						poly.add(new Point.Double(x, y));
						
						gcode += "N" + count+" G01 X"+ dc.format(x)+" Y" + dc.format(y) + " F"+feedIPM+" \r\n";
						count += 5;

						g2.drawLine((int) prev[0] + 150,
								(int) prev[1] + 150, (int) (p.x  + 150),
								(int) p.y + 150);
						double[] f = new double[2];
						f[0] = p.x;
						f[1] = p.y;
						prev = f;
					}

				}
				pathIt.next();
			}// one char
		}//multi glyph

		gcode += "N" + count + " G00 Z1 \r\n";


		return gcode;
	}//end of outline//
        
        
        
        
        /**
         * 
         * @param glyph shape outline of glyph.
         * @param xy1 point1 in shape
         * @param xy2 point2 in shape
         * @param maxDepth max tool depth
         * @return a toolpath point for vbit.
         */
        public XY shapeToVbitPath(Shape glyph, XY xy1, XY xy2, double maxDepth){
            double xMid = (xy1.x + xy2.x)/2.0;
            double yMid = (xy1.y +xy2.y)/2.0;
            double rise = xy2.y - xy1.y;
            double run = xy2.x - xy1.x;
            double angleRad = Math.atan2(rise, run);
            
            double toolRadius = maxDepth/2.0; 
            double xT = xMid + Math.cos(angleRad) * toolRadius;
            double yT = yMid + Math.sin(angleRad) * toolRadius;
            
            if(!glyph.contains(xT, yT)){
                xT = xMid + Math.cos(angleRad) * -toolRadius;
                yT = yMid + Math.sin(angleRad) * -toolRadius;
            }
            // test circle in Shape test.
            boolean contains = glyph.contains(xT, yT, toolRadius, toolRadius);
            while(!contains && toolRadius > .025){
                toolRadius = toolRadius - .025;
                xT = xMid + Math.cos(angleRad) * toolRadius;
                yT = yMid + Math.sin(angleRad) * toolRadius;
                if (!glyph.contains(xT, yT)) {
                    xT = xMid + Math.cos(angleRad) * -toolRadius;
                    yT = yMid + Math.sin(angleRad) * -toolRadius;
                }
                contains = glyph.contains(xT, yT, toolRadius, toolRadius);
            }
            
            return new XY(xT, yT);
        }
        
        
        
        /**
         * Hard code to 60 degree, and 1/4 max depth. depth==width
         * TODO keep the bit inside the shape.
         *  1) compute the max depth/diameter that fits inside shape, given x,y shape.
         *  2) use the computed diameter,depth to compute x,y tool path
         * A) test diameter, .025 to .25
         *  a) given x,y shape and diameter - compute inside shape the x,y tool path
         *      i) use the xy shape points to get line segment
         *      ii) use the mid x,y + radius
         *          angle = arctan(rise/run);
         *          xToolPath = midx+cos(angle)*R
         *          yToolPath = midy+sin(angle)*R
         *      
         * @param text
         * @param font
         * @return 
         */
        public  String getGCodeOutlineVbit(final String text, final Font font) {
		String gcode = "N5 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  " + text + ")\r\n";
		JFrame frame = new JFrame();
		frame.setSize(850, 350);
		frame.setVisible(true);
		frame.validate();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		Graphics2D g2 = (Graphics2D) frame.getContentPane().getGraphics();
		g2.setColor(Color.black);

		double[] prev = null;
		//double xoff=0;
			
		
		GlyphVector gv = font.createGlyphVector(g2.getFontRenderContext(), text);
		int numGlys = gv.getNumGlyphs();
		for(int i=0;i<numGlys;i++){
			
			Vector<Vector<Point.Double>> oneCharPolys = new Vector<Vector<Point.Double>>();
			
			Vector<Point.Double> poly = new Vector<Point.Double>();
			oneCharPolys.add(poly);
		
			
			Shape sp = gv.getGlyphOutline(i);
                        System.out.println(" shape = "+sp.getClass().getName());
			shapeToPolys.put(sp, oneCharPolys);
			String strChar=text.substring(i, i+1);
			System.out.println("char="+strChar);
			shapeToChar.put(sp, strChar);
			
			PathIterator pathIt = sp.getPathIterator(g2.getTransform());
                        pathIt.getWindingRule();

			g2.setColor(Color.black);
			while (!pathIt.isDone()) {
				double[] coords = new double[6];
				int type = pathIt.currentSegment(coords);
				if (type == pathIt.SEG_MOVETO) {
					double x = coords[0]/3;
					double y = coords[1]*-1 / 3;
					
					poly.add(new Point.Double(x, y));

					gcode += "N" + count + " G00 Z1 \r\n";
					count += 5;
					gcode += "N" + count + " G00 X" + dc.format(x)+ " Y" + dc.format(y)  + " \r\n";
					count += 5;
					gcode += "N" + count + " G01 Z0 F9 \r\n";
					count += 5;
					gcode += "N" + count + " G01 Z-0.05 F6\r\n";
					count += 5;
					//repeat xy coord for dumb cnc machine.
					gcode += "N" + count + " G00 X" + dc.format(x)+ " Y" + dc.format(y)  + " \r\n";
					count += 5;

					prev = coords;
				} else if (type == pathIt.SEG_CLOSE) {
					gcode += "N" + count + " G00 Z1 \r\n";
					count += 5;
					g2.setColor(Color.red);
					
					poly = new Vector<Point.Double>();
					oneCharPolys.add(poly);
				} else if (type == pathIt.SEG_LINETO) {
					double x = coords[0]/3;
					double y = coords[1] * -1 / 3;
					poly.add(new Point.Double(x, y));
					
					gcode += "N" + count + " G01 X" + x + " Y" + y  + " F6 \r\n";
					count += 5;

					g2.drawLine((int) prev[0] + 150,
							(int) prev[1] + 150, (int) (coords[0] + 150),
							(int) coords[1] + 150);
					
					prev = coords;
				} else if (type == pathIt.SEG_QUADTO) {
					QuadCurve2D.Double curve = new QuadCurve2D.Double(
							prev[0], prev[1], coords[0], coords[1],
							coords[2], coords[3]);
					Point2D.Double[] points = Curve.getPoints(curve);
					for (Point2D.Double p : points) {
						double x = (p.x / 3 );
						double y = p.y * -1 / 3;
						poly.add(new Point.Double(x, y));
						
						gcode += "N" + count + " G01 X" + dc.format(x) + " Y" + dc.format(y) + " F6 \r\n";
					
						count += 5;

						g2.drawLine((int) prev[0] + 150,
								(int) prev[1] + 150, (int) (p.x + 150),
								(int) p.y + 150);
						double[] f = new double[2];
						f[0] = p.x;
						f[1] = p.y;
						prev = f;
					}

				} else if (type == pathIt.SEG_CUBICTO) {
					CubicCurve2D.Double curve = new CubicCurve2D.Double(
							prev[0], prev[1], coords[0], coords[1],
							coords[2], coords[3], coords[4], coords[5]);
					Point2D.Double[] points = Curve.getPoints(curve);
					for (Point2D.Double p : points) {
						double x = (p.x / 3 );
						double y = p.y * -1 / 3;
						
						poly.add(new Point.Double(x, y));
						
						gcode += "N" + count+" G01 X"+ dc.format(x)+" Y" + dc.format(y) + " F6 \r\n";
						count += 5;

						g2.drawLine((int) prev[0] + 150,
								(int) prev[1] + 150, (int) (p.x  + 150),
								(int) p.y + 150);
						double[] f = new double[2];
						f[0] = p.x;
						f[1] = p.y;
						prev = f;
					}

				}
				pathIt.next();
			}// one char
		}//multi glyph

		gcode += "N" + count + " G00 Z1 \r\n";


		return gcode;
	}//end of outline//
	
	

	private static void drawFont(final Font f, final String text) {
		JFrame frame = new JFrame();
		JPanel p = new JPanel() {
			public void paint(Graphics g) {
				System.out.println("painting");
				g.setFont(f);
				g.drawString(text, 20, 260);
			}
		};
		frame.getContentPane().add(p);
		frame.setSize(800, 300);
		
		p.repaint();
		frame.setVisible(true);
	}
	
	
	

	public static void main(String[] args) {
//            double rise = 100;
//            double run = 200;
//            double angleRad = Math.atan2(rise, run);
//            System.out.println("Rad angle = "+angleRad);

		String text = "a";
		
		Font font = null;
		try {
//			font =Font.createFont(Font.TRUETYPE_FONT, new
//			 File("C:\\Users\\Dan\\Downloads\\sunflowers\\SUNFLOWERS Personal Use.ttf"));
			
//			font =Font.createFont(Font.TRUETYPE_FONT, new
//					 File("C:\\Users\\Dan\\Downloads\\liar_script\\Liar Script - TTF.ttf"));
			
//			font =Font.createFont(Font.TRUETYPE_FONT, new
//					 File("C:\\Users\\Dan\\Downloads\\mean_casat\\MeanCasatBold_PERSONAL_USE.ttf"));
			
//			font =Font.createFont(Font.TRUETYPE_FONT, new
//					 File("C:\\Users\\Dan\\Downloads\\lovers-quarrel\\LoversQuarrel-Regular.ttf"));
//			
//			font =Font.createFont(Font.TRUETYPE_FONT, new
//					 File("C:\\Users\\Dan\\Downloads\\lovely_day\\Lovely Day Personal Use.ttf"));
////			
//			font =Font.createFont(Font.TRUETYPE_FONT, new
//					 File("C:\\Users\\Dan\\Downloads\\lily-script-one\\LilyScriptOne-Regular.ttf"));
//			
//				
			
			
//			
//			font =Font.createFont(Font.TRUETYPE_FONT, new
//					 File("C:\\Users\\Dan\\Downloads\\allura\\Allura-Regular.ttf"));
//			
//			font =Font.createFont(Font.TRUETYPE_FONT, new
//			 File("C:\\Users\\Dan\\Downloads\\wine_date\\WineDate-Regular.ttf"));
			
//			font =Font.createFont(Font.TRUETYPE_FONT, new
//					 File("C:\\Users\\Dan\\Downloads\\lilly_mae\\LillyMae-Regular.otf"));
			
//			font =Font.createFont(Font.TRUETYPE_FONT, new
//					 File("C:\\Users\\Dan\\Downloads\\alishader\\AlishaderDemo.ttf"));
//			
//			font =Font.createFont(Font.TRUETYPE_FONT, new
//					 File("C:\\Users\\Dan\\Downloads\\black_party\\Black Party.ttf"));
//			
			
//			font =Font.createFont(Font.TRUETYPE_FONT, new
//					 File("C:\\Users\\Dan\\Downloads\\sweet_hipster\\Sweet Hipster.ttf"));
			
			
//			 font =Font.createFont(Font.TRUETYPE_FONT, new
//			 File("C:/Users/Dan/Downloads/alice_in_wonderland/Alice_in_Wonderland_3.ttf"));
//			 font =Font.createFont(Font.TRUETYPE_FONT, new
//			 File("C:/Users/Dan/Downloads/olivia_3/Olivia-Regular.otf"));
//			 font =Font.createFont(Font.TRUETYPE_FONT, new
//			 File("C:\\Users\\Dan\\Downloads\\grand_aventure\\Grand_Aventure.otf"));
			
//			 font =Font.createFont(Font.TRUETYPE_FONT, new File("C:/Users/Dan/Downloads/zallman_caps/ZallmanCaps.TTF"));
			 
//			 font =Font.createFont(Font.TRUETYPE_FONT, new
//			 File("C:/Users/Dan/Downloads/apex_lake/Apex Lake Regular.ttf"));
			 
//		 font =Font.createFont(Font.TRUETYPE_FONT, new
//			 File("C:\\Users\\Dan\\Desktop\\font\\foglihtenno07/FoglihtenNo07_084.otf"));


//not cursive
font =Font.createFont(Font.TRUETYPE_FONT, new
			 File("/home/dan2/Downloads/fonts/simplify-notation-single-line-font/SimplifyNotationSingleLine-GOPmm.ttf"));

font =Font.createFont(Font.TRUETYPE_FONT, new
			 File("/home/dan2/Downloads/fonts/cursive/auditory-perception-single-line-font/AuditoryPerceptionSingleLine-RpELl.ttf"));
//font =Font.createFont(Font.TRUETYPE_FONT, new
//			 File("/home/dan2/Downloads/fonts/worry-lines-single-line-font/WorryLinesSingleLine-ZV17K.ttf"));


//font =Font.createFont(Font.TRUETYPE_FONT, new
//			 File("/home/dan2/Downloads/fonts/znikoslsvginot-font/Znikoslsvginot8-GOB3y.ttf"));



			
			//GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			
//			String fonts[] = 
//				      GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
//
//				    for ( int i = 0; i < fonts.length; i++ )
//				    {
//				      System.out.println(fonts[i]);
//				    }
				    
			//font = new Font("SansSerif", Font.ITALIC, 48);
			//font = font.deriveFont(28.0f);
		
			//drawFont(font.deriveFont(198.0f), text);

                        //text = "Nick & Nicole";
                        
			//font = new Font("Serif", Font.ITALIC, 12);
                        font = font.deriveFont(Font.ITALIC, 36);
                        //drawFont(font, text);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}



		

		///////////////////////
		//"AaBbDdge ij OoPpQqR"
		///////////////////////
                Text2GCode tg = new Text2GCode();
                text = "LaChevet";
                double feedIPM = 11;
		//String gcode = tg.getGCodeOutline(text, font, -1.5, 0, -.05, feedIPM);
                //String gcode =  tg.getGCodeOutline(text, tg.getFontPro(150), -5, 0, -.09, feedIPM);
                String gcode =  tg.getGCodeOutline(text, tg.getFontPro(32), 0, 0, -.05, feedIPM);
                
//		gcode +=tg.getGCodeOutline("r", tg.getFontPro(150), -1.3, 5.5-11, -.09, feedIPM);;
//                gcode +=tg.getGCodeOutline("a", tg.getFontPro(150), -1.5, 3.0-11, -.09, feedIPM);
//                gcode +=tg.getGCodeOutline("b", tg.getFontPro(150), -1.5, 0-11, -.09, feedIPM);
//                gcode +=tg.getGCodeOutline("s", tg.getFontPro(150), -1.5, -2.5-11, -.09, feedIPM);
//                gcode +=tg.getGCodeOutline("k", tg.getFontPro(150), -1.5, -5.5-11, -.09, feedIPM);
//                gcode +=tg.getGCodeOutline("i", tg.getFontPro(150), -1.3, -8-11, -.09, feedIPM);
		//fillAllChar( shapeToPolys,  shapeToChar, gcode);
		gcode += "M30";
		
		try {
			//RandomAccessFile ran = new RandomAccessFile("alphabet/Kelly12Fill.ngc", "rw");// foglihtenno07/
			RandomAccessFile ran = new RandomAccessFile("LaChevet32.ngc", "rw");
			ran.write(gcode.getBytes());
			ran.close();
			System.out.println(""+ran);
			//System.exit(1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

                
                
                


	}// end of main///

}
