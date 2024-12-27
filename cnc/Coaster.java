/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc;


import static cnc.Circle.writeFile;
import static cnc.XY.add;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Vector;

/**
 *
 * @author dan2
 */
public class Coaster {
    private static DecimalFormat dc = new DecimalFormat("###.####");
    private  int count = 1;
    
    public void beerOpener(){
        Circle c = new Circle();
        double z = -.04;
        int feedIPM = 30;
        String gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
        gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
        count=1;
        
        //opener
        Vector<XY> resurface = c.getCircle(0, 0, .07, 20); //z = .04
        add(resurface, c.getCircle(0, 0, .1, 20));
        add(resurface, c.getCircle(0, 0, .2, 20));
        add(resurface, c.getCircle(0, 0, .25, 20));
        add(resurface, c.getCircle(0, 0, .3, 20));
        add(resurface, c.getCircle(0, 0, .4, 20));
        add(resurface, c.getCircle(0, 0, .5, 30));
        add(resurface, c.getCircle(0, 0, .6, 60));
        add(resurface, c.getCircle(0, 0, .68, 70));
        z = -.05;
        gcode = toGcode( gcode, resurface, z, feedIPM, 0, 0);
        z = -.099;
        gcode = toGcode( gcode, resurface, z, feedIPM, 0, 0);
        resurface = c.getCircle(0, 0, .68, 70);
        gcode = toGcode( gcode, resurface, z, feedIPM+30, 0, 0);
        gcode = toGcode( gcode, resurface, z, feedIPM+30, 0, 0);
        resurface = c.getCircleReverse(0, 0, .68, 70);
        gcode = toGcode( gcode, resurface, z, feedIPM+30, 0, 0);
        gcode = toGcode( gcode, resurface, z, feedIPM+30, 0, 0);
        gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
        
        //cap hole
        //17 x 25, .68 x 1, half circle y+ .34
        Vector<XY>  hole = c.getHalfCircle(0, .28, .05, 20);
        add(hole, c.getHalfCircle(0, .29, .1, 20));
        add(hole, c.getHalfCircle(0, .29, .15, 20));
        add(hole, c.getHalfCircle(0, .29, .2, 30));
        add(hole, c.getHalfCircle(0, .29, .25, 40));
        add(hole, c.getHalfCircle(0, .29, .3, 60));
        add(hole, c.getHalfCircle(0, .29, .35, 60));
        add(hole, c.getHalfCircle(0, .29, .4, 40));
        add(hole, c.getHalfCircle(0, .29, .45, 60));
        add(hole, c.getHalfCircle(0, .29, .5, 40));
        add(hole, c.getHalfCircle(0, .29, .55, 40));
        add(hole, c.getHalfCircle(0, .29, .6, 60));
        add(hole, c.getHalfCircle(0, .29, .63, 60));
        z = -.12;
        gcode = toGcode( gcode, hole, z, feedIPM, 0, 0);
        z = -.16;
        gcode = toGcode( gcode, hole, z, feedIPM, 0, 0);
        z = -.20;
        gcode = toGcode( gcode, hole, z, feedIPM, 0, 0);
        z = -.24;
        gcode = toGcode( gcode, hole, z, feedIPM, 0, 0);
        
        //clean up 
        hole = c.getHalfCircle(0, .29, .63, 60);
        gcode = toGcode( gcode, hole, z, feedIPM+30, 0, 0);
        gcode = toGcode( gcode, hole, z, feedIPM+30, 0, 0);
        gcode = toGcode( gcode, hole, z, feedIPM+30, 0, 0);
        //move up
        gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
        
        // CUT outside
        double step = -.05;
        Vector<XY> outCut = c.getCircle(0, 0, 1.7, 280); //z 1.12
        Vector<XY> outCutTabs = c.getCircle(0, 0, 1.7, 280); //z 1.12
        //outCut
        for (int k = 1; k <= 9; k++) {
            z = step * k;
            gcode = toGcode(gcode, outCut, z, feedIPM, 0,0);
        }
        gcode = toGcodeTabs(gcode, outCutTabs, -.485, feedIPM, 0,0);
        //move up
        gcode += "N" + (count++ * 5) + " G01 Z" + .25 + " F" + feedIPM + "\r\n";
        
        
        writeFile(gcode, "beerOpen5mm.3");
    }
    
    
    
