/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc;


import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 */
public class LineSegment {
    
    public Point2D start;
    public Point2D end;
    
    public LineSegment(Point2D start, Point2D end){
        this.start = start;
        this.end = end;
    }
    
    /**
     * Get all segments on this y row.
     * @param y
     * @param width
     * @param shape
     * @return 
     */
    public List<LineSegment> getSegments(double y, double width, Shape shape){
        List<LineSegment> segs = new ArrayList<LineSegment>();
        boolean start = false;
        Point2D ptS = null;
        Point2D ptE;
        //TODO step needs to be less than 1 inch, .05 seem good.
        for(int x=0;x<width;x++){
            if(shape.contains(x, y)){
                if(start == false){
                    start = true;
                    ptS = new Point2D.Double(x, y);
                } else if(start){
                    start = false;
                    ptE = new Point2D.Double(x, y);
                    segs.add(new LineSegment(ptS, ptE));
                }
            }
        }
        
        return segs;
    }
}