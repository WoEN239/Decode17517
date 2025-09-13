package org.woen.Telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

import java.util.ArrayList;

public class Telemetry {
    private static final Telemetry Instance = new Telemetry();

    public static Telemetry getInstance() {
        return Instance;
    }

    private TelemetryPacket telemetryPacket = new TelemetryPacket();
    private final ArrayList<Runnable> configUpdates = new ArrayList<>();

    public void loopAnd() {
        configUpdates.forEach(Runnable::run);
        updateField();
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
            Vector2d p = new Vector2d(x, y);
            p.rotate(angle);
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

    private Pose position = new Pose(0, 0, 0);

    private final double smPerInch = 1.0 / 2.54;
    private double height = 40.24 / 2.0;
    private double width = 39. / 2.0;

    public void updateField() {
        double[] xPoints;
        double[] yPoints;

        Vector2d rect = new Vector2d(height, width);
        rect.rotate(position.h);

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

        rotatePoints(xPoints, yPoints, position.h);
        plusVector(xPoints, yPoints, position.vector);

        telemetryPacket.fieldOverlay().setScale(smPerInch, smPerInch);

        telemetryPacket.fieldOverlay().setFill("blue");
        telemetryPacket.fieldOverlay().fillPolygon(xPoints, yPoints);

    }

    public void setPosition(Pose position) {
        this.position = position;
    }

}
