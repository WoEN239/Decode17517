package org.woen.OpModes.Test;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.MatchData;
import org.woen.Hardware.Factory.DeviceActivationConfig;
import org.woen.OpModes.BaseOpMode;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.DriveTrain.ActivationConfig.DriveTrainActivationConfig;
import org.woen.RobotModule.Modules.Localizer.ActivationConfig.LocalizerActivationConfig;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewPositionListener;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Vectors.Pose;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

@TeleOp(name = "localizer_test",group = "test")
public class LocalizerTest extends BaseOpMode {

    @Override
    protected void initConfig(){
        DeviceActivationConfig deviceConfig = new DeviceActivationConfig();
        deviceConfig.motors.set(false);
        deviceConfig.servos.set(false);
        deviceConfig.odometers.set(true);
        deviceActivationConfig = deviceConfig;

        ModulesActivateConfig moduleConfig = new ModulesActivateConfig();
        moduleConfig.localizer = LocalizerActivationConfig.getAllOn();
        moduleConfig.driveTrain = DriveTrainActivationConfig.getAllOff();
        moduleConfig.camera.set(false);
        moduleConfig.gun.set(false);
        moduleConfig.autonomTaskManager.set(false);
        modulesActivationConfig = moduleConfig;
    }

    DatagramSocket socket = null;
    InetAddress address = null;
    int port = 5005;

    @Override
    public void initRun(){
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
        if(timer.seconds()>0.25) {
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