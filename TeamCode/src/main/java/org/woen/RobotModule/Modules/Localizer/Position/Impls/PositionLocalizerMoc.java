package org.woen.RobotModule.Modules.Localizer.Position.Impls;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.LocalPositionObserver;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.PositionObserver;
import org.woen.RobotModule.Modules.Localizer.Position.Interface.PositionLocalizer;
import org.woen.RobotModule.Modules.Localizer.Velocity.Architecture.VelocityObserver;
import org.woen.RobotModule.Modules.Localizer.Velocity.Impls.VelocityLocalizerMoc;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.Telemetry.ConfigurableVariables.Provider;
import org.woen.Util.Vectors.Pose;

import java.util.concurrent.TimeUnit;

public class PositionLocalizerMoc implements PositionLocalizer {
    private final PositionObserver positionObserver = new PositionObserver();
    private final LocalPositionObserver localPositionObserver = new LocalPositionObserver();

    private final Provider<Double> positionProviderX = new Provider<>(MatchData.startPosition.vector.x);
    private final Provider<Double> positionProviderY = new Provider<>(MatchData.startPosition.vector.y);
    private final Provider<Double> positionProviderH = new Provider<>(MatchData.startPosition.h);
    private final Provider<Pose> localPositionProvider = new Provider<>(new Pose(0,0,0));

    private  FeedforwardReference feedforwardReference = new FeedforwardReference(new Pose(0,0,0), new Pose(0,0,0));

    ElapsedTime time = new ElapsedTime();

    

   public void setFeedforwardReference(FeedforwardReference feedforwardReference){
       this.feedforwardReference = feedforwardReference;
   }

    @Override
    public void update() {
       positionObserver.notifyListeners(new Pose(
               positionProviderH.get(),
               positionProviderX.get(),
               positionProviderY.get()
       ));

       localPositionObserver.notifyListeners(localPositionProvider.get());

        integratePose(new Pose(positionProviderX.get(),positionProviderY.get(), positionProviderH.get()));

    }

    public static double dt = 1/120;

    private Pose oldSpeed;

    private Pose oldPos;

    private Pose integrateVel(Pose v0){
        Pose v = v0;
        Pose a = feedforwardReference.accel;
        if(oldSpeed != v0) {
            v = v0.plus(a.multiply(dt));
        }
        time.reset();
        oldSpeed = v;
        return v;

    }

    private Pose integratePose(Pose x0){
        Pose x = x0;
        Pose vt = integrateVel(feedforwardReference.now);
       if(x != oldPos){

            x = x0.plus(vt.multiply(dt));
        }
       oldPos = x;
        return x;
    }

    @Override
    public void init() {
        FtcDashboard.getInstance().addConfigVariable("manual localizer","posX",positionProviderX);
        FtcDashboard.getInstance().addConfigVariable("manual localizer","posH",positionProviderH);
        FtcDashboard.getInstance().addConfigVariable("manual localizer","posY",positionProviderY);
        time.reset();
    }
}