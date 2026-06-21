package org.firstinspires.ftc.teamcode.Modules;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Modules.Intake.Intake;
import org.firstinspires.ftc.teamcode.Modules.Shooter.Flywheel;
import org.firstinspires.ftc.teamcode.Modules.Transfer.Transfer;
import org.firstinspires.ftc.teamcode.Modules.Turret.Turret;
import org.firstinspires.ftc.teamcode.Robot.ALLIANCE;

@Config
@Configurable
public class FSM {
    Turret turret = new Turret();
    Flywheel flywheel = new Flywheel();
    Intake intake = new Intake();
    Transfer transfer = new Transfer();
    HardwareMap hardwareMap;
    ElapsedTime timer = new ElapsedTime();


    double rServo = Transfer.midR;
    double lServo = Transfer.midL;
    double cServo = Transfer.midC;

    public static double shootDelay = 0.055;


    private FSM_STATE state = FSM_STATE.EAT;

    private FSM_STATE target = FSM_STATE.EAT;

    public void setState(FSM_STATE state) {
        this.state = state;
    }

    public FSM_STATE getState() {
        return state;
    }

    private Follower follower;


    public void start(HardwareMap hardwareMap, Follower follower) {
        this.hardwareMap = hardwareMap;
        this.follower = follower;
        turret.start(hardwareMap, follower::getHeading, follower::getAngularVelocity);
        flywheel.start(hardwareMap, follower, ALLIANCE.alliance);
        intake.start(hardwareMap);
        transfer.start(hardwareMap);
    }

    public void updateStates() {
        switch (state) {
            case EAT:
                target = FSM_STATE.EAT;
                transfer.setState(Transfer.STATE.DOWN);
                intake.setState(Intake.State.ON);
                break;
            case REVERSE:
                target = FSM_STATE.REVERSE;
                transfer.setState(Transfer.STATE.DOWN);
                intake.setState(Intake.State.REVERSE);
                break;
            case DRIVE:
                target = FSM_STATE.DRIVE;
                transfer.setState(Transfer.STATE.DRIVE);
                intake.setState(Intake.State.REVERSE);
                break;
            case SHOOT:
                target = FSM_STATE.EAT;

                intake.setState(Intake.State.REVERSE);

                transfer.setState(Transfer.STATE.CENTER);

                if(timer.seconds() > shootDelay)
                    transfer.setState(Transfer.STATE.UP);

                if (timer.seconds() > 0.1) {
                    setState(FSM_STATE.EAT);
                    timer.reset();
                }
                break;
            case SHOOT_LEFT:
                target = FSM_STATE.EAT;
                transfer.setState(Transfer.STATE.LEFT);
                intake.setState(Intake.State.REVERSE);


                if(timer.seconds() > 0.3) {
                    setState(FSM_STATE.DRIVE);
                    timer.reset();
                }
                break;

            case SHOOT_CENTER:
                target = FSM_STATE.EAT;
                transfer.setState(Transfer.STATE.CENTER);
                intake.setState(Intake.State.REVERSE);

                if(timer.seconds() > 0.3) {
                    setState(FSM_STATE.DRIVE);
                    timer.reset();
                }
                break;

            case SHOOT_RIGHT:
                target = FSM_STATE.EAT;
                transfer.setState(Transfer.STATE.RIGHT);
                intake.setState(Intake.State.REVERSE);

                if(timer.seconds() > 0.3) {
                    setState(FSM_STATE.DRIVE);
                    timer.reset();
                }
                break;
        }
    }

    double lastAngle = -Math.PI*0.5;
    public void update() {
        if (target == state)
            timer.reset();
        updateStates();
        Pose robotPose = follower.getPose();

        double x = Flywheel.xGoal;
        double y = Flywheel.yGoal;


        if (robotPose.distanceFrom(new Pose(Flywheel.xGoal, Flywheel.yGoal, 0)) > 120){
            x = Flywheel.xGoalFar;
            y = Flywheel.yGoalFar;
        }
        else {
            shootDelay = 0;
        }

        Pose pose = new Pose(x,y,0);

        FtcDashboard.getInstance().getTelemetry().addData("distance to goal", pose.distanceFrom(robotPose));
        FtcDashboard.getInstance().getTelemetry().update();

        double dX = x - robotPose.getX();
        double dY = y - robotPose.getY();

        double absoluteAngleToGoal = Math.atan2(dY, dX);


        //FtcDashboard.getInstance().getTelemetry().addData("relative turret angle", Math.toDegrees(relativeTurretAngle));
        FtcDashboard.getInstance().getTelemetry().addData("dx", dX);
        FtcDashboard.getInstance().getTelemetry().addData("dY", dY);

        FtcDashboard.getInstance().getTelemetry().addData("x", robotPose.getX());
        FtcDashboard.getInstance().getTelemetry().addData("Y", robotPose.getY());


        if(isInNearZone(robotPose.getX(),robotPose.getY()) || isInFarZone(robotPose.getX(),robotPose.getY())) {
            FtcDashboard.getInstance().getTelemetry().addData("in zone", true);
            turret.setAngleToHold(absoluteAngleToGoal);
            lastAngle = absoluteAngleToGoal;//-follower.getHeading();
        } else {

            FtcDashboard.getInstance().getTelemetry().addData("in zone", false);
            turret.setAngleToHold(lastAngle);
        }

        //FtcDashboard.getInstance().getTelemetry().update();
        transfer.update();
        flywheel.update();
        turret.update();
        intake.update();

    }
    private boolean isInNearZone(double x, double y){
        x-=15;
        if(x>0){
            return false;
        }
        if( y>(x*1.1) && (y< -x*1.1)){
            return true;
        }
        return false;
    }
    private boolean isInFarZone(double x, double y){
        x+=15;
        if(x<45){
            return false;
        }
        if( y<(x*1.1-48) && y>(-x*1.1+48) ){
            return true;
        }
        return false;
    }

}
