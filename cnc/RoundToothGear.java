package cnc;

import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Vector;
import javax.swing.JFrame;
import cnc.XY;
import static cnc.XY.*;

public class RoundToothGear {

    private float inner;
    public Vector<Double> sprocketX = new Vector<Double>();
    public Vector<Double> sprocketY = new Vector<Double>();

    private double subRadius = 0;
    private Vector<XY> cnts = new Vector<XY>();
    private DecimalFormat dc = new DecimalFormat("###.####");
    private int count = 1;
    public static double pixelsPerInch = 56.8; // equal to picture on screen.

    private float halfRouter;

    boolean doInner;

    public RoundToothGear(float inner, float outer, int numTeeth,
            float plusRadius, boolean doInner) {
        halfRouter = plusRadius;
        this.doInner = doInner;
        this.subRadius = inner * .80;
        cnts.add(new XY(subRadius / 1.5, 0));
        cnts.add(new XY(subRadius / 1.5 * Math.cos(2.09), subRadius / 1.5 * Math.sin(2.09)));
        cnts.add(new XY(subRadius / 1.5 * Math.cos(4.18), subRadius / 1.5 * Math.sin(4.18)));

        double deltaDeg = 360.0 / (numTeeth);

        double num = 2;//extrude number

        //double x = .04;
        for (int i = 0; i < numTeeth; i++) {
            double angle = i * deltaDeg;

            double xM = (inner + outer + num) / 2 * Math.cos(Math.toRadians(angle));
            double yM = (inner + outer + num) / 2 * Math.sin(Math.toRadians(angle));

            double xM2 = (inner + outer + num) / 2
                    * Math.cos(Math.toRadians(angle + deltaDeg * (.50)));
            double yM2 = (inner + outer + num) / 2
                    * Math.sin(Math.toRadians(angle + deltaDeg * (.50)));

            double xM22 = (inner + outer - num) / 2
                    * Math.cos(Math.toRadians(angle + deltaDeg * (.50)));
            double yM22 = (inner + outer - num) / 2
                    * Math.sin(Math.toRadians(angle + deltaDeg * (.50)));

            double xM3 = (inner + outer - num) / 2
                    * Math.cos(Math.toRadians(angle + deltaDeg));
            double yM3 = (inner + outer - num) / 2
                    * Math.sin(Math.toRadians(angle + deltaDeg));

            // going counter clockwise.
            //outside curve, tooth
            Vector<XY> curve = getCurve(new XY(xM2, yM2), new XY(xM, yM),
                    plusRadius);
            for (int k = 0; k < curve.size(); k++) {
                sprocketX.add(curve.get(k).x);
                sprocketY.add(curve.get(k).y);
            }

            //inside curve
            Vector<XY> curve2 = getCurve2(new XY(xM3, yM3), new XY(xM22, yM22),
                    -plusRadius);
            for (int k = 0; k < curve2.size(); k++) {
                sprocketX.add(curve2.get(k).x);
                sprocketY.add(curve2.get(k).y);
            }

        }
        sprocketX.add(sprocketX.firstElement());
        sprocketY.add(sprocketY.firstElement());
    }

    private Vector<XY> getCurve(XY xy1, XY xy2, double plusRadius) {
        Vector<XY> curve = new Vector<XY>();
        double radius = Math.sqrt(Math.pow(xy1.x - xy2.x, 2)
                + Math.pow(xy1.y - xy2.y, 2))
                / 2.0 + plusRadius;
        double midx = (xy1.x + xy2.x) / 2;
        double midy = (xy1.y + xy2.y) / 2;
        XY center = new XY();
        center.x = midx;
        center.y = midy;

        double start = getAngleRadian(center, xy2);
        int num = 20;
        double deltaAng = Math.PI / num;
        // clockwise?
        for (int i = 0; i <= num; i++) {
            double currAng = (i * deltaAng) + start;
            XY xy = new XY();
            xy.x = Math.cos(currAng) * radius + midx;
            xy.y = Math.sin(currAng) * radius + midy;
            curve.add(xy);
        }
        return curve;
    }

