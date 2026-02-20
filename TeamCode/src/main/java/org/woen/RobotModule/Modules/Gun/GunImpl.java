package org.woen.RobotModule.Modules.Gun;

import static org.woen.Config.ControlSystemConstant.adaptiveFireConfig;
import static org.woen.Config.ControlSystemConstant.gunConfig;
import static org.woen.RobotModule.Modules.Camera.Enums.BALL_COLOR.G;
import static org.woen.RobotModule.Modules.Camera.Enums.BALL_COLOR.P;
import static org.woen.RobotModule.Modules.Camera.Enums.MOTIF.GPP;
import static org.woen.RobotModule.Modules.Camera.Enums.MOTIF.PGP;
import static org.woen.RobotModule.Modules.Camera.Enums.MOTIF.PPG;
import static org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND.FAR;
import static org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND.NEAR;
import static org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND.NEAR_GOAL;
import static org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND.PATTERN;
import static org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND.*;
import static org.woen.RobotModule.Modules.Gun.Config.GunServoPositions.*;


import com.acmerobotics.roadrunner.PositionPath;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;
import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.MatchData;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.DevicePool.Devices.Motor.Interface.Motor;
import org.woen.Hardware.DevicePool.Devices.Servo.Impls.ServoImpl;
import org.woen.Hardware.DevicePool.Devices.Servo.Interface.ServoMotor;
import org.woen.Hardware.DevicePool.Devices.Servo.ServoWithFeedback;
import org.woen.RobotModule.Modules.Battery.NewVoltageAvailable;
import org.woen.RobotModule.Modules.Camera.Enums.BALL_COLOR;
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
import org.woen.RobotModule.Modules.Gun.Arcitecture.ServoActionUnit;
import org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Gun.Interface.Gun;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewPositionListener;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Color.LedDriver;
import org.woen.Util.Pid.Pid;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

import java.util.ArrayList;
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

//    private LedDriver rgb2;
//
//    private LedDriver rgb3;
//
//    private LedDriver rgb4;
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
                servoAction = /*buildSavePatternFireAction().copy();*/buildPatternFireAction(getInMotif(),targetMotif).copy();
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
            case C_FIRE:
                servoAction = new ServoAction(this::shotCenter);
                break;
            case L_FIRE:
                servoAction = new ServoAction(this::shotLeft);
                break;
            case R_FIRE:
                servoAction = new ServoAction(this::shotRight);
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

        motifOutColor[0] = targetMotif.colors[0];
        motifOutColor[1] = targetMotif.colors[1];
        motifOutColor[2] = targetMotif.colors[2];

        motifOutColor[3] = targetMotif.colors[0];
        motifOutColor[4] = targetMotif.colors[1];
        motifOutColor[5] = targetMotif.colors[2];

        motifOutColor[6] = targetMotif.colors[0];
        motifOutColor[7] = targetMotif.colors[1];
        motifOutColor[8] = targetMotif.colors[2 ];
    }

    private double gunTargetVel = gunConfig.shootVelSideFar;
    private double brushPower = 0.8;

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

        DevicePool.getInstance().hardwareMap.get(Servo.class, "rgb2").setPosition(1);
        DevicePool.getInstance().hardwareMap.get(Servo.class, "rgb3").setPosition(1);

//        rgb3 = DevicePool.getInstance().ledDriver3;
//        rgb4 = DevicePool.getInstance().ledDriver4;

        targetColorIdx = 0;

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
            gunTargetVel = adaptiveFireConfig.farVel.get(dist);
            setAimServoPos(adaptiveFireConfig.farAngle.get(dist));
        } else if (aimCommand == NEAR){
            gunTargetVel = adaptiveFireConfig.nearVel.get(dist);
            setAimServoPos(adaptiveFireConfig.nearAngle.get(dist));
        } else if(aimCommand == PATTERN){
            gunTargetVel = gunConfig.shootVelSidePattern;
            patternAim();
        } else if (aimCommand == NEAR_GOAL) {
            gunTargetVel = gunConfig.shootVelSideGoalNear;
            setAimServoPos(aimCGoalNear);
        }

        if(isBrushRevers){
            brushPower = -0.8;
        }else{
            brushPower = 0.8;
        }
        if(command != EAT){
            brushPower = 0;
        }

        pidR.setTarget(gunTargetVel);
        pidR.setPos(gunR.getVel());
        pidR.update();

        pidL.setTarget(gunTargetVel);
        pidL.setPos(gunL.getVel());
        pidL.update();

        pidC.setTarget(gunTargetVel);
        pidC.setPos(gunC.getVel());
        pidC.update();

        Telemetry.getInstance().add("target   motif", targetMotif);
        Telemetry.getInstance().add("in robot",
                leftColor.toString() + " " + centerColor.toString() + " " + rightColor.toString());
        Telemetry.getInstance().add("gun case", command);
        Telemetry.getInstance().add("dist to goal", dist);
        Telemetry.getInstance().add("target color to save", motifOutColor[targetColorIdx].toString() + " " + targetColorIdx);

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

    private void patternAim() {
        setAimServoPos(aimCPat);
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

    private BALL_COLOR[] motifOutColor = new  BALL_COLOR[]{P,P,G,P,P,G,P,P,G};
    private int targetColorIdx = 0;

    private ServoAction buildSavePatternFireAction(){
//        if( artCheck() ){
//            return buildPatternFireAction(getInMotif(),targetMotif);
//        }
        ArrayList<Runnable> servos = new ArrayList<>();
        ElapsedTime timer = new ElapsedTime();

        boolean[] servosBusy = new boolean[]{true, true, true};
        int targetColorIdxStart = targetColorIdx;
        for(int i = targetColorIdxStart; i<targetColorIdxStart+3; i++){
            Runnable servo1 = ()->{};
            boolean isFind = false;
            if (leftColor == motifOutColor[i].toSwatch() && servosBusy[0]) {
                //0-l
                isFind = true;
                servo1 = this::shotLeft;
                servosBusy[0] = false;
            } else if (centerColor == motifOutColor[i].toSwatch() && servosBusy[1]) {
                //0 - c
                isFind = true;
                servo1 = this::shotCenter;
                servosBusy[1] = false;
            } else if (rightColor == motifOutColor[i].toSwatch() && servosBusy[2]) {
                //0 - r
                isFind = true;
                servo1 = this::shotRight;
                servosBusy[2] = false;
            }
            if (isFind) {
                servos.add(servo1);
                targetColorIdx = targetColorIdx + 1;
            }
        }

        BooleanSupplier [] isDone = new BooleanSupplier[]{
                ()->timer.seconds()>gunConfig.patternFireDelay,
                ()->timer.seconds()>gunConfig.patternFireDelay,
                ()->timer.seconds()>gunConfig.patternFireDelay,
                ()->true,
        };

        servos.add(()->setCommand(EAT));
        return new ServoAction(
                servos.toArray(new Runnable[0]),
                isDone
        );
    }
}
