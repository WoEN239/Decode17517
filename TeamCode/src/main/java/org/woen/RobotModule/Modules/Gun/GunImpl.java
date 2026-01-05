package org.woen.RobotModule.Modules.Gun;

import static org.woen.Config.ControlSystemConstant.gunConfig;
import static org.woen.RobotModule.Modules.Camera.Enums.BALL_COLOR.G;
import static org.woen.RobotModule.Modules.Camera.Enums.BALL_COLOR.P;
import static org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND.*;
import static org.woen.RobotModule.Modules.Gun.Config.GunServoPositions.*;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;
import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.MatchData;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.DevicePool.Devices.Motor.Interface.Motor;
import org.woen.Hardware.DevicePool.Devices.Servo.Interface.ServoMotor;
import org.woen.Hardware.DevicePool.Devices.Servo.ServoWithFeedback;
import org.woen.RobotModule.Modules.Camera.Enums.BALL_COLOR;
import org.woen.RobotModule.Modules.Camera.Enums.MOTIF;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsCenterEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsLeftEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsRightEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewMotifEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.GunAtEatEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewBrushReversEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Arcitecture.ServoAction;
import org.woen.RobotModule.Modules.Gun.Arcitecture.ServoActionUnit;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Gun.Interface.Gun;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Pid.Pid;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

import java.util.function.BooleanSupplier;

public class GunImpl implements Gun {
    private ServoWithFeedback servoR;
    private ServoWithFeedback servoC;
    private ServoWithFeedback servoL;

    private ServoMotor aimR;
    private ServoMotor aimC;
    private ServoMotor aimL;

    private Motor gunR;
    private Motor gunL;
    private Motor gunC;

    private Motor brush;

    private final Pid pidR = new Pid(gunConfig.rightPidStatus);
    private final Pid pidL = new Pid(gunConfig.leftPidStatus);
    private final Pid pidC = new Pid(gunConfig.centerPidStatus);
    public void setCommand(NewGunCommandAvailable event) {
        setCommand(event.getData());
    }
    private void setCommand(GUN_COMMAND command) {
        this.command = command;
        switch (command){
            case FULL_FIRE:
                servoAction = fullFireAction.copy();
                break;
            case PATTERN_FIRE:
                servoAction = buildPatternFireAction(getInMotif(),targetMotif).copy();
                break;
            case OFF:
                break;
            case G_FIRE:
                servoAction = buildColorFireServoAction(getInMotif(),G).copy();
                break;
            case P_FIRE:
                servoAction = buildColorFireServoAction(getInMotif(),P).copy();
                break;
            case EAT:
                EventBus.getInstance().invoke(new GunAtEatEvent(1));

                servoAction = eatAction.copy();
                break;
        }

    }

    private GUN_COMMAND command = EAT;
    private ServoAction servoAction = new ServoAction();
    private boolean isFarAim = false;
    private boolean isBrushRevers = false;
    private final Vector2d goal = MatchData.team.goalPose;

    private void setAimCommand(NewAimEvent event) {isFarAim = event.getData();}
    private void setBrushReversCommand(NewBrushReversEvent event) {isBrushRevers = event.getData();}

    private MOTIF targetMotif = MOTIF.PPG;
    public void setTargetMotif(NewMotifEvent e) {
        this.targetMotif = e.getData();
    }

    private double gunVelSide = gunConfig.shootVelSide;
    private double gunVelCenter = gunConfig.shootVelC;
    private double brushPower = 1;

    private PredominantColorProcessor.Swatch center = null;
    private PredominantColorProcessor.Swatch left = null;
    private PredominantColorProcessor.Swatch right = null;


