package org.woen.RobotModule.Modules.TrajectoryFollower.Impls;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Telemetry.ConfigurableVariables.Provider;
import org.woen.Util.Vectors.Pose;

public class TrajectoryFollowerMoc implements TrajectoryFollower {
    private final FeedforwardReferenceObserver feedforwardObserver = new FeedforwardReferenceObserver();
    private final FeedbackReferenceObserver feedbackObserver = new FeedbackReferenceObserver();

    private final Provider<Pose> positionProvider = new Provider<>(MatchData.startPosition);
    private final Provider<Pose> velocityProvider = new Provider<>(new Pose(0,0,0));


    @Override
    public void update() {
        feedforwardObserver.notifyListeners(new FeedforwardReference(velocityProvider.get(),new Pose(0,0,0)));
        feedbackObserver.notifyListeners(new FeedbackReference(positionProvider.get(),velocityProvider.get()));
    }

    @Override
    public void init() {
        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","pos",positionProvider);
        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","vel",velocityProvider);
    }
}
