/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc.vectors;

import cnc.Curve;
import cnc.LineSegment;
import cnc.Renderer2;
import cnc.TextToXY;
import cnc.TextToXY;
import static cnc.TextToXY.shapeSplitK;
import static cnc.TextToXY.shapeToRowCuts;
import cnc.XY;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.util.Vector;
import javax.swing.JFrame;

/**
 *
 * @author dan2
 */
public class AlphabetShapes {
    
    /**modified K shape, with center removed.*/
    public static Vector<Vector<XY>> shapeSplitK(){
        Vector<XY> v1 = new Vector<XY>();
        v1.add(new XY(0.219375, 0.0));

        v1.add(new XY(0.219375, 0.31921875));
        v1.add(new XY(0.6556875000000001, 0.31921875));
        v1.add(new XY(0.6556875000000001, .8));
        //join
        v1.add(new XY(1.535625, .8));
        v1.add(new XY(1.535625, 0.31921875));
        v1.add(new XY(1.974375, 0.31921875));
        v1.add(new XY(1.974375, 0.0));
        v1.add(new XY(0.219375, 0.0));
        //end of 1
        
        
        
        Vector<XY> v2 = new Vector<XY>();
        //start of 2nd
        v2.add(new XY(0.6556875000000001, 1.7));
        v2.add(new XY(0.6556875000000001, 3.6178125));
        v2.add(new XY(0.219375, 3.6178125));
        v2.add(new XY(0.219375, 3.9365625));
        v2.add(new XY(1.974375, 3.9365625));
        v2.add(new XY(1.974375, 3.6178125));
        v2.add(new XY(1.535625, 3.6178125));
        v2.add(new XY(1.535625, 2.180625));
        v2.add(new XY(2.98228125, 3.6178125));
        v2.add(new XY(2.61665625, 3.6178125));
        v2.add(new XY(2.61665625, 3.9365625));
        v2.add(new XY(3.8894375, 3.9365625));
        v2.add(new XY(3.8894375, 3.6178125));
        v2.add(new XY(3.4344375, 3.6178125));
        v2.add(new XY(2.20959375, 2.401875));

        v2.add(new XY(2.78, 1.7));
        //joiner
        v2.add(new XY(1.73, 1.7));
        v2.add(new XY(1.535625, 1.9115625));
        v2.add(new XY(1.535625, 1.7));
        v2.add(new XY(0.6556875000000001, 1.7));
        
        
        Vector<XY> v3 = new Vector<XY>();
        
        //start 3rd
  v3.add(new XY(3.49, .8));
v3.add(new XY(3.875625, 0.31921875));
v3.add(new XY(4.2115937500000005, 0.31921875));
v3.add(new XY(4.2115937500000005, 0.0));
v3.add(new XY(3.0554062500000003, 0.0));
        v3.add(new XY(2.41, .8));
        v3.add(new XY(3.49, .8));
        
    

        
        Vector<Vector<XY>> master = new Vector<Vector<XY>>();
        master.add(v1);
        master.add(v2);
        master.add(v3);
    
        //
        return master;
    }
    
    
     public static Vector<Vector<XY>> shapeSplitS(){
         //1) get the Bold Times Font vector shape
         //2) at edge segments to leave center uncut.
         Vector<XY> v1 = new Vector<XY>();
         v1.add(new XY(0.36115625, 0.18984375));
         v1.add(new XY(0.66626828125, 1.03677421875));
         v1.add(new XY(0.6849293750000001, 0.9549093750000001));
         v1.add(new XY(0.7072020312500001, 0.87753046875));
         v1.add(new XY(0.7330862499999999, 0.8046375));
         v1.add(new XY(0.7625820312500001, 0.73623046875));
         v1.add(new XY(0.795689375, 0.6723093749999998));
         v1.add(new XY(0.8324082812500001, 0.6128742187499998));
         v1.add(new XY(0.8727387500000002, 0.557925));
         v1.add(new XY(0.9166807812500001, 0.50746171875));
         v1.add(new XY(0.9642343750000001, 0.461484375));
         v1.add(new XY(0.9642343750000001, 0.461484375));
         v1.add(new XY(1.01549296875, 0.41993671875000005));
         v1.add(new XY(1.07055, 0.38276250000000006));
         v1.add(new XY(1.12940546875, 0.34996171874999993));
         v1.add(new XY(1.192059375, 0.32153437499999993));
         v1.add(new XY(1.2585117187500001, 0.29748046875));
         v1.add(new XY(1.3287625, 0.2778));
         v1.add(new XY(1.4028117187500002, 0.26249296875));
         v1.add(new XY(1.4806593750000006, 0.25155937500000003));
         v1.add(new XY(1.5623054687500002, 0.24499921874999997));
         v1.add(new XY(1.64775, 0.2428125));
         v1.add(new XY(1.64775, 0.2428125));
         v1.add(new XY(1.71690796875, 0.24436640625000003));
         v1.add(new XY(1.7826818750000002, 0.249028125));
         v1.add(new XY(1.84507171875, 0.25679765625));
         v1.add(new XY(1.9040775000000003, 0.267675));
         v1.add(new XY(1.9596992187500002, 0.28166015625));
         v1.add(new XY(2.011936875, 0.298753125));
         v1.add(new XY(2.06079046875, 0.31895390625000003));
         v1.add(new XY(2.1062600000000002, 0.3422625000000001));
         v1.add(new XY(2.14834546875, 0.36867890625));
         v1.add(new XY(2.187046875, 0.398203125));
         v1.add(new XY(2.187046875, 0.398203125));
         v1.add(new XY(2.22220578125, 0.43073671875));
         v1.add(new XY(2.25366375, 0.46618125));
         v1.add(new XY(2.28142078125, 0.50453671875));
         v1.add(new XY(2.305476875, 0.5458031250000001));
         v1.add(new XY(2.32583203125, 0.58998046875));
         v1.add(new XY(2.34248625, 0.6370687500000001));
         v1.add(new XY(2.35543953125, 0.6870679687500001));
         v1.add(new XY(2.364691875, 0.739978125));
         v1.add(new XY(2.3702432812499996, 0.7957992187500001));
         v1.add(new XY(2.37209375, 0.85453125));
         v1.add(new XY(2.37083640625, 0.9009140625));
         v1.add(new XY(2.3670643750000004, 0.9451875));
         v1.add(new XY(2.36077765625, 0.9873515624999999));
         v1.add(new XY(2.3519762500000003, 1.0274062499999999));
         v1.add(new XY(2.3406601562500002, 1.0653515625));
         v1.add(new XY(3.09531015625, 1.0227656250000001));
         v1.add(new XY(3.084615625, 0.9121875000000002));
         v1.add(new XY(3.06679140625, 0.807328125));
         v1.add(new XY(3.0418375, 0.7081875));
         v1.add(new XY(3.00975390625, 0.614765625));
         v1.add(new XY(2.9705406250000004, 0.5270624999999999));
         v1.add(new XY(2.9241976562499996, 0.4450781249999999));
         v1.add(new XY(2.8707249999999997, 0.36881249999999993));
         v1.add(new XY(2.81012265625, 0.298265625));
         v1.add(new XY(2.742390625, 0.2334375));
         v1.add(new XY(2.66773203125, 0.17456718750000003));
         v1.add(new XY(2.5863500000000004, 0.12189375000000005));
         v1.add(new XY(2.49824453125, 0.07541718749999998));
         v1.add(new XY(2.4034156249999996, 0.0351375));
         v1.add(new XY(2.30186328125, 0.0010546875));
         v1.add(new XY(2.1935875, -0.026831250000000022));
         v1.add(new XY(2.07858828125, -0.04852031250000002));
         v1.add(new XY(1.9568656249999998, -0.0640125));
         v1.add(new XY(1.8284195312500002, -0.0733078125));
         v1.add(new XY(1.6932500000000001, -0.07640625));
         v1.add(new XY(1.6932500000000001, -0.07640625));
         v1.add(new XY(1.6265884375000002, -0.07574765624999999));
         v1.add(new XY(1.56001625, -0.07377187500000001));
         v1.add(new XY(1.4935334375, -0.07047890624999999));
         v1.add(new XY(1.42714, -0.06586874999999999));
         v1.add(new XY(1.3608359375, -0.05994140625));
         v1.add(new XY(1.2946212499999998, -0.052696875));
         v1.add(new XY(1.2284959375, -0.044135156249999995));
         v1.add(new XY(1.16246, -0.034256249999999995));
         v1.add(new XY(1.0965134375, -0.023060156249999998));
         v1.add(new XY(1.03065625, -0.010546875));
         v1.add(new XY(1.03065625, -0.010546875));
         v1.add(new XY(0.9647300000000001, 0.003311718749999996));
         v1.add(new XY(0.8985762500000001, 0.01854374999999999));
         v1.add(new XY(0.8321950000000001, 0.03514921875000001));
         v1.add(new XY(0.7655862499999999, 0.053128125));
         v1.add(new XY(0.69875, 0.07248046875));
         v1.add(new XY(0.6316862499999999, 0.09320625000000002));
         v1.add(new XY(0.564395, 0.11530546875000003));
         v1.add(new XY(0.49687624999999996, 0.13877812500000003));
         v1.add(new XY(0.42912999999999996, 0.16362421875));
         v1.add(new XY(0.36115625, 0.18984375));
         
         Vector<XY> v2 = new Vector<XY>();
         v2.add(new XY(0.43860375000000007, 2.13765));
         v2.add(new XY(0.4080740625, 2.20124765625));
         v2.add(new XY(0.38161500000000004, 2.269303125));
         v2.add(new XY(0.3592265625, 2.34181640625));
         v2.add(new XY(0.34090875, 2.4187875));
         v2.add(new XY(0.3266615625, 2.50021640625));
         v2.add(new XY(0.31648500000000007, 2.586103125));
         v2.add(new XY(0.31037906249999997, 2.6764476562499997));
         v2.add(new XY(0.30834375, 2.77125));
         v2.add(new XY(0.31171562500000005, 2.88574453125));
         v2.add(new XY(0.32183125, 2.995040625));
         v2.add(new XY(0.3386906250000001, 3.09913828125));
         v2.add(new XY(0.36229374999999997, 3.1980375));
         v2.add(new XY(0.392640625, 3.29173828125));
         v2.add(new XY(0.4297312500000001, 3.3802406250000003));
         v2.add(new XY(0.47356562500000005, 3.4635445312499997));
         v2.add(new XY(0.52414375, 3.54165));
         v2.add(new XY(0.5814656250000001, 3.61455703125));
         v2.add(new XY(0.6455312500000001, 3.682265625));
         v2.add(new XY(0.715674375, 3.7441195312500004));
         v2.add(new XY(0.7912287499999999, 3.7994625));
         v2.add(new XY(0.8721943750000001, 3.8482945312499997));
         v2.add(new XY(0.95857125, 3.8906156249999997));
         v2.add(new XY(1.050359375, 3.92642578125));
         v2.add(new XY(1.1475587500000002, 3.9557250000000006));
         v2.add(new XY(1.250169375, 3.9785132812499997));
         v2.add(new XY(1.3581912500000002, 3.9947906250000007));
         v2.add(new XY(1.4716243750000002, 4.00455703125));
         v2.add(new XY(1.59046875, 4.0078125));
         v2.add(new XY(1.6505896875, 4.00723359375));
         v2.add(new XY(1.71119, 4.005496875));
         v2.add(new XY(1.7722696874999997, 4.00260234375));
         v2.add(new XY(1.8338287500000001, 3.9985500000000003));
         v2.add(new XY(1.8958671875000002, 3.99333984375));
         v2.add(new XY(1.958385, 3.9869718750000005));
         v2.add(new XY(2.0213821875000004, 3.9794460937500005));
         v2.add(new XY(2.0848587500000004, 3.9707625000000006));
         v2.add(new XY(2.1488146875, 3.96092109375));
         v2.add(new XY(2.2132500000000004, 3.949921875));
         v2.add(new XY(2.2781646875000003, 3.937760156249999));
         v2.add(new XY(2.34355875, 3.9244312499999996));
         v2.add(new XY(2.4094321875, 3.90993515625));
         v2.add(new XY(2.475785, 3.8942718749999994));
         v2.add(new XY(2.5426171875, 3.87744140625));
         v2.add(new XY(2.6099287500000004, 3.8594437499999996));
         v2.add(new XY(2.6777196875, 3.8402789062499996));
         v2.add(new XY(2.7459900000000004, 3.819946875));
         v2.add(new XY(2.8147396875000004, 3.7984476562499996));
         v2.add(new XY(2.88396875, 3.77578125));
         v2.add(new XY(2.88396875, 2.90578125));
         v2.add(new XY(2.5935, 2.90578125));
         v2.add(new XY(2.57868, 2.98479375));
         v2.add(new XY(2.5607075000000004, 3.05945625));
         v2.add(new XY(2.5395825, 3.12976875));
         v2.add(new XY(2.5153049999999997, 3.19573125));
         v2.add(new XY(2.487875, 3.25734375));
         v2.add(new XY(2.4572925, 3.3146062499999998));
         v2.add(new XY(2.4235575000000003, 3.36751875));
         v2.add(new XY(2.38667, 3.416081250000001));
         v2.add(new XY(2.34663, 3.46029375));
         v2.add(new XY(2.3034375, 3.50015625));
         v2.add(new XY(2.2567715625000004, 3.535959375));
         v2.add(new XY(2.20631125, 3.5679937500000007));
         v2.add(new XY(2.1520565625, 3.596259375));
         v2.add(new XY(2.0940075, 3.62075625));
         v2.add(new XY(2.0321640625, 3.641484375));
         v2.add(new XY(1.9665262500000003, 3.65844375));
         v2.add(new XY(1.8970940625, 3.671634375));
         v2.add(new XY(1.8238675000000004, 3.68105625));
         v2.add(new XY(1.7468465625000003, 3.686709375));
         v2.add(new XY(1.66603125, 3.68859375));
         v2.add(new XY(1.6000095312500002, 3.68723671875));
         v2.add(new XY(1.5373068749999999, 3.6831656250000004));
         v2.add(new XY(1.47792328125, 3.6763804687499997));
         v2.add(new XY(1.4218587500000002, 3.6668812499999994));
         v2.add(new XY(1.3691132812500002, 3.65466796875));
         v2.add(new XY(1.319686875, 3.639740625));
         v2.add(new XY(1.27357953125, 3.6220992187500003));
         v2.add(new XY(1.2307912500000002, 3.60174375));
         v2.add(new XY(1.1913220312500001, 3.57867421875));
         v2.add(new XY(1.155171875, 3.552890625));
         v2.add(new XY(1.1224057812500001, 3.52441640625));
         v2.add(new XY(1.09308875, 3.4932749999999997));
         v2.add(new XY(1.06722078125, 3.45946640625));
         v2.add(new XY(1.0448018749999999, 3.4229906249999997));
         v2.add(new XY(1.02583203125, 3.38384765625));
         v2.add(new XY(1.01031125, 3.3420375));
         v2.add(new XY(0.99823953125, 3.2975601562500003));
         v2.add(new XY(0.9896168750000001, 3.250415625));
         v2.add(new XY(0.98444328125, 3.2006039062499996));
         v2.add(new XY(0.9827187500000001, 3.148125));
         v2.add(new XY(0.9839070312499999, 3.10026796875));
         v2.add(new XY(0.9874718750000001, 3.0547593749999997));
         v2.add(new XY(0.99341328125, 3.0115992187500003));
         v2.add(new XY(1.00173125, 2.9707875));
         v2.add(new XY(1.0124257812500002, 2.93232421875));
         v2.add(new XY(1.025496875, 2.8962093749999998));
         v2.add(new XY(1.04094453125, 2.8624429687500004));
         v2.add(new XY(1.05876875, 2.8310250000000003));
         v2.add(new XY(1.07896953125, 2.80195546875));
         v2.add(new XY(1.1015468750000001, 2.775234375));
         v2.add(new XY(1.12876359375, 2.7495914062500004));
         v2.add(new XY(1.1628825000000003, 2.72375625));
         v2.add(new XY(1.20390359375, 2.69772890625));
         v2.add(new XY(1.2518268750000001, 2.671509375));
         v2.add(new XY(1.3066523437500002, 2.64509765625));
         v2.add(new XY(1.3683800000000002, 2.6184937500000003));
         v2.add(new XY(1.4370098437500003, 2.5916976562499996));
         v2.add(new XY(1.5125418750000004, 2.564709375));
         v2.add(new XY(1.5949760937500002, 2.5375289062500004));
         v2.add(new XY(1.6843125, 2.51015625));
         v2.add(new XY(2.1961875, 2.3653125));
         v2.add(new XY(2.2886337500000002, 2.3363179687500004));
         v2.add(new XY(2.3758475, 2.3042718750000004));
         v2.add(new XY(2.45782875, 2.26917421875));
         v2.add(new XY(2.5345774999999997, 2.2310250000000003));
         v2.add(new XY(2.60609375, 2.18982421875));
         v2.add(new XY(2.6723775, 2.145571875));
 
    

        
        Vector<Vector<XY>> master = new Vector<Vector<XY>>();
        master.add(v1);
        master.add(v2);
        return master;
    }
     
