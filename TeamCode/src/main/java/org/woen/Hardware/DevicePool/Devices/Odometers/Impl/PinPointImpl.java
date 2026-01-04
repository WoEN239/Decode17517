package org.woen.Hardware.DevicePool.Devices.Odometers.Impl;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;
import org.woen.Config.MatchData;
import org.woen.Hardware.DevicePool.Devices.Odometers.Inter.PinPoint;
import org.woen.Util.Vectors.Pose;

public class PinPointImpl implements PinPoint {

    private final GoBildaPinpointDriver pinpointDriver;

    public PinPointImpl(GoBildaPinpointDriver pinpointDriver) {
        this.pinpointDriver = pinpointDriver;
    }

    public void init(){
        pinpointDriver.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.REVERSED);
        pinpointDriver.setOffsets(12.589,0, DistanceUnit.CM);
        pinpointDriver.setPosition(new Pose2D(DistanceUnit.CM, MatchData.start.pose.x, MatchData.start.pose.y,
                AngleUnit.RADIANS,MatchData.start.pose.h));
        pinpointDriver.recalibrateIMU();
    }

    public void update(){
        pinpointDriver.update();
    }
    @Override
    public Pose getPose() {
        return Pose.fromRR(pinpointDriver.getPosition());
    }

    @Override
    public Pose getVel(){
        return new Pose(pinpointDriver.getHeadingVelocity(UnnormalizedAngleUnit.RADIANS),
                        pinpointDriver.getVelX(DistanceUnit.CM),
                        pinpointDriver.getVelY(DistanceUnit.CM));
    }


}
