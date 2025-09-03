package org.woen.Hardware.Factories;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.woen.Hardware.Motor.Impl.DcMotorImpl;
import org.woen.Hardware.Motor.Impl.DcMotorMok;
import org.woen.Hardware.Motor.Inter.Motor;
import org.woen.Hardware.Odometers.Impl.Odometer;
import org.woen.Hardware.Odometers.Inter.OdometerImpl;
import org.woen.Hardware.Odometers.Inter.OdometerMoc;
import org.woen.Hardware.ServiseActivationConfig.ServiceActivationConfig;

public class HardwareFactory {

    private final HardwareMap hardwareMap;

    private final ServiceActivationConfig serviceActivationConfig;


    public HardwareFactory(HardwareMap hardwareMap, ServiceActivationConfig serviceActivationConfig) {
        this.hardwareMap = hardwareMap;
        this.serviceActivationConfig = serviceActivationConfig;
    }

    public Motor createDcMotor(String name,Double pos,Double vol){
        if(serviceActivationConfig.isMotorActive){
            return new DcMotorImpl(hardwareMap.get(DcMotorEx.class, name));
        }
        else{
            return new DcMotorMok(pos, vol);
        }
    }

    public Odometer createOdometer(String name, Double cord, Double vel){
        if(serviceActivationConfig.isOdometersActive){
            return new OdometerImpl(hardwareMap.get(DcMotorEx.class, name));
        }
        else{
            return new OdometerMoc(cord, vel);
        }
    }

    public IMU createIMU(){
        return hardwareMap.get(IMU.class, "imu");
    }

}
