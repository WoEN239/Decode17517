package org.woen.RobotModule.Impls.DriveTrain;

import org.woen.Architecture.EventBus.Bus.EventBus;
import org.woen.RobotModule.Impls.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Impls.TrajectoryFollower.Arcitecture.Feedback.RegisterFeedbackReferenceListener;
import org.woen.RobotModule.Impls.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Impls.TrajectoryFollower.Arcitecture.Feedforward.RegisterFeedforwardReferenceListener;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.Util.Vectors.Pose;

public class DriveTrain implements IRobotModule {
    private FeedforwardReference feedforwardReference;
    private Pose feedbackReference;

    public void setFeedforwardReference(FeedforwardReference feedforwardReference) {
        this.feedforwardReference = feedforwardReference;
    }

    public void setFeedbackReference(Pose feedbackReference) {
        this.feedbackReference = feedbackReference;
    }

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(
                new RegisterFeedbackReferenceListener(this::setFeedbackReference));

        EventBus.getListenersRegistration().invoke(
                new RegisterFeedforwardReferenceListener(this::setFeedforwardReference));
    }

}
