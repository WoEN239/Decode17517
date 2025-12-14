package org.woen.Autonom;

import static java.lang.Math.PI;

import org.woen.Util.Vectors.Pose;

public class PositionPool {
    public Pose goal = new Pose(0,-150,0);
    public Pose eatStart = new Pose(0,-58,-58);
    public Pose eatEnd = new Pose(0,0,-58);
    public Pose firstEat = new Pose(-PI/2.0,-10,-40);
    public Pose shoot = new Pose(-2.35+PI,-40,0);

}
