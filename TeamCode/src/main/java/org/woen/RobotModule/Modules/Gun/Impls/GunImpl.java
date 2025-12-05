package org.woen.RobotModule.Modules.Gun.Impls;

import static org.woen.Config.ControlSystemConstant.gunConfig;
import static org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND.*;
import static org.woen.RobotModule.Modules.Gun.Config.GunServoPositions.*;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.Hardware.Devices.Servo.FeedbackableServo;
import org.woen.Hardware.Devices.Servo.Interface.ServoMotor;
import org.woen.RobotModule.Modules.Camera.MOTIF;
import org.woen.RobotModule.Modules.Camera.NewMotifEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimCommandAvaliable;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Arcitecture.ServoAction;
import org.woen.RobotModule.Modules.Gun.Arcitecture.ServoActionUnit;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Gun.Interface.Gun;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Pid.Pid;
import org.woen.Util.Pid.PidStatus;

public class GunImpl implements Gun {
    private FeedbackableServo shotR;
    private FeedbackableServo shotC;
    private FeedbackableServo shotL;

    private ServoMotor aimR;
    private ServoMotor aimC;
    private ServoMotor aimL;

    private Motor gun;
    private Motor brush;

    private PidStatus status = new PidStatus(1, 0, 0, 0.0004, 0, 0, 0,50);
    private final Pid pid = new Pid(status);

    public void setCommand(NewGunCommandAvailable event) {
        timer.reset();
        firstRun = true;
        this.command = event.getData();
    }

    private GUN_COMMAND command = EAT;

    private boolean isAimHi = true;
    private void setAimCommand(NewAimCommandAvaliable event){
        isAimHi = event.getData();
    }
    private MOTIF motif = MOTIF.PGP;

    public void setMotif(NewMotifEvent e) {
        this.motif = e.getData();
    }

    private double gunVel = gunConfig.shootVel;
    private double brushPower = -1;

    @Override
    public void init() {
        shotR = new FeedbackableServo(DevicePool.getInstance().shotR,command.right);
        shotC = new FeedbackableServo(DevicePool.getInstance().shotC,command.center);
        shotL = new FeedbackableServo(DevicePool.getInstance().shotL,command.left);

        gun = DevicePool.getInstance().gun;
        brush = DevicePool.getInstance().brush;

        aimR = DevicePool.getInstance().aimR;
        aimC = DevicePool.getInstance().aimC;
        aimL = DevicePool.getInstance().aimL;

    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(NewGunCommandAvailable.class, this::setCommand);
        EventBus.getInstance().subscribe(NewAimCommandAvaliable.class, this::setAimCommand);
        EventBus.getInstance().subscribe(NewMotifEvent.class, this::setMotif);
    }

    private double delay = gunConfig.delay;
    private ElapsedTime timer = new ElapsedTime();
    private boolean firstRun = true;
    public void lateUpdate() {

        switch (command) {
            case RAPID_FIRE:
                break;
            case SHOT_CENTER:
            case SHOT_LEFT:
            case SHOT_RIGHT:
            case FULL_FIRE:
                gunVel = gunConfig.shootVel;
                if(firstRun){
                    servoAction = new ServoAction(
                            new ServoActionUnit() {
                                @Override
                                public boolean isAtTarget() {
                                    return shotR.isAtTarget();
                                }
                                @Override
                                public void run() {
                                    shotR.setTarget(command.right);
                                    shotL.setTarget(command.left);
                                }
                            },
                            new ServoActionUnit() {
                                @Override
                                public boolean isAtTarget() {
                                    return shotC.isAtTarget();
                                }
                                @Override
                                public void run() {
                                    shotC.setTarget(command.center);
                                }
                            }
                    );
                    firstRun = false;
                }
                if (servoAction.isDone()) {
                    EventBus.getInstance().invoke(new NewGunCommandAvailable(EAT));;
                }
                break;
            case EAT:
                if(firstRun){
                    servoAction = new ServoAction(
                            new ServoActionUnit() {
                                @Override
                                public boolean isAtTarget() {
                                    return true;
                                }
                                @Override
                                public void run() {
                                    shotR.setTarget(command.right);
                                    shotL.setTarget(command.left);
                                    shotC.setTarget(command.center);
                                }
                            }
                    );
                    firstRun = false;
                }
                break;
            case REVERSE:
                shotR.setTarget(command.right);
                shotL.setTarget(command.left);
                shotC.setTarget(command.center);
                break;
            case PATTERN_FIRE:
                switch (motif){
                    case GPP:
                        break;
                    case PGP:
                        break;
                    case PPG:
                        break;

                }

                if (timer.seconds() > 4 * delay) {
                    EventBus.getInstance().invoke(new NewGunCommandAvailable(EAT));;
                }
                break;
        }

        if(isAimHi){
            hiAim();
        }else{
            lowAim();
        }

        pid.setTarget(gunVel);
        pid.setPos(gun.getVel());
        pid.update();
        Telemetry.getInstance().add("gunVel",gun.getVel());
        Telemetry.getInstance().add("case",command.toString());
        gun.setPower(pid.getU());
        brush.setPower(brushPower);
        servoAction.update();

        shotL.update();
        shotR.update();
        shotC.update();
    }

    private void hiAim(){
        aim(aimLHi,aimCHi,aimRHi);
    }

    private void lowAim(){
        aim(aimLLow,aimCLow,aimRLow);
    }

    private void aim(double l, double c, double r){
        aimR.setPos(r);
        aimC.setPos(c);
        aimL.setPos(l);
    }
    private ServoAction servoAction = new ServoAction(
                                  new ServoActionUnit() {
                                        @Override
                                        public boolean isAtTarget() {
                                            return true;
                                        }
                                        @Override
                                        public void run() {
                                            shotR.setTarget(command.right);
                                            shotL.setTarget(command.left);
                                            shotC.setTarget(command.center);
                                        }
                                  });
}