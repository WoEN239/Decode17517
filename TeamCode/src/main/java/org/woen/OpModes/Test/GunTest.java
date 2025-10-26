package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.OpModes.Main.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.Util.Pid.Pid;
import org.woen.Util.Pid.PidStatus;


@TeleOp
public class GunTest extends BaseOpMode {
    Motor motor;
    public static PidStatus status = new PidStatus(0,0,0,0,0,0,0);
    Pid pid = new Pid(status);
    public static double target = 0;
    @Override
    protected void initConfig(){
        DeviceActivationConfig devConfig = DeviceActivationConfig.getAllOn();
        devConfig.servos.set(false);
        deviceActivationConfig = devConfig;

        modulesActivationConfig = ModulesActivateConfig.getAllOff();
    }

    @Override
    protected void loopRun() {

        pid.setTarget(target);
        pid.setPos(DevicePool.getInstance().motorRF.getVel());
        pid.update();
        DevicePool.getInstance().motorRF.setPower(pid.getU());
    }
}
