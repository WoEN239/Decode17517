package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class FlywheelTest extends OpMode {
    @Override
    public void init() {
        banan = hardwareMap.get(Servo.class,"banan_c");
    }
    Servo banan;

    ElapsedTime deltaTime = new ElapsedTime();
    @Override
    public void loop() {
        FtcDashboard.getInstance().getTelemetry().addData("herz",1d/(deltaTime.seconds()));
        deltaTime.reset();
        FtcDashboard.getInstance().getTelemetry().update();

        banan.setPosition(0.5);
    }
}