    /**
     * Curve is drawn in the opposite direction. deltaAngle is negative. Draws
     * concave arcs.
     *
     * @param xy1
     * @param xy2
     * @param plusRadius
     * @return
     */
    private Vector<XY> getCurve2(XY xy1, XY xy2, double plusRadius) {
        Vector<XY> curve = new Vector<XY>();
        double radius = Math.sqrt(Math.pow(xy1.x - xy2.x, 2)
                + Math.pow(xy1.y - xy2.y, 2))
                / 2.0 + plusRadius;
        double midx = (xy1.x + xy2.x) / 2;
        double midy = (xy1.y + xy2.y) / 2;
        XY center = new XY();
        center.x = midx;
        center.y = midy;

        double start = getAngleRadian(center, xy2);
        int num = 15;
        double deltaAng = -Math.PI / num;
        // clockwise?
        for (int i = 0; i <= num; i++) {
            double currAng = (i * deltaAng) + start;
            XY xy = new XY();
            xy.x = Math.cos(currAng) * radius + midx;
            xy.y = Math.sin(currAng) * radius + midy;
            curve.add(xy);
        }
        return curve;
    }

    private static double getAngleRadian(XY center, XY pt) {
        double x = pt.x - center.x;
        double y = pt.y - center.y;

        return Math.atan2(y, x);
    }
    
    private static XY getPerpandicularXY(XY xy1, XY xy2, double radius){
        double rad1 = getAngleRadian(xy1, xy2);
        double radD1 = rad1-Math.PI/2;
        double radD2 = rad1+Math.PI/2;
        XY mid = new XY((xy1.x+xy2.x)/2.0, (xy1.y+xy2.y)/2.0);
        XY p1 = new XY(mid.x+Math.cos(radD1)*radius, mid.y+Math.sin(radD1)*radius);
        XY p2 = new XY(mid.x+Math.cos(radD2)*radius, mid.y+Math.sin(radD2)*radius);
        return null;
    }

    /**
     *
     * @param depthInches
     * @param feedRateInches
     * @param depthStep
     * @return
     */
    public String getGCode(double depthInches, int feedRateInches, double depthStep, boolean addTabs) {
        String gcode = "N5 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";

        //TODO store the sprocketX y as shape.
        //TODO change renderer to get shapes to draw!
        //do the inner shaps first.
        System.out.println("doInner=" + doInner);
        if (this.doInner) {
            Vector<Vector<XY>> shapes = getInnerShapes(this);
            for (Vector<XY> shape : shapes) {
                gcode += convertCurve(shape, feedRateInches, depthStep, depthInches, addTabs);
            }
        } else {
            //just do the center point
            gcode += "(start of center drill code) \r\n";
            gcode += convertCurve(getCircle(0, 0, .01, 12), feedRateInches, depthStep, depthInches, addTabs);
        }

        //String prevLine = "";
        int numD = (int) Math.abs(depthInches / depthStep);
        System.out.println("num steps=" + numD);
        for (int j = 1; j <= numD; j++) {
            
            double z = depthStep * j;
            System.out.println("z="+z);
            //double pixelsPerInch = 56.8; // equal to picture on screen.

            double x = sprocketX.get(0) / pixelsPerInch;
            double y = sprocketY.get(0) / pixelsPerInch;
            gcode += "N" + (count++ * 5) + " G00 X" + dc.format(x) + " Y" + dc.format(y) + " \r\n";
            gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedRateInches + "\r\n";
            gcode += "N" + (count++ * 5) + " G01 X" + dc.format(x) + " Y" + dc.format(y) + " \r\n";

            //do tabs.
            if (addTabs && j == numD ) {
                System.out.println("Last step path shape");
                double xprev =0;
                double yprev =0;
                double dist = 0;
                boolean up = false;
                //tabs
                for (int i = 0; i < sprocketX.size(); i++) {
                    x = sprocketX.get(i) / pixelsPerInch;
                    y = sprocketY.get(i) / pixelsPerInch;

                    dist += distance(x, y, xprev, yprev);
                    //System.out.println("distance = " + dist+"   "+x+", "+xprev);
                    if ((up && dist > .25) || (!up && dist > 1.25)) {
                        if (up) {
                            //move down
                            gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedRateInches + "\r\n";
                            up = false;
                        } else {
                            //move up
                            gcode += "N" + (count++ * 5) + " G01 Z" + dc.format((z + Math.abs(depthStep)) )+ " F" + feedRateInches + "\r\n";
                            up = true;
                        }
                        dist = 0;
                    }
                    gcode += "N" + (count++ * 5) + " G01 X" + dc.format(x) + " Y" + dc.format(y) + " \r\n";

                    xprev = x;
                    yprev = y;
                }
            } else {
                for (int i = 0; i < sprocketX.size(); i++) {
                    x = sprocketX.get(i) / pixelsPerInch;
                    y = sprocketY.get(i) / pixelsPerInch;
                    gcode += "N" + (count++ * 5) + " G01 X" + dc.format(x) + " Y" + dc.format(y) + " \r\n";
                }
            }
        }

        gcode += "N" + (count++ * 5) + " G00 Z0.5 F" + feedRateInches + "\r\n";
        gcode += "M30";

        return gcode;
    }

