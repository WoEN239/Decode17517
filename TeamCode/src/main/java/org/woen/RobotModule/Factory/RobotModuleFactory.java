package org.woen.RobotModule.Factory;

import org.woen.RobotModule.Factory.ModuleFactories.DriveTrainFactory;
import org.woen.RobotModule.Factory.ModuleFactories.LocalizerFactory;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.RobotModule.Interface.IRobotModuleFactory;

import java.util.ArrayList;
import java.util.Arrays;

public class RobotModuleFactory {
    private final ArrayList<IRobotModuleFactory> factories = new ArrayList<>(Arrays.asList(
        new DriveTrainFactory(),new LocalizerFactory()
    ));

    public ArrayList<IRobotModule> getModules() {
        return modules;
    }

    private final ArrayList<IRobotModule> modules = new ArrayList<>();
    public void init(ModulesActivateConfig config){
        setConfig(config);
        for (IRobotModuleFactory i : factories) {
            modules.addAll(Arrays.asList(i.create()));
        }
    }

    private void setConfig(ModulesActivateConfig config){
        factories.forEach(i->i.setConfig(config));
    }
}
