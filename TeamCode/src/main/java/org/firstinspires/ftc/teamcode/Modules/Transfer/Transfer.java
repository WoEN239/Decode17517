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

    public static double upL = 1;
    public static double upC = 1;
    public static double upR = 1;
    public static double downL = 0;
    public static double downR = 0;
    public static double downC = 0;
    public void start(HardwareMap hardwareMap){
        r = hardwareMap.get(Servo.class, "");
        c = hardwareMap.get(Servo.class, "");
        l = hardwareMap.get(Servo.class, "");
    }
    public enum STATE {
        LEFT, CENTER, RIGHT, UP, DOWN
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
        }
    }


}
