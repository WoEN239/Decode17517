package org.firstinspires.ftc.teamcode.OpModes.Auto;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;

import org.firstinspires.ftc.teamcode.Modules.FSM;
import org.firstinspires.ftc.teamcode.Modules.FSM_STATE;
import org.firstinspires.ftc.teamcode.Pedro.Constants;
import org.firstinspires.ftc.teamcode.Robot.ALLIANCE;
import org.firstinspires.ftc.teamcode.Robot.Boot;

import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.*;
import static com.pedropathing.ivy.pedro.PedroCommands.*;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;


@Autonomous(name = "Pedro Pathing Autonomous", group = "Autonomous")
@Configurable // Panels
public class Far extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private int pathState; // Current autonomous path state (state machine)
    private Paths paths; // Paths defined in the Paths class
    FSM fsm = new FSM();


    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();


        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(60, -32, Math.toRadians(180)));


        paths = new Paths(follower); // Build paths


        fsm.start(hardwareMap, follower);

        fsm.setState(FSM_STATE.DRIVE);

        Scheduler.reset();

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);

        Command command = sequential(

                follow(follower, paths.Path1),
                follow(follower, paths.Path2),
                follow(follower, paths.Path3),
                follow(follower, paths.Path4),
                follow(follower, paths.Path5),
                follow(follower, paths.Path6),
                follow(follower, paths.Path7),
                follow(follower, paths.Path8)


                );
    }


    @Override
    public void loop() {
        follower.update(); // Update Pedro Pathing
        pathState = autonomousPathUpdate(); // Update autonomous state machine


        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }


    public static class Paths {
        public PathChain Path1;
        public PathChain Path2;
        public PathChain Path3;
        public PathChain Path4;
        public PathChain Path5;
        public PathChain Path6;
        public PathChain Path7;
        public PathChain Path8;


        public Paths(Follower follower) {
            Path1 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(60, -32),
                                    new Pose(35, -31),
                                    new Pose(34, -54)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();


            Path2 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(34, -54),
                                    new Pose(56, -22)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();


            Path3 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(56, -22),
                                    new Pose(56, -58)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();


            Path4 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(56, -58),
                                    new Pose(56, -22)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();


            Path5 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(56, -22),
                                    new Pose(41, -61)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();


            Path6 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(41, -61),
                                    new Pose(56, -22)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();


            Path7 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(56, -22),
                                    new Pose(26, -59)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();


            Path8 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(26, -59),
                                    new Pose(56, -22)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();
        }
    }


    public int autonomousPathUpdate() {
        // Add your state machine Here
        // Access paths with paths.pathName
        // Refer to the Pedro Pathing Docs (Auto Example) for an example state machine
        return 0;
    }


    public static Pose P(double x, double y) {
        if (Boot.alliance == ALLIANCE.RED) {
            return new Pose(-x, -y);
        }
        return new Pose(x, y);
    }

    public static double H(double deg) {
        double rad = Math.toRadians(deg);
        if (Boot.alliance == ALLIANCE.RED) {
            return rad + Math.PI;
        }
        return rad;
    }

}
