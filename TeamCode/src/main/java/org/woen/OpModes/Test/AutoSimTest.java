package org.woen.OpModes.Test;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Pools.WaypointPoolNear;
import org.woen.Autonom.Structure.SetNewWaypointsSequenceEvent;
import org.woen.Config.MatchData;
import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.OpModes.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewPositionListener;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Vectors.Pose;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

@TeleOp(name = "auto_sim")
public class AutoSimTest extends BaseOpMode {
    @Override
    protected void initConfig(){
        DeviceActivationConfig deviceConfig = DeviceActivationConfig.getAllOff();
        deviceActivationConfig = deviceConfig;

        ModulesActivateConfig moduleConfig = ModulesActivateConfig.getAllOff();
        moduleConfig.autonomTaskManager.set(true);
        moduleConfig.driveTrain.trajectoryFollower.set(true);
        modulesActivationConfig = moduleConfig;
        MatchData.setStartPose(MatchData.start.pose);
    }

    DatagramSocket socket = null;
    InetAddress address = null;
    int port = 5005;

    @Override
    protected void initRun() {
        WaypointPoolNear poolNear = new WaypointPoolNear();
        EventBus.getInstance().invoke(new SetNewWaypointsSequenceEvent(
                poolNear.aim1.copy(),
                poolNear.fire1.copy(),
                poolNear.rotate1.copy(),
                poolNear.eat1.copy().setVel(150),
                poolNear.aim2.copy().setVel(100),
                poolNear.fire2.copy(),
                poolNear.rotate2.copy(),
                poolNear.eat2.copy().setVel(150),
                poolNear.aim3.copy().setVel(150),
                poolNear.fire3.copy(),
                poolNear.rotate3.copy(),
                poolNear.eat3.copy().setVel(150),
                poolNear.aim4.copy().setVel(150),
                poolNear.fire4.copy(),
                poolNear.rotate4.copy(),
                poolNear.eat4.copy().setVel(150),
                poolNear.aim5.copy().setVel(150),
                poolNear.fire5.copy(),
                poolNear.park.copy().setVel(220)

        ));

        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("192.168.43.170");
        } catch (Exception e) {
            Telemetry.getInstance().add("e",e);
            System.exit(-1);
        }

        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));
    }

    private Pose pose = MatchData.getStartPose();
    private void setPose(Pose pose) {this.pose = pose;}
    ElapsedTime timer = new ElapsedTime();
    @Override
    protected void loopRun() {
        if(timer.seconds()>0.1) {
            timer.reset();

            @SuppressLint("DefaultLocale") String data = String.format("%f,%f,%f", pose.h, pose.x, pose.y);

            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);

            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
            Telemetry.getInstance().add("Sent", data);

            try {
                socket.send(packet);
            } catch (IOException e) {
                System.exit(-1);
                Telemetry.getInstance().add("e", e);
            }
        }
    }

}
