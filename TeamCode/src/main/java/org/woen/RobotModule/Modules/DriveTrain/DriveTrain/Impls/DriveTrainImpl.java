package org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Impls;

import org.woen.Architecture.EventBus.Bus.EventBus;
import org.woen.Config.ControlSystemConstant;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Interface.DriveTrain;
import org.woen.RobotModule.Modules.DriveTrain.FeedbackController.FeedbackController;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.RegisterFeedbackReferenceListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.RegisterFeedforwardReferenceListener;

public class DriveTrainImpl implements DriveTrain {
    private FeedforwardReference feedforwardReference;
    private FeedbackReference feedbackReference;

    private FeedbackController feedbackController = new FeedbackController(
            ControlSystemConstant.xPid,
            ControlSystemConstant.hPid);

    public void setFeedforwardReference(FeedforwardReference feedforwardReference) {
        this.feedforwardReference = feedforwardReference;
    }

    public void setFeedbackReference(FeedbackReference  feedbackReference) {
        this.feedbackReference = feedbackReference;
    }

    @Override
    public void lateUpdate() {

    }

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(
                new RegisterFeedbackReferenceListener(this::setFeedbackReference));

        EventBus.getListenersRegistration().invoke(
                new RegisterFeedforwardReferenceListener(this::setFeedforwardReference));
    }

}
