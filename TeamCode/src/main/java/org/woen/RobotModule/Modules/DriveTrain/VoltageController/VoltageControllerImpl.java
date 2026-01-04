package org.woen.RobotModule.Modules.DriveTrain.VoltageController;

import static java.lang.Math.abs;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.ControlSystemConstant;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.DevicePool.Devices.Motor.Interface.Motor;
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

    private Motor rb;
    private Motor lb;

    @Override
    public void deviceSetUpdate() {

        WheelValueMap power = target;

        double offset = ControlSystemConstant.feedforwardConfig.staticVoltageOffsetX;
        if(abs((power.lf+power.lb)+(power.rf+power.rb)) < 0.1){
            offset = ControlSystemConstant.feedforwardConfig.staticVoltageOffsetH;
        }

        power = new WheelValueMap(
                power.lf+ offset *Math.signum(power.lf),
                power.rf+ offset *Math.signum(power.rf),
                power.rb+ offset *Math.signum(power.rb),
                power.lb+ offset *Math.signum(power.lb)
        );

        power = power.border(new WheelValueMap(
                offset +0.05, offset +0.05,
                offset +0.05, offset +0.05));

        power = power.multiply(1d/voltage);

        double maxV = Math.max(
                Math.max(abs(power.lf), abs(power.rf)),
                Math.max(abs(power.rb), abs(power.lb)));

        if(maxV>1){
            double k = 1.0/maxV;
            power = power.multiply(k);
        }

        rb.setPower(power.rb);
        lb.setPower(power.lb);
    }

    @Override
    public void init() {
        rb = DevicePool.getInstance().motorR;
        lb = DevicePool.getInstance().motorL;
        EventBus.getListenersRegistration().invoke(new RegisterNewWheelsVoltageListener(this::setTarget));
    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(NewVoltageAvailable.class,this::onEvent);
    }
}
