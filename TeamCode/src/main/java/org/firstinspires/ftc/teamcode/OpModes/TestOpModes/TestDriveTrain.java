package org.firstinspires.ftc.teamcode.OpModes.TestOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;


@TeleOp
public class TestDriveTrain extends LinearOpMode {

    DcMotorEx motor_lf;
    DcMotorEx motor_rf;
    DcMotorEx motor_rb;
    DcMotorEx motor_lb;
    @Override
    public void runOpMode() throws InterruptedException {
        motor_lf = hardwareMap.get(DcMotorEx.class,"motor_lf");
        motor_rf = hardwareMap.get(DcMotorEx.class,"motor_rf");
        motor_rb = hardwareMap.get(DcMotorEx.class,"motor_rb");
        motor_lb = hardwareMap.get(DcMotorEx.class,"motor_lb");

        double lastTime = 0;
        waitForStart();
        while (opModeIsActive()){
            double h = gamepad1.left_stick_y;

            motor_lf.setPower(h);
            motor_rf.setPower(h);
            motor_rb.setPower(-h);
            motor_lb.setPower(-h);

            FtcDashboard.getInstance().getTelemetry().addData("herz",1d/( (System.nanoTime() - lastTime)/1e9 ) );
            lastTime = System.nanoTime();
            FtcDashboard.getInstance().getTelemetry().update();
        }


    }
}
