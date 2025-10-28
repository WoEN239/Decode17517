package org.woen.RobotModule.Modules.Gun.Impls;

import static org.woen.RobotModule.Modules.Gun.GUN_COMMAND.*;
import static org.woen.Util.Color.BALLS_COLOR.GREEN;
import static org.woen.Util.Color.BALLS_COLOR.VOID;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.MatchData;
import org.woen.Config.PATTERN;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.Hardware.Devices.ColorSensor.Interface.ColorSensor;
import org.woen.Hardware.Devices.Motor.Interface.Motor;
import org.woen.Hardware.Devices.Servo.Interface.ServoMotor;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.GUN_COMMAND;
import org.woen.RobotModule.Modules.Gun.Interface.Gun;
import org.woen.RobotModule.Modules.IntakeAndShoot.Impls.ColorDetection;
import org.woen.Util.Color.BALLS_COLOR;
import org.woen.Util.Color.BALLS_COMPARTMENT;
import org.woen.Util.Pid.Pid;
import org.woen.Util.Pid.PidStatus;

public class GunImpl implements Gun {
    private ServoMotor right;
    private ServoMotor center;
    private ServoMotor left;
    private ServoMotor wall;

    private Motor gunL;
    private Motor gunR;

    private ColorSensor rightColor;

    private ColorSensor centerColor;
    private ColorSensor leftColor;
    private PidStatus status = new PidStatus(10, 0, 0, 0.57, 0, 0, 0);
    private Pid pid = new Pid(status);

    public void setCommand(NewGunCommandAvailable event) {
        this.command = event.getData();
    }

    private GUN_COMMAND command = EAT;

    private double gunVel = 1300;

    private BALLS_COLOR leftBall = VOID;

    private BALLS_COLOR rightBall = VOID;
    private BALLS_COLOR centerBall = VOID;

    private ColorDetection leftColorDetection;
    private ColorDetection centerColorDetection;
    private ColorDetection rightColorDetection;

    private BALLS_COMPARTMENT greenBall = BALLS_COMPARTMENT.MID;

    private BALLS_COLOR[] ballsPos =
            new BALLS_COLOR[3];

    private PATTERN pattern;


