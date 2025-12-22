package org.woen.Autonom;

import static org.woen.Config.Team.RED;
import static java.lang.Math.PI;

import org.woen.Config.MatchData;
import org.woen.Util.Vectors.Pose;

public class PositionPool {
    public Pose fire = new Pose(0.324,145,-57);
    public Pose[] firstEat = new Pose[]{fire,new Pose(0,90,-57),new Pose(PI*0.5,90,-130)};
    public Pose[] secondAim = new Pose[]{new Pose(0,90,-130),new Pose(0,90,-57),fire};
    public Pose[] secondEat = new Pose[]{fire,new Pose(0,40,-57),new Pose(PI*0.5,40,-130)};

    public PositionPool(){
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