    public  void coaster(){
        Circle c = new Circle();
        Vector<XY> resurface = c.getCircle(0, 0, .07, 20); //z = .04
        add(resurface, c.getCircle(0, 0, .2, 20));
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
        add(resurface, c.getCircle(0, 0, 1.6, 120));
        add(resurface, c.getCircle(0, 0, 1.7, 120));
        add(resurface, c.getCircle(0, 0, 1.8, 120));
        
        double z = -.04;
        int feedIPM = 30;
        String gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
        {
            gcode = toGcode( gcode, resurface, z, feedIPM, 0, 0);
            //move up
            gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
        }
        writeFile(gcode, "Coaster.1");
        
//        gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
//        count=1;
//        FlagGcode flag = new FlagGcode(-1.25, -.625);
//        flag.setCount(count);
//        String flagCode = flag.getGcode(2.5, 1.25, 30, -.09);
//        gcode+=flagCode;
//        
//        //move up
//        gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
//        
//        Text2GCode tg = new Text2GCode();
//        tg.setCount(count);
//        String g = tg.getGCodeOutline("Shannon", tg.getFontPro(), -.9, .8, -.09, feedIPM);
//        gcode+=g;
//        g = tg.getGCodeOutline("Kurt", tg.getFontPro(), -.5, -1.12, -.09, feedIPM);
//        gcode+=g;
//        //move up
//        gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
//        writeFile(gcode, "Coaster.2Kurt");
        
        
        gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
        count=1;
        // CUT
        double step = -.05;
        Vector<XY> outCut = c.getCircle(0, 0, 1.4, 180); //z 1.12
        Vector<XY> outCutTabs = c.getCircle(0, 0, 1.4, 180); //z 1.12
        //outCut
        for (int k = 1; k <= 7; k++) {
            z = step * k;
            gcode = toGcode(gcode, outCut, z, feedIPM, 0,0);
        }
        gcode = toGcodeTabs(gcode, outCutTabs, -.385, feedIPM, 0,0);
        //move up
        gcode += "N" + (count++ * 5) + " G01 Z" + .25 + " F" + feedIPM + "\r\n";
        
        
        writeFile(gcode, "Coaster.3");
    }//
    
    
    public  void coasters3(){
        Circle c = new Circle();
        Vector<XY> resurface = c.getCircle(0, 0, .07, 20); //z = .04
        add(resurface, c.getCircle(0, 0, .2, 20));
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
        add(resurface, c.getCircle(0, 0, 1.6, 120));
        add(resurface, c.getCircle(0, 0, 1.7, 120));
        add(resurface, c.getCircle(0, 0, 1.8, 120));
        
        double z = -.04;
        int feedIPM = 9;
        String gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
        {
            gcode = toGcode( gcode, resurface, z, feedIPM, 0, 0);
            //move up
            gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
            
            gcode = toGcode( gcode, resurface, z, feedIPM, 3.8, 0);
            //move up
            gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
            
            gcode = toGcode( gcode, resurface, z, feedIPM, 7.6, 0);
            //move up
            gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
            
            
            gcode = toGcode( gcode, resurface, z, feedIPM, 11.4, 0);
            //move up
            gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
        }
        writeFile(gcode, "Coaster.1");
        
        
        {//flags + texts
        gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
        count=1;
        FlagGcode flag = new FlagGcode(-1.25, -.625);
        flag.setCount(count);
        String flagCode = flag.getGcode(2.5, 1.25, 30, -.09);
        gcode+=flagCode;
        //move up
        gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
        FlagGcode flag2 = new FlagGcode(-1.25+ 3.8, -.625);
        flag2.setCount(count);
        String flagCode2 = flag2.getGcode(2.5, 1.25, 30, -.09);
        gcode+=flagCode2;
        //move up
        gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
        FlagGcode flag3 = new FlagGcode(-1.25+ 7.6, -.625);
        flag3.setCount(count);
        String flagCode3 = flag3.getGcode(2.5, 1.25, 30, -.09);
        gcode+=flagCode3;
        //move up
        gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
//        FlagGcode flag4 = new FlagGcode(-1.25+ 11.4, -.625);
//        flag4.setCount(count);
//        String flagCode4 = flag4.getGcode(2.5, 1.25, 30, -.09);
//        gcode+=flagCode4;
        //move up
        gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
        //writeFile(gcode, "Coaster.2Flags");
        
        /////////////adding texts///////////////////
        //gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
        //ount=1;
        Text2GCode tg = new Text2GCode();
        tg.setCount(count);
        String g = tg.getGCodeOutline("Shannon", tg.getFontPro(), -.84, .8, -.09, feedIPM);
        gcode+=g;
        g = tg.getGCodeOutline("Kurt", tg.getFontPro(), -.55, -1.14, -.09, feedIPM);
        gcode+=g;
        //move up
        gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
        ////////////////////second set of texts//////////////
        g = tg.getGCodeOutline("Shannon", tg.getFontPro(), -.84 +3.8, .8, -.09,feedIPM);
        gcode+=g;
        g = tg.getGCodeOutline("Kurt", tg.getFontPro(), -.55+ 3.8, -1.14, -.09,feedIPM);
        gcode+=g;
        //move up
        gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
        ////////////////////third set of texts//////////////
        g = tg.getGCodeOutline("Shannon", tg.getFontPro(), -.84 +7.6, .8, -.09,feedIPM);
        gcode+=g;
        g = tg.getGCodeOutline("Kurt", tg.getFontPro(), -.55 +7.6, -1.14, -.09,feedIPM);
        gcode+=g;
        //move up
        gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
        ////////////////////fourth set of texts//////////////
//        g = tg.getGCodeOutline("Shannon", tg.getFontPro(), -.84 +11.4, .8, -.09,feedIPM);
//        gcode+=g;
//        g = tg.getGCodeOutline("Kurt", tg.getFontPro(), -.55 +11.4, -1.14, -.09,feedIPM);
//        gcode+=g;
//        //move up
//        gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
        writeFile(gcode, "Coaster2.Names");
        }
        
        
        
        ////////////final deep cut/////////////////
        gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
        count=1;
        // CUT
        double step = -.05;
        Vector<XY> outCut = c.getCircle(0, 0, 1.4, 180); //z 1.12
        Vector<XY> outCutTabs = c.getCircle(0, 0, 1.4, 180); //z 1.12
        //outCut1
        for (int k = 1; k <= 11; k++) {
            z = step * k;
            gcode = toGcode(gcode, outCut, z, feedIPM, 0,0);
        }
        gcode = toGcodeTabs(gcode, outCutTabs, -.59, feedIPM, 0,0);
        //move up
        gcode += "N" + (count++ * 5) + " G01 Z" + .25 + " F" + feedIPM + "\r\n";    
        //outCut2
        for (int k = 1; k <= 11; k++) {
            z = step * k;
            gcode = toGcode(gcode, outCut, z, feedIPM, 3.8,0);
            System.out.println("gcode size="+gcode.length());
        }
        gcode = toGcodeTabs(gcode, outCutTabs, -.59, feedIPM, 3.8,0);
        //move up
        gcode += "N" + (count++ * 5) + " G01 Z" + .25 + " F" + feedIPM + "\r\n";   
        //outCut3
        for (int k = 1; k <= 11; k++) {
            z = step * k;
            gcode = toGcode(gcode, outCut, z, feedIPM, 7.6,0);
        }
        gcode = toGcodeTabs(gcode, outCutTabs, -.59, feedIPM, 7.6,0);
        //move up
        gcode += "N" + (count++ * 5) + " G01 Z" + .25 + " F" + feedIPM + "\r\n";   
        //outCut4
//        for (int k = 1; k <= 10; k++) {
//            z = step * k;
//            gcode = toGcode(gcode, outCut, z, feedIPM, 11.4,0);
//        }
//        gcode = toGcodeTabs(gcode, outCutTabs, -.56, feedIPM, 11.4,0);
//        //move up
//        gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";  
        writeFile(gcode, "Coaster3.3");
        System.out.println("finished!!!!!!!1");
    }//
    
    
    public  String toGcode(String gcode, Vector<XY> v, double z, int feedIPM, double xoff, double yoff) {
        //goto first location
        double x = v.firstElement().x;
        double y = v.firstElement().y;
        System.out.println(" xoff="+xoff);
        gcode += "N" + (count++ * 5) + " G00 X" + dc.format(x+xoff) + " Y"
                + dc.format(y+yoff) + " \r\n";
        //goto depth, plus feed rate
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";

        //do all the points.
        gcode += "N" + (count++ * 5) + " G00 X" + dc.format(x+xoff) + " Y"
                + dc.format(y+yoff) + " \r\n";

        for (int i = 1; i < v.size(); i++) {
            x = v.get(i).x;
            y = v.get(i).y;
            gcode += "N" + (count++ * 5) + " G01 X" + dc.format(x+xoff) + " Y"
                    + dc.format(y+yoff) + " \r\n";
        }

        return gcode;
    }
    
    public String toGcodeTabs(String gcode, Vector<XY> v, double z, int feedIPM, double xoff, double yoff) {
        int stepsUpDown = v.size() / 16;

        //goto first location
        double x = v.firstElement().x+xoff;
        double y = v.firstElement().y+yoff;
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
            x = v.get(i).x+xoff;
            y = v.get(i).y+yoff;
            gcode += "N" + (count++ * 5) + " G01 X" + dc.format(x) + " Y"
                    + dc.format(y) + " \r\n";
        }

        return gcode;
    }
    
    
    public static void main(String[] args){
        Coaster c = new Coaster();
        //c.coaster();
        c.beerOpener();
    }
}