    private static void printSegXY(Vector<XY> seg) {
        for (XY xy : seg) {
            if(xy.y<2.1 && xy.y >1.1){
                System.err.println("" + xy.x + ", " + xy.y);
            }else
            System.out.println("" + xy.x + ", " + xy.y);
        }
    }
    
    
    
   
    
    public static Vector<Vector<XY>> areaToShape(Area area){
        Vector<Vector<XY>> shapes = new Vector<Vector<XY>>();
        JFrame frame = new JFrame();
        frame.setSize(100, 100);
        frame.setVisible(true);
        frame.validate();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double[] prev = null;
        Graphics2D g2 = (Graphics2D) frame.getContentPane().getGraphics();
        PathIterator pathIt = area.getPathIterator(g2.getTransform());
        Vector<XY> poly = new Vector<XY>();
        while (!pathIt.isDone()) {
                double[] coords = new double[6];
                int type = pathIt.currentSegment(coords);
                if (type == pathIt.SEG_MOVETO) {
                    double x = coords[0]/1000.0;
                    double y = coords[1]/1000.0 * -1;

                    poly.add(new XY(x, y));

                    prev = coords;
                } else if (type == pathIt.SEG_CLOSE) {
                    shapes.add(poly);
                    poly = new Vector<XY>();
                } else if (type == pathIt.SEG_LINETO) {
                    double x = coords[0]/1000.0;
                    double y = coords[1]/1000.0* -1;
                    poly.add(new XY(x, y));

                    prev = coords;
                } else if (type == pathIt.SEG_QUADTO) {
                    QuadCurve2D.Double curve = new QuadCurve2D.Double(
                            prev[0], prev[1], coords[0], coords[1],
                            coords[2], coords[3]);
                    Point2D.Double[] points = Curve.getPoints(curve);
                    for (Point2D.Double p : points) {
                        double x = (p.x/1000.0);
                        double y = p.y/1000.0* -1 ;
                        poly.add(new XY(x, y));

                        double[] f = new double[2];
                        f[0] = p.x/1000.0;
                        f[1] = p.y/1000.0;
                        prev = f;
                    }

                } else if (type == pathIt.SEG_CUBICTO) {
                    CubicCurve2D.Double curve = new CubicCurve2D.Double(
                            prev[0], prev[1], coords[0], coords[1],
                            coords[2], coords[3], coords[4], coords[5]);
                    Point2D.Double[] points = Curve.getPoints(curve);
                    for (Point2D.Double p : points) {
                        double x = (p.x/1000.0);
                        double y = p.y/1000.0 * -1 ;

                        poly.add(new XY(x, y));

                        double[] f = new double[2];
                        f[0] = p.x/1000.0;
                        f[1] = p.y/1000.0;
                        prev = f;
                    }

                }
                pathIt.next();
        }
        return shapes;
    }
    


      
    public static void removeSubArea(Area area, Vector<XY> subArea){
         
        Area area2 = shapeToArea(subArea);

        area.subtract(area2);
    }
    
