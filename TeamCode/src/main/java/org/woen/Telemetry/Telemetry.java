package org.woen.Telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.Telemetry.ConfigurableVariables.Provider;
import org.woen.Telemetry.ModulesInterfacesTelemetry.ModulesInterfacesTelemetry;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

import java.util.ArrayList;

public class Telemetry {
    public Provider<Boolean> robotPose = new Provider<>(false);
    public Provider<Boolean> targetVel = new Provider<>(false);
    public Provider<Boolean> gyro = new Provider<>(false);
    public Provider<Boolean> voltage = new Provider<>(false);
    public Provider<Boolean> localizeDevice = new Provider<>(false);

    public Telemetry() {
        FtcDashboard.getInstance().addConfigVariable("telemetry","pose",robotPose);
        FtcDashboard.getInstance().addConfigVariable("telemetry","target vel",targetVel);
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
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setRectPos));
    }
    private TelemetryPacket telemetryPacket = new TelemetryPacket();

    private final ModulesInterfacesTelemetry modulesTelemetry = new ModulesInterfacesTelemetry();

    private final ArrayList<Runnable> configUpdates = new ArrayList<>();

    public void loopAnd() {
        configUpdates.forEach(Runnable::run);
        if(robotPose.get()){
            modulesTelemetry.addRobotPoseToPacket(telemetryPacket);
            updateField();
        }

        if(targetVel.get()){
            modulesTelemetry.addTargetVelToPacket(telemetryPacket);
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
    }

    public <T> void add(String name, T data) {
        telemetryPacket.put(name, data);
    }

    public void add(Runnable update) {
        configUpdates.add(update);
    }

    private void rotatePoints(double[] xPoints, double[] yPoints, double angle) {
        for (int i = 0; i < xPoints.length; i++) {
            double x = xPoints[i];
            double y = yPoints[i];
            Vector2d p = new Vector2d(x, y).rotate(angle);
            xPoints[i] = p.x;
            yPoints[i] = p.y;
        }
    }

    private void plusVector(double[] x, double[] y, Vector2d p) {
        for (int j = 0; j < x.length; j++) {
            x[j] += p.x;
            y[j] += p.y;
        }
    }

    private Pose rectPos = new Pose(0, 0, 0);

    private final double smPerInch = 1.0 / 2.54;
    private final double height = 40.24 / 2.0;
    private final double width = 39. / 2.0;

    public void updateField() {
        double[] xPoints;
        double[] yPoints;

        Vector2d rect = new Vector2d(height, width);
        rect.rotate(rectPos.h);

        xPoints = new double[]{
                +height,
                +height,
                -height,
                -height};
        yPoints = new double[]{
                (+width),
                (-width),
                (-width),
                (+width)};

        rotatePoints(xPoints, yPoints, rectPos.h);
        plusVector(xPoints, yPoints, rectPos.vector);

        telemetryPacket.fieldOverlay().setScale(smPerInch, smPerInch);

        telemetryPacket.fieldOverlay().setFill("blue");
        telemetryPacket.fieldOverlay().fillPolygon(xPoints, yPoints);
    }

    public void setRectPos(Pose rectPos) {
        this.rectPos = rectPos;
    }

}
