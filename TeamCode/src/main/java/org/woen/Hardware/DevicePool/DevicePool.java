package org.woen.Hardware.DevicePool;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.Hardware.DevicePool.Devices.Odometers.Inter.PinPoint;
import org.woen.Hardware.DevicePool.Devices.Servo.Interface.ServoMotor;
import org.woen.Hardware.DevicePool.Devices.VoltageSensor.RevVoltageSensor;
import org.woen.Hardware.Factory.HardwareFactory;
import org.woen.Hardware.DevicePool.Devices.Motor.Interface.Motor;
import org.woen.Hardware.DevicePool.Devices.Odometers.Inter.Odometer;
import org.woen.Telemetry.ConfigurableVariables.Provider;

public class DevicePool {

    public PinPoint pinPoint;

    public Odometer rightOdometer;

    public IMU gyro;

    public Motor motorR;
    public Motor motorL;

    public ServoMotor ptoR;
    public ServoMotor ptoL;

    public Motor gunR;
    public Motor gunC;
    public Motor gunL;
    public Motor brush;
    public Motor light;

    public ServoMotor shotR;
    public ServoMotor shotL;
    public ServoMotor shotC;

    public ServoMotor aimR;
    public ServoMotor aimC;
    public ServoMotor aimL;

    public RevVoltageSensor revVoltageSensor;

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

        pinPoint = factory.createPinPoint("odometerComputer");

        motorL = factory.createDcMotor("motorL", config.motorConfig.leftBackPos, config.motorConfig.leftBackVol);
        motorR = factory.createDcMotor("motorR", config.motorConfig.rightBackPos, config.motorConfig.rightBackVol);

        gunC = factory.createDcMotor("gunC", new Provider<>(0d), new Provider<>(0d));
        gunC.setDir(-1);

        gunL = factory.createDcMotor("gunL", new Provider<>(0d), new Provider<>(0d));
        gunL.setDir(1);

        gunR = factory.createDcMotor("gunR", new Provider<>(0d), new Provider<>(0d));
        gunR.setDir(-1);

        brush = factory.createDcMotor("brush", new Provider<>(0d), new Provider<>(0d));
        brush.setDir(1);

        //gyro = factory.createIMU("imu");

        rightOdometer = factory.createOdometer("motorR", config.odometerConfig.rightOdPos, config.odometerConfig.rightOdVel);
        rightOdometer.setDir(-1);
        rightOdometer.reset();

        shotL = factory.createServoMotor("shotL");
        shotR = factory.createServoMotor("shotR");
        shotC = factory.createServoMotor("shotC");

        aimR   = factory.createServoMotor("aimR");
        aimC   = factory.createServoMotor("aimC");
        aimL   = factory.createServoMotor("aimL");

        ptoL = factory.createServoMotor("ptoL");
        ptoR = factory.createServoMotor("ptoR");

        motorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        gunR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        gunL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        gunC.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        light = factory.createDcMotor("light",new Provider<>(0d), new Provider<>(0d));

        brush.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        motorL.setDir(-1);
        motorR.setDir(1);

        motorL.reset();
        motorR.reset();

        revVoltageSensor = factory.createVoltageSensor();
    }

}
