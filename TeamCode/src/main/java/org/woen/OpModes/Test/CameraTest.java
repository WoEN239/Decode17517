package org.woen.OpModes.Test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.OpModes.Main.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.Camera.MOTIF;
import org.woen.RobotModule.Modules.Camera.NewMotifEvent;
import org.woen.Telemetry.Telemetry;

@TeleOp(name = "camera_test")
public class CameraTest extends BaseOpMode {
    @Override
    protected void initConfig(){
        DeviceActivationConfig deviceConfig = new DeviceActivationConfig();
        deviceConfig.colorSensor.set(false);
        deviceConfig.servos.set(false);
        deviceConfig.motors.set(false);
        deviceConfig.odometers.set(false);
        deviceActivationConfig = deviceConfig;

        ModulesActivateConfig moduleConfig = ModulesActivateConfig.getAllOff();
        moduleConfig.camera.set(true);
        modulesActivationConfig = moduleConfig;

        EventBus.getInstance().subscribe(NewMotifEvent.class,this::setMotif);
    }

    @Override
    protected void loopRun() {
        Telemetry.getInstance().add("motif",motif.toString());
    }

    private MOTIF motif = MOTIF.PGP;
    private void setMotif(NewMotifEvent event){
        this.motif = event.getData();
    }
}

