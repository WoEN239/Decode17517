package org.woen.RobotModule.Factory.ModuleFactories;



import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.RobotModule.Interface.IRobotModuleFactory;
import org.woen.RobotModule.Modules.Battery.Battery;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Impls.DriveTrainImpl;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Impls.DriveTrainMoc;
import org.woen.RobotModule.Modules.DriveTrain.DriveTrain.Interface.DriveTrain;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Impls.VoltageControllerImpl;
import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Interface.VoltageController;
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
            createTrajectoryFollower(),createDriveTrain(),createVoltageController(),new Battery()
        };
    }

    public DriveTrain createDriveTrain(){
        if(config.driveTrain.driveTrain.get()){
            return new DriveTrainImpl();
        }else{
            return new DriveTrainMoc();
        }
    }
    public VoltageController createVoltageController(){
        if(config.driveTrain.voltageController.get()){
            return new VoltageControllerImpl();
        }else{
            return new VoltageController() {};
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
