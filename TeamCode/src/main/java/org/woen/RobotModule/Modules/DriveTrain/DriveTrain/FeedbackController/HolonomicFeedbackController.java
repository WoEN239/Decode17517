package org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController;

import org.woen.Util.Pid.Pid;
import org.woen.Util.Pid.PidStatus;
import org.woen.Util.Vectors.Pose;

public class HolonomicFeedbackController {

    private final Pid xPid;
    private final Pid yPid;
    private final Pid hPid;

    public HolonomicFeedbackController(PidStatus translation, PidStatus rotation) {
        xPid = new Pid(translation);
        xPid.isNormolized = true;
        xPid.isDAccessible = false;

        yPid = new Pid(translation);
        yPid.isNormolized = true;
        yPid.isDAccessible = false;

        hPid = new Pid(rotation);
        hPid.isNormolized = true;
        hPid.isDAccessible = false;

    }

    public Pose computeU(Pose target, Pose position, Pose targetVel, Pose vel){
        xPid.setPos(position.vector.x);
        xPid.setPosD(vel.vector.x);

        yPid.setPos(position.vector.y);
        yPid.setPosD(vel.vector.y);

        hPid.setPos(position.h);
        hPid.setPosD(vel.h);

        xPid.setTarget(target.vector.x);
        xPid.setTargetD(targetVel.vector.x);

        yPid.setTarget(target.vector.y);
        yPid.setTargetD(targetVel.vector.y);

        hPid.setTarget(target.h);
        hPid.setTargetD(targetVel.h);

        xPid.update();
        yPid.update();
        hPid.update();

        return new Pose(
                hPid.getU(), xPid.getU(), yPid.getU()
        );

    }
}
