package org.woen.Util.Color;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;

public class LedDriver {
    public LedDriver(Servo port) {
        _port = port;
    }

    public enum SignalPin{
        MINUS,
        PLUS
    }

    private final Servo _port;

    private double _maxBorder;
    private double _minBorder;

    public LedDriver(HardwareMap hardwareMap, String name, SignalPin pin){
        this(hardwareMap, name, 0.999999, 0.650051, pin);
    }

    public LedDriver(HardwareMap hardwareMap, String name, double maxBorder, double minBorder, SignalPin pin){
        _minBorder = minBorder;
        _maxBorder = maxBorder;

        _port = hardwareMap.get(Servo.class, name);

        ((PwmControl)_port).setPwmRange(new PwmControl.PwmRange(0.0, 20000.0, 7000.0));

        if(pin == SignalPin.MINUS)
            _port.setDirection(Servo.Direction.REVERSE);
        else
            _port.setDirection(Servo.Direction.FORWARD);
    }

    public void setPower(double power){
        _port.setPosition(Math.max(0.0, Math.min(power, 1.0)) * (_maxBorder - _minBorder) + _minBorder);
    }

    public double getPower(){
        return (_port.getPosition() - _minBorder) / (_maxBorder - _minBorder);
    }
}
