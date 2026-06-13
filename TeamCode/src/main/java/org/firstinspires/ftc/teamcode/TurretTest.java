package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TurretTest extends OpMode {
    @Override
    public void init() {
        turret1 = hardwareMap.get(Servo.class,"turret1");
        turret2 = hardwareMap.get(Servo.class,"turret2");

        ((PwmControl)turret1).setPwmRange(new PwmControl.PwmRange(500.0, 2500.0));
        ((PwmControl)turret2).setPwmRange(new PwmControl.PwmRange(500.0, 2500.0));
    }
    Servo turret1;
    Servo turret2;
    @Override
    public void loop() {
        turret1.setPosition(1);
        turret2.setPosition(0);
    }
}
