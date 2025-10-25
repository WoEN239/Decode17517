package org.woen.Config;

import org.woen.Util.Vectors.Pose;
import org.woen.Util.Vectors.Vector2d;

public class MatchData {
    public static Pose startPosition = new Pose(
            0,
            new Vector2d(0,
                         0)
    );

    public static Pose blueWall = new Pose(
            0,
             new Vector2d(0,
                     0)
    );
    public static Pose redWall = new Pose(
            0,
            new Vector2d(0,0)
    )
    public static Team team = Team.BLUE;
}
