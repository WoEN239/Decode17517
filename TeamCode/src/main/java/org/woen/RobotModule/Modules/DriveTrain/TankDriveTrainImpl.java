package org.woen.RobotModule.Modules.DriveTrain;

import static org.woen.Config.ControlSystemConstant.*;

import static java.lang.Math.signum;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Config.ControlSystemConstant;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.ReplaceFeedbackControllerEvent;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.TankFeedbackController;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedforwardController.FeedforwardController;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Interface.DriveTrain;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelValueMap;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelsVoltageObserver;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.Localizer.Architecture.RegisterNewVelocityListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.FeedbackReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedback.RegisterNewFeedbackReferenceListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.RegisterNewFeedforwardReferenceListener;
import org.woen.Util.Vectors.Pose;

public class TankDriveTrainImpl implements DriveTrain {

    @Override
    public void lateUpdate() {
        WheelValueMap feedback = toWheelsVoltage(
                feedbackController.computeU(feedbackReference.pos, pose,
                        feedbackReference.vel,velocity));

        double kV = feedforwardConfig.xFeedforwardKV;
        double kA = feedforwardConfig.xFeedforwardKA;
        if(signum(feedforwardReference.accel.h) != signum(feedforwardReference.vel.h) ){
            kA = feedforwardConfig.xFeedforwardKAReverse;
        }
        FeedforwardController feedforwardController = new FeedforwardController(
                new WheelValueMap(kV,kV,kV,kV),
                new WheelValueMap(kA,kA,kA,kA)
        );

        WheelValueMap feedforward = feedforwardController.computeU(toWheelsVelocities(feedforwardReference.vel),
                toWheelsVelocities(feedforwardReference.accel));

        wheelsVoltageObserver.notifyListeners(feedforward.plus(feedback));
    }

    private WheelValueMap toWheelsVoltage(Pose r){
        return new WheelValueMap(
                r.vector.x-r.h,
                r.vector.x+r.h,
                r.vector.x+r.h,
                r.vector.x-r.h
        );
    }

    private WheelValueMap toWheelsVelocities(Pose r){
        return new WheelValueMap(
                (r.vector.x - feedforwardConfig.hSlip*r.h*robotSizeConfig.B*0.5)/robotSizeConfig.wheelR,
                (r.vector.x + feedforwardConfig.hSlip*r.h*robotSizeConfig.B*0.5)/robotSizeConfig.wheelR,
                (r.vector.x + feedforwardConfig.hSlip*r.h*robotSizeConfig.B*0.5)/robotSizeConfig.wheelR,
                (r.vector.x - feedforwardConfig.hSlip*r.h*robotSizeConfig.B*0.5)/robotSizeConfig.wheelR
        );
    }

    private FeedforwardReference feedforwardReference = new FeedforwardReference(new Pose(0,0,0),
            new Pose(0,0,0));
    private FeedbackReference feedbackReference       = new FeedbackReference(new Pose(0,0,0),
            new Pose(0,0,0));

    //all in robot reference frame
    private Pose pose = new Pose(0,0,0);
    private Pose velocity = new Pose(0,0,0);

    public void setPose(Pose pose) {
        this.pose = pose;
    }

    public void setLocalVelocity(Pose velocity) {
        this.velocity = velocity;
    }

    private TankFeedbackController feedbackController = new TankFeedbackController(
            ControlSystemConstant.feedbackConfig.xPid,
            ControlSystemConstant.feedbackConfig.hPid);

    private void replaceFeedbackController(ReplaceFeedbackControllerEvent event){
        feedbackController = event.getData();
    }

    public void setFeedforwardReference(FeedforwardReference feedforwardReference) {
        this.feedforwardReference = feedforwardReference;
    }

    public void setFeedbackReference(FeedbackReference  feedbackReference) {
        this.feedbackReference = feedbackReference;
    }

    private final WheelsVoltageObserver wheelsVoltageObserver = new WheelsVoltageObserver();

    @Override
    public void init() {
        EventBus.getListenersRegistration().invoke(
                new RegisterNewFeedbackReferenceListener(this::setFeedbackReference));
        EventBus.getListenersRegistration().invoke(
                new RegisterNewFeedforwardReferenceListener(this::setFeedforwardReference));

        EventBus.getListenersRegistration().invoke(
                new RegisterNewPositionListener(this::setPose));
        EventBus.getListenersRegistration().invoke(
                new RegisterNewVelocityListener(this::setLocalVelocity));

        EventBus.getInstance().subscribe(ReplaceFeedbackControllerEvent.class,this::replaceFeedbackController);

    }

}