    public static Area shapeToArea(Vector<XY> shape){
        Polygon p = new Polygon();
        for(XY xy : shape){
            p.addPoint((int)(xy.x*1000), (int)(xy.y*1000));
        }
        return new Area(p);
    }
    
    public static Vector<Vector<XY>> splitLetter(Area letter){
        Vector<XY> subArea = new Vector<XY>();
        subArea.add(new XY(0, 1));
        subArea.add(new XY(5, 1));
        subArea.add(new XY(5, 2.1));
        subArea.add(new XY(0, 2.1));
        subArea.add(new XY(0, 1));
        
        
        
        removeSubArea(letter, subArea);
        Vector<Vector<XY>> split = areaToShape(letter);
        
        return split;
    }
    
    /**
     * Mirrors the shape around the y=0 axis, than shifts back up.
     * @param letter
     * @return 
     */
    public static Area flip(Area letter){
        int height = letter.getBounds().height;
        Vector<Vector<XY>> split = areaToShape(letter);
        //Vector<Vector<XY>> flipLetter = new Vector<Vector<XY>>();
        Area flipLetter = new Area();
        for(Vector<XY> sec:split){
            Vector<XY> flipSec = new Vector<XY>();
            for(XY xy:sec){
                XY fxy = new XY(xy.x, (xy.y));//
                flipSec.add(fxy);
                //System.out.println(" y="+fxy.y);
            }
            Area flipArea = shapeToArea(flipSec);
            flipLetter.add(flipArea);
        }
        
        return flipLetter;
    }
    
