package org.woen.RobotModule;

import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Factory.RobotModuleFactory;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.Telemetry.Telemetry;

public class Robot {
    public RobotModuleFactory getFactory() {
        return factory;
    }

    private RobotModuleFactory factory;

    public void factoryInit(ModulesActivateConfig config){
        factory = new RobotModuleFactory();
        factory.init(config);
    }

    public void modulesInit(){
        factory.getModules().forEach(IRobotModule::subscribeInit);
        factory.getModules().forEach(IRobotModule::init);
        Telemetry.getInstance().subscribeInit();
    }

    public void update(){
        factory.getModules().forEach(IRobotModule::deviceReadUpdate);
        factory.getModules().forEach(IRobotModule::update);
        factory.getModules().forEach(IRobotModule::lateUpdate);
        factory.getModules().forEach(IRobotModule::deviceSetUpdate);
        Telemetry.getInstance().loopEnd();
    }
}
