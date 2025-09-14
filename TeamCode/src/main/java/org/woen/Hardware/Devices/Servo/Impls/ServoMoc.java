package org.woen.Hardware.Devices.Servo.Impls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woen.Hardware.Devices.Servo.Inter.Servo;
import org.woen.Hardware.Devices.Servo.ServoMotion;
import org.woen.Telemetry.ConfigurableVariables.Provider;

public class ServoMoc implements Servo {
    Provider<Double> maxVel;
    Provider<Double> target;
    Provider<Double> accel;
    Provider<Double> t;

    ServoMotion servoMotion;


    public ServoMoc(Provider<Double> maxVel, Provider<Double> target, Provider<Double> accel,  Provider<Double> t){
        this.maxVel = maxVel;
        this.target = target;
        this.accel = accel;
        this.t = t;
        servoMotion = new ServoMotion(accel.get(), maxVel.get(), target.get());
    }

    @Override
    public boolean isItTarget() {
        if(servoMotion.getPos(t.get()) == target.get())
            return true;
        else
            return false;
    }
}
