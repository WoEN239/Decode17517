package org.woen.Hardware.DevicePool;

import org.woen.Hardware.DeviceActivationConfig.DeviceActivationConfig;
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

    private DeviceActivationConfig deviceActivationConfig = new DeviceActivationConfig();


    private static final  DevicePool Instance = new DevicePool();
    public static DevicePool getInstance(){
        return Instance;
    }

    public void init(HardwareFactory hardwareFactory){
        motorLB = hardwareFactory.createDcMotor("motorLB", deviceActivationConfig.motorConfig.cfLeftBackMotorPos.get(),  deviceActivationConfig.motorConfig.cfLeftBackMotorVol.get());

        motorRB = hardwareFactory.createDcMotor("motorRB", deviceActivationConfig.motorConfig.cfRightBackMotorPos.get(), deviceActivationConfig.motorConfig.cfRightBackMotorVol.get());

        motorLF = hardwareFactory.createDcMotor("motorLF", deviceActivationConfig.motorConfig.cfLeftFrontMotorPos.get(), deviceActivationConfig.motorConfig.cfLeftFrontMotorVol.get());

        motorRF = hardwareFactory.createDcMotor("motorRF", deviceActivationConfig.motorConfig.cfRightFrontMotorPos.get(), deviceActivationConfig.motorConfig.cfRightFrontMotorVol.get());

        gyro = hardwareFactory.createIMU("imu", deviceActivationConfig.gyroConf.vel.get(), deviceActivationConfig.gyroConf.pos.get());

        rightOd = hardwareFactory.createOdometer("rightOdometer", deviceActivationConfig.odometerConf.rightOdPos.get(), deviceActivationConfig.odometerConf.rightOdVol.get());

        leftOd = hardwareFactory.createOdometer("leftOdometer",deviceActivationConfig.odometerConf.leftOdPos.get(), deviceActivationConfig.odometerConf.leftOdVol.get());

        sideOd = hardwareFactory.createOdometer("sideOdometer", deviceActivationConfig.odometerConf.sideOdPos.get(), deviceActivationConfig.odometerConf.sideOdVol.get());

    }


}
