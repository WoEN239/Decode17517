package org.woen.RobotModule.Factory.ModuleFactories;

import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.RobotModule.Interface.IRobotModuleFactory;
import org.woen.RobotModule.Modules.Localizer.LocalizerImpl;
import org.woen.RobotModule.Modules.Localizer.Impls.LocalizerMoc;
import org.woen.RobotModule.Modules.Localizer.Interface.Localizer;

public class LocalizerFactory implements IRobotModuleFactory {
    private ModulesActivateConfig config;

    public void setConfig(ModulesActivateConfig config) {
        this.config = config;
    }

    @Override
    public IRobotModule[] create(){
        return new IRobotModule[]{
                createPositionLocalizer()
        };
    }

    public Localizer createPositionLocalizer(){
        if(config.localizer.position.get()){
            return new LocalizerImpl();
        }else{
            return new LocalizerMoc();
        }
    }


}
