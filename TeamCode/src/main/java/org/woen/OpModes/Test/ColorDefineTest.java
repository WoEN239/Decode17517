package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Util.Color.ColorDetect.Impls.ColorDetection;

@TeleOp
public class ColorDefineTest extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        DeviceActivationConfig devConfig = DeviceActivationConfig.getAllOn();
        devConfig.colorSensor.set(true);


        //  ColorDetection colorDetection = new ColorDetection(sensor);

        waitForStart();




        while (opModeIsActive()) {

            ColorDetection colorDetectionR = new ColorDetection(DevicePool.getInstance().sensorR);


            ColorDetection colorDetectionC = new ColorDetection(DevicePool.getInstance().sensorC);


            ColorDetection colorDetectionL = new ColorDetection(DevicePool.getInstance().sensorL);

            FtcDashboard.getInstance().getTelemetry().addData("r color", colorDetectionR);

            FtcDashboard.getInstance().getTelemetry().addData("c color", colorDetectionC);

            FtcDashboard.getInstance().getTelemetry().addData("l color", colorDetectionL);

            FtcDashboard.getInstance().getTelemetry().update();
        }


    }
}
