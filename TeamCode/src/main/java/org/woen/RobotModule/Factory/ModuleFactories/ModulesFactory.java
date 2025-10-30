package org.woen.RobotModule.Factory.ModuleFactories;

import org.woen.Autonom.AutonomTaskManager;
import org.woen.Autonom.AutonomTaskManagerImpl;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.RobotModule.Interface.IRobotModuleFactory;
import org.woen.RobotModule.Modules.Gun.Impls.GunImpl;
import org.woen.RobotModule.Modules.Gun.Interface.Gun;

public class ModulesFactory implements IRobotModuleFactory {
    private ModulesActivateConfig config;

    @Override
    public void setConfig(ModulesActivateConfig config) {
        this.config = config;
    }

    @Override
    public IRobotModule[] create() {
        return new IRobotModule[]{
                createGun(),createAutonomTaskManager()
        };
    }

    private Gun createGun(){
        if(config.gun.get()){
            return new GunImpl();
        }else{
            return new Gun() {};
        }
    }

    private AutonomTaskManager createAutonomTaskManager(){
        if(config.autonomTaskManager.get()){
            return new AutonomTaskManagerImpl();
        }else{
            return new AutonomTaskManager() {};
        }
    }
}