    private MOTIF getInMotif() {
        MOTIF inMouth = null;
        if (left == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
            inMouth = MOTIF.PPG;
        else {
            if (center == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                inMouth = MOTIF.PGP;
            else {
                if (right == PredominantColorProcessor.Swatch.ARTIFACT_GREEN) {
                    inMouth = MOTIF.GPP;
                }
            }
        }
        return inMouth;
    }

    private void setLeftOnEvent(NewDetectionBallsLeftEvent event) {
        left = event.getData();
    }
    private void setRightOnEvent(NewDetectionBallsRightEvent event) {
        right = event.getData();
    }
    private void setCenterOnEvent(NewDetectionBallsCenterEvent event) {
        center = event.getData();
    }


    @Override
    public void init() {
        servoR = new ServoWithFeedback(DevicePool.getInstance().shotR);
        servoC = new ServoWithFeedback(DevicePool.getInstance().shotC);
        servoL = new ServoWithFeedback(DevicePool.getInstance().shotL);

        gunR = DevicePool.getInstance().gunR;
        gunL = DevicePool.getInstance().gunL;
        gunC = DevicePool.getInstance().gunC;

        brush = DevicePool.getInstance().brush;

        aimR = DevicePool.getInstance().aimR;
        aimC = DevicePool.getInstance().aimC;
        aimL = DevicePool.getInstance().aimL;

    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(NewGunCommandAvailable.class, this::setCommand);
        EventBus.getInstance().subscribe(NewAimEvent.class, this::setAimCommand);
        EventBus.getInstance().subscribe(NewBrushReversEvent.class, this::setBrushReversCommand);
        EventBus.getInstance().subscribe(NewMotifEvent.class, this::setTargetMotif);
        EventBus.getInstance().subscribe(NewDetectionBallsRightEvent.class, this::setRightOnEvent);
        EventBus.getInstance().subscribe(NewDetectionBallsLeftEvent.class, this::setLeftOnEvent);
        EventBus.getInstance().subscribe(NewDetectionBallsCenterEvent.class, this::setCenterOnEvent);

        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));
    }

    @Override
    public void lateUpdate() {
        double dist = pose.vector.minus(goal).length();

        if (isFarAim) {
            farAim();
        } else {
            nearAim(dist);
        }

        if(isBrushRevers){
            brushPower = -1;
        }else{
            brushPower = 1;
        }

        pidR.setTarget(gunVelSide);
        pidR.setPos(gunR.getVel());
        pidR.update();

        pidL.setTarget(gunVelSide);
        pidL.setPos(gunL.getVel());
        pidL.update();

        pidC.setTarget(gunVelCenter);
        pidC.setPos(gunC.getVel());
        pidC.update();


        Telemetry.getInstance().add("target   motif", getInMotif());
        Telemetry.getInstance().add("in robot motif", getInMotif());


        if (command == OFF) {
            gunR.setPower(0);
            gunL.setPower(0);
            gunC.setPower(0);
        } else {
            gunR.setPower(pidR.getU());
            gunL.setPower(pidL.getU());
            gunC.setPower(pidC.getU());
        }

        servoAction.update();
        brush.setPower(brushPower);
    }

    private void farAim() {
        setAimServoPos(aimLFar, aimCFar, aimRFar);
        gunVelCenter = gunConfig.shootVelC;
    }

    private void nearAim(double dist) {

        double deltaC = (dist - gunConfig.distLow) * gunConfig.deltaPosC / (gunConfig.distHi - gunConfig.distLow);
        double deltaS = (dist - gunConfig.distLow) * gunConfig.deltaPosS / (gunConfig.distHi - gunConfig.distLow);
        Telemetry.getInstance().add("deltaC", deltaC);
        Telemetry.getInstance().add("deltaS", deltaS);

        setAimServoPos(aimLNear + deltaS, aimCNear + deltaC, aimRNear + deltaS);
        gunVelCenter = gunConfig.shootVelCNear;
        gunVelSide = gunConfig.shootVelSideNear;
    }

    private void setAimServoPos(double l, double c, double r) {
        aimR.setPos(r);
        aimC.setPos(c);
        aimL.setPos(l);
    }

    private Pose pose = new Pose(0, 0, 0);
    private void setPose(Pose p) {
        pose = p;
    }

