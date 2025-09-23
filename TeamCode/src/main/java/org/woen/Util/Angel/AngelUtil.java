package org.woen.Util.Angel;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class AngelUtil {
    public static double normalize(double angle){
        while (abs(angle)>Math.PI) angle -= 2*Math.PI*signum(angle);
        return angle;
    }
}
