package cnc;

import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JFrame;

import cnc.XY;
import static cnc.XY.add;
import java.io.File;

public class Circle {

    private static DecimalFormat dc = new DecimalFormat("###.####");
    private static int count = 1;
    //private static double pixelsPerInch = 56.8; // equal to picture on screen.

    public Vector<XY> getCircle(double x, double y, double radius, int numPts) {
        // XY xy1 = new XY(Math.cos(0)*radius + x, Math.sin(0)*radius +y);
        // System.out.println("getCircle x="+x+"  radius="+radius
        // +" xy.x="+xy1.x/this.pixelsPerInch);
        Vector<XY> xyList = new Vector<XY>();
        double delta = 6.282 / numPts;
        for (int i = 0; i < numPts; i++) {
            double angle = i * delta;
            XY xy = new XY(Math.cos(angle) * radius + x, Math.sin(angle)
                    * radius + y);
            xyList.add(xy);
        }
        XY xy = new XY(Math.cos(6.281) * radius + x, Math.sin(6.281) * radius
                + y);
        xyList.add(xy);
        return xyList;
    }
    
    public Vector<XY> getCircleReverse(double x, double y, double radius, int numPts) {
        // XY xy1 = new XY(Math.cos(0)*radius + x, Math.sin(0)*radius +y);
        // System.out.println("getCircle x="+x+"  radius="+radius
        // +" xy.x="+xy1.x/this.pixelsPerInch);
        Vector<XY> xyList = new Vector<XY>();
        double delta = -6.282 / numPts;
        for (int i = 0; i < numPts; i++) {
            double angle = i * delta;
            XY xy = new XY(Math.cos(angle) * radius + x, Math.sin(angle)
                    * radius + y);
            xyList.add(xy);
        }
        XY xy = new XY(Math.cos(-6.281) * radius + x, Math.sin(-6.281) * radius
                + y);
        xyList.add(xy);
        return xyList;
    }
    
    public Vector<XY> getHalfCircle(double x, double y, double radius, int numPts) {
        // XY xy1 = new XY(Math.cos(0)*radius + x, Math.sin(0)*radius +y);
        // System.out.println("getCircle x="+x+"  radius="+radius
        // +" xy.x="+xy1.x/this.pixelsPerInch);
        Vector<XY> xyList = new Vector<XY>();
        double delta = -3.142 / (numPts-1);
        double startAngle = 90;
        double endAngle = 270;
        for (int i = 0; i < numPts; i++) {
            double angle = i * delta ;
            XY xy = new XY(Math.cos(angle) * radius + x, Math.sin(angle)
                    * radius + y);
            xyList.add(xy);
        }
        XY xy = new XY(Math.cos(6.281) * radius + x, Math.sin(6.281) * radius
                + y);
        xyList.add(xy);
        return xyList;
    }


    private Vector<XY> getSpiral(double x, double y, double radius,
            double drillRadius) {
        int numPts = 100;
        int numlayers = (int) (radius / drillRadius);
        Vector<XY> xyList = new Vector<XY>();
        for (int j = 0; j < numlayers; j++) {
            numPts -= 3;
            double subRad = radius - j * drillRadius;
            double delta = 6.282 / numPts;
            for (int i = 0; i < numPts; i++) {
                double angle = i * delta;
                XY xy = new XY(Math.cos(angle) * subRad + x, Math.sin(angle)
                        * subRad + y);
                xyList.add(xy);
            }
            XY xy = new XY(Math.cos(6.281) * subRad + x, Math.sin(6.281) * subRad
                    + y);
            xyList.add(xy);
        }

        return xyList;
    }

    /**
     * Converts into Gcode.
     *
     * @param curve
     * @return
     */
    private String convertCurve(Vector<XY> curve) {
        String gcode = "\n";
        String lastLine = "";
        double dStep = -.05;
        double z = dStep;

        double x = curve.firstElement().x;
        double y = curve.firstElement().y;

        gcode += "N" + (count++ * 5) + " G00 X" + dc.format(x) + " Y"
                + dc.format(y) + " \r\n";

        for (int j = 1; j < 6; j++) {

            z = dStep * j;
            gcode += "N" + (count++ * 5) + " G01 Z" + z + " F9\r\n";

            x = curve.firstElement().x;
            y = curve.firstElement().y;
            gcode += "N" + (count++ * 5) + " G01 X" + dc.format(x) + " Y"
                    + dc.format(y) + " \r\n";
            for (int i = 1; i < curve.size(); i++) {
                x = curve.get(i).x;
                y = curve.get(i).y;
                lastLine = "N" + (count++ * 5) + " G01 X" + dc.format(x) + " Y"
                        + dc.format(y) + " \r\n";
                gcode += lastLine;
            }
        }
        gcode += "N" + (count++ * 5) + " G01 Z0.2 F9\r\n";
        gcode += lastLine;

        return gcode;
    }

