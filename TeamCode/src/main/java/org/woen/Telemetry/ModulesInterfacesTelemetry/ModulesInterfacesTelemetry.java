package org.woen.Telemetry.ModulesInterfacesTelemetry;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.RegisterNewWheelsVoltageListener;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelValueMap;
import org.woen.RobotModule.Modules.Gyro.Arcitecture.RegisterNewAngleListener;
import org.woen.RobotModule.Modules.Gyro.Arcitecture.RegisterNewAngularVelocityListener;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Architecture.LocalizeDeviceData;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Architecture.RegisterNewLocalizeDeviceListener;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewLocalPositionListener;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.RegisterNewLocalVelocityListener;
import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.RegisterNewVelocityListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.RegisterNewFeedforwardReferenceListener;
import org.woen.Util.Vectors.Pose;

public class ModulesInterfacesTelemetry {
    private Pose robotPos = new Pose(0,0,0);
    private Pose robotVel = new Pose(0,0,0);

    private Pose targetVel = new Pose(-564,0,0);

    private Pose robotLocalPos = new Pose(0,0,0);
    private Pose robotLocalVel = new Pose(0,0,0);

    private Double gyroAngle = 0d;
    private Double gyroAngularVel = 0d;

    private WheelValueMap voltage = new WheelValueMap(0d,0d,0d,0d);
    private LocalizeDeviceData localizeDeviceData = new LocalizeDeviceData();

    public void addRobotPoseToPacket(TelemetryPacket packet){
        packet.put("robot pos",robotPos.toString());
        packet.put("robot vel",robotVel.toString());

        packet.put("robot local pos",robotLocalPos.toString());
        packet.put("robot local vel",robotLocalVel.toString());
    }

    public void addGyroToPacket(TelemetryPacket packet){
        packet.put("gyro angle",gyroAngle);
        packet.put("gyro vel",gyroAngularVel);
    }

    public void addVoltageToPacket(TelemetryPacket packet){
        packet.put("wheels",voltage.toString());
    }

    public void addTargetVelToPacket(TelemetryPacket packet){
        packet.put("feedforward reference",targetVel.toString());
    }


    public void addLocalizeDevicesToPacket(TelemetryPacket packet){
        packet.put("rightPos",localizeDeviceData.rightOdPos);
        packet.put("leftPos",localizeDeviceData.leftOdPos);
        packet.put("sidePos",localizeDeviceData.sideOdPos);

        packet.put("rightVel",localizeDeviceData.rightOdVel);
        packet.put("leftVel",localizeDeviceData.leftOdVel);
        packet.put("sideVel",localizeDeviceData.sideOdVel);
    }

    public void init(){
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setRobotPos));
        EventBus.getListenersRegistration().invoke(new RegisterNewVelocityListener(this::setRobotVel));

        EventBus.getListenersRegistration().invoke(new RegisterNewLocalPositionListener(this::setRobotLocalPos));
        EventBus.getListenersRegistration().invoke(new RegisterNewLocalVelocityListener(this::setRobotLocalVel));

        EventBus.getListenersRegistration().invoke(new RegisterNewAngleListener(this::setGyroAngle));
        EventBus.getListenersRegistration().invoke(new RegisterNewAngularVelocityListener(this::setGyroAngularVel));

        EventBus.getListenersRegistration().invoke(new RegisterNewWheelsVoltageListener(this::setVoltage));
        EventBus.getListenersRegistration().invoke(new RegisterNewLocalizeDeviceListener(this::setLocalizeDeviceData));

        EventBus.getListenersRegistration().invoke(new RegisterNewFeedforwardReferenceListener(this::setFeedforwardReference));
    }

    public void setRobotPos(Pose robotPos) {
        this.robotPos = robotPos;
    }

    public void setRobotVel(Pose robotVel) {
        this.robotVel = robotVel;
    }

    public void setRobotLocalPos(Pose robotLocalPos) {
        this.robotLocalPos = robotLocalPos;
    }

    public void setRobotLocalVel(Pose robotLocalVel) {
        this.robotLocalVel = robotLocalVel;
    }

    public void setGyroAngle(Double gyroAngle) {
        this.gyroAngle = gyroAngle;
    }

    public void setGyroAngularVel(Double gyroAngularVel) {
        this.gyroAngularVel = gyroAngularVel;
    }

    public void setVoltage(WheelValueMap voltage) {this.voltage = voltage;}

    public void setFeedforwardReference(FeedforwardReference reference) {this.targetVel = reference.now;}

    public void setLocalizeDeviceData(LocalizeDeviceData localizeDeviceData) {this.localizeDeviceData = localizeDeviceData;}
}