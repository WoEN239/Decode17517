package org.woen.Hardware.Odometers;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Telemetry.Configs.Provider;

public class OdometerConf {

    public Provider<Boolean> rightOdPos= new Provider<>(true);
    public Provider<Boolean> leftOdPos= new Provider<>(true);
    public Provider<Boolean> sideOdPos= new Provider<>(true);
    public Provider<Boolean> rightOdVol= new Provider<>(true);
    public Provider<Boolean> leftOdVol= new Provider<>(true);
    public Provider<Boolean> sideOdVol= new Provider<>(true);

    public void init(){
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "rightOdPos", rightOdPos);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "leftOdPos", leftOdPos);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "sideOdPos", sideOdPos);

        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "rightOdVol", rightOdVol);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "leftOdVol", leftOdVol);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "sideOdVol", sideOdVol);
    }

    public void off(){
        rightOdPos.set(false);
        leftOdPos.set(false);
        sideOdPos.set(false);

        rightOdVol.set(false);
        leftOdVol.set(false);
        sideOdVol.set(false);
    }
}
