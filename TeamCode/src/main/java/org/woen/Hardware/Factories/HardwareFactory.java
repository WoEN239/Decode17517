package org.woen.Hardware.Factories;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.woen.Hardware.Gyro.Impl.Gyro;
import org.woen.Hardware.Gyro.Inter.GyroImpl;
import org.woen.Hardware.Gyro.Inter.GyroMoc;
import org.woen.Hardware.Motor.Impl.DcMotorImpl;
import org.woen.Hardware.Motor.Impl.DcMotorMok;
import org.woen.Hardware.Motor.Inter.Motor;
import org.woen.Hardware.Odometers.Inter.Odometer;
import org.woen.Hardware.Odometers.Impl.OdometerImpl;
import org.woen.Hardware.Odometers.Impl.OdometerMoc;
import org.woen.Hardware.DeviceActivationConfig.DeviceActivationConfig;

public class HardwareFactory {

    private final HardwareMap hardwareMap;

    private final DeviceActivationConfig serviceActivation;


    public HardwareFactory(HardwareMap hardwareMap, DeviceActivationConfig serviceActivation) {
        this.hardwareMap = hardwareMap;
        this.serviceActivation = serviceActivation;
    }

    public Motor createDcMotor(String name,Double pos,Double vol){
        if(serviceActivation.motors.get()){
            return new DcMotorImpl(hardwareMap.get(DcMotorEx.class, name));
        }
        else{
            return new DcMotorMok(pos, vol);
        }
    }

    public Odometer createOdometer(String name, Double cord, Double vel){
        if(serviceActivation.odometers.get()){
            return new OdometerImpl(hardwareMap.get(DcMotorEx.class, name));
        }
        else{
            return new OdometerMoc(cord, vel);
        }
    }

    public Gyro createIMU(String name, double vel, Double yaw){
        if(serviceActivation.gyro.get()){
            return new GyroImpl(hardwareMap.get(IMU.class,name ));
        }
        else{
            return new GyroMoc(vel, yaw);
        }
    }

}
