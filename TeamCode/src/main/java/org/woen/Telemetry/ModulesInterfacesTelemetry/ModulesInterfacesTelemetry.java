package org.woen.Telemetry.ModulesInterfacesTelemetry;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.RegisterNewWheelsVoltageListener;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelValueMap;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewVelocityListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.RegisterNewFeedbackReferenceListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.RegisterNewFeedforwardReferenceListener;
import org.woen.Util.Vectors.Pose;

public class ModulesInterfacesTelemetry {
    private Pose robotPos = new Pose(0,0,0);
    private Pose robotVel = new Pose(0,0,0);

    private Pose targetVel = new Pose(0,0,0);
    private Pose targetPos = new Pose(-564,0,0);

    private WheelValueMap voltage = new WheelValueMap(0d,0d,0d,0d);

    public void addRobotPoseToPacket(TelemetryPacket packet){
        packet.put("robot pos",robotPos.toString());
        packet.put("robot vel",robotVel.toString());

    }

    public void addVoltageToPacket(TelemetryPacket packet){
        packet.put("wheels",voltage.toString());
    }

    public void addTargetToPacket(TelemetryPacket packet){
        packet.put("feedforward reference",targetVel.toString());
        packet.put("feedback reference",targetPos.toString());
    }



    public void init(){
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setRobotPos));
        EventBus.getListenersRegistration().invoke(new RegisterNewVelocityListener(this::setRobotVel));


        EventBus.getListenersRegistration().invoke(new RegisterNewWheelsVoltageListener(this::setVoltage));

        EventBus.getListenersRegistration().invoke(new RegisterNewFeedforwardReferenceListener(this::setFeedforwardReference));
        EventBus.getListenersRegistration().invoke(new RegisterNewFeedbackReferenceListener(this::setFeedbackReference));
    }

    public void setRobotPos(Pose robotPos) {
        this.robotPos = robotPos;
    }

    public void setRobotVel(Pose robotVel) {
        this.robotVel = robotVel;
    }

    public void setVoltage(WheelValueMap voltage) {this.voltage = voltage;}

    public void setFeedforwardReference(FeedforwardReference reference) {this.targetVel = reference.vel;}
    public void setFeedbackReference(FeedbackReference reference) {this.targetPos = reference.pos;}

}