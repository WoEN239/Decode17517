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

    public Odometer rightOd;
    public Odometer leftOd;
    public Odometer sideOd;
    public IMU gyro;

    public Motor motorRB;
    public Motor motorLB;
    public Motor motorLF;
    public Motor motorRF;

    public Motor gunR;
    public Motor gunL;

    public ServoMotor right;
    public ServoMotor left;
    public ServoMotor center;
    public ServoMotor wall;

    public RevVoltageSensor revVoltageSensor;

    public ColorSensor colorSensor;

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

        rightOd = factory.createOdometer("motorRB", config.odometerConfig.rightOdPos, config.odometerConfig.rightOdVel);
        rightOd.setDir(-1);
        rightOd.reset();

        leftOd = factory.createOdometer("motorLF", config.odometerConfig.leftOdPos, config.odometerConfig.leftOdVel);
        leftOd.setDir(1);
        leftOd.reset();

        sideOd = factory.createOdometer("sideOdometer", config.odometerConfig.sideOdPos, config.odometerConfig.sideOdVel);
        sideOd.setDir(-1);
        sideOd.reset();

        left   = factory.createServoMotor("left");
        right  = factory.createServoMotor("right");
        center = factory.createServoMotor("center");
        wall   = factory.createServoMotor("wall");

        motorLB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorLB.setDirection(DcMotorSimple.Direction.REVERSE);
        motorLF.setDirection(DcMotorSimple.Direction.REVERSE);
        motorRB.setDirection(DcMotorSimple.Direction.FORWARD);
        motorRF.setDirection(DcMotorSimple.Direction.FORWARD);

        motorLB.reset();
        motorLF.reset();
        motorRB.reset();
        motorRF.reset();


        revVoltageSensor = factory.createVoltageSensor();

        colorSensor = factory.createColorSensor("sensor", config.colorSensorConfig.red, config.colorSensorConfig.green, config.colorSensorConfig.blue);
    }

}
