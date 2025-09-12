package org.woen.RobotModule.Factory.ModuleFactories;



import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.RobotModule.Interface.IRobotModuleFactory;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Impls.DriveTrainImpl;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Impls.DriveTrainMoc;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Interface.DriveTrain;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.VoltageContrloller;
import org.woen.RobotModule.Modules.TrajectoryFollower.Impls.TrajectoryFollowerImpl;
import org.woen.RobotModule.Modules.TrajectoryFollower.Impls.TrajectoryFollowerMoc;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;

public class DriveTrainFactory implements IRobotModuleFactory {
    private ModulesActivateConfig config;

    public void setConfig(ModulesActivateConfig config) {
        this.config = config;
    }

    @Override
    public IRobotModule[] create(){
        return new IRobotModule[]{
            createTrajectoryFollower(),createDriveTrain(),createVoltageController()
        };
    }

    public DriveTrain createDriveTrain(){
        if(config.driveTrain.driveTrain.get()){
            return new DriveTrainImpl();
        }else{
            return new DriveTrainMoc();
        }
    }
    public VoltageContrloller createVoltageController(){
        if(config.driveTrain.voltageController.get()){
            return new VoltageContrloller() {};
        }else{
            return new VoltageContrloller() {};
        }
    }
    public TrajectoryFollower createTrajectoryFollower(){
        if(config.driveTrain.trajectoryFollower.get()){
            return new TrajectoryFollowerImpl();
        }else{
            return new TrajectoryFollowerMoc();
        }
    }



}
