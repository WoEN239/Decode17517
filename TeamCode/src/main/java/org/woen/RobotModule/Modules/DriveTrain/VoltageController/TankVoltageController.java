package org.woen.RobotModule.Modules.DriveTrain.VoltageController;

import static java.lang.Math.abs;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.ControlSystemConstant;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.DevicePool.Devices.Motor.Interface.Motor;
import org.woen.RobotModule.Modules.Battery.NewVoltageAvailable;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.RegisterNewTankWheelsVoltageListener;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.TankWheelValueMap;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Interface.VoltageController;
import org.woen.Telemetry.Telemetry;

public class TankVoltageController implements VoltageController {
    private TankWheelValueMap target = new TankWheelValueMap(0d,0d);
    @Override
    public void lateUpdate () {

        TankWheelValueMap power = target;

        double offset = ControlSystemConstant.feedforwardConfig.staticVoltageOffsetX;
        if(abs(power.l+power.r) < 0.05){
            offset = ControlSystemConstant.feedforwardConfig.staticVoltageOffsetH;
        }

        power = new TankWheelValueMap(
                power.l+ offset *Math.signum(power.l),
                power.r+ offset *Math.signum(power.r)
        );

        power = power.multiply(1d/voltage);

        double maxV = Math.max(power.l, power.r);

        if(maxV>1){
            double k = 1.0/maxV;
            power = power.multiply(k);
        }

        rb.setPower(power.r);
        lb.setPower(power.l);
    }

    private double voltage = 12;
    private void onEvent(NewVoltageAvailable e) {
        this.voltage = e.getData();
        Telemetry.getInstance().add("battery",voltage);
    }

    public void setTarget(TankWheelValueMap target) {
        this.target = target;
    }

    private Motor rb;
    private Motor lb;

    @Override
    public void init() {
        rb = DevicePool.getInstance().motorR;
        lb = DevicePool.getInstance().motorL;
        EventBus.getListenersRegistration().invoke(new RegisterNewTankWheelsVoltageListener(this::setTarget));
    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(NewVoltageAvailable.class,this::onEvent);
    }

}
