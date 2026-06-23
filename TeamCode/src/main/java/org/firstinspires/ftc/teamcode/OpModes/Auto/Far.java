package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;

import org.firstinspires.ftc.teamcode.Modules.Camera.LimeLight;
import org.firstinspires.ftc.teamcode.Modules.FSM;
import org.firstinspires.ftc.teamcode.Modules.FSM_STATE;
import org.firstinspires.ftc.teamcode.Pedro.Constants;
import org.firstinspires.ftc.teamcode.Robot.ALLIANCE;
import org.firstinspires.ftc.teamcode.Robot.Boot;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.*;
import static com.pedropathing.ivy.pedro.PedroCommands.*;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;


@Autonomous
@Configurable // Panels
public class Far extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private int pathState; // Current autonomous path state (state machine)
    private Paths paths; // Paths defined in the Paths class
    FSM fsm = new FSM();
    LimeLight limeLight = new LimeLight();


    @Override
    public void stop() {
        Boot.startPose = follower.getPose();
        fsm.turret.stopTurret();
    }

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        limeLight.start(hardwareMap, telemetry);


        follower = Constants.getInstance().createFollower(hardwareMap);
        follower.setStartingPose(Boot.startPose != null ? Boot.startPose : new Pose(60, -32, Math.toRadians(90)));


        paths = new Paths(follower); // Build paths


        fsm.start(hardwareMap, follower);

        fsm.setState(FSM_STATE.DRIVE);

        Scheduler.reset();

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);

        Command command = sequential(
                eatPreloadChain,
                eatPreloadChain,
                waitMs(500),
                instant(()-> limeLight.getTheCounter())

                );


        Scheduler.schedule(command);
        fsm.turret.lock(true, 0.2903);
    }

    Command eatPreloadChain = sequential(instant(() -> fsm.setState(FSM_STATE.DRIVE)),
            waitMs(1000),
            instant(() -> fsm.setState(FSM_STATE.SHOOT)),
            waitMs(350),
            parallel(follow(follower, paths.eat1),
                    instant(() -> fsm.setState(FSM_STATE.EAT))),
            waitMs(200),
            instant(() -> fsm.setState(FSM_STATE.DRIVE)),

            follow(follower, paths.shoot2),
            instant(() -> fsm.setState(FSM_STATE.SHOOT)),
            waitMs(350));

    Command eatPreloadHm = sequential(parallel(race(follow(follower, paths.eat2),
                            waitMs(1000)),
                    instant(() -> fsm.setState(FSM_STATE.EAT))),
            waitMs(350),
            instant(() -> fsm.setState(FSM_STATE.DRIVE)),

            follow(follower, paths.shoot3),
            instant(() -> fsm.setState(FSM_STATE.SHOOT)),
            waitMs(350));

    Command eatMidLeftChan = sequential(parallel(race(follow(follower, paths.eat3),
                            waitMs(1000)),
                    instant(() -> fsm.setState(FSM_STATE.EAT))),
            waitMs(350),
            instant(() -> fsm.setState(FSM_STATE.DRIVE)),
            follow(follower, paths.shoot4),
            instant(() -> fsm.setState(FSM_STATE.SHOOT)),
            waitMs(350));

    Command eatMidRightChain = sequential(parallel(race(follow(follower, paths.eat4),
                            waitMs(1000)),
                    instant(() -> fsm.setState(FSM_STATE.EAT))),
            waitMs(350),
            instant(() -> fsm.setState(FSM_STATE.DRIVE)),
            follow(follower, paths.shoot5),
            instant(() -> fsm.setState(FSM_STATE.SHOOT)),
            waitMs(350));

    Command eatRightChain = sequential(parallel(race(follow(follower, paths.eat5),
                            waitMs(1000)),
                    instant(() -> fsm.setState(FSM_STATE.EAT))),
            waitMs(350),
            instant(() -> fsm.setState(FSM_STATE.DRIVE)),
            follow(follower, paths.shoot6),
            instant(() -> fsm.setState(FSM_STATE.SHOOT)),
            waitMs(350));


    @Override
    public void loop() {
        follower.update(); // Update Pedro Pathing
        pathState = autonomousPathUpdate(); // Update autonomous state machine
        Scheduler.execute();
        fsm.update();

        limeLight.update();

        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }


    public static class Paths {
        public PathChain eat1;
        public PathChain shoot2;
        public PathChain eat2;
        public PathChain shoot3;
        public PathChain eat3;
        public PathChain shoot4;
        public PathChain eat4;
        public PathChain shoot5;
        public PathChain eat5;
        public PathChain shoot6;
        public PathChain park;


        public Paths(Follower follower) {
            eat1 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(60, -32),
                                    new Pose(35, -25),
                                    new Pose(34, -59)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(90))
                    .build();


            shoot2 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(34, -54),
                                    new Pose(56, -22)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(90))
                    .build();


            eat2 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(56, -22),
                                    new Pose(58, -65)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(90))
                    .build();


            shoot3 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(58, -65),
                                    new Pose(56, -22)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(90))
                    .build();


            eat3 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(56, -22),
                                    new Pose(42, -50),
                                    new Pose(42, -65)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(90))
                    .build();


            shoot4 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(42, -65),
                                    new Pose(56, -22)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(90))
                    .build();


            eat4 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(56, -22),
                                    new Pose(30, -50),
                                    new Pose(30, -65)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(90))
                    .build();


            shoot5 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(30, -65),
                                    new Pose(56, -22)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(90))
                    .build();
            eat5 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(56, -22),
                                    new Pose(18, -50),
                                    new Pose(18, -65)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(90))
                    .build();


            shoot6 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(18, -65),
                                    new Pose(56, -22)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(90))
                    .build();
            park = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(56, -22),
                                    new Pose(40, -22)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(90))
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
