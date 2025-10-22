package org.woen.RobotModule.Modules.Gun.Impls;

import static org.woen.RobotModule.Modules.Gun.GUN_COMMAND.*;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.Servo.FeedbackableServo;
import org.woen.RobotModule.Modules.Gun.Arcitecture.RegisterNewGunCommandListener;
import org.woen.RobotModule.Modules.Gun.GUN_COMMAND;
import org.woen.RobotModule.Modules.Gun.Interface.Gun;

public class GunImpl implements Gun {
    private FeedbackableServo right;
    private FeedbackableServo center;
    private FeedbackableServo left;
    private FeedbackableServo wall;

    public void setCommand(GUN_COMMAND command) {
        this.command = command;
    }

    private GUN_COMMAND command = EAT;

    @Override
    public void init(){
        right  = new FeedbackableServo(DevicePool.getInstance().right) ;
        center = new FeedbackableServo(DevicePool.getInstance().center);
        left   = new FeedbackableServo(DevicePool.getInstance().left)  ;
        wall   = new FeedbackableServo(DevicePool.getInstance().wall)  ;
        EventBus.getListenersRegistration().invoke(new RegisterNewGunCommandListener(this::setCommand));
    }

    public void lateUpdate(){
        right.setTarget(command.right);
        left.setTarget(command.left);
        center.setTarget(command.center);

        wall.setTarget(command.wall);
    }

}
