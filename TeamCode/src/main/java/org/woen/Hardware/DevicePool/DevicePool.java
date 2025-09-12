package org.woen.Hardware.DevicePool;

import org.woen.Hardware.Factories.HardwareFactory;
import org.woen.Hardware.Gyro.GyroConf;
import org.woen.Hardware.Gyro.Impl.Gyro;
import org.woen.Hardware.Motor.Inter.Motor;
import org.woen.Hardware.Motor.MotorConfig;
import org.woen.Hardware.Odometers.Inter.Odometer;
import org.woen.Hardware.Odometers.OdometerConf;

public class DevicePool {

    private Odometer rightOd;
    private Odometer leftOd;
    private Odometer sideOd;
    private Gyro gyro;

    private Motor motorRB;
    private Motor motorLB;
    private Motor motorLF;
    private Motor motorRF;


    private static final  DevicePool Instance = new DevicePool();
    public static DevicePool getInstance(){
        return Instance;
    }

    public void init(HardwareFactory hardwareFactory){
        motorLB = hardwareFactory.createDcMotor("motorLB", MotorConfig.leftBackPos, MotorConfig.leftBackVol);

        motorRB = hardwareFactory.createDcMotor("motorRB", MotorConfig.rightBackPos, MotorConfig.rightBackVol);

        motorLF = hardwareFactory.createDcMotor("motorLF", MotorConfig.leftFrontPos, MotorConfig.leftFrontVol);

        motorRF = hardwareFactory.createDcMotor("motorRF", MotorConfig.rightFrontPos, MotorConfig.rightFrontVol);

        gyro = hardwareFactory.createIMU("imu", GyroConf.vel, GyroConf.pos);

        rightOd = hardwareFactory.createOdometer("rightOdometer", OdometerConf.rightOdPos, OdometerConf.rightOdVol);

        leftOd = hardwareFactory.createOdometer("leftOdometer", OdometerConf.leftOdPos, OdometerConf.leftOdVol);

        sideOd = hardwareFactory.createOdometer("sideOdometer", OdometerConf.sideOdPos, OdometerConf.sideOdVol);

    }


}
