package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.OpModes.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.Camera.Enums.MOTIF;
import org.woen.RobotModule.Modules.Camera.Events.NewTargetMotifEvent;
import org.woen.Telemetry.Telemetry;


@TeleOp(name = "camera_test",group = "test")
@Config
public class CameraTest extends BaseOpMode {
    @Override
    protected void initConfig(){
        DeviceActivationConfig deviceConfig = new DeviceActivationConfig();
        deviceConfig.servos.set(false);
        deviceConfig.motors.set(false);
        deviceConfig.odometers.set(false);
        deviceActivationConfig = deviceConfig;

        ModulesActivateConfig moduleConfig = ModulesActivateConfig.getAllOff();
        moduleConfig.camera.set(true);
        modulesActivationConfig = moduleConfig;

        EventBus.getInstance().subscribe(NewTargetMotifEvent.class,this::setMotif);
    }


    public static double lightPower = 0.6;


    @Override
    protected void loopRun() {
        Telemetry.getInstance().add("motif",motif.toString());
        DevicePool.getInstance().light.setPower(lightPower);
    }

    private MOTIF motif = MOTIF.PGP;
    private void setMotif(NewTargetMotifEvent event){
        this.motif = event.getData();
    }
}