    public static double getYmid(Area area){
        double mid = area.getBounds().height/2.0;
        return mid;
    }
     
     public static void main(String[] args){
        JFrame frame = new JFrame();
        Renderer2 ren = new Renderer2();
        
        final String text = "L";
        final Font font;
        double xoff = 0;
        double yoff = 0;
        double xmult = 1.3;
        double ymult = 1.5;
        font = new Font("Serif", Font.BOLD, 180);
        Vector<Vector<Vector<XY>>> letters = TextToXY.getShapeXY(text, font, xoff, yoff, xmult, ymult);
        String gcode = "N1 G90 G20 (BLOCK #5, ABSOLUTE IN INCHES, gcode outline)\r\n";
        
        
        

       Vector<Vector<XY>> v12 = shapeSplitS();
        
        for (Vector<Vector<XY>> letter : letters) {
            System.out.println(" numbe of letter segments = " + letter.size());
            for (Vector<XY> seg : letter) {
                if (seg.size() > 0) {
                    System.out.println("letter segment size = " + seg.size());
                    
                    //gcode = TextToXY.convertXYtoGcode(seg, gcode);
                    double sy = 1.2;
                    double ey = 2.2;
                    gcode = TextToXY.convertXYtoGcodeSkipRect(seg, gcode, sy,  ey);
                    
                    Vector<Vector<LineSegment>> rowCuts = shapeToRowCuts(seg, 200);
                    gcode = TextToXY.convertRowCutsToGcode(rowCuts, gcode, sy, ey);
                    
                    
//                    Area letterS =  shapeToArea(seg);
//                    System.out.println("width ="+letterS.getBounds().width);
//                    Area flipS = flip(letterS);
//                    
//                    double mid = getYmid(letterS)/1000.0 +1;
//                    System.out.println(" mid y="+mid);
//                    
//                    Vector<XY> subArea = new Vector<XY>();
//                    subArea.add(new XY(0, mid - .600));
//                    subArea.add(new XY(5, mid - .600));
//                    subArea.add(new XY(5, mid + .600));
//                    subArea.add(new XY(0, mid + .600));
//                    subArea.add(new XY(0, mid - .600));
//                    removeSubArea(letterS, subArea);
//                    removeSubArea(flipS, subArea);
//                    Vector<Vector<XY>> split = areaToShape(letterS);
//                    Vector<Vector<XY>> split2 = areaToShape(flipS);
//                    
//                    System.out.println("splits2 = "+split2.size()+"  "+split2.get(0).size());
                    ren.add(seg);
                    //ren.add(split.get(0));
                    //ren.add(split.get(1));
                    //ren.add(split2.get(0));
                            
                    //printSegXY(seg);
                    //Vector<Vector<LineSegment>> rowCuts = shapeToRowCuts(seg, 200);
                    //ren.add(seg);
                }
                
                
//                if (seg.size() == 26) {
//
//                    
//                    ren.add(v12.get(0));
//                    ren.add(v12.get(1));
//                    ren.add(v123.get(2));
//                    Vector<Vector<LineSegment>> rowCuts = shapeToRowCuts(seg, 200);
//                    K_rowCuts = rowCuts;
//                    for(Vector<LineSegment> row:rowCuts){
//                        System.out.print(""+row.size()+" "+row.get(0).start.getY());
//                        for(LineSegment line:row){
//                            System.out.print("\t"+line.start.getX()+", "+line.end.getX());
//                        }
//                        System.out.println();
//                    }
//                    System.out.println("  " + seg.size());
//                    for (XY xy : seg) {
//                        System.out.println("" + xy.x + ", " + xy.y);
//                    }
//                }
            }
        }
        
        TextToXY.saveFile(gcode, "LetterL.ngc");
        
        frame.add(ren);
        frame.setSize(640, 480);
        frame.setVisible(true);

     }
}