    /**
     * Converts into Gcode.
     *
     * @param curve
     * @return
     */
    private String convertCurve(Vector<XY> curve, int feedRateInches, double depthStep, double depthInches, boolean tabs) {
        String gcode = "\n";
        String lastLine = "";
        double dStep = depthStep;
        int numD = (int) Math.abs(depthInches / dStep);
        double z = dStep;

        double x = curve.firstElement().x / pixelsPerInch;
        double y = curve.firstElement().y / pixelsPerInch;

        gcode += "N" + (count++ * 5) + " G00 X" + dc.format(x) + " Y"
                + dc.format(y) + " \r\n";

        for (int j = 1; j <= numD; j++) {

            z = dStep * j;
            gcode += "N" + (count++ * 5) + " G01 Z" +dc.format( z )+ " F" + feedRateInches + "\r\n";

            x = curve.firstElement().x / pixelsPerInch;
            y = curve.firstElement().y / pixelsPerInch;
            gcode += "N" + (count++ * 5) + " G01 X" + dc.format(x) + " Y"
                    + dc.format(y) + " \r\n";
            
            if (tabs && j == numD) {
                System.out.println("Last step path shape");
                XY prev = null;
                double dist = 0;
                boolean up = false;
                //tabs
                for (int i = 1; i < curve.size(); i++) {
                    XY xy = curve.get(i);
                    if(prev!=null){
                        dist+=distanceInch(prev, xy);
                        //System.out.println("distance = "+dist);
                        if(dist> .25){
                            if(up){
                                //move down
                                gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedRateInches + "\r\n";
                                up = false;
                            }else{
                                //move up
                                gcode += "N" + (count++ * 5) + " G01 Z" +dc.format( (z +Math.abs(dStep)) )+" F" + feedRateInches + "\r\n";
                                up = true;
                            }
                            dist=0;
                        }
                    }
                    x = xy.x / pixelsPerInch;
                    y = xy.y / pixelsPerInch;
                    lastLine = "N" + (count++ * 5) + " G01 X" + dc.format(x) + " Y"
                            + dc.format(y) + " \r\n";
                    gcode += lastLine;
                    prev = xy;
                }
            } else {
                for (int i = 1; i < curve.size(); i++) {
                    x = curve.get(i).x / pixelsPerInch;
                    y = curve.get(i).y / pixelsPerInch;
                    lastLine = "N" + (count++ * 5) + " G01 X" + dc.format(x) + " Y"
                            + dc.format(y) + " \r\n";
                    gcode += lastLine;
                }
            }
        }
        gcode += "N" + (count++ * 5) + " G01 Z0.5 F" + feedRateInches + "\r\n";
        gcode += lastLine;

        return gcode;
    }
    
