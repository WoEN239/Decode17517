package org.woen.Autonom.Pools;


import static java.lang.Math.PI;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Structure.AutonomTask;
import org.woen.Autonom.Structure.WayPoint;
import org.woen.Config.MatchData;
import org.woen.Config.Team;
import org.woen.RobotModule.Modules.Camera.PipeLineSwitchEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.Util.Vectors.Pose;

public class Near9Pattern extends WayPointPool {
    PositionPoolNear9Pattern pool = new PositionPoolNear9Pattern();
    ElapsedTime parkTimer = new ElapsedTime();
    public WayPoint aim1 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.NEAR)),
                    () -> RobotLog.dd("auto", "aim1"),
                    () -> EventBus.getInstance().invoke(new PipeLineSwitchEvent(0)),
                    () -> parkTimer.reset()
            },
            new AutonomTask(
                    ()->true,
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.NEAR))),
            false, pool.fireNear
    ).setName("aim1").setEndAngle(this::angleToGoal).setVel(150).setEndDetect(50);

    public WayPoint stop1 = new WayPoint(
            new Runnable[]{},
            true, pool.fireNear
    ).setName("stop1").setEndDetect(30).setEndAngle(this::angleToGoal).setVel(200);


    public WayPoint fire1 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire1"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.PATTERN_FIRE))
            ),
            true, pool.fireNear
    ).setName("fire1").setEndDetect(50).setEndAngle(this::angleToGoal).setVel(100);

    private ElapsedTime lookTimer = new ElapsedTime();
    public WayPoint lookTimerReset = new WayPoint(
            new AutonomTask(()->true,()->lookTimer.reset()), false, pool.fireNear
    ).setName("lookTimerReset").setEndDetect(40).setEndAngle(()-> 0d);
    public WayPoint look = new WayPoint(
            new AutonomTask(()->lookTimer.seconds()>1.5), false, pool.fireNear
    ).setName("look").setEndDetect(40).setEndAngle(() -> 0.0 );

    public WayPoint rotateToRotateToEat = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotateToRotateToEat")
            }, true, pool.fireNear
    ).setName("rotateToRotateToEat").setEndDetect(60).setEndAngle(() -> PI+angleTo(pool.rotateToEat.vector)).setVel(60);
    public WayPoint rotateToEat = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotateToEat")
            }, true, pool.rotateToEat
    ).setName("rotateToEat").setEndDetect(20).setEndAngle(() -> angleTo(pool.eatNear.vector)).setVel(140);
    private ElapsedTime rotateToGateTimer = new ElapsedTime();
    public WayPoint eatNear = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new PipeLineSwitchEvent(0)),
                    () -> RobotLog.dd("auto", "eatNear")
            },
            new AutonomTask(
                    ()->true,
                    ()->rotateToGateTimer.reset()),
            false, pool.eatNear
    ).setName("eatNear").setEndDetect(30).setVel(80).setInterrupt(()->rotateToGateTimer.seconds()>3);
    private final ElapsedTime rampTimer = new ElapsedTime();
    public WayPoint gateTimerReset = new WayPoint(
            new AutonomTask(()->true, rampTimer::reset),
            false, pool.eatNear
    ).setName("gateTimerReset").setEndDetect(50);
    public WayPoint gateOpen = new WayPoint(
            new AutonomTask(()->rampTimer.seconds()>1),
            false, pool.gateOpen
    ).setName("gateOpen").setEndDetect(22).setInterrupt(()->rampTimer.seconds()>1);

    public WayPoint aim2 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.NEAR)),
                    () -> RobotLog.dd("auto", "aim2")
            }, true, pool.fireNear
    ).setName("aim2").setEndAngle(this::angleToGoal).setVel(100).setEndDetect(50);

    public WayPoint stop2 = new WayPoint(
            new Runnable[]{},
            false, pool.fireNear
    ).setName("stop2").setEndDetect(30).setEndAngle(this::angleToGoal).setVel(200);

    public WayPoint fire2 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire2"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.PATTERN_FIRE))
            ),
            false, pool.fireNear
    ).setName("fire2").setEndDetect(50).setEndAngle(this::angleToGoal).setVel(100);;


    public WayPoint rotate2 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate2")
            }, true, pool.rotateToEat
    ).setName("rotate2").setEndDetect(20).setEndAngle(() -> angleTo(pool.eatMid.vector)).setVel(140);

    public WayPoint eatMid = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eatMid")
            },
            false, pool.eatMid
    ).setName("eatMid").setEndDetect(20).setVel(100).setEndAngle(()-> PI+angleTo(pool.fireNear.vector));

    public WayPoint aim3 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.NEAR )),
                    () -> RobotLog.dd("auto", "aim3")
            }, true, pool.fireNear
    ).setName("aim3").setEndAngle(this::angleToGoal).setVel(100).setEndDetect(50);

    public WayPoint stop3 = new WayPoint(
            new Runnable[]{},
            false, pool.fireNear
    ).setName("stop3").setEndDetect(30).setEndAngle(this::angleToGoal).setVel(200);


    public WayPoint fire3 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire3"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.PATTERN_FIRE))
            ),
            false, pool.fireNear
    ).setName("fire3").setEndDetect(50).setEndAngle(this::angleToGoal).setVel(100);;


    public WayPoint rotate3 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate3")
            }, false, pool.fireNear
    ).setName("rotate3").setEndDetect(50).setEndAngle(() -> angleTo(pool.eatFar.vector));

    public WayPoint eatFar = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eat3")
            },
            false, pool.eatFar
    ).setName("eat3").setEndDetect(20).setVel(150).setEndAngle(()-> PI+angleTo(pool.fireNear.vector));


    public WayPoint aim4 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.NEAR)),
                    () -> RobotLog.dd("auto", "aim4")
            }, true, pool.fireNearPark
    ).setName("aim4").setEndAngle(this::angleToGoal).setVel(100).setEndDetect(50);

    public WayPoint stop4 = new WayPoint(
            new Runnable[]{},
            false, pool.fireNear
    ).setName("stop4").setEndDetect(30).setEndAngle(this::angleToGoal).setVel(200);

    public WayPoint fire4 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire4"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.PATTERN_FIRE))
            ),
            false, pool.fireNearPark
    ).setName("fire4").setEndDetect(50).setEndAngle(this::angleToGoal).setVel(100).setInterrupt(()->parkTimer.seconds()>29);



    public WayPoint park = new WayPoint(
            new AutonomTask(
                    () -> true,
                    () -> RobotLog.dd("auto", "park")
            ), true, pool.park
    ).setName("park").setEndDetect(10).setVel(50).setEndAngle(()-> 0d);


    @Override
    public WayPoint[] getPool() {
        return new WayPoint[]{
                aim1.copy(),
                stop1.copy(),
                fire1.copy(),
                lookTimerReset.copy(),
                look.copy(),

//                rotateToRotateToEat.copy(),
//                rotateToEat.copy(),
//                eatNear.copy(),
//                gateTimerReset.copy(),
//                gateOpen.copy(),
//                aim2.copy(),
//                stop2.copy(),
//                fire2.copy(),
//
                rotate2.copy(),

                eatMid.copy(),
                aim3.copy(),
                stop3.copy(),
                fire3.copy(),

                rotate3.copy(),
                eatFar.copy(),
                aim4.copy(),
                stop4.copy(),
                fire4.copy(),
                park.copy()

        };
    }

    @Override
    protected Double angleToGoal() {
        return angleTo(pool.goal.vector)+PI;
    }
}

class PositionPoolNear9Pattern {
    public PositionPoolNear9Pattern() {
        if (MatchData.team == Team.RED) {
            fireNear = fireNear.teamReverse();
            fireNearPark = fireNearPark.teamReverse();
            goal = goal.teamReverse();
            eatNear = eatNear.teamReverse();
            eatMid = eatMid.teamReverse();
            eatFar = eatFar.teamReverse();
            park = park.teamReverse();
            rotateToEat = rotateToEat.teamReverse();
            gateOpen = gateOpen.teamReverse();
        }
    }

    public Pose fireNear = new Pose(0, -5, -45);
    public Pose fireNearPark = new Pose(0,  -5, -45);

    public Pose goal = new Pose(0,-172,-172);
    public Pose rotateToEat = new Pose(-0.5*PI, -65, -80);
    public Pose eatNear = new Pose(-0.5*PI, -5, -145);
    public Pose gateOpen = new Pose(-0.5*PI, -5, -180);
    public Pose eatMid = new Pose(-0.5 * PI, 20, -127);
    public Pose eatFar = new Pose(-0.5 * PI,90,-130);
    public Pose park = new Pose(0 , -100, -60);

}