package org.woen.Autonom.Pools;

import static org.woen.Config.Team.RED;
import static java.lang.Math.PI;

import org.woen.Config.MatchData;
import org.woen.Util.Vectors.Pose;

public class PositionPoolFar {
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
