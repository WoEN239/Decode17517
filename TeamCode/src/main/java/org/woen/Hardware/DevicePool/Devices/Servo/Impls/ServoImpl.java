package org.woen.Hardware.DevicePool.Devices.Servo.Impls;


import com.qualcomm.robotcore.hardware.Servo;

import org.woen.Hardware.DevicePool.Devices.Servo.Interface.ServoMotor;

public class ServoImpl implements ServoMotor {

    private final Servo servo;
    public ServoImpl(Servo servo){
        this.servo = servo;
    }

    @Override
    public void setPos(double pos) {
        servo.setPosition(pos);
    }
}
