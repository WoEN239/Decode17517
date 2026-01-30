package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.woen.Util.Color.LedDriver;
@Config
@TeleOp
public class LedTest extends LinearOpMode {

    public static double red = 0;

    public static double green = 0;

    public static double blue = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();


        LedDriver ledDriver2 = new LedDriver(hardwareMap, "rgb2", LedDriver.SignalPin.MINUS);
        LedDriver ledDriver3 = new LedDriver(hardwareMap, "rgb3", LedDriver.SignalPin.MINUS);
        LedDriver ledDriver4 = new LedDriver(hardwareMap, "rgb4", LedDriver.SignalPin.MINUS);

        while(opModeIsActive()){
            ledDriver2.setPower(red);
            ledDriver3.setPower(green);
            ledDriver4.setPower(blue);
            telemetry.addData("led2", ledDriver2.getPower());
            telemetry.addData("led3", ledDriver3.getPower());
            telemetry.addData("led4", ledDriver4.getPower());
            telemetry.update();
        }
    }
}
