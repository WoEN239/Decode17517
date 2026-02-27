package org.woen.Hardware.DevicePool.Devices.Odometers;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Telemetry.ConfigurableVariables.SimpleProvider;

public class OdometerConf {

    public final SimpleProvider<Double> rightOdPos= new SimpleProvider<>(0d);
    public final SimpleProvider<Double> leftOdPos= new SimpleProvider<>(0d);
    public final SimpleProvider<Double> sideOdPos= new SimpleProvider<>(0d);
    public final SimpleProvider<Double> rightOdVel = new SimpleProvider<>(0d);
    public final SimpleProvider<Double> leftOdVel = new SimpleProvider<>(0d);
    public final SimpleProvider<Double> sideOdVel = new SimpleProvider<>(0d);

    public void init(){
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "rightOdPos", rightOdPos);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "leftOdPos", leftOdPos);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "sideOdPos", sideOdPos);

        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "rightOdVol", rightOdVel);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "leftOdVol", leftOdVel);
        FtcDashboard.getInstance().addConfigVariable("OdometerConf", "sideOdVol", sideOdVel);
    }


}
