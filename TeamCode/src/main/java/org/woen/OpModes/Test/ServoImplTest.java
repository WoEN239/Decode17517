package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.woen.Hardware.Devices.Servo.Impls.ServoImpl;

@TeleOp
@Config
public class ServoImplTest extends LinearOpMode {


    public static double accel = 1;

    public static double maxVel = 0.1;

    public static double pos = 0;

    public static double oldPos = 1;



    @Override
    public void runOpMode(){
        ServoImpl servo = new ServoImpl(hardwareMap.get(Servo.class, "servo"), accel, maxVel);

        waitForStart();

        servo.resetTimer();

        while (opModeIsActive()) {
            servo.setPos(pos, oldPos);
            FtcDashboard.getInstance().getTelemetry().addData("isItTarget", servo.isItTarget());
            FtcDashboard.getInstance().getTelemetry().addData("pos", servo.getPos());
            FtcDashboard.getInstance().getTelemetry().update();
        }
    }
}
