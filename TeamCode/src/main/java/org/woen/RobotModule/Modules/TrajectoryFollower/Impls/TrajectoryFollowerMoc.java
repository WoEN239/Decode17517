package org.woen.RobotModule.Modules.TrajectoryFollower.Impls;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.RegisterNewVelocityListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.RegisterFeedbackReferenceListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.RegisterFeedforwardReferenceListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Telemetry.ConfigurableVariables.Provider;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Angel.AngelUtil;
import org.woen.Util.MotionProfile.TrapezoidMotionProfile;
import org.woen.Util.Vectors.Pose;

public class TrajectoryFollowerMoc implements TrajectoryFollower {
    private final FeedforwardReferenceObserver feedforwardObserver = new FeedforwardReferenceObserver();
    private final FeedbackReferenceObserver feedbackObserver = new FeedbackReferenceObserver();

    private Pose position = MatchData.startPosition;
    private Pose velocity = new Pose(0,0,0);
    public void setPosition(Pose position) {
        this.position = position;
    }
    public void setVelocity(Pose velocity) {
        this.velocity = velocity;
    }

    private final Provider<Double> profileAccel   = new Provider<>(5d);
    private final Provider<Double> profileMaxVel  = new Provider<>(3d);
    private final Provider<Double> profilePos     = new Provider<>(15d);

    private final Provider<Double> velH = new Provider<>(0d);
    private final Provider<Double> velY = new Provider<>(0d);
    private final Provider<Double> velX = new Provider<>(0d);

    private final Provider<Double> posH = new Provider<>(0d);
    private final Provider<Double> posY = new Provider<>(0d);
    private final Provider<Double> posX = new Provider<>(0d);

    private final Provider<String> mode = new Provider<>("manual");
    private final Provider<String> axis = new Provider<>("h");

    private TrapezoidMotionProfile motionProfile = new TrapezoidMotionProfile(profileAccel.get(),profileMaxVel.get(),profilePos.get(),position.h,velocity.h);
    private final ElapsedTime timer = new ElapsedTime();

    @Override
    public void update() {
        if(mode.get().equals("manual")) {
            feedforwardObserver.notifyListeners(new FeedforwardReference(new Pose(velH.get(), velX.get(), velY.get()),
                    new Pose(0, 0, 0)));

            feedbackObserver.notifyListeners(new FeedbackReference(new Pose(posH.get(), posX.get(), posY.get()),
                    new Pose(velH.get(), velX.get(), velY.get())));
        }else if(mode.get().equals("profile")){

            if(timer.seconds()> (motionProfile.duration + 3) || Double.isNaN(motionProfile.duration) ){
                motionProfile = new TrapezoidMotionProfile(profileAccel.get(), profileMaxVel.get(), profilePos.get(), position.h, velocity.h);
                timer.reset();
            }

            double posTarget = motionProfile.getPos(timer.seconds());
            double velTarget = motionProfile.getVel(timer.seconds());
            double accTarget = motionProfile.getAccel(timer.seconds());
            Telemetry.getInstance().add("posTarget", AngelUtil.normalize(posTarget));
            Telemetry.getInstance().add("velTarget", velTarget);
            Telemetry.getInstance().add("accTarget", accTarget);
            Telemetry.getInstance().add("posValue", position.h);
            Telemetry.getInstance().add("velValue", velocity.h);
            Telemetry.getInstance().add("t", timer.seconds());

            if(axis.get().equals("h")) {
                feedbackObserver.notifyListeners(new FeedbackReference(new Pose(posTarget, 0, 0),
                        new Pose(velTarget, 0, 0)));

                feedforwardObserver.notifyListeners(new FeedforwardReference(new Pose(velTarget, 0, 0),
                        new Pose(accTarget, 0, 0)));
            }else if(axis.get().equals("x")){
                feedbackObserver.notifyListeners(new FeedbackReference(new Pose(0, posTarget, 0),
                        new Pose(0, velTarget, 0)));

                feedforwardObserver.notifyListeners(new FeedforwardReference(new Pose(0, accTarget, 0),
                        new Pose(0, accTarget, 0)));
            }

        }
    }

    @Override
    public void init() {
        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","posH",posH);
        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","posX",posX);
        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","posY",posY);

        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","velH",velH);
        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","velX",velX);
        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","velY",velY);

        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","mode",mode);
        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow","axis",axis);

        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow_profile","accel",profileAccel);
        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow_profile","vel",profileMaxVel);
        FtcDashboard.getInstance().addConfigVariable("manual_trajectory_follow_profile","pos",profilePos);

        EventBus.getListenersRegistration().invoke(
                new RegisterNewPositionListener(this::setPosition));
        EventBus.getListenersRegistration().invoke(
                new RegisterNewVelocityListener(this::setVelocity));

    }

}
