package org.woen.RobotModule.Modules.DriveTrain.VoltageController.Impls;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.RobotModule.Modules.Battery.NewVoltageAvailable;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.RegisterNewWheelsVoltageListener;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelValueMap;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Interface.VoltageController;
import org.woen.Telemetry.Telemetry;

public class VoltageControllerImpl implements VoltageController {
    private WheelValueMap target = new WheelValueMap(0d,0d,0d,0d);
    private double voltage = 12;

    private void onEvent(NewVoltageAvailable e) {
        this.voltage = e.getData();
        Telemetry.getInstance().add("battery",voltage);
    }

    public void setTarget(WheelValueMap target) {
        this.target = target;
    }

    private Motor lf;
    private Motor rf;
    private Motor rb;
    private Motor lb;

    @Override
    public void deviceSetUpdate() {
        double maxV = Math.max(
                Math.max(Math.abs(target.lf),Math.abs(target.rf)),
                Math.max(Math.abs(target.rb),Math.abs(target.lb)));
        WheelValueMap power = target;
        if(maxV>voltage){
            double k = voltage/maxV;
            power = power.multiply(k);
        }
        power = power.multiply(1d/voltage);
        lf.setPower(power.lf);
        rf.setPower(power.rf);
        rb.setPower(power.rb);
        lb.setPower(power.lb);
    }

    @Override
    public void init() {
        lf = DevicePool.getInstance().motorLF;
        rf = DevicePool.getInstance().motorRF;
        rb = DevicePool.getInstance().motorRB;
        lb = DevicePool.getInstance().motorLB;
        EventBus.getListenersRegistration().invoke(new RegisterNewWheelsVoltageListener(this::setTarget));
    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(NewVoltageAvailable.class,this::onEvent);
    }
}
