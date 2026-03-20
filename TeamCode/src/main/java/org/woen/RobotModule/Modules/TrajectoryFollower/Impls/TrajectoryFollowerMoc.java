package org.woen.RobotModule.Modules.TrajectoryFollower.Impls;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewVelocityListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.TankFeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.TankFeedbackReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Telemetry.ConfigurableVariables.SimpleProvider;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Angel.AngleUtil;
import org.woen.Util.MotionProfile.TrapezoidMotionProfile;
import org.woen.Util.Vectors.Pose;

public class TrajectoryFollowerMoc implements TrajectoryFollower {
    private final FeedforwardReferenceObserver feedforwardObserver = new FeedforwardReferenceObserver();
    private final TankFeedbackReferenceObserver feedbackObserver = new TankFeedbackReferenceObserver();

    private Pose position = new Pose(0,0,0);
    private Pose velocity = new Pose(0,0,0);
    public void setPosition(Pose position) {
        this.position = position;
    }
    public void setVelocity(Pose velocity) {
        this.velocity = velocity;
    }

    private final SimpleProvider<Double> profileAccel   = new SimpleProvider<>(25d);
    private final SimpleProvider<Double> profileMaxVel  = new SimpleProvider<>(35d);
    private final SimpleProvider<Double> profilePos     = new SimpleProvider<>(55d);

    private final SimpleProvider<Double> velH = new SimpleProvider<>(0d);
    private final SimpleProvider<Double> velY = new SimpleProvider<>(0d);
    private final SimpleProvider<Double> velX = new SimpleProvider<>(0d);

    private final SimpleProvider<Double> posH = new SimpleProvider<>(0d);
    private final SimpleProvider<Double> posY = new SimpleProvider<>(0d);
    private final SimpleProvider<Double> posX = new SimpleProvider<>(0d);

    private final SimpleProvider<String> mode = new SimpleProvider<>("manual");
    private final SimpleProvider<String> axis = new SimpleProvider<>("h");

    private TrapezoidMotionProfile motionProfile = new TrapezoidMotionProfile(profileAccel.get(),profileMaxVel.get(),profilePos.get(),position.h,velocity.h);
    private final ElapsedTime timer = new ElapsedTime();

    private int dir = 1;

    @Override
    public void update() {
        if(mode.get().equals("manual")) {
            feedforwardObserver.notifyListeners(new FeedforwardReference(new Pose(velH.get(), velX.get(), velY.get()),
                    new Pose(0, 0, 0)));

            feedbackObserver.notifyListeners(new TankFeedbackReference(true,posH.get()));

            Telemetry.getInstance().add("x target",posX.get());
            Telemetry.getInstance().add("h target", AngleUtil.normalize(posH.get()));

            Telemetry.getInstance().add("h pos",position.h);
            Telemetry.getInstance().add("x pos",position.x);

        }else if(mode.get().equals("profile")){

            if(axis.get().equals("h")) {
                if (timer.seconds() > (motionProfile.duration + 3) || Double.isNaN(motionProfile.duration)) {
                    motionProfile = new TrapezoidMotionProfile(profileAccel.get(), profileMaxVel.get(),
                             profilePos.get()*dir + position.h, position.h, velocity.h);
                    dir = -dir;
                    timer.reset();
                }
            }else if(axis.get().equals("x")){
                if (timer.seconds() > (motionProfile.duration + 3) || Double.isNaN(motionProfile.duration)) {
                    motionProfile = new TrapezoidMotionProfile(profileAccel.get(), profileMaxVel.get(),
                            dir*profilePos.get() + position.vector.x, position.vector.x, velocity.vector.x);
                    dir = -dir;
                    timer.reset();
                }
            }

            double posTarget = motionProfile.getPos(timer.seconds());
            double velTarget = motionProfile.getVel(timer.seconds());
            double accTarget = motionProfile.getAccel(timer.seconds());

            Telemetry.getInstance().add("velTarget", velTarget);
            Telemetry.getInstance().add("accTarget", accTarget);
            Telemetry.getInstance().add("t", timer.seconds());
            Telemetry.getInstance().add("direction bdgd", dir);

            if(axis.get().equals("h")) {
                //feedbackObserver.notifyListeners(new TankFeedbackReference(posTarget,true);

                feedforwardObserver.notifyListeners(new FeedforwardReference(new Pose(velTarget, 0, 0),
                        new Pose(accTarget, 0, 0)));
                Telemetry.getInstance().add("posTarget", AngleUtil.normalize(posTarget));
                Telemetry.getInstance().add("posValue", position.h);
                Telemetry.getInstance().add("velValue", velocity.h);

            }else if(axis.get().equals("x")){
//                feedbackObserver.notifyListeners(new FeedbackReference(new Pose(0, posTarget, 0),
//                        new Pose(0, velTarget, 0)));
                feedforwardObserver.notifyListeners(new FeedforwardReference(new Pose(0, velTarget, 0),
                        new Pose(0, accTarget, 0)));

                Telemetry.getInstance().add("posValue", position.vector.x);
                Telemetry.getInstance().add("velValue", velocity.vector.x);
                Telemetry.getInstance().add("posTarget", posTarget);

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
