package org.woen.RobotModule.Modules.Gun.Impls;

import static org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND.*;
import static org.woen.RobotModule.Modules.Gun.Config.GunServoPositions.*;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.Hardware.Devices.Servo.Interface.ServoMotor;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimCommandAvaliable;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Gun.Interface.Gun;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Pid.Pid;
import org.woen.Util.Pid.PidStatus;

public class GunImpl implements Gun {
    private ServoMotor right;
    private ServoMotor center;
    private ServoMotor left;
    private ServoMotor wall;

    private ServoMotor borderR;
    private ServoMotor borderL;

    private ServoMotor aimR;
    private ServoMotor aimC;
    private ServoMotor aimL;

    private Motor gunL;
    private Motor gunR;

    private PidStatus status = new PidStatus(10, 0, 0, 0.57, 0, 0, 0);
    private Pid pid = new Pid(status);

    public void setCommand(NewGunCommandAvailable event) {
        timer.reset();
        this.command = event.getData();
    }

    private GUN_COMMAND command = EAT;
    private boolean isAimHi = false;
    private void setAimCommand(NewAimCommandAvaliable event){
        isAimHi = event.getData();
    }

    private double gunVel = 0;

    @Override
    public void init() {
        right  = DevicePool.getInstance().shotR;
        center = DevicePool.getInstance().shotC;
        left   = DevicePool.getInstance().shotL;
        wall   = DevicePool.getInstance().wall;

        gunL = DevicePool.getInstance().gunL;
        gunR = DevicePool.getInstance().gunR;

        borderR = DevicePool.getInstance().borderR;
        borderL = DevicePool.getInstance().borderL;

        aimR = DevicePool.getInstance().aimR;
        aimC = DevicePool.getInstance().aimC;
        aimL = DevicePool.getInstance().aimL;
    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(NewGunCommandAvailable.class, this::setCommand);
        EventBus.getInstance().subscribe(NewAimCommandAvaliable.class, this::setAimCommand);
    }

    private double delay = 0.5;
    private ElapsedTime timer = new ElapsedTime();

    public void lateUpdate() {
        switch (command) {
            case RAPID_FIRE:
                wall.setPos(command.wall);
                right.setPos(command.right);
                if (timer.seconds() > delay) {
                    center.setPos(command.center);
                }
                if (timer.seconds() > 2 * delay) {
                    left.setPos(command.left);
                }
                if (timer.seconds() > 3 * delay) {
                    command = EAT;
                }
                break;
            case SHOT_CENTER:
            case SHOT_LEFT:
            case SHOT_RIGHT:
            case FULL_FIRE:
                wall.setPos(command.wall);

                if(timer.seconds()>delay) {
                    right.setPos(command.right);
                    left.setPos(command.left);
                    center.setPos(command.center);
                }

                if (timer.seconds() > 3 * delay) {
                    command = EAT;
                }
                break;
            case TARGET:
                gunVel = 1100;
                right.setPos(command.right);
                left.setPos(command.left);
                center.setPos(command.center);
                wall.setPos(command.wall);
                timer.reset();
                break;
            case EAT:
                gunVel = 800;
                timer.reset();
                right.setPos(command.right);
                left.setPos(command.left);
                center.setPos(command.center);
                wall.setPos(command.wall);
        }

        if(command == EAT){
            borderOpen();
        }else{
            borderClose();
        }

        if(isAimHi){
            hiAim();
        }else{
            lowAim();
        }

        pid.setTarget(gunVel);
        pid.setPos(gunR.getVel());
        pid.update();
        Telemetry.getInstance().add("gunVel",gunR.getVel());
        gunL.setPower(pid.getU());
        gunR.setPower(pid.getU());

    }

    private void hiAim(){
        aim(aimLHi,aimCHi,aimRHi);
    }

    private void lowAim(){
        aim(aimLLow,aimCLow,aimRLow);
    }

    private void borderOpen(){
        border(borderOpen);
    }

    private void borderClose(){
        border(borderClose);
    }

    private void aim(double l, double c, double r){
        aimR.setPos(r);
        aimC.setPos(c);
        aimL.setPos(l);
    }

    private void border(double r){
        borderR.setPos(r);
        borderL.setPos(1-r);
    }
}
