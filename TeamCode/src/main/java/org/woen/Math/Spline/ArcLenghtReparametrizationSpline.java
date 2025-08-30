package org.woen.Math.Spline;




import static com.acmerobotics.roadrunner.Math.integralScan;
import static com.acmerobotics.roadrunner.Math.lerpLookup;

import com.acmerobotics.roadrunner.IntegralScanResult;

import java.util.ArrayList;
import java.util.List;

public class ArcLenghtReparametrizationSpline {

    double ds = 0;
    QuinticBezierSplineSegment splineSegment;
    List<Double> length  = new ArrayList<>();
    List<Double> samples = new ArrayList<>();

    private void computeSampels(double eps){
        IntegralScanResult scanResult =
                integralScan(0,1,eps,u->splineSegment.get(u,1).length());

        double currentU = 0;
        double currentLenth = 0;
        while(currentU<1){
            currentU = lerpLookup(scanResult.sums,scanResult.values,currentLenth);
            samples.add(currentU);
            length.add(currentLenth);
            currentLenth += ds;
        }

    }



}