    private double distanceInch(XY xy1, XY xy2){
        double Dx = xy1.x - xy2.x;
        double Dy = xy1.y - xy2.y;
        
        return Math.sqrt(Dx*Dx + Dy*Dy)/ pixelsPerInch;
    }
    
    private double distanceInch(double x, double y, double x2, double y2){
        double Dx = x - x2;
        double Dy = y - y2;
        
        return Math.sqrt(Dx*Dx + Dy*Dy)/ pixelsPerInch;
    }
    
    private double distance(double x, double y, double x2, double y2){
        double Dx = x - x2;
        double Dy = y - y2;
        
        return Math.sqrt(Dx*Dx + Dy*Dy);
    }

    private Vector<XY> getCircle(double x, double y, double radius, int numPts) {
        //XY xy1 = new XY(Math.cos(0)*radius + x, Math.sin(0)*radius +y);
        //System.out.println("getCircle x="+x+"  radius="+radius +" xy.x="+xy1.x/this.pixelsPerInch);
        Vector<XY> xyList = new Vector<XY>();
        double delta = 6.282 / numPts;
        for (int i = 0; i < numPts; i++) {
            double angle = i * delta;
            XY xy = new XY(Math.cos(angle) * radius + x, Math.sin(angle) * radius + y);
            xyList.add(xy);
        }
        XY xy = new XY(Math.cos(6.281) * radius + x, Math.sin(6.281) * radius + y);
        xyList.add(xy);
        return xyList;
    }

    public Vector<Vector<XY>> getInnerShapes(RoundToothGear s) {
        Vector<Vector<XY>> shapes = new Vector<Vector<XY>>();
        shapes.add(getCircle(0, 0, .1, 12));

        for (XY xy : s.cnts) {
            double cx = xy.x;
            double cy = xy.y;
            //System.out.println(" CX="+cx+"  cy="+cy);
            double radius = s.subRadius / 2 * .6 - halfRouter;
            shapes.add(getCircle(cx, cy, radius, 40));
        }

        Vector<Vector<XY>> list = three(s, halfRouter);
        //three(g, s, 0);
        for (Vector<XY> shape : list) {
            shapes.add(shape);
        }
        return shapes;
    }

