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

    private void setCommand(GUN_COMMAND command){
        this.command = command;
        switch (this.command){
            case EAT:
                brushPower = 1;

                servoActionR = updateRAction.copy();
                servoActionC = updateCAction.copy();
                servoActionL = updateLAction.copy();
                break;
            case TARGET:
                brushPower = 0.5;

                break;
            case FULL_FIRE:
                gunVel = gunConfig.shootVel;
                brushPower = 1;

                servoActionR = shotRAction.copy();
                servoActionC = shotCAction.copy();
                servoActionL = shotLAction.copy();
                break;
            case PATTERN_FIRE:
                break;
        }
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
        EventBus.getInstance().subscribe(NewAimCommandAvaliable.class, this::setAimCommand);
        EventBus.getInstance().subscribe(NewMotifEvent.class, this::setMotif);
    }

    @Override
    public void lateUpdate(){
        if(isAimHi){
            hiAim();
        }else{
            lowAim();
        }

        pidR.setTarget(gunVel);
        pidR.setPos(gunR.getVel());
        pidR.update();

        pidL.setTarget(gunVel);
        pidL.setPos(gunL.getVel());
        pidL.update();

        pidC.setTarget(gunVel);
        pidC.setPos(gunC.getVel());
        pidC.update();


        Telemetry.getInstance().add("gunR vel",gunR.getVel());
        Telemetry.getInstance().add("gunL vel",gunL.getVel());
        Telemetry.getInstance().add("gunC vel",gunC.getVel());

        Telemetry.getInstance().add("case",command.toString());

        Telemetry.getInstance().add("shotR target", shotR.motionProfile.getPos(shotR.motionProfile.duration+1));
        gunR.setPower(pidR.getU());
        gunL.setPower(pidL.getU());
        gunC.setPower(pidC.getU());

        brush.setPower(brushPower);

        servoActionR.update();
        servoActionL.update();
        servoActionC.update();

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
                return Math.abs(gunVel- gunR.getVel())<gunConfig.velTol;
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
            public boolean isAtTarget() {return Math.abs(gunVel- gunC.getVel())<gunConfig.velTol;}
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
                    return Math.abs(gunVel - gunL.getVel()) < gunConfig.velTol;
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
                public void run() {shotR.setTarget(EAT.right);}
            }
    );

    private final ServoAction updateCAction = new ServoAction(
            new ServoActionUnit() {
                @Override
                public boolean isAtTarget() {return shotC.isAtTarget();}
                @Override
                public void run() {shotC.setTarget(EAT.center);}
            }
    );

    private ServoAction updateLAction = new ServoAction(
            new ServoActionUnit() {
                @Override
                public boolean isAtTarget() {return shotL.isAtTarget();}
                @Override
                public void run() {shotL.setTarget(EAT.left);}
            }
    );
}