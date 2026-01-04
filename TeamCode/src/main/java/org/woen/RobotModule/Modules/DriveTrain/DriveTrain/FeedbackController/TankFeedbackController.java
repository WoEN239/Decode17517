package org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController;

import org.woen.Util.Pid.Pid;
import org.woen.Util.Pid.PidStatus;
import org.woen.Util.Vectors.Pose;

public class TankFeedbackController {
    private final Pid xPid;
    private final Pid hPid;

    public TankFeedbackController(PidStatus translation, PidStatus rotation) {
        xPid = new Pid(translation);
        xPid.isNormolized = false;
        xPid.isDAccessible = false;

        hPid = new Pid(rotation);
        hPid.isNormolized = true;
        hPid.isDAccessible = false;

    }

    public Pose computeU(Pose target, Pose localPosition, Pose targetVel, Pose localVel){
        xPid.setPos(localPosition.x);
        xPid.setTarget(target.x);

        hPid.setPos(localPosition.h);
        hPid.setTarget(target.h);

        xPid.update();
        hPid.update();

        return new Pose(
                hPid.getU(), 0, 0
        );
    }
}
