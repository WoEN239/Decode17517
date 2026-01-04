package org.woen.Config;

import org.woen.Util.Vectors.Pose;

public class MatchData {
    public static Start start = Start.FAR_BLUE;
    public static Team team = Team.BLUE;

    public static void setStartPose(Pose startPose) {MatchData.startPose = startPose;}
    private static Pose startPose = null;
    public static Pose getStartPose(){
        if(startPose == null) return  start.pose;
        return startPose;
    }
}
