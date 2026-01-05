package org.woen.Autonom.Pools;

import static java.lang.Math.PI;

import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Structure.AutonomTask;
import org.woen.Autonom.Structure.WayPoint;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.Util.Vectors.Pose;

public class WaypointPoolNear extends WaypointPool{
    PositionPoolNear pool = new PositionPoolNear();
    public WayPoint aim1 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "aim1")
            }, true, pool.fireFar.plus(new Pose(0, 5, 0))
    ).setName("aim1").setEndAngle(this::angleToGoal);

    public WayPoint fire1 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire1"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            false, pool.fireFar
    ).setName("fire1").setEndDetect(30).setEndAngle(this::angleToGoal);
    public WayPoint fire2 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire2"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            false, pool.fireFar
    ).setName("fire2").setEndDetect(30).setEndAngle(this::angleToGoal);
    public WayPoint fire3 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire3"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            false, pool.fireNear
    ).setName("fire3").setEndDetect(30).setEndAngle(this::angleToGoal);
    public WayPoint fire4 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire4"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            false, pool.fireNear
    ).setName("fire4").setEndDetect(30).setEndAngle(this::angleToGoal);
    public WayPoint fire5 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire5"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            false, pool.fireNear
    ).setName("fire5").setEndDetect(30).setEndAngle(this::angleToGoal);

    public WayPoint rotate1 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate1")
            }, false,  pool.fireFar
    ).setName("rotate1").setEndDetect(30).setEndAngle(()->angleTo(pool.eat1.vector));

    public WayPoint eat1 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eat1")
            },
            false, pool.eat1
    ).setName("eat1").setEndDetect(10);

    public WayPoint aim2 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "aim2")
            },
            true, pool.fireFar
    ).setName("aim2").setEndAngle(this::angleToGoal).setEndDetect(10);

    public WayPoint rotate2 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate2")
            }, false,  pool.fireFar
    ).setName("rotate2").setEndDetect(30).setEndAngle(()->angleTo(pool.eat2[0].vector));

    public WayPoint eat2 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eat2")
            },
            false, pool.eat2
    ).setName("eat2").setEndDetect(10).setEndAngle(()->PI+angleTo(pool.fireNear.vector));

    public WayPoint aim3 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "aim3")
            },
            true, pool.fireNear
    ).setName("aim3").setEndAngle(this::angleToGoal).setEndDetect(10);

    public WayPoint rotate3 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate3")
            }, false,  pool.fireNear
    ).setName("rotate3").setEndDetect(30).setEndAngle(()->angleTo(pool.eat3.vector));

    public WayPoint eat3 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eat3")
            },
            false, pool.eat3
    ).setName("eat3").setEndDetect(10).setEndAngle(()->PI+angleTo(pool.fireNear.vector));

    public WayPoint aim4 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "aim4")
            },
            true, pool.fireNear
    ).setName("aim4").setEndAngle(this::angleToGoal).setEndDetect(10);

    public WayPoint rotate4 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate4")
            }, false,  pool.fireNear
    ).setName("rotate4").setEndDetect(30).setEndAngle(()->angleTo(pool.eat4.vector));

    public WayPoint eat4 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eat4")
            },
            false, pool.eat4
    ).setName("eat4").setEndDetect(10).setEndAngle(()->PI+angleTo(pool.fireNear.vector));

    public WayPoint aim5 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "aim5")
            },
            true, pool.fireNear
    ).setName("aim5").setEndAngle(this::angleToGoal).setEndDetect(10);

    public WayPoint park = new WayPoint(
            new AutonomTask(
                    ()->true,
                    () -> RobotLog.dd("auto", "park")
            ),true, pool.park
    ).setName("eat4").setEndDetect(10);


}
class PositionPoolNear {
    public Pose fireFar  = new Pose(0,140,-57);
    public Pose fireNear  = new Pose(0,-25,-25);
    public Pose eat1 = new Pose(-0.5*PI,160,-148);
    public Pose[] eat2 = new Pose[]{new Pose(0,50,-57),new Pose(0,50,-110)};
    public Pose eat3 = new Pose(0,-25,-110);
    public Pose eat4 = new Pose(0,98,-103);

    public Pose park = new Pose(0,-150,-50);

}

