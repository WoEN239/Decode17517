package org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Impls;

import static org.woen.Config.ControlSystemConstant.*;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.ControlSystemConstant;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Interface.DriveTrain;
import org.woen.RobotModule.Modules.DriveTrain.FeedbackController.FeedbackController;
import org.woen.RobotModule.Modules.DriveTrain.FeedforwardController.FeedforwardController;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelValueMap;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelsVoltageObserver;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.RegisterNewVelocityListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.RegisterFeedbackReferenceListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.RegisterFeedforwardReferenceListener;
import org.woen.Util.Vectors.Pose;

public class DriveTrainImpl implements DriveTrain {
    private FeedforwardReference feedforwardReference;
    private FeedbackReference feedbackReference;


    private Pose position = MatchData.startPosition;
    private Pose velocity = new Pose(0,0,0);

    public void setPosition(Pose position) {
        this.position = position;
    }

    public void setVelocity(Pose velocity) {
        this.velocity = velocity;
    }

    private FeedbackController feedbackController = new FeedbackController(
            ControlSystemConstant.xPid,
            ControlSystemConstant.hPid);
    private FeedforwardController feedforwardController = new FeedforwardController(ControlSystemConstant.feedforwardK);

    public void setFeedforwardReference(FeedforwardReference feedforwardReference) {
        this.feedforwardReference = feedforwardReference;
    }

    public void setFeedbackReference(FeedbackReference  feedbackReference) {
        this.feedbackReference = feedbackReference;
    }

    private final WheelsVoltageObserver wheelsVoltageObserver = new WheelsVoltageObserver();

    @Override
    public void lateUpdate() {
        WheelValueMap feedback = toWheelsFromRobotVoltage(
                feedbackController.computeU(feedbackReference.pos,position,
                                            feedbackReference.vel,velocity));

        WheelValueMap feedforward = feedforwardController.computeU(toWheelsFromRobotVelocities(velocity));

        wheelsVoltageObserver.notifyListeners(feedforward.plus(feedback));
    }

    private WheelValueMap toWheelsFromRobotVoltage(Pose r){
        return new WheelValueMap(
                r.vector.x-r.vector.y+r.h,
                r.vector.x+r.vector.y-r.h,
                r.vector.x-r.vector.y-r.h,
                r.vector.x+r.vector.y+r.h

        );
    }

    private WheelValueMap toWheelsFromRobotVelocities(Pose r){
        return new WheelValueMap(
                (r.vector.x - r.vector.y -  (lx+ly)*r.h)/wheelR,
                (r.vector.x + r.vector.y +  (lx+ly)*r.h)/wheelR,
                (r.vector.x + r.vector.y -  (lx+ly)*r.h)/wheelR,
                (r.vector.x - r.vector.y +  (lx+ly)*r.h)/wheelR
        );
    }

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(
                new RegisterFeedbackReferenceListener(this::setFeedbackReference));
        EventBus.getListenersRegistration().invoke(
                new RegisterFeedforwardReferenceListener(this::setFeedforwardReference));

        EventBus.getListenersRegistration().invoke(
                new RegisterNewPositionListener(this::setPosition));
        EventBus.getListenersRegistration().invoke(
                new RegisterNewVelocityListener(this::setVelocity));
    }


}
