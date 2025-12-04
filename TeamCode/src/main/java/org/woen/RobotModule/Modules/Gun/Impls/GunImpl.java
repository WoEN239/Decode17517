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
    private ServoMotor shotR;
    private ServoMotor shotC;
    private ServoMotor shotL;

    private ServoMotor aimR;
    private ServoMotor aimC;
    private ServoMotor aimL;

    private Motor gun;
    private Motor brush;

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

    private double brushVel = 0;

    @Override
    public void init() {
        shotR = DevicePool.getInstance().shotR;
        shotC = DevicePool.getInstance().shotC;
        shotL = DevicePool.getInstance().shotL;

        gun = DevicePool.getInstance().gun;

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
                shotR.setPos(command.right);
                if (timer.seconds() > delay) {
                    shotC.setPos(command.center);
                }
                if (timer.seconds() > 2 * delay) {
                    shotL.setPos(command.left);
                }
                if (timer.seconds() > 3 * delay) {
                    EventBus.getInstance().invoke(new NewGunCommandAvailable(EAT));
                }
                break;
            case SHOT_CENTER:
            case SHOT_LEFT:
            case SHOT_RIGHT:
            case FULL_FIRE:

                if(timer.seconds()>delay/6.0) {
                    shotR.setPos(command.right);
                    shotL.setPos(command.left);
                    shotC.setPos(command.center);
                }

                if (timer.seconds() > 3 * delay/6.0) {
                    EventBus.getInstance().invoke(new NewGunCommandAvailable(EAT));;
                }
                break;
            case TARGET:
                brushVel = gunConfig.shootVel;
                shotR.setPos(command.right);
                shotL.setPos(command.left);
                shotC.setPos(command.center);
                timer.reset();
                break;
            case EAT:
                brushVel = gunConfig.eatVel;
                timer.reset();
                shotR.setPos(command.right);
                shotL.setPos(command.left);
                shotC.setPos(command.center);
                break;
            case REVERSE:
                brushVel = -200;
                shotR.setPos(command.right);
                shotL.setPos(command.left);
                shotC.setPos(command.center);
                break;
            case PATTERN_FIRE:
                brushVel = gunConfig.patternShootVel;

                switch (motif){
                    case GPP:
                        if(timer.seconds() > delay) {
                            shotR.setPos(command.right);
                        }
                        if(timer.seconds() > 2*delay){
                            shotL.setPos(command.left);
                        }
                        if(timer.seconds()>3*delay){
                            shotC.setPos(command.center);
                        }
                        break;
                    case PGP:
                        if(timer.seconds()>delay){
                            shotC.setPos(command.center);
                        }
                        if(timer.seconds() > 2*delay){
                            shotR.setPos(command.right);
                        }
                        if(timer.seconds()>3*delay){
                            shotL.setPos(command.left);
                        }
                        break;
                    case PPG:
                        if(timer.seconds()>delay){
                            shotC.setPos(command.center);
                        }
                        if(timer.seconds() > 2*delay){
                            shotL.setPos(command.left);
                        }
                        if(timer.seconds()>3*delay){
                            shotR.setPos(command.right);
                        }
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

        pid.setTarget(brushVel);
        pid.setPos(gun.getVel());
        pid.update();
        Telemetry.getInstance().add("gunVel", gun.getVel());
        gun.setPower(pid.getU());

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
}