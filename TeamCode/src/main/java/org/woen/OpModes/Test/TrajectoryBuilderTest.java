package org.woen.OpModes.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.RobotLog;

import org.woen.Trajectory.Trajectory;
import org.woen.Trajectory.TrajectoryPoint;
import org.woen.Util.Vectors.Vector2d;

import java.io.IOException;


public class TrajectoryBuilderTest extends LinearOpMode {
    private Trajectory trajectory = new Trajectory();
    @Override
    public void runOpMode() throws InterruptedException {
        trajectory.manualAddSingle(
                new TrajectoryPoint(
                new Vector2d(0,0),new Vector2d(50,0),new Vector2d(0,0)),
                new TrajectoryPoint(
                new Vector2d(100,50),new Vector2d(0,50),new Vector2d(0,0)),
                0,0
        );
        try {
            trajectory.write("/storage/emulated/0/first data/trajectory.txt");
        } catch (IOException e) {
            RobotLog.dd("write","err"+e.getMessage());
        }
        waitForStart();
        RobotLog.dd("write","done");
        while (opModeIsActive()){

        }
    }
}