    public static void writeFile(String gcode, String filename) {
        try {
            File file = new File(filename + ".ngc");
            if(file.exists()){
                file.delete();
            }
            gcode+="\r\nM30";
            RandomAccessFile ran = new RandomAccessFile(filename + ".ngc", "rw");
            ran.write(gcode.getBytes());
            ran.close();

            System.out.println("" + filename);
            // System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static String drillOutJewHole() {
        Circle c = new Circle();
        Vector<XY> resurface = c.getCircle(0, 0, .2, 20); //z = .04
        add(resurface, c.getCircle(0, 0, .3, 20));
        add(resurface, c.getCircle(0, 0, .4, 20));
        add(resurface, c.getCircle(0, 0, .5, 30));
        add(resurface, c.getCircle(0, 0, .6, 60));
        add(resurface, c.getCircle(0, 0, .7, 70));
        add(resurface, c.getCircle(0, 0, .8, 80));
        add(resurface, c.getCircle(0, 0, .8, 90));
        add(resurface, c.getCircle(0, 0, .9, 100));
        add(resurface, c.getCircle(0, 0, 1.0, 110));
        add(resurface, c.getCircle(0, 0, 1.1, 120));
        add(resurface, c.getCircle(0, 0, 1.2, 120));
        add(resurface, c.getCircle(0, 0, 1.3, 120));
        add(resurface, c.getCircle(0, 0, 1.4, 120));
        add(resurface, c.getCircle(0, 0, 1.5, 120));
        Vector<XY> inCut = c.getCircle(0, 0, .835, 120);//z .92
        add(inCut, c.getCircle(0, 0, .7, 100));
        add(inCut, c.getCircle(0, 0, .6, 90));
        add(inCut, c.getCircle(0, 0, .5, 80));
        add(inCut, c.getCircle(0, 0, .4, 70));
        add(inCut, c.getCircle(0, 0, .2, 60));
        Vector<XY> inLipCut = c.getCircle(0, 0, .915, 120);//z .16
        Vector<XY> outLipCut = c.getCircle(0, 0, 1.165, 120); //z .16
        Vector<XY> outCut = c.getCircle(0, 0, 1.24, 120); //z 1.12
        //TABS every Math.PI/4 up/down during gcode
        Vector<XY> outCutTabs = c.getCircle(0, 0, 1.24, 120); //z 1.16  

        ///////////second jew box/////////////
        Vector<XY> resurface2 = c.getCircle(3, 0, .2, 20);
        add(resurface2, c.getCircle(3, 0, .3, 20));
        add(resurface2, c.getCircle(3, 0, .4, 20));
        add(resurface2, c.getCircle(3, 0, .5, 30));
        add(resurface2, c.getCircle(3, 0, .6, 60));
        add(resurface2, c.getCircle(3, 0, .7, 70));
        add(resurface2, c.getCircle(3, 0, .8, 80));
        add(resurface2, c.getCircle(3, 0, .8, 90));
        add(resurface2, c.getCircle(3, 0, .9, 100));
        add(resurface2, c.getCircle(3, 0, 1.0, 110));
        add(resurface2, c.getCircle(3, 0, 1.1, 120));
        add(resurface2, c.getCircle(3, 0, 1.2, 120));
        add(resurface2, c.getCircle(3, 0, 1.3, 120));
        add(resurface2, c.getCircle(3, 0, 1.4, 120));
        add(resurface2, c.getCircle(3, 0, 1.5, 120));
        Vector<XY> inCut2 = c.getCircle(3, 0, .835, 120);//z .92
        add(inCut2, c.getCircle(3, 0, .7, 100));
        add(inCut2, c.getCircle(3, 0, .6, 90));
        add(inCut2, c.getCircle(3, 0, .5, 80));
        add(inCut2, c.getCircle(3, 0, .4, 70));
        add(inCut2, c.getCircle(3, 0, .2, 60));
        Vector<XY> inLipCut2 = c.getCircle(3, 0, .915, 120);
        Vector<XY> outLipCut2 = c.getCircle(3, 0, 1.165, 120);
        Vector<XY> outCut2 = c.getCircle(3, 0, 1.24, 120);
        Vector<XY> outCutTabs2 = c.getCircle(3, 0, 1.24, 120);

     
        double z = -.04;
        int feedIPM = 30;
        String gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
        {//first half of jew box
            gcode = toGcode( gcode, resurface, z, feedIPM);
            //move up
            gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
            //down step
            double step = -.08;
            //inLipCut
            gcode = toGcode( gcode, inLipCut, -.08, feedIPM);
            gcode = toGcode( gcode, inLipCut, -.16, feedIPM);
            //move up
            gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
            //inCut
            for (int k = 1; k <= 11; k++) {
                z = step * k;
                gcode = toGcode( gcode, inCut, z, feedIPM);
            }
            gcode = toGcode( gcode, inCut, -.92, feedIPM);
            //move up
            gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
            
            //outCut
            for (int k = 1; k <= 13; k++) {
                z = step * k;
                gcode = toGcode( gcode, outCut, z, feedIPM);
            }
            gcode = toGcodeTabs(gcode, outCutTabs, -1.16, feedIPM);
            //move up
            gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
        }

        {//second half of jew box
            z = -.04;
            gcode = toGcode( gcode, resurface2, z, feedIPM);
            //move up
            gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
            //down step
            double step = -.08;
            //outLipCut
            gcode = toGcode( gcode, outLipCut2, -.08, feedIPM);
            gcode = toGcode( gcode, outLipCut2, -.16, feedIPM);
            //move up
            gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
            //inCut
            for (int k = 1; k <= 11; k++) {
                z = step * k;
                gcode = toGcode( gcode, inCut2, z, feedIPM);
            }
            gcode = toGcode( gcode, inCut2, -.92, feedIPM);
            //move up
            gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
            //outCut
            for (int k = 1; k <= 13; k++) {
                z = step * k;
                gcode = toGcode( gcode, outCut2, z, feedIPM);
            }
            gcode = toGcodeTabs( gcode, outCutTabs2, -1.16, feedIPM);
            //move up
            gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
        }
        return gcode;
    }

    public static String toGcode(String gcode, Vector<XY> v, double z, int feedIPM) {
        //goto first location
        double x = v.firstElement().x;
        double y = v.firstElement().y;
        gcode += "N" + (count++ * 5) + " G00 X" + dc.format(x) + " Y"
                + dc.format(y) + " \r\n";
        //goto depth, plus feed rate
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";

        //do all the points.
        gcode += "N" + (count++ * 5) + " G00 X" + dc.format(x) + " Y"
                + dc.format(y) + " \r\n";

        for (int i = 1; i < v.size(); i++) {
            x = v.get(i).x;
            y = v.get(i).y;
            gcode += "N" + (count++ * 5) + " G01 X" + dc.format(x) + " Y"
                    + dc.format(y) + " \r\n";
        }

        return gcode;
    }

    //need 4 tabs and 4 drills holes
    public static String toGcodeTabs(String gcode, Vector<XY> v, double z, int feedIPM) {
        int stepsUpDown = v.size() / 8;

        //goto first location
        double x = v.firstElement().x;
        double y = v.firstElement().y;
        gcode += "N" + (count++ * 5) + " G00 X" + dc.format(x) + " Y"
                + dc.format(y) + " \r\n";
        //goto depth, plus feed rate
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";

        //do all the points.
        gcode += "N" + (count++ * 5) + " G00 X" + dc.format(x) + " Y"
                + dc.format(y) + " \r\n";
        boolean down = true;
        for (int i = 1; i < v.size(); i++) {
            if ((i) % stepsUpDown == 0) {
                if (down) {
                    z = z + .08;
                    down = false;
                } else {
                    z = z - .08;
                    down = true;
                }
                //goto depth, plus feed rate
                gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";
            }
            x = v.get(i).x;
            y = v.get(i).y;
            gcode += "N" + (count++ * 5) + " G01 X" + dc.format(x) + " Y"
                    + dc.format(y) + " \r\n";
        }

        return gcode;
    }

    public static void main(String args[]) {

        String gcode = drillOutJewHole();
        writeFile(gcode, "JewHole");

        // create a renderer that takes list<XY>
//        JFrame frame = new JFrame();
//        Renderer2 ren = new Renderer2();
//        ren.add(vec);
//
//        frame.add(ren);
//        frame.setSize(640, 480);
//        frame.setVisible(true);

    }
}
