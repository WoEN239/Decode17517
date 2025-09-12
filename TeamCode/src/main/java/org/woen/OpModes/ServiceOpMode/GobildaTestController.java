package org.woen.OpModes.ServiceOpMode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Util.Pid.Pid;
import org.woen.Util.Pid.PidStatus;

@TeleOp
@Config
public class GobildaTestController extends LinearOpMode {
    public static PidStatus pidStatus = new PidStatus(0,0,0,0,0,0,0);
    public static double target = 0;
    private ElapsedTime timer = new ElapsedTime();
    private Pid pid = new Pid(pidStatus);
    {
        pid.isAngle = true;
        pid.isDAccessible = true;
    }
    DcMotorEx motor;
    VoltageSensor battery;
    double time = 0;
    double velOld = 0;
    @Override
    public void runOpMode() {
        motor = this.hardwareMap.get(DcMotorEx.class,"motor");
        battery  = hardwareMap.voltageSensor.get("Control Hub");
        waitForStart();
        while (opModeIsActive()){
            double pos = motor.getCurrentPosition();
            double vel = motor.getVelocity();

            //if(timer.seconds()>0.020){
                pid.setPos(pos);
                pid.setPosD(vel);
                pid.setTarget(target);
                pid.setTargetD(0);
                pid.update();
              //  motor.setPower(pid.getU());
                motor.setPower(target/battery.getVoltage());
                time = timer.seconds();
                timer.reset();
            //}

            FtcDashboard.getInstance().getTelemetry().addData("time", time);
            FtcDashboard.getInstance().getTelemetry().addData("vel", vel);
            FtcDashboard.getInstance().getTelemetry().addData("acc", (vel-velOld)/time);
            FtcDashboard.getInstance().getTelemetry().addData("pos", pos);
            FtcDashboard.getInstance().getTelemetry().addData("target", target);
            FtcDashboard.getInstance().getTelemetry().update();
            velOld = vel;
        }
    }
}
