package org.woen.OpModes.Main;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.ActivationConfig.DeviceActivationConfig;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.ReplaceFeedbackControllerEvent;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedbackController.TankFeedbackController;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewAimCommandAvaliable;
import org.woen.RobotModule.Modules.Gun.Arcitecture.NewGunCommandAvailable;
import org.woen.RobotModule.Modules.Gun.Config.GUN_COMMAND;
import org.woen.RobotModule.Modules.Localizer.Position.Architecture.RegisterNewPositionListener;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Util.Pid.Pid;
import org.woen.Util.Pid.PidStatus;
import org.woen.Util.Vectors.Pose;

@TeleOp(name = "teleop")
public class TeleOpMode extends BaseOpMode{
    private final FeedforwardReferenceObserver feedforwardReferenceObserver = new FeedforwardReferenceObserver();

    @Override
    protected void initConfig(){
        DeviceActivationConfig devConfig =  DeviceActivationConfig.getAllOn();
        devConfig.servos.set(true);
        devConfig.odometers.set(true);
        devConfig.motors.set(true);
        devConfig.colorSensor.set(false);
        deviceActivationConfig = devConfig;

        ModulesActivateConfig modConfig = ModulesActivateConfig.getAllOn();
        modConfig.driveTrain.trajectoryFollower.set(false);
        modConfig.driveTrain.voltageController.set(true);
        modConfig.gun.set(true);
        modConfig.camera.set(false);
        modConfig.autonomTaskManager.set(false);

        modulesActivationConfig = modConfig;

    }

    private Pose position = new Pose(0,0,0);
    private void setPosition(Pose p){
        position = p;
    }

    @Override
    protected void modulesReplace(){
        robot.getFactory().replace(TrajectoryFollower.class, new TrajectoryFollower(){});
        EventBus.getInstance().invoke(new ReplaceFeedbackControllerEvent(new StubTankFeedback()));
        EventBus.getListenersRegistration().invoke(new RegisterNewPositionListener(this::setPosition));
    }

    private BorderButton hiAimButt = new BorderButton();
    private BorderButton angleControlButt = new BorderButton();
    private BorderButton angleResetButt = new BorderButton();
    private BorderButton dirReverseButt = new BorderButton();
    private BorderButton brushReverseButt = new BorderButton();
    private boolean isHiAim = true;
    private int dir = 1;
    @Override
    protected void loopRun() {

        feedforwardReferenceObserver.notifyListeners(new FeedforwardReference(new Pose(
                -(gamepad1.right_stick_x*Math.abs(gamepad1.right_stick_x))*8,
                -dir*(gamepad1.left_stick_y*Math.abs(gamepad1.left_stick_y)*270), 0
        ),new Pose(0,0,0)));

        telemetry.addData("gun vel",DevicePool.getInstance().gunR.getVel());
        telemetry.update();

        if(gamepad1.left_trigger>0.1){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.REVERSE));
        }
        if(brushReverseButt.get(gamepad1.left_trigger<0.1)){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.EAT));
        }
        if(dirReverseButt.get(gamepad1.left_bumper)){
            dir = - dir;
        }

        if(hiAimButt.get(gamepad1.dpad_up)){
            isHiAim = !isHiAim;
            EventBus.getInstance().invoke(new NewAimCommandAvaliable(isHiAim));
        }
        if(angleControlButt.get(gamepad1.right_trigger>0.1)){
            isAngleControl = !isAngleControl;
        }
        if(angleResetButt.get(gamepad1.right_bumper)){
            targetAngle = position.h;
        }
        if(gamepad1.cross){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE));
            isAngleControl = false;
        }
        if(gamepad1.circle){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.SHOT_RIGHT));
        }
        if(gamepad1.triangle){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.SHOT_CENTER));
        }
        if(gamepad1.square){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.SHOT_LEFT));
        }

    }

    private double targetAngle = 0;
    private boolean isAngleControl = false;
    private class StubTankFeedback extends TankFeedbackController{
        public StubTankFeedback() {
            super(new PidStatus(0,0,0,0,0,0,0),
                  new PidStatus(0,0,0,0,0,0,0)
            );
        }
        PidStatus pidStatus = new PidStatus(2,0,0,0,0,0,0);
        Pid pid = new Pid(pidStatus);
        {
            pid.isAngle = true;
            pid.isDAccessible = false;
        }
        @Override
        public Pose computeU(Pose target, Pose localPosition, Pose targetVel, Pose localVel)
        {
            pid.setTarget(targetAngle);
            pid.setPos(localPosition.h);
            pid.update();
            if(isAngleControl) {
                return new Pose(pid.getU(), 0, 0);
            }else{
                return new Pose(0,0,0);
            }
        }
    }

    private static class BorderButton{
        private boolean old = false;

        public boolean get(boolean button) {
            boolean indicator = (button != old) && button;
            old = button;
            return indicator;
        }

    }
}
