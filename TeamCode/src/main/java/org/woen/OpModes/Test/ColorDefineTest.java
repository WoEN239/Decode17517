package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.Hardware.Factory.DeviceActivationConfig;


public class ColorDefineTest extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        DeviceActivationConfig devConfig = DeviceActivationConfig.getAllOn();


        //  ColorDetection colorDetection = new ColorDetection(sensor);

        waitForStart();




        while (opModeIsActive()) {


            FtcDashboard.getInstance().getTelemetry().update();
        }


    }
}
