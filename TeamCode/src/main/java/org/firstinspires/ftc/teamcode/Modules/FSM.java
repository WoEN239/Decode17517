package org.firstinspires.ftc.teamcode.Modules;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Modules.Intake.Intake;
import org.firstinspires.ftc.teamcode.Modules.Shooter.Flywheel;
import org.firstinspires.ftc.teamcode.Modules.Transfer.Transfer;
import org.firstinspires.ftc.teamcode.Modules.Turret.Turret;
import org.firstinspires.ftc.teamcode.Robot.ALLIANCE;

@Config
@Configurable
public class FSM {
    Turret turret = new Turret();
    Flywheel flywheel = new Flywheel();
    Intake intake = new Intake();
    Transfer transfer = new Transfer();
    HardwareMap hardwareMap;
    ElapsedTime timer = new ElapsedTime();


    private FSM_STATE state = FSM_STATE.EAT;

    private FSM_STATE target = FSM_STATE.EAT;

    public void setState(FSM_STATE state) {
        this.state = state;
    }

    public FSM_STATE getState() {
        return state;
    }

    private Follower follower;


    public void start(HardwareMap hardwareMap, Follower follower) {
        this.hardwareMap = hardwareMap;
        this.follower = follower;
        turret.start(hardwareMap, follower::getHeading);
        flywheel.start(hardwareMap, follower, ALLIANCE.alliance);
        intake.start(hardwareMap);
        transfer.start(hardwareMap);
    }

    public void updateStates() {
        switch (state) {
            case EAT:
                target = FSM_STATE.EAT;
                transfer.setState(Transfer.STATE.DOWN);
                intake.setState(Intake.State.ON);
                break;
            case REVERSE:
                target = FSM_STATE.REVERSE;
                transfer.setState(Transfer.STATE.DOWN);
                intake.setState(Intake.State.REVERSE);
                break;
            case DRIVE:
                target = FSM_STATE.DRIVE;
                transfer.setState(Transfer.STATE.DRIVE);
                intake.setState(Intake.State.REVERSE);

                break;
            case SHOOT:
                target = FSM_STATE.EAT;
                transfer.setState(Transfer.STATE.UP);
                intake.setState(Intake.State.REVERSE);
                if (timer.seconds() > 0.3) {
                    setState(FSM_STATE.EAT);
                    timer.reset();
                }
                break;
        }
    }

    public void update() {
        if (target == state)
            timer.reset();
        updateStates();
        double dX = Flywheel.goal.getX() - follower.getPose().getX();
        double dY = Flywheel.goal.getY() - follower.getPose().getY();
        double absoluteAngleToGoal = Math.atan2(dY, dX);

        //FtcDashboard.getInstance().getTelemetry().addData("relative turret angle", Math.toDegrees(relativeTurretAngle));
        FtcDashboard.getInstance().getTelemetry().addData("dx", dX);
        FtcDashboard.getInstance().getTelemetry().addData("dY", dY);
        FtcDashboard.getInstance().getTelemetry().update();

        turret.setAngleToHold(absoluteAngleToGoal);
        transfer.update();
        flywheel.update();
        turret.update();
        intake.update();

    }

}
