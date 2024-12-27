/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc;


import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author dan2
 */
public class FlagGcode {

    double width = 2;
    double height = 1;
    int strips = 13;
    double xoff;
    double yoff;
    int count = 0;
    
    public FlagGcode(double xOff, double yOff){
        xoff=xOff;
        yoff=yOff;
    }
    
    public void setCount(int count){
        this.count=count;
    }

//    public ArrayList<XY> getFlag() {
//        ArrayList<XY> list = new ArrayList<XY>();
//        list.add(new XY(0, 0));
//        list.add(new XY(width, 0));
//        list.add(new XY(width, height));
//        list.add(new XY(0, height));
//        list.add(new XY(0, 0));
//        return null;
//    }

    public ArrayList<XY> getStripePath(double width, double height) {

        double stripeStep = height / 13;
        ArrayList<XY> list = new ArrayList<XY>();
        double curHeight = 0;
        //stripe1
        list.add(new XY(0+xoff, height-0 +yoff));
        list.add(new XY(width+xoff, height-0 +yoff));
        list.add(new XY(width+xoff, height-stripeStep +yoff));
        list.add(new XY(3.0 / 8.0 * width+xoff, height-stripeStep+yoff));
        //stripe2
        list.add(new XY(3.0 / 8.0 * width+xoff, height- stripeStep * 2+yoff));
        list.add(new XY(width+xoff, height- stripeStep * 2+yoff));
        list.add(new XY(width+xoff, height- stripeStep * 3+yoff));
        list.add(new XY(3.0 / 8 * width+xoff, height- stripeStep * 3+yoff));
        //stripe3
        list.add(new XY(3.0 / 8 * width+xoff, height-stripeStep * 4+yoff));
        list.add(new XY(width+xoff, height-stripeStep * 4+yoff));
        list.add(new XY(width+xoff, height-stripeStep * 5+yoff));
        list.add(new XY(3.0 / 8 * width+xoff, height-stripeStep * 5+yoff));
        //stripe4
        list.add(new XY(3.0 / 8 * width+xoff, height-stripeStep * 6+yoff));
        list.add(new XY(width+xoff, height-stripeStep * 6+yoff));
        list.add(new XY(width+xoff, height-stripeStep * 7+yoff));
        list.add(new XY(0+xoff, height-stripeStep * 7+yoff));

        //stripe5
        list.add(new XY(0+xoff, height-stripeStep * 8+yoff));
        list.add(new XY(width+xoff, height-stripeStep * 8+yoff));
        list.add(new XY(width+xoff, height-stripeStep * 9+yoff));
        list.add(new XY(0+xoff, height-stripeStep * 9+yoff));
        //stripe6
        list.add(new XY(0+xoff, height-stripeStep * 10+yoff));
        list.add(new XY(width+xoff, height-stripeStep * 10+yoff));
        list.add(new XY(width+xoff, height-stripeStep * 11+yoff));
        list.add(new XY(3 / 8 * width+ xoff, height-stripeStep * 11+yoff));
        //stripe7
        list.add(new XY(0+xoff, height-stripeStep * 12+yoff));
        list.add(new XY(width+xoff, height-stripeStep * 12+yoff));
        list.add(new XY(width+xoff, height-stripeStep * 13+yoff));
        list.add(new XY(0+xoff, height-stripeStep * 13+yoff));
        //outline of flag
        list.add(new XY(0+xoff, height +yoff));
        list.add(new XY(width+xoff, height +yoff));
        list.add(new XY(width+xoff, 0+yoff));
        return list;
    }

    public ArrayList<XY> getStarPts(double width, double height) {
        double stripeStep = height / 13;
        double areaHeight = stripeStep * 7;

        double starHeight = areaHeight / 9;
        double starWidth = 3.0 / 8.0 * width / 6.0;

        ArrayList<XY> list = new ArrayList<XY>();
        for (int y = 0; y < 9; y++) {
            double yval = y * starHeight + .5*starHeight;
            int mx = 6;
            if (y % 2 == 1) {
                mx = 5;
            }
            for (int x = 0; x < mx; x++) {
                double xval = x*starWidth + .5*starWidth;
                if(mx==5){
                    xval += .5*starWidth;
                }
                list.add(new XY(xval+xoff, height-yval+yoff));
            }
        }
        //TODO 5 rows by 6 cols
        // 4 rows by 5 cols
        return list;
    }
    
    private static DecimalFormat dc = new DecimalFormat("###.####");
    
    public String getGcode(double width, double height, double ipmFeed, double z){
        String gcode = "N" + (count++ * 5) +" G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
        gcode += "N" + (count++ * 5) + " G01 Z1.0 F" + ipmFeed + "\r\n";
        ArrayList<XY> list = getStripePath(width, height);
        XY xy1 = list.get(0);
        double x = xy1.x;
        double y = xy1.y;
        gcode += "N" + (count++ * 5) + " G00 X" + dc.format(x) + " Y" + dc.format(y) + " \r\n";
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + ipmFeed + "\r\n";
        gcode += "N" + (count++ * 5) + " G01 X" + dc.format(x) + " Y" + dc.format(y) + " \r\n";
     
        for(XY xy:list){
            x = xy.x;
            y = xy.y;
            gcode += "N" + (count++ * 5) + " G01 X" + dc.format(x) + " Y" + dc.format(y) + " \r\n";
        }

	//pull out
        gcode += "N" + (count++ * 5) + " G00 Z1 \r\n";
        gcode += "(Adding Stars)\r\n";
         ArrayList<XY> stars = getStarPts(width, height);
         for(XY star : stars){
             x = star.x;
             y = star.y;
             gcode += "N" + (count++ * 5) + " G01 X" + dc.format(x) + " Y" + dc.format(y) + " \r\n";
             //down
             gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + ipmFeed + "\r\n";
             //up
             gcode += "N" + (count++ * 5) + " G00 Z.25 \r\n";
         }
        //pull out
        gcode += "N" + (count++ * 5) + " G00 Z1 \r\n";

        return gcode;
    }
    
    public static void writeFile(String gcode, String filename) {
        try {
            RandomAccessFile ran = new RandomAccessFile(filename + ".ngc", "rw");
            ran.write(gcode.getBytes());
            ran.close();

            //System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    public static void main(String[] args){
        FlagGcode flag = new FlagGcode(.5, .5);
        String gcode = flag.getGcode(2, 1, 30, -.05);
        writeFile(gcode, "flag");
    }
}
