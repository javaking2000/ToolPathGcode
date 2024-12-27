package cnc;

import javax.swing.JFrame;

public class Sprocket {

	public static void main(String args[]) {
		//System.out.println(""+getRadius(11,getPitch(1.1, 11))+"  "+getPitch(1.1, 11));
		//System.out.println(""+getRadius(60,.46)+"  "+getPitch(.44, 6));
		
		
		//.628 inch * 56.8
		
		
		
		//radius=.493   6 teeth  
		double radius=.32946;
		double pitch=.46;
		pitch=.345;
		pitch=.25;
                pitch=.28;
		//pitch=.23;
		//System.out.println("pitch="+getPitch(radius, 6)+"    6 tooth Radius="+getRadius(6, pitch));
		//System.out.println("test 30 tooth Radius="+getRadius(30, getPitch(.44, 6)));
		
		System.out.println("8 tooth Radius="+RoundToothGear.getRadius(8, pitch));
		System.out.println("10 tooth Radius="+RoundToothGear.getRadius(10, pitch));
		System.out.println("15 tooth Radius="+RoundToothGear.getRadius(15, pitch));
		System.out.println("30 tooth Radius="+RoundToothGear.getRadius(30, pitch));
		System.out.println("32 tooth Radius="+RoundToothGear.getRadius(32, pitch));
		System.out.println("60 tooth Radius="+RoundToothGear.getRadius(60, pitch));

//		//RoundToothSprocket sp = new RoundToothSprocket(130, 155, 60, 0);
//		//RoundToothSprocket sp2 = new RoundToothSprocket(130, 155, 60, 3.3f); //3.6
		
		
//		RoundToothSprocket sp = new RoundToothSprocket(95, 115, 30, 0, true);
//		RoundToothSprocket sp2 = new RoundToothSprocket(95, 115, 30, 3.6f, true);
		//RoundToothSprocket sp = new RoundToothSprocket(25, 25, 6, 0, false);
		
		
		
		//2.2 *56.8 =125
//		RoundToothSprocket sp2 = new RoundToothSprocket(125, 125, 60, 2.6f, true); //3.5f  //pitch=.23
//		RoundToothSprocket sp = new RoundToothSprocket(125, 125, 60, 0f, true);
		
//		RoundToothSprocket sp2 = new RoundToothSprocket(250, 250, 60, 2.6f, true); //3.5f  //pitch=.23
//		RoundToothSprocket sp = new RoundToothSprocket(250, 250, 60, 0f, true);
		
                
		int numTeeth=30;
		int innerRadius=(int)(RoundToothGear.getRadius(numTeeth, pitch)*RoundToothGear.pixelsPerInch);
		int outterRadius=(int)(RoundToothGear.getRadius(numTeeth, pitch)*RoundToothGear.pixelsPerInch);
		
                //RoundToothGear sp2 = new RoundToothGear(innerRadius, outterRadius, numTeeth, 2.8f, false); //3.5f  //pitch=.23
		RoundToothGear sp2 = new RoundToothGear(innerRadius, outterRadius, numTeeth, 3.3f, true); //3.5f  //pitch=.23

                RoundToothGear sp = new RoundToothGear(innerRadius, outterRadius, numTeeth, 0f, false);
		
		//.2929 *56.8
//		RoundToothSprocket sp2 = new RoundToothSprocket(17, 17, 8, 2.6f, false); //3.5f  //pitch=.23
//		RoundToothSprocket sp = new RoundToothSprocket(17, 17, 8, 0f, false);

		
                int feedRateIPM = 12;
                double depthStepInches = -.1;
                double depthInches = .76;
                boolean addTabs = true;
		String mainG=sp2.getGCode(depthInches, feedRateIPM, depthStepInches, addTabs);
		
		RoundToothGear.writeFile(mainG, "sproket"+numTeeth);

		JFrame frame = new JFrame();
		Renderer ren = new Renderer();
		ren.add(sp2);
		//ren.add(sp2);

		frame.add(ren);
		frame.setSize(640, 480);
		frame.setVisible(true);

	}
}
