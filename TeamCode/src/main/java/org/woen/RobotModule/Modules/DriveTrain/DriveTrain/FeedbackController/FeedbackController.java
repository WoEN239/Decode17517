package org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController;

import org.woen.Util.Pid.Pid;
import org.woen.Util.Pid.PidStatus;
import org.woen.Util.Vectors.Pose;

public class FeedbackController {

    private final Pid xPid;
    private final Pid yPid;
    private final Pid hPid;

    public FeedbackController(PidStatus translation, PidStatus rotation) {
        xPid = new Pid(translation);
        xPid.isAngle = true;
        xPid.isDAccessible = true;

        yPid = new Pid(translation);
        yPid.isAngle = true;
        yPid.isDAccessible = true;

        hPid = new Pid(rotation);
        hPid.isAngle = true;
        hPid.isDAccessible = true;

    }

    public Pose computeU(Pose target, Pose position, Pose taregtVel, Pose vel){
        xPid.setPos(position.vector.x);
        xPid.setPosD(vel.vector.x);

        yPid.setPos(position.vector.y);
        yPid.setPosD(vel.vector.y);

        hPid.setPos(position.h);
        hPid.setPosD(vel.h);

        xPid.setTarget(target.vector.x);
        xPid.setTargetD(taregtVel.vector.x);

        yPid.setTarget(target.vector.y);
        yPid.setTargetD(taregtVel.vector.y);

        hPid.setTarget(target.h);
        hPid.setTargetD(taregtVel.h);

        xPid.update();
        yPid.update();
        hPid.update();

        return new Pose(
                hPid.getU(), xPid.getU(), yPid.getU()
        );

    }
}
