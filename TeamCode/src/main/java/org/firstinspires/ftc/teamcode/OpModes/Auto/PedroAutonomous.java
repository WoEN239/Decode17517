package org.firstinspires.ftc.teamcode.OpModes.Auto;

import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.*;
import static com.pedropathing.ivy.pedro.PedroCommands.*;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.ivy.groups.Groups;
import com.pedropathing.ivy.pedro.PedroCommands;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.Modules.FSM;
import org.firstinspires.ftc.teamcode.Modules.FSM_STATE;
import org.firstinspires.ftc.teamcode.Pedro.Constants;





@Autonomous(name = "Pedro Pathing Autonomous", group = "Autonomous")
@Configurable // Panels
public class PedroAutonomous extends OpMode {
    private TelemetryManager panelsTelemetry; 
    public Follower follower;
    private int pathState;
    private Paths paths;

    private FSM fsm = new FSM();

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);

    
        follower.setStartingPose(new Pose(-58, -47, Math.toRadians(145)));

        paths = new Paths(follower);

        fsm.start(hardwareMap, follower);

        fsm.setState(FSM_STATE.DRIVE);

        Scheduler.reset();


        Command autoRoutine = sequential(

                follow(follower, paths.shoot1),
                instant(() -> fsm.setState(FSM_STATE.SHOOT)),
                waitMs(300),


                parallel(follow(follower, paths.eat1),
                        instant(() -> fsm.setState(FSM_STATE.EAT))),
                instant(() -> fsm.setState(FSM_STATE.DRIVE)),

                follow(follower, paths.shoot2),
                waitMs(100),
                instant(() -> fsm.setState(FSM_STATE.SHOOT)),
                waitMs(300),


                parallel(follow(follower, paths.eatAndOpenGate1),
                        instant(() -> fsm.setState(FSM_STATE.EAT))),
                waitMs(150),


                instant(() -> fsm.setState(FSM_STATE.DRIVE)),
                follow(follower, paths.shoot3),
                waitMs(100),
                instant(() -> fsm.setState(FSM_STATE.SHOOT)),
                waitMs(300),

                race(  parallel(follow(follower, paths.eat3),
                        instant(() -> fsm.setState(FSM_STATE.EAT))),
                        waitMs(2000))
              ,
                waitMs(800),

                instant(() -> fsm.setState(FSM_STATE.DRIVE)),
                follow(follower, paths.shoot4),
                waitMs(100),
                instant(() -> fsm.setState(FSM_STATE.SHOOT))
              /*  waitMs(300),


                parallel(follow(follower, paths.eat4),
                        instant(() -> fsm.setState(FSM_STATE.EAT))),
                waitMs(400),

                instant(() -> fsm.setState(FSM_STATE.DRIVE)),
                follow(follower, paths.shootAndPark5),
                instant(() -> fsm.setState(FSM_STATE.SHOOT))

               */
        );

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
        Scheduler.schedule(autoRoutine);
    }



    @Override
    public void loop() {

        fsm.update();

        Scheduler.execute();

 
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }


    public static class Paths {
        public PathChain shoot1;
        public PathChain eat1;
        public PathChain shoot2;
        public PathChain eatAndOpenGate1;
        public PathChain shoot3;
        public PathChain eat3;
        public PathChain shoot4;
        public PathChain eat4;
        public PathChain shootAndPark5;

        public Paths(Follower follower) {
            shoot1 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(-58, -47),
                                    new Pose(-13, -22)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(145))
                    .build();

            eat1 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(-13, -22),
                                    new Pose(-12, -50)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(90))
                    .build();

            shoot2 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(-12, -50),
                                    new Pose(-13, -22)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(90))
                    .build();

            eatAndOpenGate1 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(-13, -22),
                                    new Pose(13, -23),
                                    new Pose(13, -55),
                                    new Pose(16, -20),
                                    new Pose(0, -50.4)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(90))
                    .build();

            shoot3 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(0, -48.4),
                                    new Pose(2, -36),
                                    new Pose(-6, -10)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(55))
                    .build();

            eat3 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(-6, -10),
                                    new Pose(10,-44),
                                    new Pose(12,-57.7)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(55), Math.toRadians(60)).setTranslationalConstraint(0.75)
                    .build();

            shoot4 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(12, -57.7),
                                    new Pose(-10, -10)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(55))
                    .build();

            eat4 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(-6, -18),
                                    new Pose(36, -12),
                                    new Pose(37, -57.5)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(55), Math.toRadians(90)).setTranslationalConstraint(0.75)
                    .build();

            shootAndPark5 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(37, -53),
                                    new Pose(36, -12),
                                    new Pose(-45, -23)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(90))
                    .build();
        }
    }

    public int autonomousPathUpdate() {
       
        return 0;
    }
}