package org.woen.Autonom.Pools;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.Gun.Arcitecture.GunAtEatEvent;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewPositionListener;
import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

public abstract class WaypointPool {
    private Pose pose = new Pose(0,0,0);
    public void setPose(Pose pose){this.pose = pose;}
    protected Double angleToGoal(){return Math.PI + MatchData.team.goalPose.minus(pose.vector).getAngle();}
    protected Double angleTo(Vector2d p){return p.minus(pose.vector).getAngle();}
    public void setGunIsEat(GunAtEatEvent event){isGunEat = event.getData();}
    protected Boolean isGunEat = false;
    public WaypointPool() {
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));
        EventBus.getInstance().subscribe(GunAtEatEvent.class,this::setGunIsEat);
    }
}
