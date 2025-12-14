package org.woen.RobotModule.Factory.ModuleFactories;

import org.woen.Autonom.WaypointsManager;
import org.woen.Autonom.WaypointsManagerImpl;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.RobotModule.Interface.IRobotModuleFactory;
import org.woen.RobotModule.Modules.Camera.Camera;
import org.woen.RobotModule.Modules.Camera.CameraImpl;
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
                createGun(),createAutonomTaskManager(),createCamera()
        };
    }

    private Gun createGun(){
        if(config.gun.get()){
            return new GunImpl();
        }else{
            return new Gun() {};
        }
    }

    private Camera createCamera(){
        if(config.camera.get()){
            return new CameraImpl();
        }else{
            return new Camera() {};
        }
    }

    private WaypointsManager createAutonomTaskManager(){
        if(config.autonomTaskManager.get()){
            return new WaypointsManagerImpl();
        }else{
            return new WaypointsManager() {};
        }
    }
}
