package cnc;

import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Vector;

public class Plain {
	private static DecimalFormat dc = new DecimalFormat("###.####");
	private static int count = 1;
	
	public static void writeFile(String gcode, String filename) {
		try {
			RandomAccessFile ran = new RandomAccessFile(filename + ".ngc", "rw");
			ran.write(gcode.getBytes());
			ran.close();

			System.out.println(""+System.getProperty("user.dir"));
                        System.out.println(filename);
			// System.exit(1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static Vector<XY> path(int widthInch, int lengthInch){
		double sideStep=.25;
		//compute x,y,z
		Vector<XY> path= new Vector<XY>();
		int steps=(int)(widthInch/sideStep);

		double y=0;
                int i =0;
                path.add(new XY(0, y));
                path.add(new XY(lengthInch, y));

		while(i<steps){
                    //side step and back
                    i++;
                    y = i * sideStep;
                    path.add(new XY(lengthInch, y));
                    path.add(new XY(0, y));
                    
                    //side step and down length.
                    i++;
                    y = i * sideStep;
                    path.add(new XY(0, y));
                    path.add(new XY(lengthInch, y));
		}
		return path;
	}
	
	private static String convert(Vector<XY> path, double feedIPM){
		String gcode="";
		for(XY xy:path){
			gcode += "N" + (count++ * 5) + " G01 X" + dc.format(xy.x) + " Y"+ dc.format(xy.y) + " \r\n";
		}
                gcode += "N" + (count++ * 5) + " G01 Z" + 1 + " F" + feedIPM + "\r\n";
                XY xy1 = path.firstElement();
                gcode += "N" + (count++ * 5) + " G01 X" + dc.format(xy1.x) + " Y"+ dc.format(xy1.y) + " \r\n";
                gcode += "N" + (count++ * 5) + " G01 Z" + -.02 + " F" + feedIPM + "\r\n";
		for(XY xy:path){
			gcode += "N" + (count++ * 5) + " G01 X" + dc.format(xy.x) + " Y"+ dc.format(xy.y) + " \r\n";
		}
                gcode += "N" + (count++ * 5) + " G01 Z" + 1 + " F" + feedIPM + "\r\n";
                gcode += "N" + (count++ * 5) + " G01 X" + dc.format(xy1.x) + " Y"+ dc.format(xy1.y) + " \r\n";
                gcode += "N" + (count++ * 5) + " G01 Z" + -.04 + " F" + feedIPM + "\r\n";
		for(XY xy:path){
			gcode += "N" + (count++ * 5) + " G01 X" + dc.format(xy.x) + " Y"+ dc.format(xy.y) + " \r\n";
		}
                gcode += "N" + (count++ * 5) + " G01 Z" + 1 + " F" + feedIPM + "\r\n";
                return gcode;
	}

	public static void main(String args[]) {
		String gcode=  "N5 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
		gcode+= "N" + (count++ * 5) + " G01 F30\r\n";
		gcode+=convert(path(5, 15), 30.0);
		gcode+= "M30";
		writeFile(gcode, "Plain");

	}
}
