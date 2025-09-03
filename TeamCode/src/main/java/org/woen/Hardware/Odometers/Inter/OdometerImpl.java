package org.woen.Hardware.Odometers.Inter;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Hardware.Odometers.Impl.Odometer;

public class OdometerImpl implements Odometer {

    protected DcMotorEx dcMotorEx;

    protected ElapsedTime time = new ElapsedTime();


    public OdometerImpl(DcMotorEx dcMotorEx){
        this.dcMotorEx = dcMotorEx;
    }


    @Override
    public double getPos(){
        return  dcMotorEx.getCurrentPosition();
    }

    @Override
    public double getVel(){
        return  dcMotorEx.getCurrentPosition();
    }

    @Override
    public void reset(){
        dcMotorEx.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        dcMotorEx.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }


}
