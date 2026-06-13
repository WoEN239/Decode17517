    package org.firstinspires.ftc.teamcode.Modules.Shooter;

    import com.acmerobotics.dashboard.FtcDashboard;
    import com.acmerobotics.dashboard.config.Config;
    import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
    import com.bylazar.configurables.annotations.Configurable;
    import com.pedropathing.control.PIDFCoefficients;
    import com.pedropathing.control.PIDFController;
    import com.pedropathing.follower.Follower;
    import com.pedropathing.geometry.Pose;
    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.DcMotorEx;
    import com.qualcomm.robotcore.hardware.DcMotorSimple;
    import com.qualcomm.robotcore.hardware.HardwareMap;
    import com.qualcomm.robotcore.hardware.Servo;
    import com.qualcomm.robotcore.util.Range;

    import org.firstinspires.ftc.teamcode.Robot.ALLIANCE;
    import org.firstinspires.ftc.teamcode.Utility;


    @Config
    @Configurable
    public class Flywheel {
        public static PIDFCoefficients flywheelMotorCoef = new PIDFCoefficients(0, 0, 0, 0);

        private PIDFController lPIDFCotroler = new PIDFController(flywheelMotorCoef);

        private PIDFController cPIDFCotroler = new PIDFController(flywheelMotorCoef);

        private PIDFController rPIDFCotroler = new PIDFController(flywheelMotorCoef);
        private DcMotorEx lMotor;
        private DcMotorEx rMotor;
        private DcMotorEx cMotor;

        public static boolean debug = false;

        public static double kV = 0;

        private Servo l;
        private Servo r;
        private Servo c;

        public static double errorBorder = 0;

        ShooterConst shooterConst = new ShooterConst();

        private Follower follower;

        public static Pose goal = new Pose(180, 180, 0);

        public void start(HardwareMap hardwareMap, Follower follower, ALLIANCE alliance) {
            this.follower = follower;
            lMotor = hardwareMap.get(DcMotorEx.class, "");
            rMotor = hardwareMap.get(DcMotorEx.class, "");
            cMotor = hardwareMap.get(DcMotorEx.class, "");
            lMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            cMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            rMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            lMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            cMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            cMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            r = hardwareMap.get(Servo.class, "");
            c = hardwareMap.get(Servo.class, "");
            l = hardwareMap.get(Servo.class, "");
            if (alliance == ALLIANCE.RED) {
                goal = Utility.revertPose(goal);
            }
        }

        public void update() {
            follower.update();

            double distToTarget = goal.distanceFrom(follower.getPose());///add for future

            lPIDFCotroler.setCoefficients(flywheelMotorCoef);
            rPIDFCotroler.setCoefficients(flywheelMotorCoef);
            cPIDFCotroler.setCoefficients(flywheelMotorCoef);

            double errL = ShooterConst.leftS[0] - lMotor.getVelocity();
            double errC = ShooterConst.centerS[0] - cMotor.getVelocity();
            double errR = ShooterConst.rightS[0] - rMotor.getVelocity();


            if (Math.abs(errL) < errorBorder) {
                errL = 0;
            }
            if (Math.abs(errR) < errorBorder) {
                errR = 0;
            }
            if (Math.abs(errC) < errorBorder) {
                errC = 0;
            }


            lPIDFCotroler.updateError(errL);
            cPIDFCotroler.updateError(errC);
            rPIDFCotroler.updateError(errR);



            lPIDFCotroler.updateFeedForwardInput(ShooterConst.leftS[0] * kV);
            rPIDFCotroler.updateFeedForwardInput(ShooterConst.rightS[0] * kV);
            cPIDFCotroler.updateFeedForwardInput(ShooterConst.centerS[0] * kV);

            double powerR = Range.clip(rPIDFCotroler.run(),0,1);
            double powerC = Range.clip(cPIDFCotroler.run(),0,1);
            double powerL = Range.clip(lPIDFCotroler.run(),0,1);


            rMotor.setPower(powerR);
            lMotor.setPower(powerL);
            cMotor.setPower(powerC);

            l.setPosition(ShooterConst.leftS[1]);
            r.setPosition(ShooterConst.rightS[1]);
            c.setPosition(ShooterConst.centerS[1]);
            if(debug) {
                FtcDashboard dashboard = FtcDashboard.getInstance();
                TelemetryPacket packet = new TelemetryPacket();

                packet.put("Target Velocity", ShooterConst.leftS[0]);


                packet.put("Current Velocity L", lMotor.getVelocity());
                packet.put("Current Velocity C", cMotor.getVelocity());
                packet.put("Current Velocity R", rMotor.getVelocity());

                dashboard.sendTelemetryPacket(packet);

            }
        }


    }
