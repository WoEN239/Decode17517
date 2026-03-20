package org.woen.RobotModule.Modules.DriveTrain.DriveTrain;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.ControlSystemConstant;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Interface.DriveTrain;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.TankKinematics;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.TankWheelValueMap;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.TankWheelsVoltageObserver;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewVelocityListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.RegisterNewTankFeedbackReferenceListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.TankFeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.RegisterNewFeedforwardReferenceListener;
import org.woen.Util.Pid.Pid;
import org.woen.Util.Vectors.Pose;

public class TankDriveTrainImpl implements DriveTrain {
    private final Pid lPid = new Pid(ControlSystemConstant.tankFeedbackConfig.wheelVelPid);
    private final Pid rPid = new Pid(ControlSystemConstant.tankFeedbackConfig.wheelVelPid);
    private final TankKinematics kinematics = new TankKinematics(ControlSystemConstant.robotSizeConfig.B,ControlSystemConstant.robotSizeConfig.wheelR);
    private final Pid anglePid = new Pid(ControlSystemConstant.tankFeedbackConfig.hPid);
    @Override
    public void lateUpdate() {
        double dir = Math.signum(velocity.vector.rotate(-pose.h).x);
        TankWheelValueMap wheelVelocity = kinematics.getWheel(velocity.h, dir*velocity.vector.length());

        lPid.setPos(wheelVelocity.l);
        rPid.setPos(wheelVelocity.r);

        TankWheelValueMap wheelTaget = kinematics.getWheel(feedforwardReference.vel.h+feedback(),feedforwardReference.vel.x);

        lPid.setTarget(wheelTaget.l);
        rPid.setTarget(wheelTaget.r);

        lPid.update();
        rPid.update();

        wheelsVoltageObserver.notifyListeners(new TankWheelValueMap(lPid.getU(), rPid.getU()));

    }
    private Double feedback(){
        if (!feedbackReference.isEnable){
            return 0d;
        }
        anglePid.setTarget(feedbackReference.angle);
        anglePid.setPos(pose.h);
        anglePid.update();
        return anglePid.getU();
    }
    private TankFeedbackReference feedbackReference = new TankFeedbackReference(false,0);
    private FeedforwardReference feedforwardReference = new FeedforwardReference(new Pose(0,0,0), new Pose(0,0,0));
    public void setFeedforwardReference(FeedforwardReference r) {this.feedforwardReference = r;}
    public void setFeedbackReference(TankFeedbackReference r) {this.feedbackReference = r;}
    private Pose pose = new Pose(0,0,0);
    private Pose velocity = new Pose(0,0,0);
    public void setPose(Pose pose) {
        this.pose = pose;
    }
    public void seVel(Pose velocity) {
        this.velocity = velocity;
    }
    private final TankWheelsVoltageObserver wheelsVoltageObserver = new TankWheelsVoltageObserver();
    @Override
    public void init() {
        anglePid.isNormolized = true;
        EventBus.getListenersRegistration().invoke(new RegisterNewFeedforwardReferenceListener(this::setFeedforwardReference));
        EventBus.getListenersRegistration().invoke(new RegisterNewTankFeedbackReferenceListener(this::setFeedbackReference));
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPose));
        EventBus.getListenersRegistration().invoke(new RegisterNewVelocityListener(this::seVel));

    }
}
