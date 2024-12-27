package cnc;

import java.util.Vector;

public class XY {
	public XY() {
	}

	public XY(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double x;
	public double y;
	
	
	public static void join(Vector<XY> main, Vector<XY> add){
		for(XY xy:add){
			main.add(xy);
		}
	}
	
	public static Vector<XY> reverse(Vector<XY> list){
		Vector<XY> rev=new Vector<XY>();
		for(int i=list.size()-1;i>0;i--){
			rev.add(list.get(i));
		}
		list.clear();
		for(XY xy: rev){
			list.add(xy);
		}
		return list;
	}
	
	public static boolean pnpolys(Vector<Vector<XY>> shapes, XY p){
		for(Vector<XY> shape: shapes){
			if(pnpoly(shape, p)){
				return true;
			}
		}
		return false;
	}
	
        /**
         * point in polygon test.
         * @param poly
         * @param p
         * @return 
         */
	public static boolean pnpoly(Vector<XY> poly, XY p){

		int nvert = poly.size();
		int i, j;
		boolean c = false;
		for (i = 0, j = nvert-1; i < nvert; j = i++) {
			XY xyi=poly.get(i);
			XY xyj=poly.get(j);
			if ( ((xyi.y>p.y) != (xyj.y>p.y)) &&
	                (p.x < (xyj.x-xyi.x) * (p.y-xyi.y) / (xyj.y-xyi.y) + xyi.x) )
				c = !c;
	    }
	    return c;
	}
	
	public static boolean pnpoly(double[] vertx, double[] verty, double testx, double testy)
	{
	    int nvert = vertx.length;
	    int i, j;
	    boolean c = false;
	    for (i = 0, j = nvert-1; i < nvert; j = i++) {
	        if ( ((verty[i]>testy) != (verty[j]>testy)) &&
	                (testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
	            c = !c;
	    }
	    return c;
	}   
        
        
       public static Vector<XY> add(Vector<XY> v1, Vector<XY> v2) {
        for (XY xy : v2) {
            v1.add(xy);
        }
        return v1;
    }
}

