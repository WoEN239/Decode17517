package org.woen.Autonom.Pools;

import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.AutonomTask;
import org.woen.Autonom.WayPoint;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.Gun.Arcitecture.GunAtEatEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;

public class WaypointPoolNear {
    PositionPoolNear pool = new PositionPoolNear();

    public Boolean isGunEat = false;

    public void setGunCommand(NewGunCommandAvailable event){
        isGunEat = event.getData() == GUN_COMMAND.EAT;
    }

    public WaypointPoolNear() {
        EventBus.getInstance().subscribe(GunAtEatEvent.class,this::setGunIs);
        EventBus.getInstance().subscribe(NewGunCommandAvailable.class,this::setGunCommand);
    }

    public void setGunIs(GunAtEatEvent event){
        isGunEat = true;
    }

    public WayPoint look = new WayPoint(
            new Runnable[]{
                    ()-> RobotLog.dd("auto","look")
            },true, MatchData.start.pose,pool.look
    );

    public WayPoint firstAim = new WayPoint(new Runnable[]{
            ()-> RobotLog.dd("auto","aim1")
    },false,pool.look,pool.firstAim
    ).setName("aim1");

    public WayPoint fire1 = new WayPoint(
            new AutonomTask(
                    ()-> isGunEat,
                    ()->RobotLog.dd("auto","fire1"),
                    ()->EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true,pool.fire,pool.fire
    ).setName("fire1");

    public WayPoint fire2 = new WayPoint(
            new AutonomTask(
                    ()-> isGunEat,
                    ()->RobotLog.dd("auto","fire2"),
                    ()->EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true,pool.fire,pool.fire
    ).setName("fire2");

    public WayPoint fire3 = new WayPoint(
            new AutonomTask(
                    ()-> isGunEat,
                    ()->RobotLog.dd("auto","fire3"),
                    ()->EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true,pool.fire,pool.fire
    ).setName("fire3");

    public WayPoint firstEat = new WayPoint(
        new AutonomTask(
                ()->true,
                ()->RobotLog.dd("auto","eat1")
        ),false,pool.firstEat
    ).setName("firstEat");

    public WayPoint secondAim = new WayPoint(
            new Runnable[]{
            ()->RobotLog.dd("auto","secondAim")
            }, false,pool.secondAim
    ).setName("secondAim");

    public WayPoint secondEat = new WayPoint(
            new AutonomTask(
                    ()->true,
                    ()->RobotLog.dd("auto","eat2")
            ),false,pool.secondEat
    ).setName("secondEat");

    public WayPoint thirdAim = new WayPoint(
            new Runnable[]{
                    ()->RobotLog.dd("auto","thirdAim")
            }, false,pool.thirdAim
    ).setName("thirdAim");
}
