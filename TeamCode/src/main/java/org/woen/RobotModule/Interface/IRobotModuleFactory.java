package org.woen.RobotModule.Interface;


import org.woen.RobotModule.Factory.ModulesActivateConfig;

public interface IRobotModuleFactory {
    IRobotModule[] create();
    void setConfig(ModulesActivateConfig config);
}
