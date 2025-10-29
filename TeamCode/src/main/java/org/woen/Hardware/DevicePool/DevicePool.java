package org.woen.Hardware.DevicePool;

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

    public Motor motorRB;
    public Motor motorLB;
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

    private static final  DevicePool Instance = new DevicePool();
    public static DevicePool getInstance(){
        return Instance;
    }

    public void init(HardwareMap map, DeviceActivationConfig config){
        HardwareFactory factory = new HardwareFactory(map,config);

        motorLB = factory.createDcMotor("motorLB", config.motorConfig.leftBackPos, config.motorConfig.leftBackVol);

        motorRB = factory.createDcMotor("motorRB", config.motorConfig.rightBackPos, config.motorConfig.rightBackVol);

        motorLF = factory.createDcMotor("motorLF", config.motorConfig.leftFrontPos, config.motorConfig.leftFrontVol);

        motorRF = factory.createDcMotor("motorRF", config.motorConfig.rightFrontPos, config.motorConfig.rightFrontVol);

        gunL = factory.createDcMotor("gunL", new Provider<>(0d), new Provider<>(0d));
        gunR = factory.createDcMotor("gunR", new Provider<>(0d), new Provider<>(0d));

        gunR.setDirection(DcMotorSimple.Direction.REVERSE);
        gunL.setDirection(DcMotorSimple.Direction.REVERSE);

        gyro = factory.createIMU("imu");

        rightOdometer = factory.createOdometer("motorRB", config.odometerConfig.rightOdPos, config.odometerConfig.rightOdVel);
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

        motorLB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorLB.setDir(-1);
        motorLF.setDir(1);
        motorRB.setDir(-1);
        motorRF.setDir( -1);

        motorLB.reset();
        motorLF.reset();
        motorRB.reset();
        motorRF.reset();


        revVoltageSensor = factory.createVoltageSensor();

        sensorR = factory.createColorSensor("sensorR", config.colorSensorConfig.red, config.colorSensorConfig.green, config.colorSensorConfig.blue);
        sensorL = factory.createColorSensor("sensorL", config.colorSensorConfig.red, config.colorSensorConfig.green, config.colorSensorConfig.blue);
        sensorC = factory.createColorSensor("sensorC", config.colorSensorConfig.red, config.colorSensorConfig.green, config.colorSensorConfig.blue);
    }

}
