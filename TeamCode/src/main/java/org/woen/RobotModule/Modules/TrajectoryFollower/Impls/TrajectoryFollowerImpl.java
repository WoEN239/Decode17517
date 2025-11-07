package org.woen.RobotModule.Modules.TrajectoryFollower.Impls;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Trajectory.Trajectory;
import org.woen.Util.Vectors.Pose;

public class TrajectoryFollowerImpl implements TrajectoryFollower {
    private Trajectory trajectory = new Trajectory();
    private final FeedforwardReferenceObserver feedforwardObserver = new FeedforwardReferenceObserver();
    private final FeedbackReferenceObserver feedbackObserver = new FeedbackReferenceObserver();
    private final ElapsedTime timer = new ElapsedTime();

    @Override
    public void update() {
        double time = timer.seconds();
        Pose vel = new Pose(trajectory.getAngularVelocity(time), trajectory.getVelocity(time));
        Pose nextVel = new Pose(trajectory.getAngularVelocity(time), trajectory.getVelocity(time));
        Pose pose = new Pose(trajectory.getVelocity(time).getAngle(), trajectory.getPosition(time));

        feedbackObserver.notifyListeners(new FeedbackReference(pose, vel));
        feedforwardObserver.notifyListeners(new FeedforwardReference(vel, nextVel));

    }

    private void set(Trajectory trajectory) {
        this.trajectory = trajectory;
    }

}
