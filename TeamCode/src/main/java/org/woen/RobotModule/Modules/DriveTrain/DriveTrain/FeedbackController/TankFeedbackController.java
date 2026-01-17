package org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController;

import org.woen.Util.Pid.Pid;
import org.woen.Util.Pid.PidStatus;
import org.woen.Util.Vectors.Pose;

public class TankFeedbackController {
    private final Pid xPid;
    private final Pid hPid;
    private final Pid xPidVel;
    private final Pid hPidVel;

    public TankFeedbackController(PidStatus translation, PidStatus rotation, PidStatus rotationVel, PidStatus translationVel) {
        xPid = new Pid(translation);
        xPid.isNormolized = false;
        xPid.isDAccessible = false;

        xPidVel = new Pid(translationVel);
        xPidVel.isNormolized = false;
        xPidVel.isDAccessible = false;

        hPidVel = new Pid(rotationVel);
        hPidVel.isNormolized = false;
        hPidVel.isDAccessible = false;

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

        xPidVel.setPos(localVel.vector.rotate(-localPosition.h).x);
        xPidVel.setTarget(targetVel.x);

        hPidVel.setPos(localVel.h);
        hPidVel.setTarget(targetVel.h);

        xPidVel.update();
        hPidVel.update();

        return new Pose(
                hPid.getU(), 0, 0
        );
    }
}
