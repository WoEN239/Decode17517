package org.woen.OpModes.Test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

@TeleOp(name = "upd_test")
public class UPDTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DatagramSocket socket = null;
        InetAddress address = null;
        int port = 5005;
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("192.168.43.170");
        } catch (SocketException e) {
            System.err.println("Socket exception: " + e);
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("IO exception: " + e);
            System.exit(-1);
        }
        long start = System.currentTimeMillis();
        waitForStart();
        while (opModeIsActive()){
            double time = (double)(System.currentTimeMillis() - start) / 1000.0;
            double value = Math.sin(time * Math.PI * 0.5);
            String data = String.format("%f,%f,%f", time, value,0.0);
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
            FtcDashboard.getInstance().getTelemetry().addData("data",data);
            FtcDashboard.getInstance().getTelemetry().update();
            try {
                socket.send(packet);
            } catch (IOException e) {
                System.err.println("IOException " + e);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {

            }

        }
    }
}
