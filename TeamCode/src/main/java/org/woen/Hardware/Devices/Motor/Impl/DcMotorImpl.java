package org.woen.Hardware.Devices.Motor.Impl;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.Hardware.Devices.Motor.Interface.Motor;

public class DcMotorImpl implements Motor {
    private final DcMotorEx dcMotorEx;

    private int dir = 1;

    public DcMotorImpl(DcMotorEx motor) {
        this.dcMotorEx = motor;
    }

    @Override
    public double getPosition() {
        return dir*dcMotorEx.getCurrentPosition();
    }

    @Override
    public void setPower(double power) {
        dcMotorEx.setPower(dir*power);
    }

    @Override
    public void setDir(int dir) {
        this.dir = dir;
    }

    @Override
    public void setDirection(DcMotorSimple.Direction direction) {
        dcMotorEx.setDirection(direction);
    }

    @Override
    public double getVel() {
        return dir*dcMotorEx.getVelocity();
    }

    @Override
    public double getCurrent() {
        return dcMotorEx.getCurrent(CurrentUnit.AMPS);
    }

    @Override
    public void reset() {
        dcMotorEx.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        dcMotorEx.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior m) {
        dcMotorEx.setZeroPowerBehavior(m);
    }

}
