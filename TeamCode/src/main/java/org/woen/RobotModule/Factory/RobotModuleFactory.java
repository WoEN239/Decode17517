package org.woen.RobotModule.Factory;

import org.woen.RobotModule.Factory.ModuleFactories.DriveTrainFactory;
import org.woen.RobotModule.Factory.ModuleFactories.GunFactory;
import org.woen.RobotModule.Factory.ModuleFactories.LocalizerFactory;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.RobotModule.Interface.IRobotModuleFactory;
import org.woen.RobotModule.Modules.TrajectoryFollower.Interface.TrajectoryFollower;

import java.util.ArrayList;
import java.util.Arrays;

public class RobotModuleFactory {
    private final ArrayList<IRobotModuleFactory> factories = new ArrayList<>(Arrays.asList(
            new LocalizerFactory(), new DriveTrainFactory(), new GunFactory()
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

    public <T extends IRobotModule> void replace(Class<T> type, T module){
        modules.removeIf(i -> type.isAssignableFrom(i.getClass()));
        modules.add(module);
    }

    private void setConfig(ModulesActivateConfig config){
        factories.forEach(i->i.setConfig(config));
    }
}
