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

    
        follower.setStartingPose(new Pose(23.739, 121.587, Math.toRadians(90)));

        paths = new Paths(follower);

        fsm.start(hardwareMap, follower);

        fsm.setState(FSM_STATE.DRIVE);

        Scheduler.reset();

        Command autoRouthine = sequential(

                follow(follower, paths.shoot1),
                instant(() -> fsm.setState(FSM_STATE.SHOOT)),
                waitMs(300),


                parallel(follow(follower, paths.eat1),
                        instant(() -> fsm.setState(FSM_STATE.EAT))),
                waitMs(400),

                instant(() -> fsm.setState(FSM_STATE.DRIVE)),
                follow(follower, paths.shoot2),
                instant(() -> fsm.setState(FSM_STATE.SHOOT)),
                waitMs(300),


                parallel(follow(follower, paths.eatAndOpenGate1),
                        instant(() -> fsm.setState(FSM_STATE.EAT))),
                waitMs(400),

                instant(() -> fsm.setState(FSM_STATE.DRIVE)),
                follow(follower, paths.shoot3),
                instant(() -> fsm.setState(FSM_STATE.SHOOT)),
                waitMs(300),


                parallel(follow(follower, paths.eat3),
                        instant(() -> fsm.setState(FSM_STATE.EAT))),
                waitMs(400),

                instant(() -> fsm.setState(FSM_STATE.DRIVE)),
                follow(follower, paths.shoot4),
                instant(() -> fsm.setState(FSM_STATE.SHOOT)),
                waitMs(300),


                parallel(follow(follower, paths.eat4),
                        instant(() -> fsm.setState(FSM_STATE.EAT))),
                waitMs(400),

                instant(() -> fsm.setState(FSM_STATE.DRIVE)),
                follow(follower, paths.shootAndPark5),
                instant(() -> fsm.setState(FSM_STATE.SHOOT))
        );

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
       Scheduler.schedule(autoRouthine);
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
                                    new Pose(23.739, 121.587),
                                    new Pose(58.856, 85.064)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))
                    .build();

            eat1 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(58.856, 85.064),
                                    new Pose(18.996, 84.968)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();

            shoot2 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(18.996, 84.968),
                                    new Pose(59.013, 85.060)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();

            eatAndOpenGate1 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(59.013, 85.060),
                                    new Pose(54.477, 49.498),
                                    new Pose(6.789, 57.571),
                                    new Pose(9.462, 65.471)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))
                    .build();

            shoot3 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(9.462, 65.471),
                                    new Pose(9.746, 53.097),
                                    new Pose(59.496, 84.821)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(135))
                    .build();

            eat3 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(59.496, 84.821),
                                    new Pose(10.723, 63.455)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(135))
                    .build();

            shoot4 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(10.723, 63.455),
                                    new Pose(59.362, 84.658)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(135))
                    .build();

            eat4 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(59.362, 84.658),
                                    new Pose(51.700, 28.565),
                                    new Pose(17.523, 36.258)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(180))
                    .build();

            shootAndPark5 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(17.523, 36.258),
                                    new Pose(57.202, 113.106)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();
        }
    }

    public int autonomousPathUpdate() {
       
        return 0;
    }
}