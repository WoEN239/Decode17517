package org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Impls;

import static org.woen.Config.ControlSystemConstant.RobotSizeConfig.B;
import static org.woen.Config.ControlSystemConstant.FeedforwardConfig.hSlip;
import static org.woen.Config.ControlSystemConstant.RobotSizeConfig.wheelR;
import static org.woen.Config.ControlSystemConstant.FeedforwardConfig.xFeedforwardKA;
import static org.woen.Config.ControlSystemConstant.FeedforwardConfig.xFeedforwardKV;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.ControlSystemConstant;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.ReplaceFeedbackControllerEvent;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.TankFeedbackController;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedforwardController.FeedforwardController;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Interface.DriveTrain;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelValueMap;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelsVoltageObserver;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewLocalPositionListener;
import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.RegisterNewLocalVelocityListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.RegisterNewFeedbackReferenceListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.RegisterNewFeedforwardReferenceListener;
import org.woen.Util.Vectors.Pose;

public class TankDriveTrainImpl implements DriveTrain {

    @Override
    public void lateUpdate() {
        WheelValueMap feedback = toWheelsVoltage(
                feedbackController.computeU(feedbackReference.pos,position,
                        feedbackReference.vel,velocity));

        FeedforwardController feedforwardController = new FeedforwardController(
                new WheelValueMap(xFeedforwardKV, xFeedforwardKV, xFeedforwardKV, xFeedforwardKV),
                new WheelValueMap(xFeedforwardKA, xFeedforwardKA, xFeedforwardKA, xFeedforwardKA));

        WheelValueMap feedforward = feedforwardController.computeU(toWheelsVelocities(feedforwardReference.now),
                toWheelsVelocities(feedforwardReference.accel));

        wheelsVoltageObserver.notifyListeners(feedforward.plus(feedback));
    }

    private WheelValueMap toWheelsVoltage(Pose r){
        return new WheelValueMap(
                r.vector.x-r.h,
                r.vector.x+r.h,
                r.vector.x+r.h,
                r.vector.x-r.h
        );
    }

    private WheelValueMap toWheelsVelocities(Pose r){
        return new WheelValueMap(
                (r.vector.x - hSlip*r.h*B*0.5)/wheelR,
                (r.vector.x + hSlip*r.h*B*0.5)/wheelR,
                (r.vector.x + hSlip*r.h*B*0.5)/wheelR,
                (r.vector.x - hSlip*r.h*B*0.5)/wheelR
        );
    }

    private FeedforwardReference feedforwardReference = new FeedforwardReference(new Pose(0,0,0),
            new Pose(0,0,0));
    private FeedbackReference feedbackReference       = new FeedbackReference(new Pose(0,0,0),
            new Pose(0,0,0));

    //all in robot reference frame
    private Pose position = new Pose(0,0,0);
    private Pose velocity = new Pose(0,0,0);

    public void setLocalPosition(Pose position) {
        this.position = position;
    }

    public void setLocalVelocity(Pose velocity) {
        this.velocity = velocity;
    }

    private TankFeedbackController feedbackController = new TankFeedbackController(
            ControlSystemConstant.FeedbackConfig.xPid,
            ControlSystemConstant.FeedbackConfig.hPid);

    private void replaceFeedbackController(ReplaceFeedbackControllerEvent event){
        feedbackController = event.getData();
    }

    public void setFeedforwardReference(FeedforwardReference feedforwardReference) {
        this.feedforwardReference = feedforwardReference;
    }

    public void setFeedbackReference(FeedbackReference  feedbackReference) {
        this.feedbackReference = feedbackReference;
    }

    private final WheelsVoltageObserver wheelsVoltageObserver = new WheelsVoltageObserver();

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(
                new RegisterNewFeedbackReferenceListener(this::setFeedbackReference));
        EventBus.getListenersRegistration().invoke(
                new RegisterNewFeedforwardReferenceListener(this::setFeedforwardReference));

        EventBus.getListenersRegistration().invoke(
                new RegisterNewLocalPositionListener(this::setLocalPosition));
        EventBus.getListenersRegistration().invoke(
                new RegisterNewLocalVelocityListener(this::setLocalVelocity));

        EventBus.getInstance().subscribe(ReplaceFeedbackControllerEvent.class,this::replaceFeedbackController);

    }

}
