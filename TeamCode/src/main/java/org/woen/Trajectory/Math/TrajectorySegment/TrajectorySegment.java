package org.woen.Trajectory.Math.TrajectorySegment;




import static com.acmerobotics.roadrunner.Math.integralScan;
import static com.acmerobotics.roadrunner.Math.lerpLookup;

import static java.lang.Double.min;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;

import com.acmerobotics.roadrunner.IntegralScanResult;

import org.woen.Trajectory.Config.RobotDriveConstants;
import org.woen.Trajectory.Math.Spline.QuinticBezierSplineSegment;
import org.woen.Trajectory.TrajectoryPoint;
import org.woen.Util.Vectors.Vector2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TrajectorySegment {

    private double ds = 1e-6;
    public void setDs(double ds) {
        this.ds = ds;
    }

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
    private final List<Double> lengthSamples    = new ArrayList<>();


    public double getLengthOfCurve() {
        return lengthOfCurve;
    }
    public double getDuration(){return timeSamples.get(timeSamples.size()-1);}

    public TrajectorySegment(
        QuinticBezierSplineSegment splineSegment, double startVelocityValue, double endVelocityValue,
        double maxVelocityValue, double maxAccelValue, double maxAngularVel, double maxCentralForce, double mass){

        this.maxTranslationVel = maxVelocityValue;
        this.maxAngularVel = maxAngularVel;
        this.maxTranslationAccel = maxAccelValue;
        this.maxCentralForce = maxCentralForce;
        this.mass = mass;
        this.endVelocity = endVelocityValue;
        this.startVelocity = startVelocityValue;
        this.splineSegment = splineSegment;

        computeSamples(ds*0.1d);
        computeMotionProfile();
    }

    public static TrajectorySegment createTrajectorySegment(QuinticBezierSplineSegment splineSegment,
                    double startVelocityValue, double endVelocityValue, RobotDriveConstants constants){

        return new TrajectorySegment(splineSegment,startVelocityValue,endVelocityValue,constants.maxVel,
                    constants.maxAccel,constants.maxAngularVel,constants.maxCentralForce,constants.mass);

    }

    private double lengthOfCurve = 0;

    private void computeSamples(double eps){
        IntegralScanResult scanResult =
                integralScan(0,1,eps,u->splineSegment.get(u,1).length());

        lengthOfCurve = scanResult.sums.get(scanResult.sums.size()-1);
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
        //time samples
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

    private double getLength(double time){
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
