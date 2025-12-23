package org.woen.RobotModule.Modules.Gun.Impls;

import static org.woen.Config.ControlSystemConstant.gunConfig;
import static org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND.*;
import static org.woen.RobotModule.Modules.Gun.Config.GunServoPositions.*;

import static java.lang.Math.abs;

import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;
import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.Hardware.Devices.Servo.ServoWithFeedback;
import org.woen.Hardware.Devices.Servo.Interface.ServoMotor;
import org.woen.RobotModule.Modules.Camera.Enums.BALL_COLOR;
import org.woen.RobotModule.Modules.Camera.Enums.MOTIF;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsCenterEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsLeftEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsRightEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewMotifCheck;
import org.woen.RobotModule.Modules.Camera.Events.NewMotifEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.GunAtEatEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimEvent;
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

public class GunImpl implements Gun {
    private ServoWithFeedback shotR;
    private ServoWithFeedback shotC;
    private ServoWithFeedback shotL;

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

    boolean shooter = false;

    private void setCommand(GUN_COMMAND command){
        this.command = command;
        switch (this.command){
            case EAT:
                brushPower = 1;
                servoActionR = updateRAction.copy();
                servoActionC = updateCAction.copy();
                servoActionL = updateLAction.copy();

                EventBus.getInstance().invoke(new GunAtEatEvent(1));

                break;
            case TARGET:
                brushPower = 1;

                break;
            case FULL_FIRE:
                gunVelSide = gunConfig.shootVelSide;
                gunVelC = gunConfig.shootVelC;
                if(!isFarAim){
                    gunVelC = gunConfig.shootVelCNear;
                }

                brushPower = 1;

                servoActionR = shotRAction.copy();
                servoActionC = shotCAction.copy();
                servoActionL = shotLAction.copy();
                break;
            case OFF:
                brushPower = 0;

                break;
            case PATTERN_FIRE:
                shooter = true;

                if(isItMotif || shooter){

                    gunVelSide = gunConfig.shootVelSide;
                    gunVelC = gunConfig.shootVelC;
                    if(!isFarAim){
                        gunVelC = gunConfig.shootVelCNear;
                    }

                    brushPower = 1;

                    chooseServoComb();


                }
                else {
                    //TODO поставить состояние для обычной стрельбы
                }
                break;
        }
    }

    private void chooseServoComb(){
        if(motif == MOTIF.GPP){
            if(getMotif() == MOTIF.GPP){
                shotLCR();
            }
            if(getMotif() == MOTIF.PGP){
                shotCLR();
            }
            if(getMotif() == MOTIF.PPG){
                shotRLC();
            }
        }
        if(motif == MOTIF.PGP){
            if(getMotif() == MOTIF.GPP){
                shotCLR();
            }
            if(getMotif() == MOTIF.PGP){
                shotLCR();
            }
            if(getMotif() == MOTIF.PPG){
                shotLRC();
            }
        }
        if(motif == MOTIF.PPG){
            if(getMotif() == MOTIF.GPP){
                shotCRL();
            }
            if(getMotif() == MOTIF.PGP){
                shotRLC();
            }
            if(getMotif() == MOTIF.PPG){
                shotLCR();
            }
        }
        shooter = false;
    }
    private void shotLCR(){
        servoActionL = updateLAction.copy();
        if(servoActionL.isDone()){
            Telemetry.getInstance().add("work ", true );
            servoActionC = updateCAction.copy();
            if(servoActionC.isDone()){
                servoActionR = updateRAction.copy();
            }
        }
    }
    private void shotLRC() {
        servoActionL = updateLAction.copy();
        if (servoActionL.isDone()) {
            servoActionR = updateRAction.copy();
            if (servoActionR.isDone()) {
                servoActionC = updateCAction.copy();
            }
        }
    }
    private void shotRCL(){
            servoActionR = updateRAction.copy();
            if(servoActionR.isDone()){
                servoActionC = updateCAction.copy();
                if(servoActionC.isDone()){
                    servoActionL = updateLAction.copy();
                }
            }

        }
    private void shotRLC(){
        servoActionR = updateRAction.copy();
        if(servoActionR.isDone()){
            servoActionL = updateLAction.copy();
            if(servoActionL.isDone()){
                servoActionC = updateCAction.copy();
            }
        }

    }
    private void shotCLR(){
        servoActionC = updateCAction.copy();
        if(servoActionC.isDone()){
            servoActionL = updateLAction.copy();
            if(servoActionL.isDone()){
                servoActionR = updateRAction.copy();
            }
        }

    }
    private void shotCRL(){
        servoActionC = updateCAction.copy();
        if(servoActionC.isDone()){
            servoActionR = updateRAction.copy();
            if(servoActionR.isDone()){
                servoActionL = updateLAction.copy();
            }
        }


    }



