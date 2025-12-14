package org.woen.Hardware.Devices.Odometers.Impl;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;
import org.woen.Config.MatchData;
import org.woen.Hardware.Devices.Odometers.Inter.PinPoint;
import org.woen.Util.Vectors.Pose;

public class PinPointImpl implements PinPoint {

    private final GoBildaPinpointDriver pinpointDriver;

    public PinPointImpl(GoBildaPinpointDriver pinpointDriver) {
        this.pinpointDriver = pinpointDriver;
    }

    public void init(){
        pinpointDriver.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.REVERSED);
        pinpointDriver.setOffsets(12.75,0, DistanceUnit.CM);
        pinpointDriver.setPosition(new Pose2D(DistanceUnit.CM, MatchData.startPosition.x, MatchData.startPosition.y,
                AngleUnit.RADIANS,MatchData.startPosition.h));
        pinpointDriver.recalibrateIMU();
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
