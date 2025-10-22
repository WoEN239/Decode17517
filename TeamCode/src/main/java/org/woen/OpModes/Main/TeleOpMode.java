package org.woen.OpModes.Main;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Util.Vectors.Pose;

@TeleOp(name = "teleop")
public class TeleOpMode extends BaseOpMode{
    private final FeedforwardReferenceObserver velocityObserver = new FeedforwardReferenceObserver();

    @Override
    protected void initConfig(){
        robot.getFactory().replace(TrajectoryFollower.class, new TrajectoryFollower() {
            @Override
            public void update() {
                TrajectoryFollower.super.update();
            }
        });
    }

    @Override
    protected void loopRun() {
        velocityObserver.notifyListeners(new FeedforwardReference(new Pose(
                gamepad1.left_stick_x,gamepad1.left_stick_y, gamepad1.left_stick_x
        ),new Pose(0,0,0)));
    }

}
