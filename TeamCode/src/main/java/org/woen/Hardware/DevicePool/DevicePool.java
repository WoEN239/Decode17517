package org.woen.Hardware.DevicePool;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.woen.Hardware.Factories.HardwareFactory;
import org.woen.Hardware.Gyro.GyroConf;
import org.woen.Hardware.Gyro.Impl.Gyro;
import org.woen.Hardware.Motor.Interface.Motor;
import org.woen.Hardware.Motor.MotorConfig;
import org.woen.Hardware.Odometers.Inter.Odometer;
import org.woen.Hardware.Odometers.OdometerConf;

public class DevicePool {

    public Odometer rightOd;
    public Odometer leftOd;
    public Odometer sideOd;
    public IMU gyro;

    public Motor motorRB;
    public Motor motorLB;
    public Motor motorLF;
    public Motor motorRF;


    private static final  DevicePool Instance = new DevicePool();
    public static DevicePool getInstance(){
        return Instance;
    }

    public void init(HardwareFactory hardwareFactory){
        motorLB = hardwareFactory.createDcMotor("motorLB", MotorConfig.leftBackPos, MotorConfig.leftBackVol);

        motorRB = hardwareFactory.createDcMotor("motorRB", MotorConfig.rightBackPos, MotorConfig.rightBackVol);

        motorLF = hardwareFactory.createDcMotor("motorLF", MotorConfig.leftFrontPos, MotorConfig.leftFrontVol);

        motorRF = hardwareFactory.createDcMotor("motorRF", MotorConfig.rightFrontPos, MotorConfig.rightFrontVol);

        gyro = hardwareFactory.createIMU("imu");

        rightOd = hardwareFactory.createOdometer("motorRF", OdometerConf.rightOdPos, OdometerConf.rightOdVol);
        rightOd.setDir(-1);

        leftOd = hardwareFactory.createOdometer("motorLF", OdometerConf.leftOdPos, OdometerConf.leftOdVol);
        leftOd.setDir(-1);

        sideOd = hardwareFactory.createOdometer("motorRB", OdometerConf.sideOdPos, OdometerConf.sideOdVol);
        sideOd.setDir(-1);

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

    }

}
