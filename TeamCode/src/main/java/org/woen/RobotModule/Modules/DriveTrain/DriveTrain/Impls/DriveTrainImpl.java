package org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Impls;

import static org.woen.Config.ControlSystemConstant.*;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Interface.DriveTrain;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.HolonomicFeedbackController;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedforwardController.FeedforwardController;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelValueMap;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelsVoltageObserver;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.RegisterNewVelocityListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.RegisterNewFeedbackReferenceListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.RegisterNewFeedforwardReferenceListener;
import org.woen.Util.Vectors.Pose;

public class DriveTrainImpl implements DriveTrain {
    private FeedforwardReference feedforwardReference = new FeedforwardReference(new Pose(0,0,0),
                                                                                 new Pose(0,0,0));
    private FeedbackReference feedbackReference       = new FeedbackReference(new Pose(0,0,0),
                                                                              new Pose(0,0,0));

    private Pose position = MatchData.startPosition;
    private Pose velocity = new Pose(0,0,0);

    public void setPosition(Pose position) {
        this.position = position;
    }

    public void setVelocity(Pose velocity) {
        this.velocity = velocity;
    }

    private final HolonomicFeedbackController feedbackController = new HolonomicFeedbackController(
            feedbackConfig.xPid,
            feedbackConfig.hPid);
    private FeedforwardController feedforwardController = new FeedforwardController(
            new WheelValueMap(feedforwardConfig.xFeedforwardKV, feedforwardConfig.xFeedforwardKV,
                    feedforwardConfig.xFeedforwardKV, feedforwardConfig.xFeedforwardKV),
            new WheelValueMap(feedforwardConfig.xFeedforwardKA, feedforwardConfig.xFeedforwardKA,
                    feedforwardConfig.xFeedforwardKA, feedforwardConfig.xFeedforwardKA));

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

        feedforwardController = new FeedforwardController(
                new WheelValueMap(feedforwardConfig.xFeedforwardKV, feedforwardConfig.xFeedforwardKV,
                        feedforwardConfig.xFeedforwardKV, feedforwardConfig.xFeedforwardKV),
                new WheelValueMap(feedforwardConfig.xFeedforwardKA, feedforwardConfig.xFeedforwardKA,
                        feedforwardConfig.xFeedforwardKA, feedforwardConfig.xFeedforwardKA));

        WheelValueMap feedforward = feedforwardController.computeU(toWheelsFromRobotVelocities(feedforwardReference.now),
                                                                   toWheelsFromRobotVelocities(feedforwardReference.accel));

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
                (r.vector.x - r.vector.y - feedforwardConfig.hSlip*(robotSizeConfig.lx+ robotSizeConfig.ly)*r.h)/ robotSizeConfig.wheelR,
                (r.vector.x + r.vector.y + feedforwardConfig.hSlip*(robotSizeConfig.lx+ robotSizeConfig.ly)*r.h)/ robotSizeConfig.wheelR,
                (r.vector.x - r.vector.y + feedforwardConfig.hSlip*(robotSizeConfig.lx+ robotSizeConfig.ly)*r.h)/ robotSizeConfig.wheelR,
                (r.vector.x + r.vector.y - feedforwardConfig.hSlip*(robotSizeConfig.lx+ robotSizeConfig.ly)*r.h)/ robotSizeConfig.wheelR
        );
    }

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(
                new RegisterNewFeedbackReferenceListener(this::setFeedbackReference));
        EventBus.getListenersRegistration().invoke(
                new RegisterNewFeedforwardReferenceListener(this::setFeedforwardReference));

        EventBus.getListenersRegistration().invoke(
                new RegisterNewPositionListener(this::setPosition));
        EventBus.getListenersRegistration().invoke(
                new RegisterNewVelocityListener(this::setVelocity));
    }


}
