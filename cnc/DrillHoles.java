/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;

/**
 *
 * @author dan2
 */
public class DrillHoles {
    private static DecimalFormat dc = new DecimalFormat("###.####");
    
    public static void main(String[] args){
        String g = getCode4Holes();
        writeFile(g, "4Holes");
    }
    
    
    public static String getCode4Holes() {
        int count = 5;
        double feedIPM = 11;
        double z = .5;
        String gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
        double x1=0;double y1=0;
        double x2=1.4;double y2=0;
        double x3=1.4;double y3=1.4;
        double x4=0;double y4=1.4;
        
        //first point
        gcode += "N" + (count++ * 5) + " G00 X" + dc.format(x1) + " Y"
                + dc.format(y1) + " \r\n";
        z=0;
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";
        //drill through
        z=-.25;
        feedIPM = .5;
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";
        //pull up
        z=.5;
        feedIPM = 45;
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";
        
        //sencond point
        gcode += "N" + (count++ * 5) + " G00 X" + dc.format(x2) + " Y"
                + dc.format(y2) + " \r\n";
        z=0;
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";
        //drill through
        z=-.25;
        feedIPM = .5;
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";
        //pull up
        z=.5;
        feedIPM = 45;
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";
        
        //third point 
        gcode += "N" + (count++ * 5) + " G00 X" + dc.format(x3) + " Y"
                + dc.format(y3) + " \r\n";
        z=0;
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";
        //drill through
        z=-.25;
        feedIPM = .5;
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";
        //pull up
        z=.5;
        feedIPM = 45;
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";
        
        //fourth point
        gcode += "N" + (count++ * 5) + " G00 X" + dc.format(x4) + " Y"
                + dc.format(y4) + " \r\n";
        z=0;
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";
        //drill through
        z=-.25;
        feedIPM = .5;
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";
        //pull up
        z=.5;
        feedIPM = 45;
        gcode += "N" + (count++ * 5) + " G01 Z" + dc.format(z) + " F" + feedIPM + "\r\n";
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
}


