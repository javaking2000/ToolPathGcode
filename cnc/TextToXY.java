/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc;

import cnc.vectors.AlphabetShapes;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.JFrame;

/**
 *
 * @author dan2
 */
public class TextToXY {

    static Hashtable<Shape, Vector<Vector<Point.Double>>> shapeToPolys = new Hashtable<Shape, Vector<Vector<Point.Double>>>();
    static Hashtable<String, Vector<Vector<Point.Double>>> charToPolys = new Hashtable<String, Vector<Vector<Point.Double>>>();

    static Hashtable<Shape, String> shapeToChar = new Hashtable<Shape, String>();
    static Hashtable<String, Shape> charToShape = new Hashtable<String, Shape>();

    /**
     *
     * @param text converts a bunch of text to bunch of vectors.
     * @param font
     * @param xoff
     * @param yoff
     * @param xMult
     * @param yMult
     * @return
     */
    public static Vector<Vector<Vector<XY>>> getShapeXY(final String text, final Font font, double xoff, double yoff, double xMult, double yMult) {
        Vector<Vector<Vector<XY>>> charsList = new Vector<Vector<Vector<XY>>>();
        double mult = -1;

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

        double[] prev = null;
        GlyphVector gv = font.createGlyphVector(g2.getFontRenderContext(), text);
        int numGlys = gv.getNumGlyphs();
        for (int i = 0; i < numGlys; i++) {

            Vector<Vector<Point.Double>> oneCharPolys = new Vector<Vector<Point.Double>>();

            Vector<Point.Double> poly = new Vector<Point.Double>();
            oneCharPolys.add(poly);

            Shape sp = gv.getGlyphOutline(i);

            shapeToPolys.put(sp, oneCharPolys);
            charToPolys.put(text, oneCharPolys);
            String strChar = text.substring(i, i + 1);
            System.out.println("char=" + strChar);
            shapeToChar.put(sp, strChar);
            charToShape.put(strChar, sp);

            PathIterator pathIt = sp.getPathIterator(g2.getTransform());

            double div = 50.0;
            double xM = xMult;
            double yM = yMult;

            while (!pathIt.isDone()) {
                double[] coords = new double[6];
                int type = pathIt.currentSegment(coords);
                if (type == pathIt.SEG_MOVETO) {
                    double x = coords[0] * xM / div + xoff;
                    double y = coords[1] * yM * mult / div + yoff;

                    poly.add(new Point.Double(x, y));

                    prev = coords;
                } else if (type == pathIt.SEG_CLOSE) {

                    poly = new Vector<Point.Double>();
                    oneCharPolys.add(poly);
                } else if (type == pathIt.SEG_LINETO) {
                    double x = coords[0] * xM / div + xoff;
                    double y = coords[1] * yM * mult / div + yoff;
                    poly.add(new Point.Double(x, y));

                    prev = coords;
                } else if (type == pathIt.SEG_QUADTO) {
                    QuadCurve2D.Double curve = new QuadCurve2D.Double(
                            prev[0], prev[1], coords[0], coords[1],
                            coords[2], coords[3]);
                    Point2D.Double[] points = Curve.getPoints(curve);
                    for (Point2D.Double p : points) {
                        double x = (p.x * xM / div + xoff);
                        double y = p.y * yM * mult / div + yoff;
                        poly.add(new Point.Double(x, y));

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
                        double x = (p.x * xM / div + xoff);
                        double y = p.y * yM * mult / div + yoff;

                        poly.add(new Point.Double(x, y));

                        double[] f = new double[2];
                        f[0] = p.x;
                        f[1] = p.y;
                        prev = f;
                    }

                }
                pathIt.next();
            }// one char
        }//multi glyph

        Iterator<Vector<Vector<Point.Double>>> letters = shapeToPolys.values().iterator();

        while (letters.hasNext()) {
            Vector<Vector<Point.Double>> letter = letters.next();
            Vector<Vector<XY>> letterXY = new Vector<Vector<XY>>();
            charsList.add(letterXY);
            for (Vector<Point.Double> sub : letter) {
                Vector<XY> letterPart = new Vector<XY>();
                letterXY.add(letterPart);
                for (Point.Double pt : sub) {
                    letterPart.add(new XY(pt.x, pt.y));
                }
            }
        }

        return charsList;
    }//end of outline//

