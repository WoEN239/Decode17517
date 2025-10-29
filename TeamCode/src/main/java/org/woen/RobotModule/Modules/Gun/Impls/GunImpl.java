package org.woen.RobotModule.Modules.Gun.Impls;

import static org.woen.RobotModule.Modules.Gun.GUN_COMMAND.*;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.MatchData;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.Hardware.Devices.Servo.Interface.ServoMotor;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.GUN_COMMAND;
import org.woen.RobotModule.Modules.Gun.Interface.Gun;
import org.woen.Util.Pid.Pid;
import org.woen.Util.Pid.PidStatus;

public class GunImpl implements Gun {
    private ServoMotor right;
    private ServoMotor center;
    private ServoMotor left;
    private ServoMotor wall;

    private Motor gunL;
    private Motor gunR;

    private PidStatus status = new PidStatus(10, 0, 0, 0.57, 0, 0, 0);
    private Pid pid = new Pid(status);

    public void setCommand(NewGunCommandAvailable event) {
        this.command = event.getData();
    }

    private GUN_COMMAND command = EAT;

    private double gunVel = 1300;

    @Override
    public void init() {
        right  = DevicePool.getInstance().shotR;
        center = DevicePool.getInstance().shotC;
        left   = DevicePool.getInstance().shotL;
        wall   = DevicePool.getInstance().wall;

        gunL = DevicePool.getInstance().gunL;
        gunR = DevicePool.getInstance().gunR;

    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(NewGunCommandAvailable.class, this::setCommand);
    }

    private double delay = 0.2;
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
                right.setPos(command.right);
                left.setPos(command.left);
                center.setPos(command.center);
                wall.setPos(command.wall);
                if (timer.seconds() > 2 * delay) {
                    command = EAT;
                }
                break;
            case TARGET:
                gunVel = 1300;
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
        pid.setTarget(gunVel);
        pid.setPos(gunL.getVel());
        pid.update();

        gunL.setPower(pid.getU());
        gunR.setPower(pid.getU());
    }

}
