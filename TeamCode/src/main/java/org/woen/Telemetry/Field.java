package org.woen.Telemetry;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

public class Field {
    private TelemetryPacket telemetryPacket = new TelemetryPacket();

    public void setTelemetryPacket(TelemetryPacket telemetryPacket) {
        this.telemetryPacket = telemetryPacket;
        telemetryPacket.fieldOverlay().setScale(smPerInch, smPerInch);
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

    private final double smPerInch = 1.0 / 2.54;
    private final double height = 45.7/ 2.0;
    private final double width =  45/ 2.0;

    public void robot(Pose pose) {
        double[] xPoints;
        double[] yPoints;
        Vector2d rectAngle = new Vector2d(height, width).rotate(pose.h);

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

        rotatePoints(xPoints, yPoints, pose.h);
        plusVector(xPoints, yPoints, pose.vector);

        telemetryPacket.fieldOverlay().setFill("blue");
        telemetryPacket.fieldOverlay().fillPolygon(xPoints, yPoints);

        telemetryPacket.fieldOverlay().strokeLine(pose.x,pose.y,pose.x+rectAngle.x,pose.y+rectAngle.y);
    }

    public void line(Vector2d start, Vector2d end){
        telemetryPacket.fieldOverlay().strokeLine(start.x, start.y, end.x, end.y);
    }

    public void circle(Vector2d c, double r){
        telemetryPacket.fieldOverlay().fillCircle(c.x,c.y,r);
    }
}