    public static Vector<Vector<LineSegment>> shapeToRowCuts(Vector<XY> shape, int numRows) {
        Vector<Vector<LineSegment>> rowCuts = new Vector<Vector<LineSegment>>();
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (XY xy : shape) {
            if (xy.x > maxX) {
                maxX = xy.x;
            }
            if (xy.x < minX) {
                minX = xy.x;
            }
            if (xy.y > maxY) {
                maxY = xy.y;
            }
            if (xy.y < minY) {
                minY = xy.y;
            }
        }
        System.out.println("minx=" + minX + "  max=" + maxX);
        System.out.println("miny=" + minY + "  may=" + maxY);
        System.out.println("  num rows=" + numRows);

        //start at minY step to maxY
        double DY = maxY - minY;
        double yStep = DY / numRows;
        double currY = minY;
        for (int i = 0; i < numRows; i++) {
            //check all x on this y, and return lineSegments
            //if(currY<.8 || currY>1.7){
            Vector<LineSegment> segs = getXSegments(currY, minX, maxX, shape);
            if (segs.size() > 0) {
                rowCuts.add(segs);
            }
            // }
            currY = currY + yStep;

        }

        return rowCuts;
    }

    /**
     * //check all x on this y, and return lineSegments // each step compute
     * (start x, end x pair)
     *
     * @param y
     * @param minX
     * @param maxX
     * @param shape
     * @return
     */
    private static Vector<LineSegment> getXSegments(double y, double minX, double maxX, Vector<XY> shape) {
        double DX = maxX - minX;
        double xStep1000 = DX / 1000.0;
        Vector<LineSegment> segs = new Vector<LineSegment>();
        double currX = minX - xStep1000;
        double prevX = currX;
        boolean inShape = false;
        double startX = currX;
        double endX = currX;

        for (int i = 0; i < 1005; i++) {

            if (XY.pnpoly(shape, new XY(currX, y))) {

                if (!inShape) {
                    startX = currX;
                }
                inShape = true;
            } else if (inShape) {
                endX = prevX;

                segs.add(new LineSegment(new Point2D.Double(startX + xStep1000, y), new Point2D.Double(endX - xStep1000, y)));
                inShape = false;
            }
            prevX = currX;
            currX = currX + xStep1000;
        }

        return segs;
    }

    public static Vector<Vector<XY>> shapeSplitK() {
        Vector<XY> v1 = new Vector<XY>();
        v1.add(new XY(0.219375, 0.0));

        v1.add(new XY(0.219375, 0.31921875));
        v1.add(new XY(0.6556875000000001, 0.31921875));
        v1.add(new XY(0.6556875000000001, .8));
        //join
        v1.add(new XY(1.535625, .8));
        v1.add(new XY(1.535625, 0.31921875));
        v1.add(new XY(1.974375, 0.31921875));
        v1.add(new XY(1.974375, 0.0));
        v1.add(new XY(0.219375, 0.0));
        //end of 1

        Vector<XY> v2 = new Vector<XY>();
        //start of 2nd
        v2.add(new XY(0.6556875000000001, 1.7));
        v2.add(new XY(0.6556875000000001, 3.6178125));
        v2.add(new XY(0.219375, 3.6178125));
        v2.add(new XY(0.219375, 3.9365625));
        v2.add(new XY(1.974375, 3.9365625));
        v2.add(new XY(1.974375, 3.6178125));
        v2.add(new XY(1.535625, 3.6178125));
        v2.add(new XY(1.535625, 2.180625));
        v2.add(new XY(2.98228125, 3.6178125));
        v2.add(new XY(2.61665625, 3.6178125));
        v2.add(new XY(2.61665625, 3.9365625));
        v2.add(new XY(3.8894375, 3.9365625));
        v2.add(new XY(3.8894375, 3.6178125));
        v2.add(new XY(3.4344375, 3.6178125));
        v2.add(new XY(2.20959375, 2.401875));

        v2.add(new XY(2.78, 1.7));
        //joiner
        v2.add(new XY(1.73, 1.7));
        v2.add(new XY(1.535625, 1.9115625));
        v2.add(new XY(1.535625, 1.7));
        v2.add(new XY(0.6556875000000001, 1.7));

        Vector<XY> v3 = new Vector<XY>();

        //start 3rd
        v3.add(new XY(3.49, .8));
        v3.add(new XY(3.875625, 0.31921875));
        v3.add(new XY(4.2115937500000005, 0.31921875));
        v3.add(new XY(4.2115937500000005, 0.0));
        v3.add(new XY(3.0554062500000003, 0.0));
        v3.add(new XY(2.41, .8));
        v3.add(new XY(3.49, .8));

        Vector<Vector<XY>> master = new Vector<Vector<XY>>();
        master.add(v1);
        master.add(v2);
        master.add(v3);

        //
        return master;
    }

    private static DecimalFormat dc = new DecimalFormat("###.####");
    static int count = 5;
    static double feedIPM = 30;
    static double z = -.11;

