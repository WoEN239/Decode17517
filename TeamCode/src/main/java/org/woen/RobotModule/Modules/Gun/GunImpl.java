package org.woen.RobotModule.Modules.Gun;

import static org.woen.Config.ControlSystemConstant.adaptiveFireConfig;
import static org.woen.Config.ControlSystemConstant.gunConfig;
import static org.woen.RobotModule.Modules.Camera.Enums.MOTIF.GPP;
import static org.woen.RobotModule.Modules.Camera.Enums.MOTIF.PGP;
import static org.woen.RobotModule.Modules.Camera.Enums.MOTIF.PPG;
import static org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND.FAR;
import static org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND.NEAR;
import static org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND.NEAR_GOAL;
import static org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND.PATTERN;
import static org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND.*;
import static org.woen.RobotModule.Modules.Gun.Config.GunServoPositions.*;

import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static java.lang.Math.toRadians;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;
import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.MatchData;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.DevicePool.Devices.Motor.Interface.Motor;
import org.woen.Hardware.DevicePool.Devices.Servo.Interface.ServoMotor;
import org.woen.Hardware.DevicePool.Devices.Servo.ServoWithFeedback;
import org.woen.RobotModule.Modules.Battery.NewVoltageAvailable;
import org.woen.RobotModule.Modules.Camera.Enums.MOTIF;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsCenterEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsLeftEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsRightEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewTargetMotifEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.GunAtEatEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewBrushReversEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Arcitecture.ServoAction;
import org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Gun.Interface.Gun;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewPositionListener;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Color.LedDriver;
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
    private DcMotor light;

    private LedDriver rgb2;

    private LedDriver rgb3;

    private LedDriver rgb4;

    private final Pid pidR = new Pid(gunConfig.rightPidStatus);
    private final Pid pidL = new Pid(gunConfig.leftPidStatus);
    private final Pid pidC = new Pid(gunConfig.centerPidStatus);
    public void setCommand(NewGunCommandAvailable event) {
        setCommand(event.getData());
    }
    private void setCommand(GUN_COMMAND command) {
        this.command = command;

        EventBus.getInstance().invoke(new GunAtEatEvent(command==EAT));

        switch (command){
            case FULL_FIRE:
                servoAction = fullFireAction.copy();
                break;
            case PATTERN_FIRE:
                servoAction = buildPatternFireAction(getInMotif(),targetMotif).copy();
                break;
            case FAST_PATTERN_FIRE:
                servoAction = buildFastPatternFireAction(getInMotif(),targetMotif).copy();
                break;
            case OFF:
                break;
            case G_FIRE:
                servoAction = buildGFireServoAction().copy();
                break;
            case P_FIRE:
                servoAction = buildPFireServoAction().copy();
                break;
            case EAT:
                servoAction = eatAction.copy();
                break;
        }
    }

    private GUN_COMMAND command = EAT;
    private ServoAction servoAction = new ServoAction();
    private AIM_COMMAND aimCommand = FAR;
    private boolean isBrushRevers = false;
    private final Vector2d goal = MatchData.team.goalPose;
    private double voltage = 12;

    private void newVoltageOnEvent(NewVoltageAvailable e) {
        this.voltage = e.getData();
    }

    private void setAimCommand(NewAimEvent event) {aimCommand = event.getData();}
    private void setBrushReversCommand(NewBrushReversEvent event) {isBrushRevers = event.getData();}

    private MOTIF targetMotif = PPG;
    public void setTargetMotif(NewTargetMotifEvent e) {
        this.targetMotif = e.getData();
    }

    private double gunVelSide = gunConfig.shootVelSideFar;
    private double gunVelCenter = gunConfig.shootVelCFar;
    private double brushPower = 1;

    private PredominantColorProcessor.Swatch centerColor = PredominantColorProcessor.Swatch.ARTIFACT_GREEN;
    private PredominantColorProcessor.Swatch leftColor = PredominantColorProcessor.Swatch.ARTIFACT_PURPLE;
    private PredominantColorProcessor.Swatch rightColor = PredominantColorProcessor.Swatch.ARTIFACT_PURPLE;


    private MOTIF getInMotif() {
        MOTIF inMouth = GPP;
        if (centerColor == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
            inMouth = PGP;
        else if (rightColor == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
            inMouth = PPG;

        return inMouth;
    }

    private void setLeftOnEvent(NewDetectionBallsLeftEvent event) {
        leftColor = event.getData();
    }
    private void setRightOnEvent(NewDetectionBallsRightEvent event) {
        rightColor = event.getData();
    }
    private void setCenterOnEvent(NewDetectionBallsCenterEvent event) {
        centerColor = event.getData();
    }


    @Override
    public void init() {
        servoR = new ServoWithFeedback(DevicePool.getInstance().shotR,eatRPos);
        servoC = new ServoWithFeedback(DevicePool.getInstance().shotC,eatCPos);
        servoL = new ServoWithFeedback(DevicePool.getInstance().shotL,eatLPos);

        gunR = DevicePool.getInstance().gunR;
        gunL = DevicePool.getInstance().gunL;
        gunC = DevicePool.getInstance().gunC;

        brush = DevicePool.getInstance().brush;
        light = DevicePool.getInstance().light;

        aimR = DevicePool.getInstance().aimR;
        aimC = DevicePool.getInstance().aimC;
        aimL = DevicePool.getInstance().aimL;

        rgb2 = DevicePool.getInstance().ledDriver2;
        rgb3 = DevicePool.getInstance().ledDriver3;
        rgb4 = DevicePool.getInstance().ledDriver4;

    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(NewGunCommandAvailable.class, this::setCommand);
        EventBus.getInstance().subscribe(NewAimEvent.class, this::setAimCommand);
        EventBus.getInstance().subscribe(NewBrushReversEvent.class, this::setBrushReversCommand);
        EventBus.getInstance().subscribe(NewTargetMotifEvent.class, this::setTargetMotif);
        EventBus.getInstance().subscribe(NewDetectionBallsRightEvent.class, this::setRightOnEvent);
        EventBus.getInstance().subscribe(NewDetectionBallsLeftEvent.class, this::setLeftOnEvent);
        EventBus.getInstance().subscribe(NewDetectionBallsCenterEvent.class, this::setCenterOnEvent);
        EventBus.getInstance().subscribe(NewVoltageAvailable.class,this::newVoltageOnEvent);

        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));
    }

    private boolean getBalls(PredominantColorProcessor.Swatch swatch){
        if(swatch == PredominantColorProcessor.Swatch.ARTIFACT_GREEN || swatch == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE)
            return true;
        else
            return false;
    }

    private boolean artCheck(){
        if(getBalls(centerColor) && getBalls(rightColor) && getBalls(leftColor)){
            return true;
        }else {
            return false;
        }
    }


    @Override
    public void lateUpdate() {
        double dist = pose.vector.minus(goal).length();


        if (aimCommand == FAR) {
            gunVelSide   = gunConfig.shootVelSideFar;
            gunVelCenter = gunConfig.shootVelCFar;
            farAim(dist);
        } else if (aimCommand == NEAR){
//            gunVelSide   = gunConfig.shootVelSideNear;
//            gunVelCenter = gunConfig.shootVelCNear;
            nearAim(dist);
        } else if(aimCommand == PATTERN){
            gunVelSide   = gunConfig.shootVelSidePattern;
            gunVelCenter = gunConfig.shootVelCPattern;
            patternAim();
        } else if (aimCommand == NEAR_GOAL) {
            gunVelSide   = gunConfig.shootVelSideGoalNear;
            gunVelCenter = gunConfig.shootVelCGoalNear;
            setAimServoPos(aimCGoalNear);
        }

        if(isBrushRevers){
            brushPower = -1;
        }else{
            brushPower = 1;
        }
        if(command != EAT){
            brushPower = 0;
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

        if(artCheck()){
            rgb2.setPower(0);
            rgb3.setPower(0);
            rgb4.setPower(0);
        }else{
            rgb2.setPower(0);
            rgb3.setPower(1);
            rgb4.setPower(1);
        }


        Telemetry.getInstance().add("target   motif", targetMotif);
        Telemetry.getInstance().add("in robot motif", getInMotif());
        Telemetry.getInstance().add("gun case", command);
        Telemetry.getInstance().add("dist to goal", dist);

        if(command == OFF){
            gunR.setPower(0);
            gunL.setPower(0);
            gunC.setPower(0);
        }else {
            gunR.setPower(pidR.getU() * 12 / voltage);
            gunL.setPower(pidL.getU() * 12 / voltage);
            gunC.setPower(pidC.getU() * 12 / voltage);
        }

        servoAction.update();
        servoR.update();
        servoC.update();
        servoL.update();
        brush.setPower(brushPower);
        light.setPower(gunConfig.lightPower);
    }

    private void farAim(double dist) {
        double dist1 = dist;
        if(dist1>adaptiveFireConfig.hiDistFar){
            dist1 = adaptiveFireConfig.hiDistFar;
        }

        double deltaAngle = adaptiveFireConfig.kFarAngle*(dist1-adaptiveFireConfig.lowDistFar);

        if(deltaAngle<0){
            deltaAngle = 0;
        }

        double deltaVel = adaptiveFireConfig.kFarVel*(dist1-adaptiveFireConfig.lowDistFar);
        if(deltaVel<0){
            deltaVel = 0;
        }

        setAimServoPos(1-adaptiveFireConfig.lowAngleFar+deltaAngle);
        gunVelCenter = adaptiveFireConfig.lowVelFar + deltaVel;
        gunVelSide   = adaptiveFireConfig.lowVelFar + deltaVel;
    }
    private void patternAim() {
        setAimServoPos(aimCPat);
    }


    private void nearAim(double dist) {
        double dist1 =dist;
        if(dist1>adaptiveFireConfig.hiDistNear){
            dist1 = adaptiveFireConfig.hiDistNear;
        }
        double deltaVel = adaptiveFireConfig.kNearVel*(dist1-adaptiveFireConfig.lowDistNear);
        if(deltaVel<0){
            deltaVel = 0;
        }
        double deltaAngle = adaptiveFireConfig.kNearAngle*(dist1-adaptiveFireConfig.lowDistNear);
        if(deltaAngle>0){
            deltaAngle = 0;
        }

        setAimServoPos(1-adaptiveFireConfig.lowAngleNear+deltaAngle);

        gunVelSide   = adaptiveFireConfig.lowVelNear+deltaVel;
        gunVelCenter = adaptiveFireConfig.lowVelNear+deltaVel;

    }

    private void setAimServoPos(double l, double c, double r) {
        aimR.setPos(r);
        aimC.setPos(c);
        aimL.setPos(l);
    }
    private void setAimServoPos(double c) {
        aimR.setPos(c+servoDeltaR);
        aimC.setPos(c);
        aimL.setPos(c+servoDeltaL);
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
                    ()->true,
                    ()->fireTimer.seconds()>adaptiveFireConfig.fullFireDelay,
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

    private ServoAction buildPFireServoAction(){
        Runnable servo  = ()->{};
        if(centerColor == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE){
            servo = this::shotCenter;
        }
        if(rightColor == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE){
            servo = this::shotRight;
        }
        if(leftColor == PredominantColorProcessor.Swatch.ARTIFACT_PURPLE){
            servo = this::shotLeft;
        }
        return new ServoAction(servo::run);
    }
    private ServoAction buildGFireServoAction(){
        Runnable servo  = ()->{};
        if(centerColor == PredominantColorProcessor.Swatch.ARTIFACT_GREEN){
            servo = this::shotCenter;
        }
        if(rightColor == PredominantColorProcessor.Swatch.ARTIFACT_GREEN){
            servo = this::shotRight;
        }
        if(leftColor == PredominantColorProcessor.Swatch.ARTIFACT_GREEN){
            servo = this::shotLeft;
        }
        return new ServoAction(servo::run);
    }
    private ServoAction buildPatternFireAction(MOTIF in, MOTIF out) {
        Runnable[] servos = new Runnable[3];

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
                        () -> patterFireTimer.seconds() > gunConfig.patternFireDelay,
                        () -> patterFireTimer.seconds() > gunConfig.patternFireDelay,
                        () -> patterFireTimer.seconds() > 0.3,
                        () -> true
                },
                new Runnable[]{
                        ()->{servos[0].run();patterFireTimer.reset();},
                        ()->{servos[1].run();patterFireTimer.reset();},
                        ()->{servos[2].run();patterFireTimer.reset();},
                        ()->setCommand(EAT),
                });
    }

    private ServoAction buildFastPatternFireAction(MOTIF in, MOTIF out) {
        Runnable[] servos = new Runnable[3];
        BooleanSupplier[] isDone = new BooleanSupplier[3];

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
        if(out == PGP){
            isDone = new BooleanSupplier[]{
                    () -> patterFireTimer.seconds() > gunConfig.patternFireDelay,
                    () -> patterFireTimer.seconds() > gunConfig.patternFireDelay,
                    () -> patterFireTimer.seconds() > 0.3,
                    () -> true
            };
        }
        if(out == PPG){
            isDone = new BooleanSupplier[]{
                    () -> true,
                    () -> patterFireTimer.seconds() > gunConfig.patternFireDelay,
                    () -> patterFireTimer.seconds() > 0.3,
                    () -> true
            };
        }
        if(out == GPP){
            isDone = new BooleanSupplier[]{
                    () -> patterFireTimer.seconds() > gunConfig.patternFireDelay,
                    () -> true,
                    () -> patterFireTimer.seconds() > 0.3,
                    () -> true
            };
        }
        return new ServoAction(
                isDone,
                new Runnable[]{
                        () -> {servos[0].run();patterFireTimer.reset();},
                        () -> {servos[1].run();patterFireTimer.reset();},
                        () -> {servos[2].run();patterFireTimer.reset();},
                        () -> setCommand(EAT),
                });
    }

}
