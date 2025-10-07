package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.woen.Hardware.Devices.Servo.Impls.ServoImpl;

@TeleOp( name = "servoImplTest")
@Config
public class ServoImplTest extends LinearOpMode {


    public static double accel = 100;

    public static double maxVel = 1;

    public static double pos = 1;

    private double oldPos = 1;


    @Override
    public void runOpMode(){
        ServoImpl servo = new ServoImpl(hardwareMap.get(Servo.class, "servo"), accel, maxVel);

        waitForStart();

        while (opModeIsActive()) {
            servo.setPos(pos, oldPos);

            if (servo.isItTarget()) {
                pos = oldPos;
            }
        }
    }
}
