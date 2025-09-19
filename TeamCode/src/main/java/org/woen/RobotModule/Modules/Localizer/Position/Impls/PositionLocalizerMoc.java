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

    private final Provider<Pose> positionProvider = new Provider<>(MatchData.startPosition);
    private final Provider<Pose> localPositionProvider = new Provider<>(new Pose(0,0,0));

    @Override
    public void update() {
       positionObserver.notifyListeners(positionProvider.get());
       localPositionObserver.notifyListeners(localPositionProvider.get());
    }

    @Override
    public void init() {
        FtcDashboard.getInstance().addConfigVariable("manual_localizer","pos",positionProvider);
        FtcDashboard.getInstance().addConfigVariable("manual_localizer","local_pos",localPositionProvider);
    }
}
