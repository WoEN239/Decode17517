package org.woen.RobotModule.Modules.Gun.Impls;

import static org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND.*;
import static org.woen.RobotModule.Modules.Gun.Config.GunServoPositions.*;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.ColorSensor.Interface.ColorSensor;
import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.Hardware.Devices.Servo.Interface.ServoMotor;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimCommandAvaliable;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.BallColor;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Gun.Interface.Gun;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Color.RgbColorVector;
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

    private ColorSensor sensorR;
    private ColorSensor sensorC;
    private ColorSensor sensorL;

    private PidStatus status = new PidStatus(20, 0, 0, 0.6, 0, 0, 0);
    private Pid pid = new Pid(status);

    public void setCommand(NewGunCommandAvailable event) {
        timer.reset();
        this.command = event.getData();
    }

    private GUN_COMMAND command = EAT;
    private boolean isAimHi = true;
    private void setAimCommand(NewAimCommandAvaliable event){
        isAimHi = event.getData();
    }

    private double gunVel = 0;

    private BallColor colorR = BallColor.NONE;
    private BallColor colorC = BallColor.NONE;
    private BallColor colorL = BallColor.NONE;

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

        sensorL = DevicePool.getInstance().sensorL;
        sensorC = DevicePool.getInstance().sensorC;
        sensorR = DevicePool.getInstance().sensorR;

    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(NewGunCommandAvailable.class, this::setCommand);
        EventBus.getInstance().subscribe(NewAimCommandAvaliable.class, this::setAimCommand);
    }

    private double delay = 0.5;
    private ElapsedTime timer = new ElapsedTime();

    public void lateUpdate() {
      //      colorDetect();
        Telemetry.getInstance().add("gun state",command.toString());
        Telemetry.getInstance().add("in right  gun",colorR.toString());
        Telemetry.getInstance().add("in center gun",colorC.toString());
        Telemetry.getInstance().add("in left   gun",colorL.toString());

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
                gunVel = 1200;
                right.setPos(command.right);
                left.setPos(command.left);
                center.setPos(command.center);
                wall.setPos(command.wall);
                timer.reset();
                break;
            case EAT:
                gunVel = 1100;
                timer.reset();
                right.setPos(command.right);
                left.setPos(command.left);
                center.setPos(command.center);
                wall.setPos(command.wall);

                if(colorL != BallColor.NONE && colorC != BallColor.NONE && colorR != BallColor.NONE ){
                    //          command = TARGET;
                }
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

    private void colorDetect(){
        RgbColorVector l = sensorL.getVector().findNearest(BallColor.PURPLE.color,BallColor.GREEN.color,BallColor.NONE.color);
        RgbColorVector c = sensorC.getVector().findNearest(BallColor.PURPLE.color,BallColor.GREEN.color,BallColor.NONE.color);
        RgbColorVector r = sensorR.getVector().findNearest(BallColor.PURPLE.color,BallColor.GREEN.color,BallColor.NONE.color);

        if(l == BallColor.GREEN.color){
            colorL = BallColor.GREEN;
        }else if(l == BallColor.PURPLE.color){
            colorL = BallColor.PURPLE;
        }else{
            colorL = BallColor.NONE;
        }

        if(c == BallColor.GREEN.color){
            colorC = BallColor.GREEN;
        }else if(c == BallColor.PURPLE.color){
            colorC = BallColor.PURPLE;
        }else{
            colorC = BallColor.NONE;
        }

        if(r == BallColor.GREEN.color){
            colorR = BallColor.GREEN;
        }else if(r == BallColor.PURPLE.color){
            colorR = BallColor.PURPLE;
        }else{
            colorR = BallColor.NONE;
        }

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
