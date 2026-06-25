package org.firstinspires.ftc.teamcode.OpModes.Auto;

import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.*;
import static com.pedropathing.ivy.pedro.PedroCommands.*;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
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
import org.firstinspires.ftc.teamcode.Robot.ALLIANCE;
import org.firstinspires.ftc.teamcode.Robot.Boot;


@Autonomous
@Configurable // Panels
public class NearAuto extends OpMode {
    private TelemetryManager panelsTelemetry;
    public Follower follower;
    private int pathState;
    private Paths paths;


    private FSM fsm = new FSM();


    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.getInstance().createFollower(hardwareMap);
        follower.setStartingPose(Boot.startPose != null ? Boot.startPose :new Pose(-58, -45.5, Math.toRadians(142)));

        paths = new Paths(follower);


        fsm.start(hardwareMap, ()->follower.getPose(),()->new Pose(follower.getVelocity().getXComponent(),follower.getVelocity().getYComponent(),follower.getAngularVelocity()));


        fsm.setState(FSM_STATE.DRIVE);

        Scheduler.reset();


        Command autoRoutine = sequential(

                follow(follower, paths.shoot1),
                waitMs(150),
                instant(() -> fsm.setState(FSM_STATE.SHOOT)),
                waitMs(150),


                parallel(follow(follower, paths.eat1),
                        instant(() -> fsm.setState(FSM_STATE.EAT))),
                instant(() -> fsm.setState(FSM_STATE.DRIVE)),

                follow(follower, paths.shoot2),
                waitMs(200),
                instant(() -> fsm.setState(FSM_STATE.SHOOT)),
                waitMs(150),


                parallel(race(follow(follower, paths.eatAndOpenGate1),
                                waitMs(1500)),
                        instant(() -> fsm.setState(FSM_STATE.EAT))),
                waitMs(350),


                instant(() -> fsm.setState(FSM_STATE.DRIVE)),
                follow(follower, paths.shoot3),
                waitMs(200),
                instant(() -> fsm.setState(FSM_STATE.SHOOT)),
                waitMs(150),

                race(parallel(follow(follower, paths.eat3),
                                instant(() -> fsm.setState(FSM_STATE.EAT))),
                        waitMs(1200)),
                waitMs(1000),

                instant(() -> fsm.setState(FSM_STATE.DRIVE)),
                follow(follower, paths.shoot4),
                waitMs(150),
                instant(() -> fsm.setState(FSM_STATE.SHOOT)),
                waitMs(150),
                race(parallel(follow(follower, paths.eat3),
                                instant(() -> fsm.setState(FSM_STATE.EAT))),
                        waitMs(1200)),
                waitMs(2000),

                instant(() -> fsm.setState(FSM_STATE.DRIVE)),
                follow(follower, paths.shoot4),
                waitMs(150),
                instant(() -> fsm.setState(FSM_STATE.SHOOT)),
                waitMs(250),
                race(parallel(follow(follower, paths.eat3),
                                instant(() -> fsm.setState(FSM_STATE.EAT))),
                        waitMs(1200)),
                waitMs(2000),

                instant(() -> fsm.setState(FSM_STATE.DRIVE)),
                follow(follower, paths.shoot4),
                waitMs(150),
                instant(() -> fsm.setState(FSM_STATE.SHOOT)),
                waitMs(150),
                race(parallel(follow(follower, paths.eat3),
                                instant(() -> fsm.setState(FSM_STATE.EAT))),
                        waitMs(1200)),
                waitMs(2000),

                instant(() -> fsm.setState(FSM_STATE.DRIVE)),
                follow(follower, paths.shoot4),
                waitMs(150),
                instant(() -> fsm.setState(FSM_STATE.SHOOT)),
                waitMs(150),
                race(parallel(follow(follower, paths.eat3),
                                instant(() -> fsm.setState(FSM_STATE.EAT))),
                        waitMs(1200)),
                waitMs(2000),

                instant(() -> fsm.setState(FSM_STATE.DRIVE)),
                waitMs(150),
                follow(follower, paths.shoot4),
                instant(() -> fsm.setState(FSM_STATE.SHOOT)),
                waitMs(150)
              /*



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
        follower.update();

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
        public PathChain openGate;
        public PathChain shootAndPark5;


        public Paths(Follower follower) {
            shoot1 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    P(-57, -45.5),
                                    P(-13, -22)
                            )
                    )
                    .setLinearHeadingInterpolation(H(142), H(90))
                    .build();

            eat1 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    P(-13, -22),
                                    P(-12, -52)
                            )
                    )
                    .setConstantHeadingInterpolation(H(90)).setVelocityConstraint(20).setTranslationalConstraint(0.75)
                    .build();

            shoot2 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    P(-12, -50),
                                    P(-13, -22)
                            )
                    )
                    .setConstantHeadingInterpolation(H(90))
                    .build();

            eatAndOpenGate1 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    P(-13, -22),
                                    P(13, -25),
                                    P(13, -55),



                                   P(3, -20),
                                  P(3, -52.4)
                            )
                    )
                    .setLinearHeadingInterpolation(H(90), H(90)).setTranslationalConstraint(0.75)
                    .build();

            shoot3 = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    P(3, -20),
                                    P(-13, -22)
                            )
                    )
                    .setLinearHeadingInterpolation(H(90), H(55))
                    .build();

            eat3 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    P(-13, -22),
                                    P(11, -44),
                                    P(11, -58.7)
                            )
                    )
                    .setLinearHeadingInterpolation(H(90), H(60)).setTranslationalConstraint(0.75)
                    .build();

            shoot4 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    P(11, -57.7),
                                    P(8, -52.4),
                                    P(-13, -22)
                            )
                    )
                    .setLinearHeadingInterpolation(H(90), H(90))
                    .build();



            shootAndPark5 = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    P(37, -53),
                                    P(36, -12),
                                    P(-45, -23)
                            )
                    )
                    .setConstantHeadingInterpolation(H(90))
                    .build();


        }
    }


    public static Pose P(double x, double y) {
        if (Boot.alliance == ALLIANCE.RED) {
            return new Pose(x, -y);
        }
        return new Pose(x, y);
    }

    public static double H(double deg) {
        double rad = Math.toRadians(deg);
        if (Boot.alliance == ALLIANCE.RED) {
            return -rad + Math.PI;
        }
        return rad;
    }


    public int autonomousPathUpdate() {

        return 0;
    }

    @Override
    public void stop() {
        fsm.turret.stopTurret();
        Boot.startPose = follower.getPose();
    }

}