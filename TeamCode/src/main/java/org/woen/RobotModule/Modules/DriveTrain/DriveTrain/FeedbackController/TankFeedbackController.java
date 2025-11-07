package org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController;

import org.woen.Util.Pid.Pid;
import org.woen.Util.Pid.PidStatus;
import org.woen.Util.Vectors.Pose;

public class TankFeedbackController {
    private final Pid xPid;
    private final Pid hPid;

    public TankFeedbackController(PidStatus translation, PidStatus rotation) {
        xPid = new Pid(translation);
        xPid.isAngle = true;
        xPid.isDAccessible = false;

        hPid = new Pid(rotation);
        hPid.isAngle = true;
        hPid.isDAccessible = false;

    }

    public Pose computeU(Pose target, Pose localPosition, Pose targetVel, Pose localVel){
        xPid.setPos(localPosition.vector.x);
        xPid.setTarget(target.vector.x);

        hPid.setPos(localPosition.h);
        hPid.setTarget(target.h);

        xPid.update();
        hPid.update();

        return new Pose(
                hPid.getU(), xPid.getU(), 0
        );

    }
}
