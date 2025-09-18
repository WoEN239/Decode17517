package org.woen.RobotModule.Modules.DriveTrain.VoltageController.Impls;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.RegisterNewWheelsVoltageListener;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelValueMap;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Interface.VoltageController;

public class VoltageControllerImpl implements VoltageController {
    private WheelValueMap target = new WheelValueMap(0,0,0,0);

    public void setTarget(WheelValueMap target) {
        this.target = target;
    }

    private Motor lf;
    private Motor rf;
    private Motor rb;
    private Motor lb;

    @Override
    public void deviceSetUpdate() {

    }

    @Override
    public void init() {
        lf = DevicePool.getInstance().motorLF;
        rf = DevicePool.getInstance().motorRF;
        rb = DevicePool.getInstance().motorRB;
        lb = DevicePool.getInstance().motorLB;
        EventBus.getListenersRegistration().invoke(new RegisterNewWheelsVoltageListener(this::setTarget));
    }
}
