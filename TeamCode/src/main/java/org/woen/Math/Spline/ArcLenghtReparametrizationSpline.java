package org.woen.Math.Spline;




import static com.acmerobotics.roadrunner.Math.integralScan;
import static com.acmerobotics.roadrunner.Math.lerpLookup;

import static java.lang.Double.min;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;

import com.acmerobotics.roadrunner.IntegralScanResult;

import org.woen.Util.Vectors.Vector2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ArcLenghtReparametrizationSpline {

    private double ds = 0;
    private double maxTranslationVel = 0;
    private double maxAngularVel  = 0;
    private double maxTranslationAccel = 0;
    private double maxCentralForce = 0;
    private double mass = 0;

    private double endVelocity = 0;
    private double startVelocity = 0;

    private QuinticBezierSplineSegment splineSegment;
    private final List<Double> samples          = new ArrayList<>();
    private final List<Double> timeSamples      = new ArrayList<>();
    private final List<Double> curvatureSamples = new ArrayList<>();
    private final List<Double> velocitySamples  = new ArrayList<>();
    private final List<Double> accelerations    = new ArrayList<>();
    private final List<Double> lengthSamples    = new ArrayList<>();



    private double lenghtOfCurve = 0;

    private void computeSampels(double eps){
        IntegralScanResult scanResult =
                integralScan(0,1,eps,u->splineSegment.get(u,1).length());

        lenghtOfCurve = scanResult.sums.get(scanResult.sums.size()-1);
        double currentS = 0;

        for(currentS+=ds; currentS < scanResult.sums.get(scanResult.sums.size()-1);){
            lengthSamples.add(currentS);
            samples.add(lerpLookup(scanResult.sums,scanResult.values,currentS));
        }
    }

    private void computeMotionProfile(){
        computeCurvature();
        //isolated constrains
        AtomicInteger index = new AtomicInteger();
        samples.forEach(
        i->{
            velocitySamples.add(
        min(maxTranslationVel,
        min(maxAngularVel/abs(curvatureSamples.get(index.get())),
        sqrt(maxCentralForce/(mass*abs( curvatureSamples.get(index.get()) )) )
        )));
        index.getAndIncrement();
        }
        );
        velocitySamples.set(0,startVelocity);
        //acceleration constrains
        computeForwardConsistency();
        velocitySamples.set(velocitySamples.size()-1,endVelocity);
        computeBackwardConsistency();
        computeTimeSamples();
    }

    private void computeCurvature(){
        samples.forEach(
        i-> curvatureSamples.add(
        ( splineSegment.get(i,2).length()
        * cos( PI*0.5 - atan2(splineSegment.get(i,1).x, splineSegment.get(i,1).y) +
                        atan2(splineSegment.get(i,2).x, splineSegment.get(i,2).y)) )
        / splineSegment.get(i,1).lengthSquare()
        ));
    }

    private void computeForwardConsistency(){
        for(int i = 1; i < velocitySamples.size()-1; i++){

            double newVel = sqrt(velocitySamples.get(i-1)* velocitySamples.get(i-1)+ 2*ds*maxTranslationAccel);

            if(newVel< velocitySamples.get(i)){
                velocitySamples.set(i,newVel);
            }
        }
    }

    private void computeBackwardConsistency(){
        for(int i = velocitySamples.size()-1 -1; i > 0; i--){

            double newVel = sqrt(velocitySamples.get(i+1)* velocitySamples.get(i+1)+ 2*ds*maxTranslationAccel);

            if(newVel< velocitySamples.get(i)){
                velocitySamples.set(i,newVel);
            }
        }
    }

    private void computeTimeSamples(){
        for (int i = 1; i < velocitySamples.size(); i++) {
            timeSamples.add( 2*ds/( velocitySamples.get(i)+ velocitySamples.get(i-1) ) );
        }
    }

    public double getLength(double time){
        int index = Arrays.binarySearch(timeSamples.toArray(),time);
        if(index >= 0){
            return lengthSamples.get(index);
        }
        int loIndex = -(index + 1) - 1;

        double sLo = lengthSamples.get(loIndex);
        double tLo = timeSamples.get(loIndex);

        double t = time - tLo;
        double accel = (velocitySamples.get(loIndex+1) - velocitySamples.get(loIndex))/
                             (timeSamples.get(loIndex+1) - timeSamples.get(loIndex));

        double vel0 = velocitySamples.get(loIndex);

        return sLo + vel0*t + accel*0.5 * t*t;
    }

    public Vector2d getVelocity(double time){
        double velValue = lerpLookup(timeSamples, velocitySamples,time);
        Vector2d velTangent = splineSegment.get(
                lerpLookup(lengthSamples,samples,getLength(time))
                ,1).norm();

        return velTangent.multiply(velValue);
    }

    public Vector2d getPosition(double time){
        return splineSegment.get(
                lerpLookup(lengthSamples,samples,getLength(time))
                ,0);
    }
}
