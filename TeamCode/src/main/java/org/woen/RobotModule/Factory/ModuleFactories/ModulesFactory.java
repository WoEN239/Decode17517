package org.woen.RobotModule.Factory.ModuleFactories;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.Architecture.EventBus.EventBus;
import org.woen.Autonom.Structure.WaypointsManager;
import org.woen.Autonom.Structure.WaypointsManagerImpl;
import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.RobotModule.Interface.IRobotModuleFactory;
import org.woen.RobotModule.Modules.Camera.Interfaces.Camera;
import org.woen.RobotModule.Modules.Camera.CameraImpl;
import org.woen.RobotModule.Modules.Gun.Arcitecture.GunAtEatEvent;
import org.woen.RobotModule.Modules.Gun.GunImpl;
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
            return new Gun() {
                ElapsedTime timer = new ElapsedTime();
                @Override
                public void update() {
                    if(timer.seconds()>2) {
                        EventBus.getInstance().invoke(new GunAtEatEvent(true));
                        timer.reset();
                    }
                }
            };
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
