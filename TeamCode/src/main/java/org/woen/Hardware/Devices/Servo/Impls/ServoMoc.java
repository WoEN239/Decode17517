package org.woen.Hardware.Devices.Servo.Impls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woen.Hardware.Devices.Servo.Inter.Servo;
import org.woen.Hardware.Devices.Servo.ServoMotion;
import org.woen.Telemetry.ConfigurableVariables.Provider;

public class ServoMoc implements Servo {

    Provider<Boolean> isItTarget;


    public ServoMoc( Provider<Boolean> isItTarget){
        this.isItTarget = isItTarget;
    }

    @Override
    public void setPos(double pos, double startPos){

    }


    @Override
    public boolean isItTarget() {
      return isItTarget.get();
    }
}
