package org.firstinspires.ftc.teamcode.Modules.Transfer;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Util.CashedServo;

@Configurable
@Config
public class Transfer {

    public static CashedServo l;
    public static CashedServo c;
    public static CashedServo r;

    public static double upL = 0.45;
    public static double upC = 0.43;
    public static double upR = 0.59;

    public static double downL = 0.16;
    public static double downR = 0.82;
    public static double downC = 0.2;

    public static double midL = 0.34;
    public static double midR = 0.7;
    public static double midC = 0.35;
    public void start(HardwareMap hardwareMap){
        r = new CashedServo(hardwareMap.get(Servo.class, "upper_r"));
        c = new CashedServo(hardwareMap.get(Servo.class, "upper_c"));
        l = new CashedServo(hardwareMap.get(Servo.class, "upper_l"));
    }
    public enum STATE {
        LEFT, CENTER, RIGHT, UP, DOWN, DRIVE
    }



    private STATE state = STATE.UP;

    public void setState(STATE state){
        this.state = state;
    }
    private ElapsedTime timer = new ElapsedTime();
    STATE lastState =  STATE.UP;
    public void update(){
        if(lastState != state){
            timer.reset();
        }
        lastState = state;
        switch (state){
            case UP:
                r.setPosition(upR);
                l.setPosition(upL);
                c.setPosition(upC);
                break;
            case DOWN:
                r.setPosition(downR);
                l.setPosition(downL);
                c.setPosition(downC);
                break;
            case DRIVE:
                double t = timer.seconds();
                r.setPosition(Math.max(downR -  Math.abs(midR-downR)/0.25 * t,midR));
                l.setPosition(Math.min(downL + Math.abs(midL-downL)/0.25 * t, midL));
                c.setPosition(Math.min(downC + Math.abs(midC-downC)/0.25 * t, midC));
                break;
            case LEFT:
                l.setPosition(upL);
                break;
            case RIGHT:
                r.setPosition(upR);
                break;
            case CENTER:
                c.setPosition(upC);
                break;
        }
    }


}
