package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.logging.Handler;

@Config
@TeleOp
public class PTOTest extends LinearOpMode {

    public static double power = 0.5;
    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor left = hardwareMap.get(DcMotor.class, "motorL");
        DcMotor right = hardwareMap.get(DcMotor.class,  "motorR");

        Servo ptoR = hardwareMap.get(Servo.class, "ptoR");
        Servo ptoL = hardwareMap.get(Servo.class, "ptoL");


        waitForStart();


        while (opModeIsActive()){
            left.setPower(-power);
            right.setPower(power);
        }
    }
}
