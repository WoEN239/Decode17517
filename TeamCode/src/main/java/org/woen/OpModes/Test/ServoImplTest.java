package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Hardware.Devices.Servo.Impls.ServoImpl;
import org.woen.Hardware.Devices.Servo.ServoMotion;

@TeleOp
@Config
public class ServoImplTest extends LinearOpMode {


    public static double accel = 1;

    public static double maxVel = 0.1;

    public static double pos = 1;

    public static double oldPos = 0;

    ServoMotion servoMotion = new ServoMotion(accel, maxVel, pos, oldPos);

    double accelTime = maxVel / accel;
    double accelLength = (accelTime * accelTime * accel) / 2.0;
    double lengthWithoutAccel = (pos - oldPos) - accelLength * 2;



    @Override
    public void runOpMode(){
        ServoImpl servo = new ServoImpl(hardwareMap.get(Servo.class, "servo"), accel, maxVel);

        waitForStart();

        servo.resetTimer();

        ElapsedTime time1 = new ElapsedTime();

        while (opModeIsActive()) {
            Servo servo1 = hardwareMap.get(Servo.class, "servo");
            FtcDashboard.getInstance().getTelemetry().addData("isItTarget", servo.isItTarget());
            FtcDashboard.getInstance().getTelemetry().addData("t1", servoMotion.t1);
            FtcDashboard.getInstance().getTelemetry().addData("t2", ((pos - oldPos) - 2 * accelTime) / maxVel);
            FtcDashboard.getInstance().getTelemetry().addData("t3", servoMotion.t3);
            FtcDashboard.getInstance().getTelemetry().addData("if", (pos - oldPos) > accelLength * 2 + lengthWithoutAccel);


            FtcDashboard.getInstance().getTelemetry().addData("accelTime", accelTime);
            FtcDashboard.getInstance().getTelemetry().addData("accelLength", accelLength);
            FtcDashboard.getInstance().getTelemetry().addData("lengthWithoutAccel", lengthWithoutAccel);

            servo1.setPosition(servoMotion.getPos(time1.milliseconds()));
            FtcDashboard.getInstance().getTelemetry().update();
        }
    }
}
