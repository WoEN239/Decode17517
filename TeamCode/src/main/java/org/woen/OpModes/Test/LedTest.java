package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.RobotModule.Modules.Gun.Config.GunServoPositions;
import org.woen.Util.Color.LedDriver;
@Config
@TeleOp(group = "test")
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

        DcMotor leftMotor = hardwareMap.get(DcMotor.class, "motorL");

        DcMotor rightMotor = hardwareMap.get(DcMotor.class, "motorR");

        Servo ptoL = hardwareMap.get(Servo.class, "ptoL");
        Servo ptoR = hardwareMap.get(Servo.class, "ptoR");

        while(opModeIsActive()){

             if(gamepad1.right_bumper){
                 red = 0;
                blue = green = 1;
             }
             if(gamepad1.left_bumper){
                red = green = blue = 0;
             }

             if(gamepad1.dpadUpWasPressed()){
                ptoL.setPosition(GunServoPositions.ptoLClose);
                ptoR.setPosition(GunServoPositions.ptoRClose);
             }

             if(gamepad1.dpadUpWasPressed()){
                leftMotor.setPower(1);
               rightMotor.setPower(1);
             }

             if(gamepad1.dpadDownWasPressed()){
                leftMotor.setPower(0);
                rightMotor.setPower(0);
             }

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
