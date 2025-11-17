package org.woen.Hardware.DevicePool;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.Hardware.Devices.ColorSensor.Interface.ColorSensor;
import org.woen.Hardware.Devices.Servo.Interface.ServoMotor;
import org.woen.Hardware.Devices.VoltageSensor.RevVoltageSensor;
import org.woen.Hardware.Factories.HardwareFactory;
import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.Hardware.Devices.Odometers.Inter.Odometer;
import org.woen.Telemetry.ConfigurableVariables.Provider;

public class DevicePool {

    public Odometer rightOdometer;

    public IMU gyro;

    public Motor motorR;
    public Motor motorL;
    public Motor motorLF;
    public Motor motorRF;

    public Motor gunR;
    public Motor gunL;

    public ServoMotor shotR;
    public ServoMotor shotL;
    public ServoMotor shotC;
    public ServoMotor wall;

    public ServoMotor aimR;
    public ServoMotor aimC;
    public ServoMotor aimL;

    public ServoMotor borderR;
    public ServoMotor borderL;

    public RevVoltageSensor revVoltageSensor;

    public ColorSensor sensorR;
    public ColorSensor sensorC;
    public ColorSensor sensorL;

    public HardwareMap hardwareMap;

    private static final  DevicePool Instance = new DevicePool();
    public static DevicePool getInstance(){
        return Instance;
    }

    public void init(HardwareMap map, DeviceActivationConfig config){
        HardwareFactory factory = new HardwareFactory(map,config);
        hardwareMap = map;

        for (LynxModule i : hardwareMap.getAll(LynxModule.class)) {
            i.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        motorL = factory.createDcMotor("motorL", config.motorConfig.leftBackPos, config.motorConfig.leftBackVol);

        motorR = factory.createDcMotor("motorR", config.motorConfig.rightBackPos, config.motorConfig.rightBackVol);

        gunL = factory.createDcMotor("gunL", new Provider<>(0d), new Provider<>(0d));
        gunR = factory.createDcMotor("gunR", new Provider<>(0d), new Provider<>(0d));

        gunR.setDirection(DcMotorSimple.Direction.REVERSE);
        gunL.setDirection(DcMotorSimple.Direction.REVERSE);

        gyro = factory.createIMU("imu");

        rightOdometer = factory.createOdometer("motorR", config.odometerConfig.rightOdPos, config.odometerConfig.rightOdVel);
        rightOdometer.setDir(-1);
        rightOdometer.reset();

        shotL = factory.createServoMotor("shotL");
        shotR = factory.createServoMotor("shotR");
        shotC = factory.createServoMotor("shotC");
        wall  = factory.createServoMotor("wall") ;

        aimR   = factory.createServoMotor("aimR");
        aimC   = factory.createServoMotor("aimC");
        aimL   = factory.createServoMotor("aimL");

        borderL = factory.createServoMotor("borderL");
        borderR = factory.createServoMotor("borderR");

        motorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        gunR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        gunL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        motorL.setDir(1);
        motorR.setDir(-1);

        motorL.reset();
        motorR.reset();

        revVoltageSensor = factory.createVoltageSensor();

        sensorR = factory.createColorSensor("sensorR", config.colorSensorConfig.red, config.colorSensorConfig.green, config.colorSensorConfig.blue);
        sensorL = factory.createColorSensor("sensorL", config.colorSensorConfig.red, config.colorSensorConfig.green, config.colorSensorConfig.blue);
        sensorC = factory.createColorSensor("sensorC", config.colorSensorConfig.red, config.colorSensorConfig.green, config.colorSensorConfig.blue);
    }

}
