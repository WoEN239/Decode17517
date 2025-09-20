package org.woen.RobotModule.Modules.Localizer.Position.Impls;

import com.acmerobotics.dashboard.FtcDashboard;

import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.LocalPositionObserver;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.PositionObserver;
import org.woen.RobotModule.Modules.Localizer.Position.Interface.PositionLocalizer;
import org.woen.Telemetry.ConfigurableVariables.Provider;
import org.woen.Util.Vectors.Pose;

public class PositionLocalizerMoc implements PositionLocalizer {
    private final PositionObserver positionObserver = new PositionObserver();
    private final LocalPositionObserver localPositionObserver = new LocalPositionObserver();

    private final Provider<Double> positionProviderX = new Provider<>(MatchData.startPosition.vector.x);
    private final Provider<Double> positionProviderY = new Provider<>(MatchData.startPosition.vector.y);
    private final Provider<Double> positionProviderH = new Provider<>(MatchData.startPosition.h);
    private final Provider<Pose> localPositionProvider = new Provider<>(new Pose(0,0,0));

    @Override
    public void update() {
       positionObserver.notifyListeners(new Pose(
               positionProviderH.get(),
               positionProviderX.get(),
               positionProviderY.get()
       ));
       localPositionObserver.notifyListeners(localPositionProvider.get());
    }

    @Override
    public void init() {
        FtcDashboard.getInstance().addConfigVariable("manual localizer","posX",positionProviderX);
        FtcDashboard.getInstance().addConfigVariable("manual localizer","posH",positionProviderH);
        FtcDashboard.getInstance().addConfigVariable("manual localizer","posY",positionProviderY);
      //  FtcDashboard.getInstance().addConfigVariable("manual localizer","local_pos",localPositionProvider);
    }
}
