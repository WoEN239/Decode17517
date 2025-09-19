package org.woen.OpModes.ConfigBuild;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.OpModes.Main.BaseOpMode;

@TeleOp(name = "build device")
public class BulidDeviceConfigOpMode extends BaseOpMode {
    @Override
    protected void loopRun() {

    }
    DeviceActivationConfig config = DeviceActivationConfig.getManual();
    @Override
    public void runOpMode() {
        waitForStart();
        while (opModeIsActive()){
            deviceActivationConfig = config;
        }
    }

}
