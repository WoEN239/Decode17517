package org.woen.Hardware.Devices.Odometers;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Telemetry.Configs.Provider;

public class OdometerConf {

    public final Provider<Double> rightOdPos= new Provider<>(0d);
    public final Provider<Double> leftOdPos= new Provider<>(0d);
    public final Provider<Double> sideOdPos= new Provider<>(0d);
    public final Provider<Double> rightOdVel = new Provider<>(0d);
    public final Provider<Double> leftOdVel = new Provider<>(0d);
    public final Provider<Double> sideOdVel = new Provider<>(0d);

    public void init(){
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "rightOdPos", rightOdPos);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "leftOdPos", leftOdPos);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "sideOdPos", sideOdPos);

        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "rightOdVol", rightOdVel);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "leftOdVol", leftOdVel);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "sideOdVol", sideOdVel);
    }


}