    @Override
    public void init() {
        right = DevicePool.getInstance().right;
        center = DevicePool.getInstance().center;
        left = DevicePool.getInstance().left;
        wall = DevicePool.getInstance().wall;

        gunL = DevicePool.getInstance().gunL;
        gunR = DevicePool.getInstance().gunR;

        rightColor = DevicePool.getInstance().rightColor;
        centerColor = DevicePool.getInstance().centerColor;
        leftColor = DevicePool.getInstance().leftColor;

        leftColorDetection = new ColorDetection(leftColor);
        rightColorDetection = new ColorDetection(rightColor);
        centerColorDetection = new ColorDetection(centerColor);

        pattern = MatchData.pattern;
    }

    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(NewGunCommandAvailable.class, this::setCommand);
    }

    private double delay = 0.2;
    private ElapsedTime timer = new ElapsedTime();

    public void lateUpdate() {
        switch (command) {
            case RAPID_FIRE:
                wall.setPos(command.wall);
                right.setPos(command.right);
                if (timer.seconds() > delay) {
                    center.setPos(command.center);
                }
                if (timer.seconds() > 2 * delay) {
                    left.setPos(command.left);
                }
                if (timer.seconds() > 3 * delay) {
                    command = EAT;
                }
                break;
            case SHOT_CENTER:
            case SHOT_LEFT:
            case SHOT_RIGHT:
            case FULL_FIRE:
                right.setPos(command.right);
                left.setPos(command.left);
                center.setPos(command.center);
                wall.setPos(command.wall);
                if (timer.seconds() > 2 * delay) {
                    command = EAT;
                }
                break;
            case TARGET:
                gunVel = 1300;
                right.setPos(command.right);
                left.setPos(command.left);
                center.setPos(command.center);
                wall.setPos(command.wall);
                timer.reset();
                break;
            case EAT:
                gunVel = 800;
                timer.reset();
                right.setPos(command.right);
                left.setPos(command.left);
                center.setPos(command.center);
                wall.setPos(command.wall);
                if (centerColorDetection.def_color_easy() != VOID) {
                    ballsPos[1] = centerColorDetection.def_color_easy();
                    if (centerColorDetection.def_color_easy() == GREEN)
                        greenBall = BALLS_COMPARTMENT.MID;
                }
                if (rightColorDetection.def_color_easy() != VOID) {
                    ballsPos[0] = rightColorDetection.def_color_easy();
                    if (rightColorDetection.def_color_easy() == GREEN)
                        greenBall = BALLS_COMPARTMENT.RIGHT;
                }
                if (leftColorDetection.def_color_easy() != VOID) {
                    ballsPos[2] = leftColorDetection.def_color_easy();
                    if (leftColorDetection.def_color_easy() == GREEN)
                        greenBall = BALLS_COMPARTMENT.LEFT;
                }
                break;
            case PATTERN_FIRE:
                if (pattern == PATTERN.PPG)
                    command = PPG;
                if (pattern == PATTERN.GPP)
                    command = GPP;
                if (pattern == PATTERN.PGP)
                    command = PGP;
                break;
            case PPG:
                if (greenBall == BALLS_COMPARTMENT.RIGHT) {
                    left.setPos(command.left);
                    center.setPos(command.center);
                    if (timer.seconds() > 0.5)
                        right.setPos(command.right);
                }
                if (greenBall == BALLS_COMPARTMENT.LEFT) {
                    right.setPos(command.right);
                    center.setPos(command.center);
                    if (timer.seconds() > 0.5)
                        left.setPos(command.right);
                }
                if (greenBall == BALLS_COMPARTMENT.MID) {
                    left.setPos(command.left);
                    right.setPos(command.right);
                    if (timer.seconds() > 0.5)
                        center.setPos(command.center);
                }
                if (timer.seconds() > 2 * delay)
                    command = EAT;
                break;
            case GPP:
                if (greenBall == BALLS_COMPARTMENT.RIGHT) {
                    if (timer.seconds() > 0.5) {
                        left.setPos(command.left);
                        center.setPos(command.center);
                    }
                    right.setPos(command.right);
                }
                if (greenBall == BALLS_COMPARTMENT.LEFT) {
                    if (timer.seconds() > 0.5) {
                        right.setPos(command.right);
                        center.setPos(command.center);
                    }
                    left.setPos(command.right);
                }
                if (greenBall == BALLS_COMPARTMENT.MID) {
                    if (timer.seconds() > 0.5) {
                        left.setPos(command.left);
                        right.setPos(command.right);
                    }
                    center.setPos(command.center);
                }
                if (timer.seconds() > 2 * delay)
                    command = EAT;
                break;
            case PGP:
                if (greenBall == BALLS_COMPARTMENT.RIGHT) {
                    if (timer.seconds() > 0.5)
                        right.setPos(command.right);
                    if(timer.seconds() > 0.75)
                        left.setPos(command.left);
                    center.setPos(command.center);
                }
                if (greenBall == BALLS_COMPARTMENT.LEFT) {
                    if (timer.seconds() > 0.5)
                        left.setPos(command.left);
                    if(timer.seconds() > 0.75)
                        right.setPos(command.right);
                    center.setPos(command.center);
                }
                if (greenBall == BALLS_COMPARTMENT.MID) {
                    if (timer.seconds() > 0.5)
                        center.setPos(command.center);
                    if(timer.seconds() > 0.75)
                        right.setPos(command.right);
                    left.setPos(command.left);
                }
                if (timer.seconds() > 2 * delay)
                    command = EAT;

        }
        // right .update();
        // left  .update();
        // center.update();
        // wall  .update();

        pid.setTarget(gunVel);
        pid.setPos(gunL.getVel());
        pid.update();

        gunL.setPower(pid.getU());
        gunR.setPower(pid.getU());
    }

}
