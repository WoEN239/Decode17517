package org.woen.Telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import java.util.ArrayList;

public class Telemetry {
    private static final Telemetry Instance = new Telemetry();
    public static Telemetry getInstance() {return Instance;}
    private TelemetryPacket telemetryPacket = new TelemetryPacket();
    private final ArrayList<Runnable> configUpdates = new ArrayList<>();

    public void loopAnd() {
        configUpdates.forEach(Runnable::run);
        FtcDashboard.getInstance().sendTelemetryPacket(telemetryPacket);
        telemetryPacket = new TelemetryPacket();
    }

    public <T> void  add( String name, T data){
        telemetryPacket.put(name, data);
    }

    public void add(Runnable update){
        configUpdates.add(update);
    }

}
