package org.woen.Util.Angel;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class AngelUtil {
    public static double normalize(double angle){
        while (abs(angle)>2*Math.PI) angle -= 4*Math.PI*signum(angle);
        return angle;
    }
}
