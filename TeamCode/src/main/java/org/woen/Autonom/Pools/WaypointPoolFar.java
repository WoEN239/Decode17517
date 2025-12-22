package org.woen.Autonom.Pools;

import static java.lang.Math.PI;

import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.AutonomTask;
import org.woen.Autonom.WayPoint;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.Gun.Arcitecture.GunAtEatEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.Util.Vectors.Pose;

public class WaypointPoolFar {
    PositionPoolFar pool = new PositionPoolFar();
    public Boolean isGunEat = false;

    public void setGunCommand(NewGunCommandAvailable event){
        isGunEat = event.getData() == GUN_COMMAND.EAT;
    }

    public WaypointPoolFar() {
        EventBus.getInstance().subscribe(GunAtEatEvent.class,this::setGunIs);
        EventBus.getInstance().subscribe(NewGunCommandAvailable.class,this::setGunCommand);
    }

    public void setGunIs(GunAtEatEvent event){
        isGunEat = true;
    }

    public WayPoint firstAim = new WayPoint(
            new Runnable[]{
                    ()-> RobotLog.dd("auto","firstAim")
            },true, MatchData.start.pose,pool.fire
    ).setName("firstAim");

    public WayPoint fire1 = new WayPoint(
            new AutonomTask(
                    ()-> isGunEat,
                    ()->RobotLog.dd("auto","firstFire"),
                    ()->EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true,pool.fire,pool.fire
    ).setName("firstFire");
    public WayPoint fire2 = new WayPoint(
            new AutonomTask(
                    ()-> isGunEat,
                    ()->RobotLog.dd("auto","firstFire"),
                    ()->EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true,pool.fire,pool.fire
    ).setName("firstFire");
    public WayPoint fire3 = new WayPoint(
            new AutonomTask(
                    ()-> isGunEat,
                    ()->RobotLog.dd("auto","firstFire"),
                    ()->EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true,pool.fire,pool.fire
    ).setName("firstFire");

    public WayPoint rotate = new WayPoint(
            new Runnable[]{
                    ()-> RobotLog.dd("auto","rotate")
            },false,pool.fire,new Pose(PI,pool.fire.vector)
    ).setName("firstRotate");

    public WayPoint firstEat = new WayPoint(
            new Runnable[]{
                    ()-> RobotLog.dd("auto","firstEat"),
            },
            false,pool.firstEat
    ).setName("firstEat").setVel(80);

    public WayPoint secondAim = new WayPoint(
            new Runnable[]{
                    ()->RobotLog.dd("auto","secondAim")
            },
            false,pool.secondAim
    ).setName("secondAim").setVel(80);


    public WayPoint secondEat = new WayPoint(
            new Runnable[]{
                    ()-> RobotLog.dd("auto","secondEat")
            },
            false,pool.secondEat
    ).setName("firstEat").setVel(80);

}

