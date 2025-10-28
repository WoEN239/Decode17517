package org.woen.RobotModule.Modules.Gun.Shooter;

import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import android.bluetooth.BluetoothA2dp;
import android.print.PrintAttributes;

import com.acmerobotics.dashboard.config.Config;

import org.opencv.core.Mat;
import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.MatchData;
import org.woen.Config.Team;
import org.woen.RobotModule.Modules.Gun.GunServoPositions;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;

import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.RegisterNewFeedbackReferenceListener;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

@Config
public class ShooterPower implements Shooter{

    private Pose robotPostion = MatchData.startPosition;


    private double angle = GunServoPositions.open*270;

    private Pose wall = new Pose(0,0,0);



    public static double hMax = 95;

    public static double g = 9.81;

    private double L = 0 ;

    private static double power = 1;

    public double getPower(){
        return power;
    }

    public double getAngle(){
        return  angle/270.0;
    }




    private FeedbackReference feedbackReference;

    public void setFeedbackReference(FeedbackReference feedbackReference) {
        this.feedbackReference = feedbackReference;
    }

    public void setStartAngle(double servoPos){
        angle = servoPos * 270;
    }

    /// TODO permission to shoot
    ///private boolean permissonToShoot = false;



    @Override
    public void init(){
        EventBus.getListenersRegistration().invoke(new RegisterNewFeedbackReferenceListener(this::setFeedbackReference));

        if(MatchData.team == Team.RED)
            wall = MatchData.redWall;
        else
            wall = MatchData.blueWall;

    }

    @Override
    public void update(){
        robotPostion = feedbackReference.pos.minus(MatchData.startPosition);

        L = robotPostion.minus(robotPostion).vector.x;
    }

    @Override
    public void lateUpdate(){

        angle = atan2(hMax , L);

        power = sqrt(L * g / (sin(angle) * cos(angle)));
    }



}
