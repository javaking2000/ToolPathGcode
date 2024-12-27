/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import javax.imageio.ImageIO;

/**
 *
 * @author dan2
 */
public class ImageToGcode {
    public static void main(String[] args) throws IOException{
        File file = new File("/home/dan2/Desktop/cnc/CNCimg/buck-fireplace-screen-art-3.jpg");
        BufferedImage image = ImageIO.read(file);
   
        String gcode = imageToGcode(image);
        writeFile(gcode, "fake");
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
    
    
    public static double feedIPM = 30;
    public static double maxDepth = .25;
    public static double minDepth = .05;
    public static boolean cutBlack = false;
    public static double pixelPerInch = 72.0;
    public static int count=1;
    private static DecimalFormat dc = new DecimalFormat("###.####");
    
    public static String imageToGcode(BufferedImage bufferedImage){
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);  
        ColorConvertOp op = new ColorConvertOp(cs, null);  
        BufferedImage image = op.filter(bufferedImage, null);
        String gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES,  )\r\n";
        gcode = processImage(gcode, .25, image);
        //gcode = processImage(gcode, .125, image);
        //gcode = processImage(gcode, .052, image);
        
        return gcode;
    }
    
    private static String processImage(String gcode, double toolWidth, BufferedImage image) {
        int pixelStep = (int) ((toolWidth / 4) * pixelPerInch);
        System.out.println("pixel step=" + pixelStep);
        int width = image.getWidth();
        int height = image.getHeight();
        WritableRaster raster = image.getRaster();
        int[] pix = new int[1];
        int ySteps = (int) Math.ceil(height / pixelStep);
        int xSteps = (int) Math.ceil(width / pixelStep);
        gcode += "N" + (count++ * 5) + " G01 Z" + .5 + " F" + feedIPM + "\r\n";
        for (int yStep = 0; yStep < ySteps; yStep++) {
            int y = yStep * pixelStep;
            System.out.println("y=" + y);
            //TODO can't just start at zero, odd steps start at xSteps-1;
            for (int xStep = 0; xStep < xSteps; xStep++) {
                int x = xStep * pixelStep;
                if (x >= width) {
                    x = width - 1;
                }
                if (y >= height) {
                    y = height - 1;
                }
                raster.getPixel(x, y, pix);
                int val = pix[0];
                System.out.println("val=" + val);
                double t = val / 255.0;

                double depth = lerp(minDepth, maxDepth, t);
                if (!cutBlack) {
                    depth = lerp(maxDepth, minDepth, t);
                }

                double xpos = x / pixelPerInch;
                double ypos = y / pixelPerInch;
                //TODO can the bit fit
                gcode += "N" + (count++ * 5) + " G00 X" + dc.format(xpos) + " Y"
                        + dc.format(ypos) + " Z" + dc.format(depth) + " \r\n";

            }
        }

        return gcode;
    }
    
    /**
     * interpolate between v0 and v1, given t.
     *
     * @param v0
     * @param v1
     * @param t .5 is the middle. 1 is at v1.
     * @return
     */
    public static double lerp(double v0, double v1, double t) {
        return (1 - t) * v0 + t * v1;
    }
}