    public static String convertXYtoGcode(Vector<XY> seg, String gcode) {

        gcode += "N" + count + " G00 Z.25 \r\n";
        count += 5;

        XY xy1 = seg.get(0);
        gcode += "N" + count + " G00 X" + dc.format(xy1.x) + " Y" + dc.format(xy1.y) + " \r\n";
        count += 5;
        gcode += "N" + count + " G01 Z" + z + " F" + feedIPM + "\r\n";
        count += 5;
        for (XY xy : seg) {
            double x = xy.x;
            double y = xy.y;
            gcode += "N" + count + " G01 X" + dc.format(x) + " Y" + dc.format(y) + " F" + feedIPM + " \r\n";
            count += 5;
        }
        gcode += "N" + count + " G01 Z.25 \r\n";
        count += 5;

        return gcode;
    }

    public static String convertXYtoGcodeSkipRect(Vector<XY> seg, String gcode, double sy, double ey) {

        gcode += "N" + count + " G00 Z.25 \r\n";
        count += 5;

        XY xy1 = seg.get(0);
        gcode += "N" + count + " G00 X" + dc.format(xy1.x) + " Y" + dc.format(xy1.y) + " \r\n";
        count += 5;
        gcode += "N" + count + " G01 Z" + z + " F" + feedIPM + "\r\n";
        count += 5;
        for (XY xy : seg) {
            double x = xy.x;
            double y = xy.y;
            if (!(y > sy && y < ey)) {
                gcode += "N" + count + " G01 Z" + z + " F" + feedIPM + "\r\n";
                count += 5;
                gcode += "N" + count + " G01 X" + dc.format(x) + " Y" + dc.format(y) + " F" + feedIPM + " \r\n";
                count += 5;
            } else {
                gcode += "N" + count + " G00 Z.25 \r\n";
                gcode += "N" + count + " G00 X" + dc.format(xy.x) + " Y" + dc.format(xy.y) + " \r\n";
                count += 5;
            }
        }
        gcode += "N" + count + " G01 Z.25 \r\n";
        count += 5;

        return gcode;
    }

    public static String convertRowCutsToGcode(Vector<Vector<LineSegment>> rowCuts, String gcode) {
        LineSegment line1 = rowCuts.get(0).get(0);
        double x = line1.start.getX();
        double y = line1.start.getY();
        gcode += "N" + count + " G01 Z.25 \r\n";
        count += 5;
        gcode += "N" + count + " G00 X" + dc.format(x) + " Y" + dc.format(y) + " \r\n";
        count += 5;
        boolean odd = false;
        for (Vector<LineSegment> row : rowCuts) {
            for (int i = 0; i < row.size(); i++) {
                LineSegment cut = row.get(i);
                if (odd) {
                    cut = row.get(row.size() - i - 1);
                }
                double x1 = cut.start.getX();
                double y1 = cut.start.getY();
                double x2 = cut.end.getX();
                double y2 = cut.end.getY();
                if (odd) {
                    x2 = cut.start.getX();
                    y2 = cut.start.getY();
                    x1 = cut.end.getX();
                    y1 = cut.end.getY();
                }

                //move to x1, y1
                gcode += "N" + count + " G00 X" + dc.format(x1) + " Y" + dc.format(y1) + " \r\n";
                count += 5;
                //drill down z
                gcode += "N" + count + " G01 Z" + z + " F" + feedIPM + "\r\n";
                count += 5;
                //drill out line
                gcode += "N" + count + " G01 X" + dc.format(x2) + " Y" + dc.format(y2) + " F" + feedIPM + " \r\n";
                count += 5;
                //move up z
                gcode += "N" + count + " G00 Z.25 \r\n";
                count += 5;

            }//row

            if (odd) {
                odd = false;
            } else {
                odd = true;
            }
        }
        //finish gcode.
        gcode += "M30";
        return gcode;
    }

