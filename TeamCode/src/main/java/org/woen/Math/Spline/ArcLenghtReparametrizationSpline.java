package org.woen.Math.Spline;




import static com.acmerobotics.roadrunner.Math.integralScan;
import static com.acmerobotics.roadrunner.Math.lerpLookup;

import static java.lang.Double.max;
import static java.lang.Double.min;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;

import com.acmerobotics.roadrunner.IntegralScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ArcLenghtReparametrizationSpline {

    private double ds = 0;
    private double maxTranslationVel = 0;
    private double maxAngularVel  = 0;
    private double maxTranslationAccel = 0;
    private double maxCentralForce = 0;
    private double mass = 0;

    private QuinticBezierSplineSegment splineSegment;
    private List<Double> samples      = new ArrayList<>();
    private List<Double> curivative   = new ArrayList<>();
    private List<Double> velocity     = new ArrayList<>();
    private List<Double> acceleration = new ArrayList<>();
 //   private List<Double> length       = new ArrayList<>();


    private void computeSampels(double eps){
        IntegralScanResult scanResult =
                integralScan(0,1,eps,u->splineSegment.get(u,1).length());

        double currentS = 0;

        for(currentS+=ds; currentS < scanResult.sums.get(scanResult.sums.size()-1);){
        //    length.add(currentS);
            samples.add(lerpLookup(scanResult.sums,scanResult.values,currentS));
        }
    }

    private void computeMotionProfile(){
        //isolated constrains
        AtomicInteger index = new AtomicInteger();
        samples.forEach(
        i->{velocity.add(
        min(maxTranslationVel,
        min(maxAngularVel/abs(curivative.get(index.get())),
        sqrt(maxCentralForce/(mass*abs( curivative.get(index.get()) )) )
        )));
        index.getAndIncrement();
        }
        );
    }

    private void computeCurivative(){
        samples.forEach(
        i->curivative.add(
        ( splineSegment.get(i,2).length()
        * cos( PI*0.5 - atan2(splineSegment.get(i,1).x, splineSegment.get(i,1).y) +
                        atan2(splineSegment.get(i,2).x, splineSegment.get(i,2).y)) )
        / splineSegment.get(i,1).lengthSquare()
        ));
    }

    private void forwardConsistencyCompute(){
        for(int i = 1; i<velocity.size()-1; i++){

            double newVel = sqrt(velocity.get(i-1)*velocity.get(i-1)+ 2*ds*maxTranslationAccel);

            if(newVel<velocity.get(i)){
                velocity.set(i,newVel);
            }
        }
    }

    private void backwardConsistencyCompute(){
        for(int i = velocity.size()-2; i > 0; i--){

            double newVel = sqrt(velocity.get(i+1)*velocity.get(i+1)+ 2*ds*maxTranslationAccel);

            if(newVel<velocity.get(i)){
                velocity.set(i,newVel);
            }
        }
    }

}
