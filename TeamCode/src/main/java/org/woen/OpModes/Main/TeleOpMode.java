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
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReference;
import org.woen.RobotModule.Modules.TrajectoryFollower.Arcitecture.Feedforward.FeedforwardReferenceObserver;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;
import org.woen.Telemetry.Telemetry;
import org.woen.Util.Pid.PidStatus;
import org.woen.Util.Vectors.Pose;

@TeleOp(name = "teleop")
public class TeleOpMode extends BaseOpMode{
    private final FeedforwardReferenceObserver velocityObserver = new FeedforwardReferenceObserver();

    @Override
    protected void initConfig(){
        DeviceActivationConfig devConfig =  DeviceActivationConfig.getAllOn();
        devConfig.servos.set(true);
        devConfig.colorSensor.set(true);
        deviceActivationConfig = devConfig;

        ModulesActivateConfig modConfig = ModulesActivateConfig.getAllOn();
        modConfig.driveTrain.trajectoryFollower.set(false);
        modConfig.gun.set(true);
        modConfig.autonomTaskManager.set(false);

        modulesActivationConfig = modConfig;

    }

    @Override
    protected void modulesReplace(){
        robot.getFactory().replace(TrajectoryFollower.class, new TrajectoryFollower(){});
        EventBus.getInstance().invoke(new ReplaceFeedbackControllerEvent(new StubTankFeedback()));
    }

    private BorderButton isHiAimButt = new BorderButton();
    boolean isHiAim = true;
    @Override
    protected void loopRun() {

        velocityObserver.notifyListeners(new FeedforwardReference(new Pose(
                -gamepad1.right_stick_x*5,-gamepad1.left_stick_y*150, 0
        ),new Pose(0,0,0)));

        Telemetry.getInstance().add("triangle",gamepad1.triangle);

        Telemetry.getInstance().add("r", DevicePool.getInstance().sensorC.getRed());
        Telemetry.getInstance().add("g",DevicePool.getInstance().sensorC.getGreen());
        Telemetry.getInstance().add("b",DevicePool.getInstance().sensorC.getBlue());

        telemetry.addData("gun vel",DevicePool.getInstance().gunR.getVel());
        telemetry.update();

        if(gamepad1.right_bumper){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.TARGET));
        }
        if(gamepad1.left_bumper){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.REVERSE));
        }
        if(isHiAimButt.get(gamepad1.square)){
            isHiAim = !isHiAim;
            EventBus.getInstance().invoke(new NewAimCommandAvaliable(isHiAim));
        }

        if(gamepad1.right_trigger>0.1){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.FULL_FIRE));
        }
        if(gamepad1.dpad_left){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.SHOT_LEFT));
        }
        if(gamepad1.dpad_right){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.SHOT_RIGHT));
        }
        if(gamepad1.dpad_up){
            EventBus.getInstance().invoke(new NewGunCommandAvailable(GUN_COMMAND.SHOT_CENTER));
        }

    }


    private static class StubTankFeedback extends TankFeedbackController{
        public StubTankFeedback() {
            super(new PidStatus(0,0,0,0,0,0,0),
                  new PidStatus(0,0,0,0,0,0,0)
            );
        }
        @Override
        public Pose computeU(Pose target, Pose localPosition, Pose targetVel, Pose localVel)
        {
            return new Pose(0,0,0);
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
