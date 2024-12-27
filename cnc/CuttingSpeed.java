/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnc;

/**
 *
 * @author dan2
 */
public class CuttingSpeed {
    
    double V;  //cutting speed
    double pi; //circular constant
    double D; //diameter
    double S; //spindle speed
    double F; //feed
    double f; //feed per tooth
    double N; //number of flutes
    
    {
        V = (pi *D *S)/1000;
        S = V/pi/D*1000;
        F = S *f*N;
        f = F/(S*N);
    }
    
    
    ////////////short cut////////////////
    {
        double RPM = 12000;
        double N = 2; //number flutes
        //.001 x 2 x6000
        double chipLoadEigth = .003;
        double chipLoadFouth = .009;
        double chipLoad3Eigth = .015;
        double chipLoadHalf = .019;
        
        double chipLoad = chipLoadEigth;
        double feedRate = RPM * N *chipLoad;
        //.003x2x6000 = 36 inches.
    }
    
    
    {
        double Vc; //cutting speed, surface meters per min
        double D; //tool diameter
        double pi = 3.14159;
        //double rpm = Vc *1000/ D*pi; //rev/min
        
        double rpm = 12000;
        D = 3.125; // 1/8 inch x 25mm
        Vc = rpm * (D *pi)/1000; //surface in meters per min
        
        double Vf; //table feed mm/min
        double fz; //feed per tooth mm
        double z; //number of teeth
        //fz table
        //D   fz
        //3mm .016 mm/tooth
        //6mm .036 mm/tooth
        //9mm .058 mm/tooth
       
        z = 4;
        fz = .016;  //
        Vf = fz * z * rpm;
    }
}
