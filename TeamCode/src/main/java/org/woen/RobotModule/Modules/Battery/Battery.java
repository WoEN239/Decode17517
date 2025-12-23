package org.woen.RobotModule.Modules.Battery;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.VoltageSensor.RevVoltageSensor;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.Util.Arrays.ArrayExtra;

public class Battery implements IRobotModule {
    private RevVoltageSensor revVoltageSensor;
    private final ElapsedTime timer = new ElapsedTime();

    private final double[] reads = new double[5];

    @Override
    public void init(){
        revVoltageSensor = DevicePool.getInstance().revVoltageSensor;
    }

    public void update() {
        if (timer.seconds() > 1) {
            ArrayExtra.updateLikeBuffer(revVoltageSensor.getVoltage(), reads);
            EventBus.getInstance().invoke(new NewVoltageAvailable(ArrayExtra.findMedian(reads)));
            timer.reset();
        }
    }
}
