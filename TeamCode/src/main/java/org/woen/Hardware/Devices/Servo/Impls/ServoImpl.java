package org.woen.Hardware.Devices.Servo.Impls;

import com.qualcomm.robotcore.hardware.Servo;

import org.woen.Telemetry.ConfigurableVariables.Provider;

public class ServoImpl implements org.woen.Hardware.Devices.Servo.Inter.Servo {

    Servo servo;

    Provider<Double> target;

    public ServoImpl(Servo servo, Provider<Double> target){
        this.servo = servo;
        this.target = target;
    }

    @Override
   public boolean isItTarget() {
        return target.get() == servo.getPosition();
    }
}
