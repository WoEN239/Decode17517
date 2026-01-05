package org.woen.RobotModule.Modules.Localizer.Impls;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.Localizer.Architecture.PositionObserver;
import org.woen.RobotModule.Modules.Localizer.Interface.PositionLocalizer;
import org.woen.RobotModule.Modules.Localizer.Architecture.VelocityObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.RegisterNewFeedbackReferenceListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.RegisterNewFeedforwardReferenceListener;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

public class PositionLocalizerMoc implements PositionLocalizer {
    private final PositionObserver positionObserver = new PositionObserver();
    private final VelocityObserver velocityObserver = new VelocityObserver();

    private Pose velocity = new Pose(0,0,0);
    private Pose accel = new Pose(0,0,0);
    private Pose position = MatchData.getStartPose();
    private final ElapsedTime timer = new ElapsedTime();

    private boolean isFirstRun = true;
    @Override
    public void update() {
        if(isFirstRun){
            timer.reset();
        }
        double dt = timer.seconds();
        timer.reset();
        Pose dp =  velocity.multiply(dt).plus(accel.multiply(dt*dt*0.5));
        double dh = dp.h;
        Vector2d dvector = new Vector2d(dp.x,0).rotate(position.h);
        position = position.plus(new Pose(dh,dvector));
        positionObserver.notifyListeners(position);
        velocityObserver.notifyListeners(velocity);
        isFirstRun = false;
        Telemetry.getInstance().add("mock localizer pos",position);
        Telemetry.getInstance().add("mock localizer vel",velocity);
    }


    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(new RegisterNewFeedforwardReferenceListener(this::setVelocity));
        EventBus.getListenersRegistration().invoke(new RegisterNewFeedbackReferenceListener(this::setPose));
    }
    private void setVelocity(FeedforwardReference r){
        velocity = r.vel;
        accel    = r.accel;
    }
    private void setPose(FeedbackReference r){
        position = new Pose(r.pos.h,position.vector);
    }
}