    /**
     * set of three curved triangles. Uses the center points of two side balls,
     * and the center to generate three curved lines.
     *
     * @param s
     * @param of
     * @return
     */
    private Vector<Vector<XY>> three(RoundToothGear s, float of) {
        Vector<Vector<XY>> list = new Vector<Vector<XY>>();

        double tweek = 0;
        double shapeRadiusMult = 1.06;
        shapeRadiusMult = 0.9;

        //ONE - lower right tri
        //the three main lines of a triangle cure.
        Vector<XY> line1 = getShapeArc(45, 75, 0, 0, s.subRadius * shapeRadiusMult - of);
        Vector<XY> line2 = getShapeArc(100, 135, s.cnts.get(0).x, s.cnts.get(0).y, s.subRadius / 2 + tweek + of);
        Vector<XY> line3 = getShapeArc(-15, 20, s.cnts.get(1).x, s.cnts.get(1).y, s.subRadius / 2 + tweek + of);
        
        //the end curves connection the lines.
        Vector<XY> curve1 = getCurve(
                new XY(line1.firstElement().x, line1.firstElement().y),
                new XY(line2.firstElement().x, line2.firstElement().y), 0);
        Vector<XY> curve2 = getCurve(
                new XY(line2.lastElement().x, line2.lastElement().y),
                new XY(line3.firstElement().x, line3.firstElement().y), 0);
        Vector<XY> curve3 = getCurve(
                new XY(line3.lastElement().x, line3.lastElement().y),
                new XY(line1.lastElement().x, line1.lastElement().y), 0);
        //combine into one shape
        Vector<XY> main = new Vector<XY>();
        join(main, XY.reverse(line1));
        join(main, reverse(curve1));
        join(main, line2);
        join(main, reverse(curve2));
        join(main, line3);
        join(main, reverse(curve3));
        list.add(main);

        //TWO - left tri
        Vector<XY> line11 = getShapeArc(165, 195, 0, 0, s.subRadius * shapeRadiusMult - of);
        Vector<XY> line22 = getShapeArc(220, 255, s.cnts.get(1).x, s.cnts.get(1).y, s.subRadius / 2 + tweek + of);
        Vector<XY> line33 = getShapeArc(105, 140, s.cnts.get(2).x, s.cnts.get(2).y, s.subRadius / 2 + tweek + of);
        Vector<XY> curve11 = getCurve(
                new XY(line11.firstElement().x, line11.firstElement().y),
                new XY(line22.firstElement().x, line22.firstElement().y), 0);
        Vector<XY> curve22 = getCurve(new XY(line22.lastElement().x,
                line22.lastElement().y), new XY(line33.firstElement().x,
                line33.firstElement().y), 0);
        Vector<XY> curve33 = getCurve(new XY(line33.lastElement().x,
                line33.lastElement().y),
                new XY(line11.lastElement().x, line11.lastElement().y), 0);
        //combine into one shape
        Vector<XY> main2 = new Vector<XY>();
        join(main2, reverse(line11));
        join(main2, reverse(curve11));
        join(main2, line22);
        join(main2, reverse(curve22));
        join(main2, line33);
        join(main2, reverse(curve33));
        list.add(main2);

        //THREE - top right
        Vector<XY> line111 = getShapeArc(285, 315, 0, 0, s.subRadius * shapeRadiusMult - of);
        Vector<XY> line222 = getShapeArc(340, 375, s.cnts.get(2).x, s.cnts.get(2).y, s.subRadius / 2 + tweek + of);
        Vector<XY> line333 = getShapeArc(225, 260, s.cnts.get(0).x, s.cnts.get(0).y, s.subRadius / 2 + tweek + of);
        Vector<XY> curve111 = getCurve(
                new XY(line111.firstElement().x, line111.firstElement().y),
                new XY(line222.firstElement().x, line222.firstElement().y), 0);
        Vector<XY> curve222 = getCurve(new XY(line222.lastElement().x,
                line222.lastElement().y), new XY(line333.firstElement().x,
                line333.firstElement().y), 0);
        Vector<XY> curve333 = getCurve(new XY(line333.lastElement().x,
                line333.lastElement().y),
                new XY(line111.lastElement().x, line111.lastElement().y), 0);
        //combine into one shape
        Vector<XY> main3 = new Vector<XY>();
        join(main3, reverse(line111));
        join(main3, reverse(curve111));
        join(main3, line222);
        join(main3, reverse(curve222));
        join(main3, line333);
        join(main3, reverse(curve333));
        list.add(main3);

        return list;
    }

    private Vector<XY> getShapeArc(int startAngle, int endAngle,
            double cx, double cy, double radius) {
        // 30 to 90 main radius
        Vector<XY> outterLine = new Vector<XY>();
        //int[] xPoints = new int[60];
        //int[] yPoints = new int[60];
        //int count = 0;
        for (int i = startAngle; i < endAngle; i++) {

            double x = cx + Math.cos(Math.toRadians(i)) * (radius);
            double y = cy + Math.sin(Math.toRadians(i)) * (radius);
            outterLine.add(new XY(x, y));

            //xPoints[count] = (int) x + off;
            //yPoints[count] = (int) y + off;
            //count++;
        }
        //g.drawPolyline(xPoints, yPoints, yPoints.length);

        // first sphere 60 to 120;
        // second shhere -0 to 60;
        return outterLine;
    }

