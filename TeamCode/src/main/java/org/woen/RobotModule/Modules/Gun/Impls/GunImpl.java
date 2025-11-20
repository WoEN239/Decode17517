package org.woen.RobotModule.Modules.Gun.Impls;

import static org.woen.Config.ControlSystemConstant.gunConfig;
import static org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND.*;
import static org.woen.RobotModule.Modules.Gun.Config.GunServoPositions.*;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.Hardware.Devices.Servo.Interface.ServoMotor;
import org.woen.RobotModule.Modules.Camera.MOTIF;
import org.woen.RobotModule.Modules.Camera.NewMotifEvent;
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

    private PidStatus status = new PidStatus(1, 0, 0, 0.0004, 0, 0, 0,50);
    private final Pid pid = new Pid(status);

    public void setCommand(NewGunCommandAvailable event) {
        timer.reset();
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
        EventBus.getInstance().subscribe(NewMotifEvent.class, this::setMotif);
    }

    private double delay = gunConfig.delay;
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
                    EventBus.getInstance().invoke(new NewGunCommandAvailable(EAT));
                }
                break;
            case SHOT_CENTER:
            case SHOT_LEFT:
            case SHOT_RIGHT:
            case FULL_FIRE:
                wall.setPos(command.wall);

                if(timer.seconds()>delay/6.0) {
                    right.setPos(command.right);
                    left.setPos(command.left);
                    center.setPos(command.center);
                }

                if (timer.seconds() > 3 * delay/6.0) {
                    EventBus.getInstance().invoke(new NewGunCommandAvailable(EAT));;
                }
                break;
            case TARGET:
                gunVel = gunConfig.shootVel;
                right.setPos(command.right);
                left.setPos(command.left);
                center.setPos(command.center);
                wall.setPos(command.wall);
                timer.reset();
                break;
            case EAT:
                gunVel = gunConfig.eatVel;
                timer.reset();
                right.setPos(command.right);
                left.setPos(command.left);
                center.setPos(command.center);
                wall.setPos(command.wall);
                break;
            case REVERSE:
                gunVel = -200;
                right.setPos(command.right);
                left.setPos(command.left);
                center.setPos(command.center);
                wall.setPos(command.wall);
                break;
            case PATTERN_FIRE:
                gunVel = gunConfig.patternShootVel;
                wall.setPos(command.wall);

                switch (motif){
                    case GPP:
                        if(timer.seconds() > delay) {
                            right.setPos(command.right);
                        }
                        if(timer.seconds() > 2*delay){
                            left.setPos(command.left);
                        }
                        if(timer.seconds()>3*delay){
                            center.setPos(command.center);
                        }
                        break;
                    case PGP:
                        if(timer.seconds()>delay){
                            center.setPos(command.center);
                        }
                        if(timer.seconds() > 2*delay){
                            right.setPos(command.right);
                        }
                        if(timer.seconds()>3*delay){
                            left.setPos(command.left);
                        }
                        break;
                    case PPG:
                        if(timer.seconds()>delay){
                            center.setPos(command.center);
                        }
                        if(timer.seconds() > 2*delay){
                            left.setPos(command.left);
                        }
                        if(timer.seconds()>3*delay){
                            right.setPos(command.right);
                        }
                        break;

                }

                if (timer.seconds() > 4 * delay) {
                    EventBus.getInstance().invoke(new NewGunCommandAvailable(EAT));;
                }
                break;
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