    private void shotRight (){
        servoR.setTarget(shotRPos);
    }
    private void shotCenter (){
        servoC.setTarget(shotCPos);
    }
    private void shotLeft (){
        servoL.setTarget(shotLPos);
    }
    private boolean rightGunAtVel(){
        return abs(gunR.getVel()-gunVelSide)<gunConfig.velTol;
    }
    private boolean centerGunAtVel(){
        return abs(gunC.getVel()- gunVelCenter)<gunConfig.velTol;
    }
    private boolean leftGunAtVel(){
        return abs(gunL.getVel()-gunVelSide)<gunConfig.velTol;
    }
    private final ElapsedTime fireTimer = new ElapsedTime();
    private final ServoAction fullFireAction = new ServoAction(
            new BooleanSupplier[]{
                    ()->rightGunAtVel()&&leftGunAtVel(),
                    ()->fireTimer.seconds()>0.1&&centerGunAtVel(),
                    ()->servoC.isAtTarget(),
                    ()->true},
            new Runnable[]{
                    ()->{},
                    ()->{servoL.setTarget(shotLPos);servoR.setTarget(shotRPos);fireTimer.reset();},
                    ()->servoC.setTarget(shotCPos),
                    ()->setCommand(EAT)});

    private final ServoAction eatAction = new ServoAction(
            new BooleanSupplier[]{()->true},
            new Runnable[]{()->{servoL.setTarget(eatLPos);servoR.setTarget(eatRPos);servoC.setTarget(eatCPos);}});

    private ServoAction buildColorFireServoAction(MOTIF in,BALL_COLOR outColor){
        Runnable servo  = this::shotLeft;
        if(in.colors[1] == outColor){
            servo = this::shotCenter;
        }
        if(in.colors[2] == outColor){
            servo = this::shotRight;
        }
        return new ServoAction(servo::run);
    }
    private ServoAction buildPatternFireAction(MOTIF in, MOTIF out) {
        Runnable[] servos = new Runnable[4];
        servos[3] = ()->setCommand(EAT);

        boolean[] servosBusy = new boolean[]{true, true, true};
        if (out.colors[0] == in.colors[0] && servosBusy[0]) {
            //0-l
            servos[0] = this::shotLeft;
            servosBusy[0] = false;
        } else if (out.colors[0] == in.colors[1] && servosBusy[1]) {
            //0 - c
            servos[0] = this::shotCenter;
            servosBusy[1] = false;
        } else if (out.colors[0] == in.colors[2] && servosBusy[2]) {
            //0 - r
            servos[0] = this::shotRight;
            servosBusy[2] = false;
        }

        if (out.colors[1] == in.colors[0] && servosBusy[0]) {
            //1-l
            servos[1] = this::shotLeft;
            servosBusy[0] = false;
        } else if (out.colors[1] == in.colors[1] && servosBusy[1]) {
            //1 - c
            servos[1] = this::shotCenter;
            servosBusy[1] = false;
        } else if (out.colors[1] == in.colors[2] && servosBusy[2]) {
            //1 - r
            servos[1] = this::shotRight;
            servosBusy[2] = false;
        }

        if (out.colors[2] == in.colors[0] && servosBusy[0]) {
            //2-l
            servos[2] = this::shotLeft;
            servosBusy[0] = false;
        } else if (out.colors[2] == in.colors[1] && servosBusy[1]) {
            //2 - c
            servos[2] = this::shotCenter;
            servosBusy[1] = false;
        } else if (out.colors[2] == in.colors[2] && servosBusy[2]) {
            //2 - r
            servos[2] = this::shotRight;
            servosBusy[2] = false;
        }

        ElapsedTime patterFireTimer = new ElapsedTime();

        return new ServoAction(
                new BooleanSupplier[]{
                        () -> true,
                        () -> patterFireTimer.seconds() > gunConfig.patternFireDelay,
                        () -> patterFireTimer.seconds() > gunConfig.patternFireDelay,
                        () -> true
                }, servos);
    }

}
