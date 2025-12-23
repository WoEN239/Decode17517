package org.woen.Autonom.Pools;

import static org.woen.Config.Team.RED;
import static java.lang.Math.PI;

import org.woen.Config.MatchData;
import org.woen.Util.Vectors.Pose;

public class PositionPoolFar {
    public Pose fire = new Pose(0.324,145,-57);
    public Pose[] firstEat = new Pose[]{fire,new Pose(-2.36+PI,90,-130)};
    public Pose[] secondAim = new Pose[]{new Pose(-2.36+PI,90,-130),fire};
    public Pose[] secondEat = new Pose[]{fire,new Pose(-2.66+PI,40,-115)};
    public Pose[] thirdAim = new Pose[]{new Pose(-2.66+PI,50,-115),fire};

    public PositionPoolFar(){
        if(MatchData.team==RED){
            fire = fire.teamReverse();

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
