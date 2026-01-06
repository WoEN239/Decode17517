package org.woen.OpModes.Test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.OpModes.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.Camera.Enums.MOTIF;
import org.woen.RobotModule.Modules.Camera.Events.NewTargetMotifEvent;
import org.woen.Telemetry.Telemetry;

@Disabled
@TeleOp(name = "camera_test")
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

    @Override
    protected void loopRun() {
        Telemetry.getInstance().add("motif",motif.toString());
    }

    private MOTIF motif = MOTIF.PGP;
    private void setMotif(NewTargetMotifEvent event){
        this.motif = event.getData();
    }
}

