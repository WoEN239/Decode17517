package org.woen.OpModes.ConfigBuild;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.OpModes.BaseOpMode;

@TeleOp(name = "build device",group = "test")
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