    public static void writeFile(String gcode, String filename) {
        try {
            RandomAccessFile ran = new RandomAccessFile(filename + ".ngc", "rw");
            System.out.println("" + System.getProperty("user.dir"));
            ran.write(gcode.getBytes());
            ran.close();

            // System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static double getPitch(double radius, int teeth) {
        return radius * 6.283 / teeth;
    }

    public static double getRadius(int numTeeth, double pitch) {
        return (numTeeth * pitch) / 6.283;
    }

    public static void main(String args[]) {
        //System.out.println(""+getRadius(11,getPitch(1.1, 11))+"  "+getPitch(1.1, 11));
        //System.out.println(""+getRadius(60,.46)+"  "+getPitch(.44, 6));

        //.628 inch * 56.8
        //radius=.493   6 teeth  
        double radius = .32946;
        double pitch = .46;
        pitch = .345;
        pitch = .23;
        //System.out.println("pitch="+getPitch(radius, 6)+"    6 tooth Radius="+getRadius(6, pitch));
        //System.out.println("test 30 tooth Radius="+getRadius(30, getPitch(.44, 6)));

        System.out.println("8 tooth Radius=" + getRadius(8, pitch));
        System.out.println("10 tooth Radius=" + getRadius(10, pitch));
        System.out.println("15 tooth Radius=" + getRadius(15, pitch));
        System.out.println("30 tooth Radius=" + getRadius(30, pitch));
        System.out.println("32 tooth Radius=" + getRadius(32, pitch));
        System.out.println("60 tooth Radius=" + getRadius(60, pitch));

//		//RoundToothSprocket sp = new RoundToothSprocket(130, 155, 60, 0);
//		//RoundToothSprocket sp2 = new RoundToothSprocket(130, 155, 60, 3.3f); //3.6
//		RoundToothSprocket sp = new RoundToothSprocket(95, 115, 30, 0, true);
//		RoundToothSprocket sp2 = new RoundToothSprocket(95, 115, 30, 3.6f, true);
        //RoundToothSprocket sp = new RoundToothSprocket(25, 25, 6, 0, false);
        //2.2 *56.8 =125
//		RoundToothSprocket sp2 = new RoundToothSprocket(125, 125, 60, 2.6f, true); //3.5f  //pitch=.23
//		RoundToothSprocket sp = new RoundToothSprocket(125, 125, 60, 0f, true);
//		RoundToothSprocket sp2 = new RoundToothSprocket(250, 250, 60, 2.6f, true); //3.5f  //pitch=.23
//		RoundToothSprocket sp = new RoundToothSprocket(250, 250, 60, 0f, true);
        int numTeeth = 8;
        double depthInch = .4;
        int innerRadius = (int) (getRadius(numTeeth, pitch) * pixelsPerInch);
        int outterRadius = (int) (getRadius(numTeeth, pitch) * pixelsPerInch);
        RoundToothGear sp2 = new RoundToothGear(innerRadius, outterRadius, numTeeth, 2.8f, false); //3.5f  //pitch=.23
        RoundToothGear sp = new RoundToothGear(innerRadius, outterRadius, numTeeth, 0f, false);

        //.2929 *56.8
//		RoundToothSprocket sp2 = new RoundToothSprocket(17, 17, 8, 2.6f, false); //3.5f  //pitch=.23
//		RoundToothSprocket sp = new RoundToothSprocket(17, 17, 8, 0f, false);
        int feedRateInches = 36;
        double depthStepInches = -.1;
        boolean addTabs = true;

        String mainG = sp2.getGCode(depthInch, feedRateInches, depthStepInches, addTabs);

        writeFile(mainG, "sproket" + numTeeth);

        JFrame frame = new JFrame();
        Renderer ren = new Renderer();
        ren.add(sp);
        //ren.add(sp2);

        frame.add(ren);
        frame.setSize(640, 480);
        frame.setVisible(true);

    }
}
