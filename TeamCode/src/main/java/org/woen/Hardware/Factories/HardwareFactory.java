package org.woen.Hardware.Factories;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.woen.Hardware.Devices.Motor.Impl.DcMotorImpl;
import org.woen.Hardware.Devices.Motor.Impl.DcMotorMok;
import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.Hardware.Devices.Odometers.Inter.Odometer;
import org.woen.Hardware.Devices.Odometers.Impl.OdometerImpl;
import org.woen.Hardware.Devices.Odometers.Impl.OdometerMoc;
import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.Hardware.Devices.VoltageSensor.RevVoltageSensor;
import org.woen.Hardware.Devices.VoltageSensor.RevVoltageSensorFake;
import org.woen.Hardware.Devices.VoltageSensor.RevVoltageSensorImpl;
import org.woen.Telemetry.ConfigurableVariables.Provider;

public class HardwareFactory {

    private final HardwareMap hardwareMap;

    private final DeviceActivationConfig config;

    public HardwareFactory(HardwareMap hardwareMap, DeviceActivationConfig serviceActivation) {
        this.hardwareMap = hardwareMap;
        this.config = serviceActivation;
    }

    public Motor createDcMotor(String name, Provider<Double> pos, Provider<Double> vol){
        if(config.motors.get()){
            return new DcMotorImpl(hardwareMap.get(DcMotorEx.class, name));
        }
        else{
            return new DcMotorMok(pos, vol);
        }
    }

    public Odometer createOdometer(String name, Provider<Double> cord, Provider<Double> vel){
        if(config.odometers.get()){
            return new OdometerImpl(hardwareMap.get(DcMotorEx.class, name));
        }
        else{
            return new OdometerMoc(cord, vel);
        }
    }

    public RevVoltageSensor createVoltageSensor(){
        if(config.rev.get()){
            return new RevVoltageSensorImpl(hardwareMap.voltageSensor.get("Control Hub"));
        }else {
            return new RevVoltageSensorFake();
        }
    }

    public IMU createIMU(String name){
        return hardwareMap.get(IMU.class,name );
    }

}
