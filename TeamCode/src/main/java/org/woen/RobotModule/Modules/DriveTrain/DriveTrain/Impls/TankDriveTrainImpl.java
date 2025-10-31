package org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Impls;

import static org.woen.Config.ControlSystemConstant.B;
import static org.woen.Config.ControlSystemConstant.hSlip;
import static org.woen.Config.ControlSystemConstant.wheelR;
import static org.woen.Config.ControlSystemConstant.xFeedforwardKA;
import static org.woen.Config.ControlSystemConstant.xFeedforwardKV;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.ControlSystemConstant;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.ReplaceFeedbackControllerEvent;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.TankFeedbackController;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedforwardController.FeedforwardController;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Interface.DriveTrain;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelValueMap;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelsVoltageObserver;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewLocalPositionListener;
import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.RegisterNewVelocityListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.RegisterNewFeedbackReferenceListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.RegisterNewFeedforwardReferenceListener;
import org.woen.Util.Vectors.Pose;

public class TankDriveTrainImpl implements DriveTrain {
    private FeedforwardReference feedforwardReference = new FeedforwardReference(new Pose(0,0,0),
            new Pose(0,0,0));
    private FeedbackReference feedbackReference       = new FeedbackReference(new Pose(0,0,0),
            new Pose(0,0,0));

    private Pose position = new Pose(0,0,0);
    private Pose velocity = new Pose(0,0,0);

    public void setLocalPosition(Pose position) {
        this.position = position;
    }

    public void setVelocity(Pose velocity) {
        this.velocity = velocity;
    }

    private TankFeedbackController feedbackController = new TankFeedbackController(
            ControlSystemConstant.xPid,
            ControlSystemConstant.hPid);

    private void replaceFeedbackController(ReplaceFeedbackControllerEvent event){
        feedbackController = event.getData();
    }


    private FeedforwardController feedforwardController = new FeedforwardController(
            new WheelValueMap(xFeedforwardKV, xFeedforwardKV, xFeedforwardKV, xFeedforwardKV),
            new WheelValueMap(xFeedforwardKA, xFeedforwardKA, xFeedforwardKA, xFeedforwardKA));

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
                new WheelValueMap(xFeedforwardKV, xFeedforwardKV, xFeedforwardKV, xFeedforwardKV),
                new WheelValueMap(xFeedforwardKA, xFeedforwardKA, xFeedforwardKA, xFeedforwardKA));

        WheelValueMap feedforward = feedforwardController.computeU(toWheelsFromRobotVelocities(feedforwardReference.now),
                toWheelsFromRobotVelocities(feedforwardReference.accel));

        wheelsVoltageObserver.notifyListeners(feedforward.plus(feedback));
    }

    private WheelValueMap toWheelsFromRobotVoltage(Pose r){
        return new WheelValueMap(
                r.vector.x-r.h,
                r.vector.x+r.h,
                r.vector.x+r.h,
                r.vector.x-r.h
        );
    }

    private WheelValueMap toWheelsFromRobotVelocities(Pose r){
        return new WheelValueMap(
                (r.vector.x - hSlip*r.h*B*0.5)/wheelR,
                (r.vector.x + hSlip*r.h*B*0.5)/wheelR,
                (r.vector.x + hSlip*r.h*B*0.5)/wheelR,
                (r.vector.x - hSlip*r.h*B*0.5)/wheelR
        );
    }

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(
                new RegisterNewFeedbackReferenceListener(this::setFeedbackReference));
        EventBus.getListenersRegistration().invoke(
                new RegisterNewFeedforwardReferenceListener(this::setFeedforwardReference));

        EventBus.getListenersRegistration().invoke(
                new RegisterNewLocalPositionListener(this::setLocalPosition));
        EventBus.getListenersRegistration().invoke(
                new RegisterNewVelocityListener(this::setVelocity));

        EventBus.getInstance().subscribe(ReplaceFeedbackControllerEvent.class,this::replaceFeedbackController);

    }

}
