package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.DevicePool.Devices.Servo.Impls.ServoWithFeedback;
import org.woen.OpModes.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.DriveTrain.ActivationConfig.DriveTrainActivationConfig;
import org.woen.RobotModule.Modules.Localizer.ActivationConfig.LocalizerActivationConfig;
import org.woen.Telemetry.ConfigurableVariables.Provider;
import org.woen.Telemetry.Telemetry;
@Disabled
@TeleOp(name = "servo_profile_test", group = "test")
public class ServoProfileTest extends BaseOpMode {
    @Override
    protected void initConfig(){
        DeviceActivationConfig deviceConfig = new DeviceActivationConfig();
        deviceConfig.motors.set(false);
        deviceConfig.servos.set(true);
        deviceConfig.odometers.set(false);
        deviceActivationConfig = deviceConfig;

        ModulesActivateConfig moduleConfig = new ModulesActivateConfig();
        moduleConfig.localizer = LocalizerActivationConfig.getAllOff();
        moduleConfig.driveTrain = DriveTrainActivationConfig.getAllOff();
        moduleConfig.gun.set(false);
        moduleConfig.camera.set(false);
        moduleConfig.autonomTaskManager.set(false);
        modulesActivationConfig = moduleConfig;
        FtcDashboard.getInstance().addConfigVariable("servo test","target",target);
    }

    ServoWithFeedback servo;
    @Override
    protected void initRun() {
         servo = new ServoWithFeedback(DevicePool.getInstance().aimR);
    }

    Provider<Double> target = new Provider<>(0.5);
    double oldTarget = 0.5;
    @Override
    protected void loopRun() {
        if(oldTarget!=target.get()){
            servo.setTarget(target.get());
        }
        Telemetry.getInstance().add("servo pos",servo.getPos());
        Telemetry.getInstance().add("servo vel",servo.getVel());
        oldTarget = target.get();
        servo.update();
    }
}
