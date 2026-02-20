package org.woen.Autonom.Pools;

import static java.lang.Math.PI;

import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Architecture.AutonomTask;
import org.woen.Autonom.Architecture.WayPoint;
import org.woen.Config.MatchData;
import org.woen.Config.Team;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.Util.Vectors.Pose;


public class Far9Pattern3Ball extends WayPointPool {
    PositionPool3 pool = new PositionPool3();
    public WayPoint aim1 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.PATTERN)),
                    () -> RobotLog.dd("auto", "aim1")
            }, true, pool.fireFar.plus(new Pose(0, 10, 0))
    ).setName("aim1").setEndAngle(this::angleToGoal).setVel(100);

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
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FAST_PATTERN_FIRE))
            ),
            false, pool.fireFar.plus(new Pose(0,10,0))
    ).setName("fire2").setEndDetect(30).setEndAngle(this::angleToGoal);
    public WayPoint fire3 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire3"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            false, pool.fireFar
    ).setName("fire3").setEndDetect(30).setEndAngle(this::angleToGoal);
    public WayPoint fire4 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire4"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FAST_PATTERN_FIRE))
            ),
            false, pool.fireFar
    ).setName("fire3").setEndDetect(50).setEndAngle(this::angleToGoal);

    public WayPoint rotate1 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate1")
            }, false, pool.fireFar
    ).setName("rotate1").setEndDetect(30).setEndAngle(() -> angleTo(pool.eat1.vector));

    public WayPoint eat1 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eat1")
            },
            false, pool.eat1
    ).setName("eat1").setEndDetect(10).setEndAngle(() -> PI + angleTo(pool.fireFar.plus(new Pose(0,10,0)).vector));

    public WayPoint aim2 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "aim2")
            },
            true, pool.fireFar.plus(new Pose(0,10,0))
    ).setName("aim2").setEndAngle(this::angleToGoal).setEndDetect(40);

    public WayPoint rotate2 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate2")
            }, false, pool.fireFar
    ).setName("rotate2").setEndDetect(30).setEndAngle(() -> angleTo(pool.eat2.vector));

    public WayPoint eat2 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eat2")
            },
            false, pool.eat2
    ).setName("eat2").setEndDetect(30).setEndAngle(() -> angleTo(pool.fireFar.vector)).setLookAheadRadius(40);

    public WayPoint aim3 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "aim3")
            },
            false, pool.fireFar
    ).setName("aim3").setEndAngle(this::angleToGoal).setEndDetect(30);

    public WayPoint rotate3 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate3")
            }, false, pool.fireFar
    ).setName("rotate3").setEndDetect(30).setEndAngle(() -> angleTo(pool.eat3.vector));

    public WayPoint rotate4 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate4")
            }, false, pool.fireFar
    ).setName("rotate3").setEndDetect(30).setEndAngle(() -> angleTo(pool.eat4.vector));

    public WayPoint rotate5 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate5")
            }, false, pool.fireFar
    ).setName("rotate3").setEndDetect(30).setEndAngle(() -> angleTo(pool.eat5.vector));






    public WayPoint eat3 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eat3")
            },
            false, pool.eat3
    ).setName("eat3").setEndDetect(10).setEndAngle(() -> PI + angleTo(pool.fireFar.vector));

    public WayPoint aim4 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "aim4")
            },
            true, pool.fireFar.plus(new Pose(0, -10, 0))
    ).setName("aim4").setEndAngle(this::angleToGoal).setEndDetect(30);

    public WayPoint eat4 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eat4")
            },
            false, pool.eat4
    ).setName("eat4").setEndDetect(30).setEndAngle(() -> PI + angleTo(pool.fireFar.plus(new Pose(0, -10, 0)).vector));

    public WayPoint eat5 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eat5")
            },
            false, pool.eat5
    ).setName("eat5").setEndDetect(30).setEndAngle(() -> PI + angleTo(pool.fireNear.vector));

    public WayPoint aim5 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "aim5")
            },
            true, pool.fireNear
    ).setName("aim5").setEndAngle(this::angleToGoal).setEndDetect(30);

    public WayPoint fire5 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire5"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FAST_PATTERN_FIRE))
            ),
            false, pool.fireNear
    ).setName("fire3").setEndDetect(50).setEndAngle(this::angleToGoal);


    public WayPoint park = new WayPoint(
            new AutonomTask(
                    () -> true,
                    () -> RobotLog.dd("auto", "park")
            ), true, pool.park
    ).setName("eat4").setEndDetect(10);


    @Override
    public WayPoint[] getPool() {
        return new WayPoint[]{
                aim1.copy(),
                fire1.copy(),


                rotate2.copy(),
                eat2.copy().setVel(150),
                aim3.copy().setVel(150),
                fire3.copy(),


                rotate4.copy(),
                eat4.copy().setVel(150),
                aim4.copy().setVel(150),
                fire4.copy(),


                rotate1.copy(),
                eat1.copy().setVel(150),
                aim2.copy().setVel(150),
                fire2.copy(),

                rotate5.copy(),
                eat5.copy().setVel(180),
                aim5.copy().setVel(150),
                fire5.copy(),


        };
    }
}

class PositionPool3 {
    public PositionPool3() {
        if (MatchData.team == Team.RED) {
            fireFar = fireFar.teamReverse();
            fireNear = fireNear.teamReverse();
            eat1 = eat1.teamReverse();
            eat2 = eat2.teamReverse();
            eat3 = eat3.teamReverse();
            eat4 = eat4.teamReverse();
            park = park.teamReverse();
        }
    }

    public Pose fireFar = new Pose(0, 137.8, -63.5);
    public Pose fireNear = new Pose(0, -80, -50);
    public Pose eat1 = new Pose(-0.5 * PI, 151.1, -159.2);
    public Pose eat2 = new Pose(0, 92.9, -119.5);
    public Pose eat3 = new Pose(0, -60, -130);
    public Pose eat4 = new Pose(0, 7.0, -142.2);
    public Pose eat5 = new Pose(0,-34,-116);
    public Pose park = new Pose(0, -34, -103.5);

}