    public static String convertRowCutsToGcode(Vector<Vector<LineSegment>> rowCuts, String gcode, double sy, double ey) {
        LineSegment line1 = rowCuts.get(0).get(0);
        double x = line1.start.getX();
        double y = line1.start.getY();
        gcode += "N" + count + " G01 Z.25 \r\n";
        count += 5;
        gcode += "N" + count + " G00 X" + dc.format(x) + " Y" + dc.format(y) + " \r\n";
        count += 5;
        boolean odd = false;
        for (Vector<LineSegment> row : rowCuts) {
            double ytest = row.get(0).start.getY();
            double xtest = row.get(0).start.getX();
            if (!(ytest > sy && ytest < ey)) {
                for (int i = 0; i < row.size(); i++) {
                    LineSegment cut = row.get(i);
                    if (odd) {
                        cut = row.get(row.size() - i - 1);
                    }
                    double x1 = cut.start.getX();
                    double y1 = cut.start.getY();
                    double x2 = cut.end.getX();
                    double y2 = cut.end.getY();
                    if (odd) {
                        x2 = cut.start.getX();
                        y2 = cut.start.getY();
                        x1 = cut.end.getX();
                        y1 = cut.end.getY();
                    }

                    //move to x1, y1
                    gcode += "N" + count + " G00 X" + dc.format(x1) + " Y" + dc.format(y1) + " \r\n";
                    count += 5;
                    //drill down z
                    gcode += "N" + count + " G01 Z" + z + " F" + feedIPM + "\r\n";
                    count += 5;
                    //drill out line
                    gcode += "N" + count + " G01 X" + dc.format(x2) + " Y" + dc.format(y2) + " F" + feedIPM + " \r\n";
                    count += 5;
                    //move up z
                    gcode += "N" + count + " G00 Z.25 \r\n";
                    count += 5;

                }//row

                if (odd) {
                    odd = false;
                } else {
                    odd = true;
                }
            } else {
                odd = false;
                //move up z
                gcode += "N" + count + " G00 Z.25 \r\n";
                count += 5;
                //move to x1, y1
                gcode += "N" + count + " G00 X" + dc.format(xtest) + " Y" + dc.format(ytest) + " \r\n";
                count += 5;
            }
        }
        //finish gcode.
        gcode += "M30";
        return gcode;
    }

    public static void saveFile(String gcode, String file) {
        try {
            RandomAccessFile ran = new RandomAccessFile(file, "rw");
            ran.write(gcode.getBytes());
            ran.close();
            System.out.println("" + ran);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void cutRowsOutAndLetter(Vector<Vector<XY>> letter) {
        String gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES, gcode outline)\r\n";
        for (Vector<XY> seg : letter) {
//                    ren.add(v123.get(0));
//                    ren.add(v123.get(1));
//                    ren.add(v123.get(2));
            Vector<Vector<LineSegment>> rowCuts = shapeToRowCuts(seg, 100);

            for (Vector<LineSegment> row : rowCuts) {
                System.out.print("" + row.size() + " " + row.get(0).start.getY());
                for (LineSegment line : row) {
                    System.out.print("\t" + line.start.getX() + ", " + line.end.getX());
                }
                System.out.println();
            }
            System.out.println("  " + seg.size());
            for (XY xy : seg) {
                System.out.println("" + xy.x + ", " + xy.y);
            }
            gcode = convertXYtoGcode(seg, gcode);
            gcode = convertRowCutsToGcode(rowCuts, gcode);
        }
        saveFile(gcode, "LetterS.ngc");

    }

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        Renderer2 ren = new Renderer2();

        final String text = "Kaczynski";
        final Font font;
        double xoff = 0;
        double yoff = 0;
        double xmult = 1.3;
        double ymult = 1.5;
        font = new Font("Serif", Font.BOLD, 180);
        //Vector<Vector<Vector<XY>>> letters = getShapeXY(text, font, xoff, yoff, xmult, ymult);

        //Vector<Vector<XY>> v123 = shapeSplitK();
        Vector<Vector<XY>> v12 = AlphabetShapes.shapeSplitS();
        String gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES, gcode outline)\r\n";
        gcode = convertXYtoGcode(v12.get(0), gcode);
        gcode = convertXYtoGcode(v12.get(1), gcode);

//        for (Vector<Vector<XY>> letter : letters) {
//            System.out.println(" let segments = " + letter.size());
        for (Vector<XY> seg : v12) {
//                if (seg.size() == 26) {
            //K_outline = seg;

            ren.add(v12.get(0));
            ren.add(v12.get(1));
//                    ren.add(v123.get(0));
//                    ren.add(v123.get(1));
//                    ren.add(v123.get(2));
            Vector<Vector<LineSegment>> rowCuts = shapeToRowCuts(seg, 50);
            gcode = convertRowCutsToGcode(rowCuts, gcode);
            for (Vector<LineSegment> row : rowCuts) {

                System.out.print("" + row.size() + " " + row.get(0).start.getY());
                for (LineSegment line : row) {
                    System.out.print("\t" + line.start.getX() + ", " + line.end.getX());
                }
                System.out.println();
            }
//                    System.out.println("  " + seg.size());
//                    for (XY xy : seg) {
//                        System.out.println("" + xy.x + ", " + xy.y);
//                    }
//                }
        }
//        }

        frame.add(ren);
        frame.setSize(640, 480);
        frame.setVisible(true);

        //gcode = convertXYtoGcode(v123.get(2), gcode);
        saveFile(gcode, "LetterS.ngc");

//Vector<Vector<XY>> v12 = AlphabetShapes.shapeSplitS();
//cutRowsOutAndLetter(v12);
    }
}
