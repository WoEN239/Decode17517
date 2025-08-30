package org.woen.Math.NumericalMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class NuericalMethods {

    private static double lerp( double x, double fromLo, double fromHi, double toLo,double toHi) {
        if (fromLo == fromHi) return  0.0;
        else
            return toLo + (x - fromLo) * (toHi - toLo) / (fromHi - fromLo);
    }

    public static double lerpLookup(List<Double> source, List<Double> target, double query){
        int index = Arrays.binarySearch(source.toArray(),query);
        if (index >= 0) {
            return target.get(index);
        } else {
            int insIndex = -(index + 1);
            if(insIndex <= 0) return target.get(0);
            if(insIndex >= source.size()) return target.get(target.size()-1);
            else {
                double sLo = source.get(insIndex - 1);
                double sHi = source.get(insIndex);
                double tLo = target.get(insIndex - 1);
                double tHi = target.get(insIndex);
                return lerp(query, sLo, sHi, tLo, tHi);
            }
        }
    }

    public static IntegralScanResult integralScan(Double a, Double b, Double eps, Function<Double,Double> f){
        Double m = (a + b) / 2;
        Double fa = f.apply(a);
        Double fm = f.apply(m);
        Double fb = f.apply(b);

        double i = (b - a) / 8 * (
                fa + fm + fb +
                        f.apply(a + 0.9501 * (b - a)) +
                        f.apply(a + 0.2311 * (b - a)) +
                        f.apply(a + 0.6068 * (b - a)) +
                        f.apply(a + 0.4860 * (b - a)) +
                        f.apply(a + 0.8913 * (b - a))
        );

        if (i == 0.0) {
            i = b - a;
        }

        i *= eps / Math.ulp(1.0);

        ArrayList<Double> values = new ArrayList<>(Arrays.asList(0.0));
        ArrayList<Double> sums   = new ArrayList<>(Arrays.asList(0.0));

        helper(a, m, b, fa, fm, fb, i, f, values, sums);

        return new IntegralScanResult(values, sums);
    }

    private static void helper(Double a,Double m, Double b,Double fa,Double fm,Double fb,
                               double i, Function<Double,Double> f, ArrayList<Double> values,ArrayList<Double> sums) {
        Double h = (b - a) / 4;
        Double ml = a + h;
        Double mr = b - h;
        Double fml = f.apply(ml);
        Double fmr = f.apply(mr);
        Double i1 = h / 1.5 * (fa + 4 * fm + fb);
        Double i2 = h / 3 * (fa + 4 * (fml + fmr) + 2 * fm + fb);
        i1 = (16 * i2 - i1) / 15;
        if (i + (i1 - i2) == i || m <= a || b <= m) {
            values.add(b);
            sums.add(sums.get(sums.size()-1) + i1);
        } else {
            helper(a, ml, m, fa, fml, fm,i,f,values,sums);
            helper(m, mr, b, fm, fmr, fb,i,f,values,sums);
        }
    }
}
