package org.woen.RobotModule.Modules.Gun.Impls;

import static org.woen.Config.ControlSystemConstant.gunConfig;
import static org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND.*;
import static org.woen.RobotModule.Modules.Gun.Config.GunServoPositions.*;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;
import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.Hardware.Devices.Servo.Interface.ServoMotor;
import org.woen.RobotModule.Modules.Camera.Enums.MOTIF;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsCenterEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsLeftEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsRightEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewMotifCheck;
import org.woen.RobotModule.Modules.Camera.Events.NewMotifEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.GunAtEatEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Gun.Config.GunServoPositions;
import org.woen.RobotModule.Modules.Gun.Interface.Gun;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Pid.Pid;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

public class GunImpl implements Gun {
    private ServoMotor shotR;
    private ServoMotor shotC;
    private ServoMotor shotL;

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

    boolean isItTar = false;

    private void setCommand(GUN_COMMAND command) {
        this.command = command;

    }

    private void update2(){
        if(this.command == previousCommand)
            time.reset();
        switch (this.command) {
            case EAT:
                previousCommand = command;
                brushPower = 1;
                shotR.setPos(eatR);
                shotC.setPos(eatC);
                shotL.setPos(eatL);

                EventBus.getInstance().invoke(new GunAtEatEvent(1));

                break;
            case TARGET:
                brushPower = 1;
                previousCommand = command;
                break;
            case FULL_FIRE:
                gunVelSide = gunConfig.shootVelSide;
                gunVelC = gunConfig.shootVelC;
                if (!isFarAim) {
                    gunVelC = gunConfig.shootVelCNear;
                }

                brushPower = 1;
                if(time.seconds() > 0.1) {
                    shotR.setPos(GunServoPositions.shotR);
                    shotC.setPos(GunServoPositions.shotC);
                    shotL.setPos(GunServoPositions.shotL);
                }
                if(time.seconds() > 0.6) {
                    setCommand(EAT);
                    previousCommand = command;
                }
                break;
            case OFF:
                brushPower = 0;
                break;
            case PATTERN_FIRE:
                if(isItMotif) {
                    gunVelSide = gunConfig.shootVelSide;
                    gunVelC = gunConfig.shootVelC;
                    if (!isFarAim) {
                        gunVelC = gunConfig.shootVelCNear;
                    }
                    brushPower = 1;

                    if (time.seconds() < 0.1)
                        shooterComb();
                    if (time.seconds() > 0.1)
                        servoMovement();

                    if (time.seconds() > 0.7) {
                        setCommand(EAT);
                        previousCommand = command;
                    }
                }
                else{
                    setCommand(FULL_FIRE);
                }
                break;
        }
    }

    private char[] motifToSymbols(MOTIF motif) {
        return motif.toString().toCharArray();
    }

    private int numb = 0;

    private int numb2 = 0;

    //1 - левый
    //2 - средний
    //3 - правый

    private PoseInBrush numbToEnum(int n) {
        if (n == 1)
            return PoseInBrush.LEFT;
        if (n == 2)
            return PoseInBrush.CENTER;
        if (n == 3)
            return PoseInBrush.RIGHT;
        return PoseInBrush.LEFT;
    }

    PoseInBrush targetGreen = PoseInBrush.LEFT;
    PoseInBrush whereGreenBall = PoseInBrush.LEFT;



    boolean isItPat = true;