    private GUN_COMMAND command = EAT;

    private boolean isFarAim = true;
    private Vector2d goal = new Vector2d();
    private void setAimCommand(NewAimEvent event){
        isFarAim = event.getData();
        goal = event.getGoal();
    }

    private MOTIF motif = MOTIF.PGP;
    public void setMotif(NewMotifEvent e) {this.motif = e.getData();}

    private double gunVelSide = gunConfig.shootVelSide;
    private double gunVelC    = gunConfig.shootVelC;
    private double brushPower = 1;

    PredominantColorProcessor.Swatch center = null;
    PredominantColorProcessor.Swatch left = null;
    PredominantColorProcessor.Swatch right = null;
    boolean isItMotif = false;

    MOTIF inMouth = null;

    private MOTIF getMotif() {

            if (left == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                inMouth = MOTIF.GPP;
            else{
                if (center == PredominantColorProcessor.Swatch.ARTIFACT_GREEN)
                    inMouth = MOTIF.PGP;
                else{
                    if (right == PredominantColorProcessor.Swatch.ARTIFACT_GREEN){
                        inMouth = MOTIF.PPG;
                    }
                }
        }
        return inMouth;
    }

    private void setLeft(NewDetectionBallsLeftEvent event){
        left = event.getData();
    }
    private void setRight(NewDetectionBallsRightEvent event){
        right = event.getData();
    }
    private void setCenter(NewDetectionBallsCenterEvent event){
        center = event.getData();
    }

    private void setStatusOfMotrif(NewMotifCheck event){
        isItMotif = event.getData();
    }

    @Override
    public void init() {
        shotR = new ServoWithFeedback(DevicePool.getInstance().shotR,command.right);
        shotC = new ServoWithFeedback(DevicePool.getInstance().shotC,command.center);
        shotL = new ServoWithFeedback(DevicePool.getInstance().shotL,command.left);

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
        EventBus.getInstance().subscribe(NewMotifEvent.class, this::setMotif);
        EventBus.getInstance().subscribe(NewDetectionBallsRightEvent.class, this::setRight);
        EventBus.getInstance().subscribe(NewDetectionBallsLeftEvent.class, this::setLeft);
        EventBus.getInstance().subscribe(NewDetectionBallsCenterEvent.class, this::setCenter);
        EventBus.getInstance().subscribe(NewMotifCheck.class, this::setStatusOfMotrif);
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));
    }

    @Override
    public void lateUpdate(){
        double dist = pose.vector.minus(goal).length();
        Telemetry.getInstance().add("distance to goal",dist);

        if(isFarAim){
            farAim();
        }else{
            nearAim(dist);
        }

        pidR.setTarget(gunVelSide);
        pidR.setPos(gunR.getVel());
        pidR.update();

        pidL.setTarget(gunVelSide);
        pidL.setPos(gunL.getVel());
        pidL.update();

        pidC.setTarget(gunVelC);
        pidC.setPos(gunC.getVel());
        pidC.update();


        Telemetry.getInstance().add("gunR vel",gunR.getVel());
        Telemetry.getInstance().add("gunL vel",gunL.getVel());
        Telemetry.getInstance().add("gunC vel",gunC.getVel());

        Telemetry.getInstance().add("case",command.toString());

        Telemetry.getInstance().add("shotR target", shotR.motionProfile.getPos(shotR.motionProfile.duration+1));

        Telemetry.getInstance().add("getMotif", getMotif());
        Telemetry.getInstance().add("isItMotif", isItMotif);
        if(command == OFF){
            gunR.setPower(0);
            gunL.setPower(0);
            gunC.setPower(0);
        }else {
            gunR.setPower(pidR.getU());
            gunL.setPower(pidL.getU());
            gunC.setPower(pidC.getU());
        }
        brush.setPower(brushPower);

        servoActionR.update();
        servoActionL.update();
        servoActionC.update();

        shotL.update();
        shotR.update();
        shotC.update();

        if(command == PATTERN_FIRE){
            chooseServoComb();
        }
    }

    private void farAim(){
        aim(aimLFar, aimCFar, aimRFar);
        gunVelC = gunConfig.shootVelC;
    }

    private void nearAim(double dist){

        double deltaC = (dist - gunConfig.distLow) * gunConfig.deltaPosC /(gunConfig.distHi - gunConfig.distLow);
        double deltaS = (dist - gunConfig.distLow) * gunConfig.deltaPosS /(gunConfig.distHi - gunConfig.distLow);
        Telemetry.getInstance().add("deltaC",deltaC);
        Telemetry.getInstance().add("deltaS",deltaS);

        aim(aimLNear+deltaS, aimCNear+deltaC, aimRNear+deltaS);
        gunVelC = gunConfig.shootVelCNear;
        gunVelSide = gunConfig.shootVelSideNear;
    }

    private void aim(double l, double c, double r){
        aimR.setPos(r);
        aimC.setPos(c);
        aimL.setPos(l);
    }

    private Pose pose = new Pose(0,0,0);
    private void setPose(Pose p){
        pose = p;
    }

    private ServoAction servoActionR = new ServoAction(
          new ServoActionUnit() {
                @Override
                public boolean isAtTarget() {
                    return true;
                }
                @Override
                public void run() {}
          });

    private ServoAction servoActionL = new ServoAction(
          new ServoActionUnit() {
                @Override
                public boolean isAtTarget() {
                    return true;
                }
                @Override
                public void run() {}
          });

    private ServoAction servoActionC = new ServoAction(
          new ServoActionUnit() {
                @Override
                public boolean isAtTarget() {
                    return true;
                }
                @Override
                public void run() {}
          });

    private ServoAction shotRAction = new ServoAction(
        new ServoActionUnit() {
            @Override
            public boolean isAtTarget() {
                return Math.abs(gunVelSide - gunR.getVel())<gunConfig.velTol;
            }

            @Override
            public void run() {
            }
        },
        new ServoActionUnit() {
                @Override
                public boolean isAtTarget() {
                    return shotR.isAtTarget();
                }
                @Override
                public void run() {shotR.setTarget(command.right);}
    });
    private ServoAction shotCAction = new ServoAction(
        new ServoActionUnit() {
            @Override
            public boolean isAtTarget() {return Math.abs(gunVelC - gunC.getVel())<gunConfig.velTol;}
            @Override
            public void run() {}
        },
        new ServoActionUnit() {
                @Override
                public boolean isAtTarget() {return shotC.isAtTarget();}
                @Override
                public void run() {shotC.setTarget(command.center);}
    });
    private ServoAction shotLAction = new ServoAction(
            new ServoActionUnit() {
                @Override
                public boolean isAtTarget() {
                    return Math.abs(gunVelSide - gunL.getVel()) < gunConfig.velTol;
                }

                @Override
                public void run() {
                }
            },
            new ServoActionUnit() {
                @Override
                public boolean isAtTarget() {
                    return shotL.isAtTarget();
                }

                @Override
                public void run() {
                    shotL.setTarget(command.left);
                }

            },
            new ServoActionUnit() {
                @Override
                public boolean isAtTarget() {
                    return shotR.isAtTarget() && shotC.isAtTarget() && shotL.isAtTarget();
                }

                @Override
                public void run() {

                }
            },
            () -> setCommand(EAT)
    );

    private final ServoAction updateRAction = new ServoAction(
            new ServoActionUnit() {
                @Override
                public boolean isAtTarget() {return shotR.isAtTarget();}
                @Override
                public void run() {shotR.setTarget(command.right);}
            }
    );

    private final ServoAction updateCAction = new ServoAction(
            new ServoActionUnit() {
                @Override
                public boolean isAtTarget() {return shotC.isAtTarget();}
                @Override
                public void run() {shotC.setTarget(command.center);}
            }
    );

    private ServoAction updateLAction = new ServoAction(
            new ServoActionUnit() {
                @Override
                public boolean isAtTarget() {return shotL.isAtTarget();}
                @Override
                public void run() {shotL.setTarget(command.left);}
            }
    );
}