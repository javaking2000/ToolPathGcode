/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc;

import static cnc.Circle.toGcode;
import static cnc.Circle.toGcodeTabs;
import static cnc.XY.add;
import java.io.RandomAccessFile;
import java.util.Vector;

/**
 *
 * @author dan2
 */
public class Box {
    private static int count = 1;
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
    
    public static void writeFile(String gcode, String filename) {
        try {
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

    
    
}
