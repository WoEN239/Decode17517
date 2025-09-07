package org.woen.RobotModule.Impls.TrajectoryFollower;

import static org.woen.Config.ControlSystemConstant.T;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.RobotModule.Impls.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Impls.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Impls.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.Trajectory.Trajectory;
import org.woen.Util.Vectors.Pose;

public class TrajectoryFollower implements IRobotModule {
    private Trajectory trajectory = new Trajectory();
    private final FeedforwardReferenceObserver feedforwardObserver = new FeedforwardReferenceObserver();
    private final FeedbackReferenceObserver feedbackObserver       = new FeedbackReferenceObserver();
    private final ElapsedTime timer = new ElapsedTime();

    @Override
    public void update() {
        double time = timer.seconds();
        Pose vel  = new Pose(trajectory.getAngularVelocity(time), trajectory.getVelocity(time));
        Pose nextVel  = new Pose(trajectory.getAngularVelocity(time+T), trajectory.getVelocity(time+T));
        Pose pose = new Pose(trajectory.getVelocity(time).getAngle(),trajectory.getPosition(time));

        feedbackObserver.notifyListeners(pose);
        feedforwardObserver.notifyListeners(new FeedforwardReference(vel,nextVel));

    }

    private void set(Trajectory trajectory){
        this.trajectory = trajectory;
    }

}
