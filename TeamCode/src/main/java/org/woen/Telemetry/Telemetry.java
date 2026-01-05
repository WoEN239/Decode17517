package org.woen.Telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewPositionListener;
import org.woen.Telemetry.ConfigurableVariables.Provider;
import org.woen.Telemetry.ModulesInterfacesTelemetry.ModulesInterfacesTelemetry;

import java.util.ArrayList;

public class Telemetry {
    public Provider<Boolean> robotPose = new Provider<>(false);
    public Provider<Boolean> target = new Provider<>(false);
    public Provider<Boolean> gyro = new Provider<>(false);
    public Provider<Boolean> voltage = new Provider<>(false);
    public Provider<Boolean> localizeDevice = new Provider<>(false);

    public Telemetry() {
        FtcDashboard.getInstance().addConfigVariable("telemetry","pose",robotPose);
        FtcDashboard.getInstance().addConfigVariable("telemetry","target", target);
        FtcDashboard.getInstance().addConfigVariable("telemetry","gyro",gyro);
        FtcDashboard.getInstance().addConfigVariable("telemetry","voltage",voltage);
        FtcDashboard.getInstance().addConfigVariable("telemetry","localizeDevice",localizeDevice);
    }

    private static final Telemetry Instance = new Telemetry();
    public static Telemetry getInstance() {
        return Instance;
    }
    public void subscribeInit(){
        modulesTelemetry.init();
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(field::robot));
    }
    private TelemetryPacket telemetryPacket = new TelemetryPacket();

    private final ModulesInterfacesTelemetry modulesTelemetry = new ModulesInterfacesTelemetry();

    private final ArrayList<Runnable> configUpdates = new ArrayList<>();

    public void loopEnd() {
        configUpdates.forEach(Runnable::run);

        if(target.get()){
            modulesTelemetry.addTargetToPacket(telemetryPacket);
        }

        if(robotPose.get()){
            modulesTelemetry.addRobotPoseToPacket(telemetryPacket);
        }

        if(gyro.get()){
            modulesTelemetry.addGyroToPacket(telemetryPacket);
        }

        if(voltage.get()){
            modulesTelemetry.addVoltageToPacket(telemetryPacket);
        }

        if(localizeDevice.get()){
            modulesTelemetry.addLocalizeDevicesToPacket(telemetryPacket);
        }

        FtcDashboard.getInstance().sendTelemetryPacket(telemetryPacket);
        telemetryPacket = new TelemetryPacket();
        field.setTelemetryPacket(telemetryPacket);
    }

    public <T> void add(String name, T data) {
        telemetryPacket.put(name, data);
    }
    public void add(Runnable update) {
        configUpdates.add(update);
    }

    private final Field field = new Field();
    public Field getField() {
        return field;
    }
}
