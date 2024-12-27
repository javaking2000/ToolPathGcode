/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc;


import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.kabeja.dxf.DXFCircle;
import org.kabeja.dxf.DXFConstants;
import org.kabeja.dxf.DXFDocument;
import org.kabeja.dxf.DXFLayer;
import org.kabeja.dxf.DXFLine;
import org.kabeja.dxf.DXFLineType;
import org.kabeja.dxf.DXFSpline;
import org.kabeja.dxf.helpers.SplinePoint;
import org.kabeja.parser.DXFParser;
import org.kabeja.parser.ParseException;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;

/**
 *
 * @author dan2
 */
public class Dxf {
    public static void main(String args[]) throws ParseException{
        double z = -.04;
        int feedIPM = 30;
        String gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
       
            
        
        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse("/home/dan2/Desktop/cncfiles/mayan-calendar.dxf", DXFParser.DEFAULT_ENCODING);
       
        DXFDocument doc = parser.getDocument();
        //Iterator iter = doc.getDXFLayerIterator();
        
        
        DXFLayer layer1 = doc.getDXFLayer("Layer 1");
        double height = layer1.getBounds().getHeight();
        double width = layer1.getBounds().getWidth();
        System.out.println("width="+width);
        System.out.println("height="+height);
        List<DXFSpline> lines = layer1.getDXFEntities(DXFConstants.ENTITY_TYPE_SPLINE);
        layer1.hasDXFEntities(DXFConstants.ENTITY_TYPE_SPLINE);
        int index=0;
        for(DXFSpline line:lines){
            Iterator<ArrayList> iterPts = line.getSplinePointIterator();
         
            //get SplinePoints from arraylist
            //add spline point to xyList
            Vector<XY> xyList = new Vector<XY>();
            int i=0;
            while(iterPts.hasNext()){
                Object obj = iterPts.next();
                if(i>1){
                    
                    SplinePoint sp = (SplinePoint)obj;
                    xyList.add(new XY(sp.getX(), sp.getY()));
                    //System.out.println("list="+sp.getX()+","+sp.getY()+", "+sp.getZ());
                }
                i++;
            }
            
            gcode = toGcode( gcode, xyList, z, feedIPM, 0, 0);
            //move up
            gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
            System.out.println("break "+index);
            index++;
            
        }
         gcode+="\r\nM30";

        //save gcode to file
        writeFile(gcode, "mayanCalendar");
    }
    
    
    private static DecimalFormat dc = new DecimalFormat("###.####");
    static int count = 10;
    public  static String toGcode(String gcode, Vector<XY> v, double z, int feedIPM, double xoff, double yoff) {
        //goto first location
        double x = v.firstElement().x;
        double y = v.firstElement().y;
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
    
}
