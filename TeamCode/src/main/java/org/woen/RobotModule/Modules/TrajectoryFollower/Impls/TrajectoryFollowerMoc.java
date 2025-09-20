package org.woen.RobotModule.Modules.TrajectoryFollower.Impls;

import com.acmerobotics.dashboard.FtcDashboard;

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

    private final Provider<Double> velH = new Provider<>(0d);
    private final Provider<Double> velY = new Provider<>(0d);
    private final Provider<Double> velX = new Provider<>(0d);

    private final Provider<Double> posH = new Provider<>(0d);
    private final Provider<Double> posY = new Provider<>(0d);
    private final Provider<Double> posX = new Provider<>(0d);

    @Override
    public void update() {
        feedforwardObserver.notifyListeners(new FeedforwardReference(new Pose(velH.get(),velX.get(),velY.get()),
                                                                     new Pose(0,0,0)));

        feedbackObserver.notifyListeners(new FeedbackReference(new Pose(posH.get(),posX.get(),posY.get()),
                                                               new Pose(velH.get(),velX.get(),velY.get())));
    }

    @Override
    public void init() {
        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","posH",posH);
        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","posX",posX);
        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","posY",posY);

        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","velH",velH);
        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","velX",velX);
        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","velY",velY);
    }
}
