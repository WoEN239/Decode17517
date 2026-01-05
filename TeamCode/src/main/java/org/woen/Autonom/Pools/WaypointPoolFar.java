package org.woen.Autonom.Pools;

import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.AutonomTask;
import org.woen.Autonom.WayPoint;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.Gun.Arcitecture.GunAtEatEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.Util.Vectors.Pose;

public class WaypointPoolFar {
    PositionPoolFar pool = new PositionPoolFar();
    public Boolean isGunEat = false;

    public void setGunCommand(NewGunCommandAvailable event){
        isGunEat = event.getData() == GUN_COMMAND.EAT;
    }

    private Pose pose = new Pose(0,0,0);
    public void setPose(Pose pose){this.pose = pose;}
    private Double angleToGoal(){
        return Math.PI + MatchData.team.goalPose.minus(pose.vector).getAngle();
    }

    public WaypointPoolFar() {
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));
        EventBus.getInstance().subscribe(GunAtEatEvent.class,this::setGunIs);
        EventBus.getInstance().subscribe(NewGunCommandAvailable.class,this::setGunCommand);
    }

    public void setGunIs(GunAtEatEvent event){
        isGunEat = true;
    }

    public WayPoint firstAim = new WayPoint(
            new Runnable[]{
                    ()-> RobotLog.dd("auto","firstAim")
            },true, MatchData.start.pose,pool.fire.plus(new Pose( 0,5,0))
    ).setName("firstAim").setEndAngle(this::angleToGoal);

    public WayPoint fire1 = new WayPoint(
            new AutonomTask(
                    ()-> isGunEat,
                    ()->RobotLog.dd("auto","firstFire"),
                    ()->EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true,pool.fire,pool.fire
    ).setName("firstFire").setEndDetect(10).setEndAngle(this::angleToGoal);

    public WayPoint fire2 = new WayPoint(
            new AutonomTask(
                    ()-> isGunEat,
                    ()->RobotLog.dd("auto","firstFire"),
                    ()->EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true,pool.fire,pool.fire
    ).setName("firstFire").setEndDetect(30).setEndAngle(this::angleToGoal);

    public WayPoint fire3 = new WayPoint(
            new AutonomTask(
                    ()-> isGunEat,
                    ()->RobotLog.dd("auto","firstFire"),
                    ()->EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true,pool.fire,pool.fire
    ).setName("firstFire").setEndDetect(10).setEndAngle(this::angleToGoal);

    public WayPoint fire4 = new WayPoint(
            new AutonomTask(
                    ()-> isGunEat,
                    ()->RobotLog.dd("auto","fire4"),
                    ()->EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true,pool.fire,pool.fire
    ).setName("fire4").setEndDetect(10).setEndAngle(this::angleToGoal);

    public WayPoint fire5 = new WayPoint(
            new AutonomTask(
                    ()-> isGunEat,
                    ()->RobotLog.dd("auto","fire5"),
                    ()->EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true,pool.fire,pool.fire
    ).setName("fire5").setEndDetect(30).setEndAngle(this::angleToGoal);

    public WayPoint rotate1 = new WayPoint(
            new Runnable[]{
                    ()-> RobotLog.dd("auto","rotate")
            },false,pool.fire,pool.rotate1
    ).setName("firstRotate").setEndDetect(30);

    public WayPoint rotate2 = new WayPoint(
            new Runnable[]{
                    ()-> RobotLog.dd("auto","rotate")
            },false,pool.fire,pool.rotate2
    ).setName("secondRotate").setEndDetect(30);

    public WayPoint rotate3 = new WayPoint(
            new Runnable[]{
                    ()-> RobotLog.dd("auto","rotate")
            },false,pool.fire,pool.rotate3
    ).setName("secondRotate").setEndDetect(30);

    public WayPoint rotate4 = new WayPoint(
            new Runnable[]{
                    ()-> RobotLog.dd("auto","rotate")
            },false,pool.fire,pool.rotate4
    ).setName("secondRotate").setEndDetect(30);


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
    ).setName("secondAim").setVel(50).setEndAngle(this::angleToGoal).setEndDetect(10);

    public WayPoint secondEat = new WayPoint(
            new Runnable[]{
                    ()-> RobotLog.dd("auto","secondEat")
            },
            false,pool.secondEat
    ).setName("firstEat").setVel(80).setEndDetect(10);

    public WayPoint thirdAim = new WayPoint(
            new Runnable[]{
                    ()->RobotLog.dd("auto","thirdAim")
            },
            false,pool.thirdAim
    ).setName("thirdAim").setVel(50).setEndAngle(this::angleToGoal).setEndDetect(10);

    public WayPoint thirdEat = new WayPoint(
            new Runnable[]{
                    ()-> RobotLog.dd("auto","secondEat")
            },
            false,pool.thirdEat
    ).setName("firstEat").setVel(120).setEndDetect(30);

    public WayPoint thirdEatRotate = new WayPoint(
            new Runnable[]{
                    ()-> RobotLog.dd("auto","thirdEatRotate")
            },
            false,pool.thirdEatRotate
    ).setName("firstEat").setVel(120).setEndDetect(60);

    public WayPoint forthAim = new WayPoint(
            new Runnable[]{
                    ()->RobotLog.dd("auto","thirdAim")
            },
            false,pool.forthAim
    ).setName("thirdAim").setVel(50).setEndAngle(this::angleToGoal).setEndDetect(10);


    public WayPoint forthEat = new WayPoint(
            new Runnable[]{
                    ()-> RobotLog.dd("auto","secondEat")
            },
            false,pool.forthEat
    ).setName("firstEat").setVel(120).setEndDetect(10);

    public WayPoint fiveAim = new WayPoint(
            new Runnable[]{
                    ()->RobotLog.dd("auto","thirdAim")
            },
            true,pool.fiveAim
    ).setName("thirdAim").setVel(50).setEndAngle(this::angleToGoal).setEndDetect(25);

    public WayPoint park = new WayPoint(
            AutonomTask.Stub,
            true,pool.fire,new Pose(0,120,-57)
    ).setVel(120);


    public WayPoint fire1Pat = new WayPoint(
            new AutonomTask(
                    ()-> isGunEat,
                    ()->RobotLog.dd("auto","firstFire"),
                    ()->EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.PATTERN_FIRE))
            ),
            true,pool.fire,pool.fire
    ).setName("firstFire").setEndDetect(10).setEndAngle(this::angleToGoal);

    public WayPoint fire2Pat = new WayPoint(
            new AutonomTask(
                    ()-> isGunEat,
                    ()->RobotLog.dd("auto","firstFire"),
                    ()->EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.PATTERN_FIRE))
            ),
            true,pool.fire,pool.fire
    ).setName("firstFire").setEndDetect(30).setEndAngle(this::angleToGoal);

    public WayPoint fire3Pat = new WayPoint(
            new AutonomTask(
                    ()-> isGunEat,
                    ()->RobotLog.dd("auto","firstFire"),
                    ()->EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.PATTERN_FIRE))
            ),
            true,pool.fire,pool.fire
    ).setName("firstFire").setEndDetect(10).setEndAngle(this::angleToGoal);
}

