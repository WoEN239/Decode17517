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
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewBrushReversEvent;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.AIM_COMMAND;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.Util.Vectors.Pose;

public class AirPool extends WayPointPool{
    @Override
    protected Double angleToGoal(){
        return Math.PI + MatchData.team.farGoalPose.minus(pose.vector).getAngle();
    }


    private PositionPoolAir pool = new PositionPoolAir();
    private ElapsedTime gunTimer = new ElapsedTime();
    public WayPoint aim1 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "firstAim")
            },new AutonomTask(
            ()->true,
            ()-> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.FAR)),
            ()->gunTimer.reset()
    )
            , true, pool.fireFar
    ).setName("firstAim").setEndAngle(this::angleToFarGoal).setVel(200).setEndDetect(5);

    public WayPoint fire1 = new WayPoint(
            new AutonomTask(()->gunTimer.seconds()>1.2)
            ,new AutonomTask(
            () -> isGunEat,
            () -> RobotLog.dd("auto", "firstFire"),
            () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
    ),
            true, pool.fireFar
    ).setName("firstFire").setEndDetect(30).setEndAngle(this::angleToFarGoal);

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
    ).setName("firstFire").setEndDetect(30).setEndAngle(this::angleToFarGoal);

    public WayPoint rotateToEatFar = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate")
            }, false, pool.fireFar
    ).setName("firstRotate").setEndDetect(30).setEndAngle(() -> angleTo(pool.eatFar[0].vector));
    public WayPoint eatFar = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "firstEat"),
            },
            false, pool.eatFar
    ).setName("firstEat").setVel(200).setEndDetect(10).setEndAngle(()->PI+angleTo(pool.fireFar.vector));
    public WayPoint aim3 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "firstAim")
            }, true, pool.fireFar
    ).setName("firstAim").setEndAngle(this::angleToFarGoal).setVel(200).setEndDetect(10);
    public WayPoint fire3 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "firstFire"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fireFar
    ).setName("firstFire").setEndDetect(30).setEndAngle(this::angleToFarGoal);

    public WayPoint park = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.FAR)),
                    () -> RobotLog.dd("auto", "firstAim")
            }, true, pool.park
    ).setName("firstAim").setEndAngle(this::angleToFarGoal).setVel(250).setEndDetect(10);



    public WayPoint rotateToEatHuman2 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate"),
                    () -> EventBus.getInstance().invoke(new NewBrushReversEvent(false))
            }, false, pool.fireFar
    ).setName("firstRotate").setEndDetect(30).setEndAngle(() -> angleTo(pool.eatHuman1.vector));

    private boolean isEatHuman2TimerReset = false;
    private ElapsedTime eatHuman2Timer = new ElapsedTime();
    public WayPoint eatHuman2 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "firstEat"),
            },new AutonomTask(()->true,()->{eatHuman2Timer.reset();isEatHuman2TimerReset=true;}),
            false, pool.eatHuman1
    ).setName("firstEat").setVel(200).setEndDetect(20).setEndAngle(()->PI+angleTo(pool.fireFar.vector))
    .setInterrupt(()->eatHuman2Timer.seconds()>1.2&isEatHuman2TimerReset);//.setLookAheadRadius(50);

    public WayPoint aim22 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.FAR)),
                    () -> RobotLog.dd("auto", "firstAim"),
                    () -> EventBus.getInstance().invoke(new NewBrushReversEvent(true))
            }, true, pool.fireFar
    ).setName("firstAim").setEndAngle(this::angleToFarGoal).setVel(200).setEndDetect(10);
    public WayPoint fire22 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "firstFire"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fireFar
    ).setName("firstFire").setEndDetect(30).setEndAngle(this::angleToFarGoal);




    public WayPoint rotateToEatHuman3 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate"),

                    () -> EventBus.getInstance().invoke(new NewBrushReversEvent(false))
            }, false, pool.fireFar
    ).setName("firstRotate").setEndDetect(30).setEndAngle(() -> angleTo(pool.eatHuman1.vector));

    private boolean isEatHuman3TimerReset = false;
    private ElapsedTime eatHuman3Timer = new ElapsedTime();
    public WayPoint eatHuman3 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "firstEat"),
            },new AutonomTask(()->true,()->{eatHuman3Timer.reset();isEatHuman3TimerReset=true;}),
            false,  pool.eatHuman1//new Pose[]{pool.eatHuman2[0].plus(new Pose(0,00,0)),pool.eatHuman2[1]}
    ).setName("firstEat").setVel(200).setEndDetect(20).setEndAngle(()->PI+angleTo(pool.fireFar.vector))
            .setInterrupt(()->eatHuman3Timer.seconds()>1.4&isEatHuman3TimerReset).setLookAheadRadius(50);

    public WayPoint aim23 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.FAR)),
                    () -> RobotLog.dd("auto", "firstAim"),
                    () -> EventBus.getInstance().invoke(new NewBrushReversEvent(true))
            }, true, pool.fireFar
    ).setName("firstAim").setEndAngle(this::angleToFarGoal).setVel(200).setEndDetect(10);
    public WayPoint fire23 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "firstFire"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fireFar
    ).setName("firstFire").setEndDetect(30).setEndAngle(this::angleToFarGoal);




    public WayPoint rotateToEatHuman4 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate"),
                    () -> EventBus.getInstance().invoke(new NewBrushReversEvent(false))
            }, false, pool.fireFar
    ).setName("firstRotate").setEndDetect(30).setEndAngle(() -> angleTo(pool.eatHuman1.vector));

    private boolean isEatHuman4TimerReset = false;
    private ElapsedTime eatHuman4Timer = new ElapsedTime();
    public WayPoint eatHuman4 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "firstEat"),
            },new AutonomTask(()->true,()->{eatHuman4Timer.reset();isEatHuman4TimerReset=true;}),
            false, pool.eatHuman1
    ).setName("firstEat").setVel(200).setEndDetect(20).setEndAngle(()->PI+angleTo(pool.fireFar.vector))
            .setInterrupt(()->eatHuman4Timer.seconds()>1.4&isEatHuman4TimerReset).setLookAheadRadius(50);

    public WayPoint aim24 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.FAR)),
                    () -> RobotLog.dd("auto", "firstAim"),
                    () -> EventBus.getInstance().invoke(new NewBrushReversEvent(true))
            }, true, pool.fireFar
    ).setName("firstAim").setEndAngle(this::angleToFarGoal).setVel(200).setEndDetect(10);
    public WayPoint fire24 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "firstFire"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fireFar
    ).setName("firstFire").setEndDetect(30).setEndAngle(this::angleToFarGoal);




    public WayPoint rotateToEatHuman5 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate"),
                    () -> EventBus.getInstance().invoke(new NewBrushReversEvent(false))
            }, false, pool.fireFar
    ).setName("firstRotate").setEndDetect(30).setEndAngle(() -> angleTo(pool.eatHuman1.vector));

    private boolean isEatHuman5TimerReset = false;
    private ElapsedTime eatHuman5Timer = new ElapsedTime();
    public WayPoint eatHuman5 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "firstEat"),
            },new AutonomTask(()->true,()->{eatHuman5Timer.reset();isEatHuman5TimerReset=true;}),
            false, pool.eatHuman1
    ).setName("firstEat").setVel(200).setEndDetect(20).setEndAngle(()->PI+angleTo(pool.fireFar.vector))
            .setInterrupt(()->eatHuman5Timer.seconds()>1.4 &isEatHuman5TimerReset).setLookAheadRadius(50)   ;

    public WayPoint aim25 = new WayPoint(
            new Runnable[]{
                    () -> EventBus.getInstance().invoke(new NewAimEvent(AIM_COMMAND.FAR)),
                    () -> RobotLog.dd("auto", "firstAim"),
                    () -> EventBus.getInstance().invoke(new NewBrushReversEvent(true))
            }, true, pool.fireFar
    ).setName("firstAim").setEndAngle(this::angleToFarGoal).setVel(200).setEndDetect(10);
    public WayPoint fire25 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "firstFire"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fireFar
    ).setName("firstFire").setEndDetect(30).setEndAngle(this::angleToFarGoal);

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

                rotateToEatHuman2.copy(),
                eatHuman2.copy(),
                aim22.copy(),
                fire22.copy(),

                rotateToEatHuman3.copy(),
                eatHuman3.copy(),
                aim23.copy(),
                fire23.copy(),


                rotateToEatHuman4.copy(),
                eatHuman4.copy(),
                aim24.copy(),
                fire24.copy(),



                rotateToEatHuman5 .copy(),
                eatHuman5.copy(),
                aim25.copy(),
                fire25.copy(),


                park.copy()
        };
    }

    private static class PositionPoolAir {
        public Pose fireFar = new Pose(0,145,-58.5);
        public Pose eatHuman1 = new Pose(0, 160, -170);
        public Pose[] eatHuman2 = new Pose[]{new Pose(0, 160, -170),new Pose(0, 160, -170)};//new Pose[]{new Pose(0,90,-100), new Pose(0, 90, -170)};
        public Pose[] eatFar = new Pose[]{new Pose(0, 135, -110),new Pose(0, 80, -120)};

        public Pose park = new Pose(0,100,-58.5);
        public PositionPoolAir() {
            if (MatchData.team == RED) {
                fireFar = fireFar.teamReverse();
                eatHuman1 = eatHuman1.teamReverse();
                eatHuman2 = new Pose[]{eatHuman2[0].teamReverse(),eatHuman2[1].teamReverse()};
                eatFar = new Pose[]{eatFar[0].teamReverse(), eatFar[1].teamReverse()};

                park = park.teamReverse();
            }
        }
    }
}
