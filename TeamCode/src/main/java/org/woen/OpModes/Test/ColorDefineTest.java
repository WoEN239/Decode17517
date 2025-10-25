package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.ColorSensor.Interface.ColorSensor;
import org.woen.RobotModule.Modules.IntakeAndShoot.Impls.ColorDetection;


@TeleOp
public class ColorDefineTest extends LinearOpMode {



    @Override
    public void runOpMode() throws InterruptedException {

        DevicePool devicePool = new DevicePool();
        AdafruitI2cColorSensor sensor = hardwareMap.get(AdafruitI2cColorSensor.class, "sensor");


        ColorDetection colorDetection = new ColorDetection(sensor);

        waitForStart();

        while(opModeIsActive()){
            FtcDashboard.getInstance().getTelemetry().addData("BALLS COLOR", colorDetection.def_color_easy());
            FtcDashboard.getInstance().getTelemetry().addData("red", sensor.red());
            FtcDashboard.getInstance().getTelemetry().addData("green", sensor.green());
            FtcDashboard.getInstance().getTelemetry().addData("blue", sensor.blue());
            FtcDashboard.getInstance().getTelemetry().update();

        }


    }
}
