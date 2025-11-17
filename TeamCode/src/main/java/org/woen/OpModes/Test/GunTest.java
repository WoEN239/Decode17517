package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.OpModes.Main.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.Telemetry.ConfigurableVariables.Provider;
import org.woen.Util.Pid.Pid;
import org.woen.Util.Pid.PidStatus;

@Config
@TeleOp(name = "gun_test")
public class GunTest extends BaseOpMode {
    Motor motor;
    public static PidStatus status = new PidStatus(0,0,0,0,0,0,0,0);
    Pid pid = new Pid(status);
    public static double target = 0;
    public Provider<Double> border = new Provider<>(0.5);

    @Override
    protected void initConfig(){
        DeviceActivationConfig devConfig = DeviceActivationConfig.getAllOn();
        devConfig.servos.set(true);
        deviceActivationConfig = devConfig;

        modulesActivationConfig = ModulesActivateConfig.getAllOff();
        FtcDashboard.getInstance().addConfigVariable("Gun config","border",border);
    }

    @Override
    protected void loopRun() {
        DevicePool.getInstance().borderL.setPos(1-border.get());
        DevicePool.getInstance().borderR.setPos(border.get());

        pid.setTarget(target);
        pid.setPos(DevicePool.getInstance().gunR.getVel());
        pid.update();
        DevicePool.getInstance().gunL.setPower(pid.getU());
        DevicePool.getInstance().gunR.setPower(pid.getU());
    }
}
