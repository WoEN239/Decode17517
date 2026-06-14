package org.firstinspires.ftc.teamcode.Modules.Transfer;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Configurable
@Config
public class Transfer {

    Servo l;
    Servo c;
    Servo r;

    public static double upL = 0.367;
    public static double upC = 0.4;
    public static double upR = 0.61;
    public static double downL = 0.13;
    public static double downR = 0.82;
    public static double downC = 0.18;
    public static double midL = 0.294;
    public static double midR = 0.7;
    public static double midC = 0.315;
    public void start(HardwareMap hardwareMap){
        r = hardwareMap.get(Servo.class, "upper_r");
        c = hardwareMap.get(Servo.class, "upper_c");
        l = hardwareMap.get(Servo.class, "upper_l");
    }
    public enum STATE {
        LEFT, CENTER, RIGHT, UP, DOWN, DRIVE
    }



    STATE state = STATE.UP;

    public void setState(STATE state){
        this.state = state;
    }

    public void update(){
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
                r.setPosition(midR);
                l.setPosition(midL);
                c.setPosition(midC);
                break;
        }
    }


}
