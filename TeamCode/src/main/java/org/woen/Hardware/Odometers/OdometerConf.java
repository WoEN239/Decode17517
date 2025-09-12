package org.woen.Hardware.Odometers;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Telemetry.Configs.Provider;

public class OdometerConf {

    public Provider<Double> rightOdPos= new Provider<>(0d);
    public Provider<Double> leftOdPos= new Provider<>(0d);
    public Provider<Double> sideOdPos= new Provider<>(0d);
    public Provider<Double> rightOdVol= new Provider<>(0d);
    public Provider<Double> leftOdVol= new Provider<>(0d);
    public Provider<Double> sideOdVol= new Provider<>(0d);

    public void init(){
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "rightOdPos", rightOdPos);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "leftOdPos", leftOdPos);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "sideOdPos", sideOdPos);

        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "rightOdVol", rightOdVol);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "leftOdVol", leftOdVol);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "sideOdVol", sideOdVol);
    }

    public void off(){
        rightOdPos.set(null);
        leftOdPos.set(null);
        sideOdPos.set(null);

        rightOdVol.set(null);
        leftOdVol.set(null);
        sideOdVol.set(null);
    }
}
