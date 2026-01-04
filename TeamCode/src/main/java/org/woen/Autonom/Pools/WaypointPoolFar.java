package org.woen.Autonom.Pools;

import static org.woen.Config.Team.RED;
import static java.lang.Math.PI;

import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Structure.AutonomTask;
import org.woen.Autonom.Structure.WayPoint;
import org.woen.Config.MatchData;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.Util.Vectors.Pose;

public class WaypointPoolFar extends WaypointPool {
    PositionPoolFar pool = new PositionPoolFar();
    public WayPoint firstAim = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "firstAim")
            }, true, MatchData.start.pose, pool.fire.plus(new Pose(0, 5, 0))
    ).setName("firstAim").setEndAngle(this::angleToGoal);

    public WayPoint fire1 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "firstFire"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fire, pool.fire
    ).setName("firstFire").setEndDetect(10).setEndAngle(this::angleToGoal);

    public WayPoint fire2 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "firstFire"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fire, pool.fire
    ).setName("firstFire").setEndDetect(10).setEndAngle(this::angleToGoal);

    public WayPoint fire3 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "firstFire"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fire, pool.fire
    ).setName("firstFire").setEndDetect(10).setEndAngle(this::angleToGoal);

    public WayPoint fire4 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire4"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fire, pool.fire
    ).setName("fire4").setEndDetect(10).setEndAngle(this::angleToGoal);

    public WayPoint fire5 = new WayPoint(
            new AutonomTask(
                    () -> isGunEat,
                    () -> RobotLog.dd("auto", "fire5"),
                    () -> EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE))
            ),
            true, pool.fire, pool.fire
    ).setName("fire5").setEndDetect(30).setEndAngle(this::angleToGoal);

    public WayPoint rotate1 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate")
            }, false, pool.fire, pool.rotate1
    ).setName("firstRotate").setEndDetect(30);

    public WayPoint rotate2 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate")
            }, false, pool.fire, pool.rotate2
    ).setName("secondRotate").setEndDetect(30);

    public WayPoint rotate3 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate")
            }, false, pool.fire, pool.rotate3
    ).setName("secondRotate").setEndDetect(30);

    public WayPoint rotate4 = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "rotate")
            }, false, pool.fire, pool.rotate4
    ).setName("secondRotate").setEndDetect(30);


    public WayPoint firstEat = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "firstEat"),
            },
            false, pool.firstEat
    ).setName("firstEat").setVel(80).setEndDetect(10);

    public WayPoint secondAim = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "secondAim")
            },
            false, pool.secondAim
    ).setName("secondAim").setVel(50).setEndAngle(this::angleToGoal).setEndDetect(10);

    public WayPoint secondEat = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "secondEat")
            },
            false, pool.secondEat
    ).setName("firstEat").setVel(80).setEndDetect(10);

    public WayPoint thirdAim = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "thirdAim")
            },
            false, pool.thirdAim
    ).setName("thirdAim").setVel(50).setEndAngle(this::angleToGoal).setEndDetect(10);

    public WayPoint thirdEat = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "secondEat")
            },
            false, pool.thirdEat
    ).setName("firstEat").setVel(120).setEndDetect(30);

    public WayPoint thirdEatRotate = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "thirdEatRotate")
            },
            false, pool.thirdEatRotate
    ).setName("firstEat").setVel(120).setEndDetect(60);

    public WayPoint forthAim = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "thirdAim")
            },
            false, pool.forthAim
    ).setName("thirdAim").setVel(50).setEndAngle(this::angleToGoal).setEndDetect(10);


    public WayPoint forthEat = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "secondEat")
            },
            false, pool.forthEat
    ).setName("firstEat").setVel(120).setEndDetect(10);

    public WayPoint fiveAim = new WayPoint(
            new Runnable[]{
                    () -> RobotLog.dd("auto", "thirdAim")
            },
            true, pool.fiveAim
    ).setName("thirdAim").setVel(50).setEndAngle(this::angleToGoal).setEndDetect(25);

    public WayPoint park = new WayPoint(
            AutonomTask.Stub,
            true, pool.fire, new Pose(0, 0, -70)
    ).setVel(200);
}
class PositionPoolFar {
    public Pose fire = new Pose(0.324,140,-57);
    public Pose rotate1 = new Pose(-2.34,fire.vector);
    public Pose rotate2 = new Pose(-2.9,fire.vector);
    public Pose rotate3 = new Pose(-2.9,fire.vector);
    public Pose rotate4 = new Pose(-0.5*PI,fire.vector);
    public Pose[] firstEat = new Pose[]{fire,new Pose(-2.34+PI,98,-103)};
    public Pose[] secondAim = new Pose[]{new Pose(-2.34+PI,98,-103),fire};
    public Pose[] secondEat = new Pose[]{fire,new Pose(-2.9+PI,50,-110)};
    public Pose[] thirdAim = new Pose[]{new Pose(-2.9+PI,50,-110),fire};
    public Pose[] thirdEat = new Pose[]{fire, new Pose(-2.9-PI*0.5,-28,-115)};
    public Pose[] thirdEatRotate = new Pose[]{new Pose(-2.9-PI*0.5,-28,-115),
                                              new Pose(-2.9-PI,-28,-115)};
    public Pose[] forthAim = new Pose[]{new Pose(-2.9+PI,-28,-115),fire};
    public Pose[] forthEat = new Pose[]{fire,new Pose(-0.5*PI,160,-148)};
    public Pose[] fiveAim  = new Pose[]{new Pose(-0.5*PI,160,-148),new Pose(-0.5*PI,150,-40)};


    public PositionPoolFar(){
        if(MatchData.team==RED){
            fire = fire.teamReverse();
            rotate1 = rotate1.teamReverse();
            rotate2 = rotate2.teamReverse();

            for (int i = 0; i < firstEat.length; i++) {
                firstEat[i] = firstEat[i].teamReverse();
            }
            for (int i = 0; i < secondAim.length; i++) {
                secondAim[i] = secondAim[i].teamReverse();
            }
            for (int i = 0; i < secondEat.length; i++) {
                secondEat[i] = secondEat[i].teamReverse();
            }


        }
    }
}


