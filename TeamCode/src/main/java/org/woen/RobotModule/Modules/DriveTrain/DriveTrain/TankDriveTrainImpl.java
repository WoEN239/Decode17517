package org.woen.RobotModule.Modules.DriveTrain.DriveTrain;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.ControlSystemConstant;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Interface.DriveTrain;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.TankKinematics;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.TankWheelValueMap;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.TankWheelsVoltageObserver;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewVelocityListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.RegisterNewFeedforwardReferenceListener;
import org.woen.Util.Pid.Pid;
import org.woen.Util.Vectors.Pose;

public class TankDriveTrainImpl implements DriveTrain {
    private Pid lPid = new Pid(ControlSystemConstant.feedbackConfig.wheelVelPid);
    private Pid rPid = new Pid(ControlSystemConstant.feedbackConfig.wheelVelPid);
    private TankKinematics kinematics = new TankKinematics(ControlSystemConstant.robotSizeConfig.B,ControlSystemConstant.robotSizeConfig.wheelR);
    @Override
    public void lateUpdate() {
        double dir = Math.signum(velocity.vector.rotate(-pose.h).x);
        TankWheelValueMap wheelVelocity = kinematics.getWheel(velocity.h, dir*velocity.vector.length());

        lPid.setPos(wheelVelocity.l);
        rPid.setPos(wheelVelocity.r);

        TankWheelValueMap wheelTaget = kinematics.getWheel(feedforwardReference.vel.h,feedforwardReference.vel.x);
        TankWheelValueMap wheelF = wheelTaget.multiply(ControlSystemConstant.feedforwardConfig.motorFeedforward);

        lPid.setTarget(wheelTaget.l);
        rPid.setTarget(wheelTaget.r);

        lPid.update();
        rPid.update();

        wheelsVoltageObserver.notifyListeners(new TankWheelValueMap(lPid.getU() + wheelF.l, rPid.getU() + wheelF.r));

    }

    private FeedforwardReference feedforwardReference = new FeedforwardReference(new Pose(0,0,0), new Pose(0,0,0));
    private Pose pose = new Pose(0,0,0);
    private Pose velocity = new Pose(0,0,0);
    public void setPose(Pose pose) {
        this.pose = pose;
    }
    public void seVel(Pose velocity) {
        this.velocity = velocity;
    }
    public void setFeedforwardReference(FeedforwardReference feedforwardReference) {
        this.feedforwardReference = feedforwardReference;
    }
    private final TankWheelsVoltageObserver wheelsVoltageObserver = new TankWheelsVoltageObserver();

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(new RegisterNewFeedforwardReferenceListener(this::setFeedforwardReference));
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));
        EventBus.getListenersRegistration().invoke(new RegisterNewVelocityListener(this::seVel));
    }
}
