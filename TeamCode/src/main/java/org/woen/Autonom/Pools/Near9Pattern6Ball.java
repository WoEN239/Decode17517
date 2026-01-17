package org.woen.Autonom.Pools;

import static java.lang.Math.PI;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Structure.AutonomTask;
import org.woen.Autonom.Structure.WayPoint;
import org.woen.Config.MatchData;
import org.woen.Config.Team;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.Util.Vectors.Pose;


public class Near9Pattern6Ball extends WaypointPool {
    PositionPool4 pool = new PositionPool4();
    public WayPoint aim1 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.NEAR_GOAL)),
                    () -> RobotLog.dd("auto", "aim1")
            },
            new AutonomTask(
                    ()->true,
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.NEAR_GOAL))),
            false, pool.fireNear
    ).setName("aim1").setEndAngle(this::angleToGoal).setVel(100).setEndDetect(10);

    public WayPoint fire1 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire1"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            false, pool.fireNear
    ).setName("fire1").setEndDetect(30).setEndAngle(this::angleToGoal);

    public WayPoint rotate1 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate1")
            }, false, pool.fireNear
    ).setName("rotate1").setEndDetect(30).setEndAngle(() -> angleTo(pool.eat1.vector));

    private ElapsedTime lookTimer = new ElapsedTime();
    public WayPoint lookTimerReset = new WayPoint(
            new AutonomTask(()->true,()->lookTimer.reset()), false, pool.fireNear
    ).setName("eat3").setEndDetect(50).setEndAngle(()-> 0d);
    public WayPoint look = new WayPoint(
            new AutonomTask(()->lookTimer.seconds()>1), false, pool.fireNear
    ).setName("rotate1").setEndDetect(50).setEndAngle(() -> 0.0 );

    public WayPoint eat1 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eat3")
            },
            false, pool.eat1
    ).setName("eat3").setEndDetect(10).setVel(150);
    private ElapsedTime rampTimer = new ElapsedTime();
    public WayPoint rampTimerReset = new WayPoint(
            new AutonomTask(()->true,()->rampTimer.reset()),
            false, pool.eat1
    ).setName("eat3").setEndDetect(50).setEndAngle(()-> pool.eat1R.h);
    public WayPoint rampTimerWait = new WayPoint(
            new AutonomTask(()->rampTimer.seconds()>1.5),
            false, pool.eat1
    ).setName("eat3").setEndDetect(50).setEndAngle(()-> pool.eat1R.h);;

    public WayPoint aim2 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.NEAR_GOAL)),
                    () -> RobotLog.dd("auto", "aim2")
            }, true, pool.fireNear
    ).setName("aim1").setEndAngle(this::angleToGoal).setVel(100).setEndDetect(10);

    public WayPoint fire2 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire2"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FAST_PATTERN_FIRE))
            ),
            false, pool.fireNear
    ).setName("fire1").setEndDetect(30).setEndAngle(this::angleToGoal);


    public WayPoint rotate2 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate1")
            }, false, pool.fireNear
    ).setName("rotate1").setEndDetect(30).setEndAngle(() -> angleTo(pool.eat2.vector));

    public WayPoint eat2 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eat3")
            },
            false, pool.eat2
    ).setName("eat3").setEndDetect(10).setVel(150).setEndAngle(()-> PI+angleTo(pool.fireNear.vector));

    public WayPoint aim3 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.NEAR_GOAL)),
                    () -> RobotLog.dd("auto", "aim2")
            }, true, pool.fireNear
    ).setName("aim1").setEndAngle(this::angleToGoal).setVel(100).setEndDetect(10);

    public WayPoint fire3 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire2"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FAST_PATTERN_FIRE))
            ),
            false, pool.fireNear
    ).setName("fire1").setEndDetect(30).setEndAngle(this::angleToGoal);


    public WayPoint rotate3 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate3")
            }, false, pool.fireNear
    ).setName("rotate1").setEndDetect(30).setEndAngle(() -> angleTo(pool.eat3.vector));

    public WayPoint eat3 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eat3")
            },
            false, pool.eat3
    ).setName("eat3").setEndDetect(10).setVel(150).setEndAngle(()-> PI+angleTo(pool.fireNear.vector));


    public WayPoint aim4 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.NEAR_GOAL)),
                    () -> RobotLog.dd("auto", "aim4")
            }, true, pool.fireNearPark
    ).setName("aim4").setEndAngle(this::angleToGoal).setVel(100).setEndDetect(30);

    public WayPoint fire4 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire4"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FAST_PATTERN_FIRE))
            ),
            false, pool.fireNearPark
    ).setName("fire4").setEndDetect(30).setEndAngle(this::angleToGoal);



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
                lookTimerReset.copy(),
                look.copy(),
                rotate1.copy(),
                eat1.copy().setVel(150),
                rampTimerReset.copy(),
                rampTimerWait.copy(),
                aim2.copy(),
                fire2.copy(),
                rotate2.copy(),

                eat2.copy().setVel(100),
                aim3.copy(),
                fire3.copy(),

                rotate3.copy(),
                eat3.copy(),
                aim4.copy(),
                fire4.copy()

        };
    }

    @Override
    protected Double angleToGoal() {
        return angleTo(pool.goal.vector)+PI;
    }
}

class PositionPool4 {
    public PositionPool4() {
        if (MatchData.team == Team.RED) {
            fireNear = fireNear.teamReverse();
            fireNearPark = fireNearPark.teamReverse();
            goal = goal.teamReverse();
            eat1 = eat1.teamReverse();
            eat2 = eat2.teamReverse();
            eat3 = eat3.teamReverse();
            eat1R = eat1R.teamReverse();
            park = park.teamReverse();
        }
    }

    public Pose fireNear = new Pose(0, -54, -38);
    public Pose fireNearPark = new Pose(0, -90, -31);

    public Pose goal = new Pose(0,-180,-180);
    public Pose eat1 = new Pose(-0.5 * PI, -18, -155);
    public Pose eat1R = new Pose(-0.51 * PI , -18, -146);
    public Pose eat2 = new Pose(-0.5 * PI, 40, -125);
    public Pose eat3 = new Pose(-0.5 * PI,90,-125);
    public Pose park = new Pose(0, -34, -103.5);

}


