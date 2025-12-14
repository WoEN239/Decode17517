package org.woen.Hardware.Factories;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.woen.Hardware.Devices.ColorSensor.Impl.ColorSensorImpl;
import org.woen.Hardware.Devices.ColorSensor.Impl.ColorSensorMok;
import org.woen.Hardware.Devices.ColorSensor.Interface.ColorSensor;
import org.woen.Hardware.Devices.Motor.Impl.DcMotorImpl;
import org.woen.Hardware.Devices.Motor.Impl.DcMotorMok;
import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.Hardware.Devices.Odometers.Impl.PinPointImpl;
import org.woen.Hardware.Devices.Odometers.Inter.Odometer;
import org.woen.Hardware.Devices.Odometers.Impl.OdometerImpl;
import org.woen.Hardware.Devices.Odometers.Impl.OdometerMoc;
import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.Hardware.Devices.Odometers.Inter.PinPoint;
import org.woen.Hardware.Devices.Servo.Impls.ServoImpl;
import org.woen.Hardware.Devices.Servo.Interface.ServoMotor;
import org.woen.Hardware.Devices.VoltageSensor.RevVoltageSensor;
import org.woen.Hardware.Devices.VoltageSensor.RevVoltageSensorFake;
import org.woen.Hardware.Devices.VoltageSensor.RevVoltageSensorImpl;
import org.woen.RobotModule.Modules.Camera.Camera;
import org.woen.RobotModule.Modules.Camera.CameraImpl;
import org.woen.Telemetry.ConfigurableVariables.Provider;
import org.woen.Util.Vectors.Pose;

public class HardwareFactory {

    private final HardwareMap hardwareMap;

    private final DeviceActivationConfig config;

    public HardwareFactory(HardwareMap hardwareMap, DeviceActivationConfig serviceActivation) {
        this.hardwareMap = hardwareMap;
        this.config = serviceActivation;
    }

    public PinPoint createPinPoint(String name){
        if(config.odometers.get()) {
            return new PinPointImpl(hardwareMap.get(GoBildaPinpointDriver.class, name));
        }else{
            return new PinPoint(){
                public Pose getPose() {return new Pose(0,0,0);}
                public Pose getVel()  {return new Pose(0,0,0);}
            };
        }
    }

    public Motor createDcMotor(String name, Provider<Double> pos, Provider<Double> vol){
        if(config.motors.get()){
            return new DcMotorImpl(hardwareMap.get(DcMotorEx.class, name));
        }
        else{
            return new DcMotorMok(pos, vol);
        }
    }


    public ServoMotor createServoMotor(String name){
        if(config.servos.get()){
            return new ServoImpl(hardwareMap.get(Servo.class, name));
        }
        else{
            return pos -> {};
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

    public ColorSensor createColorSensor(String name, Provider<Integer> red, Provider<Integer> green, Provider<Integer> blue){
        if (config.colorSensor.get())
            return new ColorSensorImpl(hardwareMap.get(AdafruitI2cColorSensor.class, name));
        else
            return new ColorSensorMok(red, green, blue);
    }

    public IMU createIMU(String name){
        return hardwareMap.get(IMU.class,name );
    }

}
