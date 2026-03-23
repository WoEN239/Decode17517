package org.woen.Autonom.Pools;

import static org.woen.Config.Team.RED;
import static java.lang.Math.PI;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Architecture.AutonomTask;
import org.woen.Autonom.Architecture.WayPoint;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.Util.Vectors.Pose;

public class Far15BallPool extends WayPointPool {
    private PositionPoolFar pool = new PositionPoolFar();
    private ElapsedTime gunTimer = new ElapsedTime();
    public WayPoint aim1 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "firstAim")
            },new AutonomTask(
                    ()->true,
                    ()->EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.FAR)),
                    ()->gunTimer.reset()
            )
            , true, pool.fireFar
    ).setName("firstAim").setEndAngle(this::angleToGoal).setVel(200).setEndDetect(5);

    public WayPoint fire1 = new WayPoint(
            new AutonomTask(()->gunTimer.seconds()>1.2)
            ,new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "firstFire"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fireFar
    ).setName("firstFire").setEndDetect(30).setEndAngle(this::angleToGoal);

    public WayPoint rotateToEatHuman = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate")
            }, false, pool.fireFar
    ).setName("firstRotate").setEndDetect(30).setEndAngle(() -> angleTo(pool.eatHuman1.vector));

    private boolean isEatHuman1TimerReset = false;
    private ElapsedTime eatHuman1Timer = new ElapsedTime();
    public WayPoint eatHuman = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "firstEat"),
            },new AutonomTask(()->true,()->{eatHuman1Timer.reset();isEatHuman1TimerReset=true;}),
            false, pool.eatHuman1
    ).setName("firstEat").setVel(200).setEndDetect(20).setEndAngle(()->PI+angleTo(pool.fireFar.vector))
            .setInterrupt(()->eatHuman1Timer.seconds()>1.2&isEatHuman1TimerReset);

    public WayPoint aim2 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.FAR)),
                    () -> RobotLog.dd("auto", "firstAim")
            }, true, pool.fireFar
    ).setName("firstAim").setEndAngle(this::angleToGoal).setVel(200).setEndDetect(10);
    public WayPoint fire2 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "firstFire"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fireFar
    ).setName("firstFire").setEndDetect(30).setEndAngle(this::angleToGoal);

    public WayPoint rotateToEatFar = new WayPoint(
            new Runnable[]{
                    ()->EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.NEAR)),
                    () -> RobotLog.dd("auto", "rotate")
            }, false, pool.fireFar
    ).setName("firstRotate").setEndDetect(30).setEndAngle(() -> angleTo(pool.eatFar[0].vector));
    public WayPoint eatFar = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "firstEat"),
            },
            false, pool.eatFar
    ).setName("firstEat").setVel(200).setEndDetect(10).setEndAngle(()->PI+angleTo(pool.halfFirePos.vector));
    public WayPoint aim3 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "firstAim")
            }, true, pool.halfFirePos,pool.fireNear
    ).setName("firstAim").setEndAngle(this::angleToGoal).setVel(200).setEndDetect(10);
    public WayPoint fire3 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "firstFire"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fireNear
    ).setName("firstFire").setEndDetect(30).setEndAngle(this::angleToGoal);

    public WayPoint rotateToRotateToEat = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotateToRotateToEat")
            }, true, pool.fireNear
    ).setName("rotateToRotateToEat").setEndDetect(30).setEndAngle(() -> PI + angleTo(pool.rotateToEatNearPos.vector)).setVel(60);
    public WayPoint rotateToEatNear = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotateToEat")
            }, true, pool.rotateToEatNearPos
    ).setName("rotateToEat").setEndDetect(10).setEndAngle(() -> angleTo(pool.eatNear.vector)).setVel(200);
    private ElapsedTime rotateToGateTimer = new ElapsedTime();
    public WayPoint eatNear = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eatNear")
            },
            new AutonomTask(
                    () -> true,
                    () -> rotateToGateTimer.reset()
            ),
            false, pool.eatNear
    ).setName("eatNear").setEndDetect(10).setVel(200);
    private final ElapsedTime rampTimer = new ElapsedTime();
    public WayPoint gateTimerReset = new WayPoint(
            new AutonomTask(() -> true, rampTimer::reset),
            false, pool.eatNear
    ).setName("gateTimerReset").setEndDetect(30);
    public WayPoint gateOpen = new WayPoint(
            new AutonomTask(() -> rampTimer.seconds() > 1),
            false, pool.gateOpen
    ).setName("gateOpen").setEndDetect(20).setInterrupt(() -> rampTimer.seconds() > 1);

    public WayPoint aim4 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "aim2")
            }, true, pool.fireNear
    ).setName("aim2").setEndAngle(this::angleToGoal).setVel(200).setEndDetect(10);


    public WayPoint fire4 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "firstFire"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fireNear
    ).setName("firstFire").setEndDetect(30).setEndAngle(this::angleToGoal);

    public WayPoint rotateToEatMid = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate2")
            }, true, pool.rotateToEatMidPos
    ).setName("rotate2").setEndDetect(10).setEndAngle(() -> angleTo(pool.eatMid.vector)).setVel(200);

    public WayPoint eatMid = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "eatMid")
            },
            false, 0.1, pool.eatMid
    ).setName("eatMid").setEndDetect(10).setVel(200).setEndAngle(() -> PI + angleTo(pool.fireNear.vector));

    public WayPoint aim5 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "aim3")
            }, true, pool.fireNear
    ).setName("aim3").setEndAngle(this::angleToGoal).setVel(200).setEndDetect(10);

    public WayPoint fire5 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "firstFire"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fireNear
    ).setName("firstFire").setEndDetect(30).setEndAngle(this::angleToGoal);
    public WayPoint rotateToEatHuman2 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate")
            }, false, pool.fireNear
    ).setName("firstRotate").setEndDetect(30).setEndAngle(() -> angleTo(pool.eatHuman2[0].vector));

    private ElapsedTime eatHuman2Timer = new ElapsedTime();
    private boolean isEatHuman2TimerReset = false;
    public WayPoint eatHuman2 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.FAR)),
                    () -> RobotLog.dd("auto", "eat3")
            },
            new AutonomTask(()->true,()->{
                eatHuman2Timer.reset();
                isEatHuman2TimerReset =true;}),
            false, pool.eatHuman2
    ).setName("eat3").setEndDetect(10).setVel(220).setInterrupt(()-> eatHuman2Timer.seconds()>2.2&& isEatHuman2TimerReset)
    .setEndAngle(()->PI+angleTo(pool.fireFar.vector));

    public WayPoint aim6 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.FAR)),
                    () -> RobotLog.dd("auto", "firstAim")
            }, true, pool.fireFar
    ).setName("firstAim").setEndAngle(this::angleToGoal).setVel(200).setEndDetect(10);


    public WayPoint fire6 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "firstFire"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fireFar
    ).setName("firstFire").setEndDetect(30).setEndAngle(this::angleToGoal);

    public WayPoint park = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.FAR)),
                    () -> RobotLog.dd("auto", "firstAim")
            }, true, pool.park
    ).setName("firstAim").setEndAngle(this::angleToGoal).setVel(250).setEndDetect(10);

    @Override
    public WayPoint[] getPool() {
        return new WayPoint[]{
                aim1.copy(),
                fire1.copy(),
                rotateToEatHuman.copy(),
                eatHuman.copy(),
                aim2.copy(),
                fire2.copy(),
                rotateToEatFar.copy(),
                eatFar.copy(),
                aim3.copy().setLookAheadRadius(30),
                fire3.copy(),

                rotateToRotateToEat.copy(),
                rotateToEatNear.copy(),
                eatNear.copy(),
                gateTimerReset.copy(),
                gateOpen.copy(),
                aim4.copy(),
                fire4.copy(),

                rotateToEatMid.copy(),
                eatMid.copy(),
                aim5.copy(),
                fire5.copy(),

                rotateToEatHuman2.copy(),
                eatHuman2.copy().setLookAheadRadius(40),
                aim6.copy(),
                fire6.copy(),

                park.copy()
        };
    }

    static class PositionPoolFar {
        public Pose fireFar = new Pose(0,150,-58.5);
        public Pose eatHuman1 = new Pose(0, 165, -170);
        public Pose[] eatFar = new Pose[]{new Pose(0, 135, -110),new Pose(0, 80, -120)};

        public Pose halfFirePos = new Pose(0, 70, -40);
        public Pose fireNear = new Pose(0, -10, -40);


        public Pose rotateToEatNearPos = new Pose(0, -120, -122);
        public Pose rotateToEatMidPos = new Pose(0, -20, -122);
        public Pose eatNear = new Pose(-0.5 * PI, -20, -122);
        public Pose gateOpen = new Pose(-0.5 * PI, -20, -180);
        public Pose eatMid = new Pose(-0.5 * PI, 30, -122);
        public Pose[] eatHuman2 = new Pose[]{new Pose(0,110,-60),new Pose(0,165, -165)};

        public Pose park = new Pose(0,100,-58.5);
        public PositionPoolFar() {
            if (MatchData.team == RED) {
                fireFar = fireFar.teamReverse();
                eatHuman1 = eatHuman1.teamReverse();
                eatFar = new Pose[]{eatFar[0].teamReverse(), eatFar[1].teamReverse()};
                halfFirePos = halfFirePos.teamReverse();
                fireNear = fireNear.teamReverse();
                rotateToEatNearPos = rotateToEatNearPos.teamReverse();
                rotateToEatMidPos = rotateToEatMidPos.teamReverse();
                eatNear = eatNear.teamReverse();
                gateOpen = gateOpen.teamReverse();
                eatMid = eatMid.teamReverse();
                eatHuman2 = new Pose[]{eatHuman2[0].teamReverse(), eatHuman2[1].teamReverse()};
                park = park.teamReverse();
            }
        }
    }
}

