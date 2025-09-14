package org.woen.Telemetry.ModulesInterfacesTelemetry;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.woen.Architecture.EventBus.Bus.EventBus;
import org.woen.RobotModule.Modules.Gyro.Arcitecture.RegisterNewAngleListener;
import org.woen.RobotModule.Modules.Gyro.Arcitecture.RegisterNewAngularVelocityListener;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewLocalPositionListener;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.RegisterNewLocalVelocityListener;
import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.RegisterNewVelocityListener;
import org.woen.Util.Vectors.Pose;

public class ModulesInterfacesTelemetry {
    private Pose robotPos = new Pose(0,0,0);
    private Pose robotVel = new Pose(0,0,0);

    private Pose robotLocalPos = new Pose(0,0,0);
    private Pose robotLocalVel = new Pose(0,0,0);

    private Double gyroAngle = 0d;
    private Double gyroAngularVel = 0d;

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

    public void init(){
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setRobotPos));
        EventBus.getListenersRegistration().invoke(new RegisterNewVelocityListener(this::setRobotVel));

        EventBus.getListenersRegistration().invoke(new RegisterNewLocalPositionListener(this::setRobotLocalPos));
        EventBus.getListenersRegistration().invoke(new RegisterNewLocalVelocityListener(this::setRobotLocalVel));

        EventBus.getListenersRegistration().invoke(new RegisterNewAngleListener(this::setGyroAngle));
        EventBus.getListenersRegistration().invoke(new RegisterNewAngularVelocityListener(this::setGyroAngularVel));
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


}