    private void shooterComb() {
        char[] target = motifToSymbols(motif);
        char[] inShooter = motifToSymbols(getMotif());
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                if (target[i] == inShooter[j] && target[i] == 'G') {
                    numb = i + 1;
                    numb2 = j + 1;
                }
            }
        }
            whereGreenBall = numbToEnum(numb2);
            targetGreen = numbToEnum(numb);


    }

    private enum PoseInBrush {
        LEFT, CENTER, RIGHT, NULL
    }

        private void servoMovement() {
            switch (targetGreen) {
                case LEFT:
                    switch (whereGreenBall) {
                        case RIGHT:
                            shotL.setPos(GunServoPositions.shotL);
                            if (time.seconds()>0.4) {
                                shotC.setPos(GunServoPositions.shotC);
                                shotR.setPos(GunServoPositions.shotR);
                            }
                            break;
                        case CENTER:
                            shotC.setPos(GunServoPositions.shotC);
                            if (time.seconds()>0.4) {
                                shotR.setPos(GunServoPositions.shotR);
                                shotL.setPos(GunServoPositions.shotL);
                            }
                            break;
                        case LEFT:
                            shotR.setPos(GunServoPositions.shotR);
                            if (time.seconds()>0.4) {
                                shotC.setPos(GunServoPositions.shotC);
                                shotL.setPos(GunServoPositions.shotL);
                            }
                            break;
                    }
                    break;
                case CENTER:
                    switch (whereGreenBall) {
                        case RIGHT:
                            shotC.setPos(GunServoPositions.shotC);
                            if (time.seconds() > 0.3) {
                                shotL.setPos(GunServoPositions.shotL);
                            }
                            if (time.seconds() > 0.7) {
                               shotR.setPos(GunServoPositions.shotR);
                            }
                            break;
                        case CENTER:
                            shotR.setPos(GunServoPositions.shotR);
                            if (time.seconds() > 0.3) {
                                shotC.setPos(GunServoPositions.shotC);
                            }
                            if (time.seconds() > 0.7) {
                                shotL.setPos(GunServoPositions.shotL);
                            }
                            break;
                        case LEFT:
                            shotL.setPos(GunServoPositions.shotL);
                            if (time.seconds() > 0.3) {
                                shotR.setPos(GunServoPositions.shotR);
                            }
                            if (time.seconds() > 0.7) {
                                shotC.setPos(GunServoPositions.shotC);
                            }
                            break;
                    }
                    break;
                case RIGHT:
                    switch (whereGreenBall) {
                        case RIGHT:
                            shotR.setPos(GunServoPositions.shotR);
                            shotC.setPos(GunServoPositions.shotC);
                            if(time.seconds() > 0.4){
                                shotL.setPos(GunServoPositions.shotL);
                            }
                            break;
                        case CENTER:
                            shotL.setPos(GunServoPositions.shotL);
                            shotR.setPos(GunServoPositions.shotR);
                            if(time.seconds() > 0.4){
                                shotC.setPos(GunServoPositions.shotC);
                            }
                            break;
                        case LEFT:
                            shotC.setPos(GunServoPositions.shotC);
                            shotL.setPos(GunServoPositions.shotL);
                            if(time.seconds() > 0.4){
                                shotR.setPos(GunServoPositions.shotR);
                            }
                            break;
                    }
                    break;

            }


        }


    private GUN_COMMAND command = EAT;

    private GUN_COMMAND previousCommand = EAT;

    private boolean isFarAim = true;
    private Vector2d goal = new Vector2d();

    private void setAimCommand(NewAimEvent event) {
        isFarAim = event.getData();
        goal = event.getGoal();
    }

    private MOTIF motif = MOTIF.PPG;

    public void setMotif(NewMotifEvent e) {
        this.motif = e.getData();
    }

    private boolean isItMotif = false;

    public void setStatusMotif(NewMotifCheck e){
        isItMotif = e.getData();
    }

    private double gunVelSide = gunConfig.shootVelSide;
    private double gunVelC = gunConfig.shootVelC;
    private double brushPower = 1;

    PredominantColorProcessor.Swatch center = null;
    PredominantColorProcessor.Swatch left = null;
    PredominantColorProcessor.Swatch right = null;

    MOTIF inMouth = null;

    private MOTIF getMotif() {
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

    private void setLeft(NewDetectionBallsLeftEvent event) {
        left = event.getData();
    }

    private void setRight(NewDetectionBallsRightEvent event) {
        right = event.getData();
    }

    private void setCenter(NewDetectionBallsCenterEvent event) {
        center = event.getData();
    }


    ElapsedTime time = new ElapsedTime();

    @Override
    public void init() {
        shotR = DevicePool.getInstance().shotR;
        shotC = DevicePool.getInstance().shotC;
        shotL = DevicePool.getInstance().shotL;

        gunR = DevicePool.getInstance().gunR;
        gunL = DevicePool.getInstance().gunL;
        gunC = DevicePool.getInstance().gunC;

        brush = DevicePool.getInstance().brush;

        aimR = DevicePool.getInstance().aimR;
        aimC = DevicePool.getInstance().aimC;
        aimL = DevicePool.getInstance().aimL;

        time.reset();
    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(NewGunCommandAvailable.class, this::setCommand);

        EventBus.getInstance().subscribe(NewAimEvent.class, this::setAimCommand);

        EventBus.getInstance().subscribe(NewMotifEvent.class, this::setMotif);

        EventBus.getInstance().subscribe(NewDetectionBallsRightEvent.class, this::setRight);

        EventBus.getInstance().subscribe(NewDetectionBallsLeftEvent.class, this::setLeft);

        EventBus.getInstance().subscribe(NewDetectionBallsCenterEvent.class, this::setCenter);

        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));

        EventBus.getInstance().subscribe(NewMotifCheck.class, this::setStatusMotif);
    }

    @Override
    public void lateUpdate() {
        double dist = pose.vector.minus(goal).length();
        Telemetry.getInstance().add("distance to goal", dist);

        if (isFarAim) {
            farAim();
        } else {
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


        Telemetry.getInstance().add("gunR vel", gunR.getVel());
        Telemetry.getInstance().add("gunL vel", gunL.getVel());
        Telemetry.getInstance().add("gunC vel", gunC.getVel());

        Telemetry.getInstance().add("timer", time.seconds());

        Telemetry.getInstance().add("case", command.toString());


        Telemetry.getInstance().add("getMotif", getMotif());

        Telemetry.getInstance().add("targetGreen", targetGreen);
        Telemetry.getInstance().add("in Robot", inMouth);
        Telemetry.getInstance().add("Green Ball", whereGreenBall);


        if (command == OFF) {
            gunR.setPower(0);
            gunL.setPower(0);
            gunC.setPower(0);
        } else {
            gunR.setPower(pidR.getU());
           gunL.setPower(pidL.getU());
            gunC.setPower(pidC.getU());
        }
        brush.setPower(brushPower);
        update2();

    }

    private void farAim() {
        aim(aimLFar, aimCFar, aimRFar);
        gunVelC = gunConfig.shootVelC;
    }

    private void nearAim(double dist) {

        double deltaC = (dist - gunConfig.distLow) * gunConfig.deltaPosC / (gunConfig.distHi - gunConfig.distLow);
        double deltaS = (dist - gunConfig.distLow) * gunConfig.deltaPosS / (gunConfig.distHi - gunConfig.distLow);
        Telemetry.getInstance().add("deltaC", deltaC);
        Telemetry.getInstance().add("deltaS", deltaS);

        aim(aimLNear + deltaS, aimCNear + deltaC, aimRNear + deltaS);
        gunVelC = gunConfig.shootVelCNear;
        gunVelSide = gunConfig.shootVelSideNear;
    }

    private void aim(double l, double c, double r) {
        aimR.setPos(r);
        aimC.setPos(c);
        aimL.setPos(l);
    }

    private Pose pose = new Pose(0, 0, 0);

    private void setPose(Pose p) {
        pose = p;
    }

}