package org.firstinspires.ftc.teamcode.Modules;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Configurable
@Config
public class Intake {
    public static double maxPower = 0.8;
    public static boolean debug = false;
    private DcMotorEx brush;
    private double power = maxPower;
    private State currentState = State.ON;

    public enum State {
        ON, OFF, REVERSE
    }

    public Intake(HardwareMap hardwareMap){
        brush = hardwareMap.get(DcMotorEx.class, "brush");
        brush.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void setState(State newState) {
        this.currentState = newState;
    }

    public void update(){

        switch (currentState) {
            case ON:
                power = maxPower;
                break;
            case REVERSE:
                power = -maxPower;
                break;
            case OFF:
                power = 0;
                break;
        }

        brush.setPower(power);


        if(debug) {
            FtcDashboard dashboard = FtcDashboard.getInstance();
            TelemetryPacket packet = new TelemetryPacket();
            packet.put("Intake State", currentState.toString());
            packet.put("Intake Current ", brush.getCurrent(CurrentUnit.AMPS));
            dashboard.sendTelemetryPacket(packet);
        }
    }
}
