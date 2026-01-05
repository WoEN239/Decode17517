package org.woen.RobotModule.Modules.Localizer.Position.Impls;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.PositionObserver;
import org.woen.RobotModule.Modules.Localizer.Position.Interface.PositionLocalizer;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.VelocityObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.RegisterNewFeedforwardReferenceListener;
import org.woen.Util.Vectors.Pose;

public class PositionLocalizerMoc implements PositionLocalizer {
    private final PositionObserver positionObserver = new PositionObserver();
    private final VelocityObserver velocityObserver = new VelocityObserver();

    private Pose velocity = new Pose(0,0,0);
    private Pose accel = new Pose(0,0,0);
    private Pose position = new Pose(0,0,0);
    private final ElapsedTime timer = new ElapsedTime();

    private boolean isFirstRun = true;
    @Override
    public void update() {
        if(isFirstRun){
            timer.reset();
        }
        double dt = timer.seconds();
        Pose dp =  velocity.multiply(dt).plus(accel.multiply(dt*dt*0.5));
        position = position.plus(dp);
        positionObserver.notifyListeners(position);
        velocityObserver.notifyListeners(velocity);
        isFirstRun = false;
    }


    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(new RegisterNewFeedforwardReferenceListener(this::setVelocity));
    }
    private void setVelocity(FeedforwardReference r){
        velocity = r.vel;
        accel    = r.accel;
    }
}