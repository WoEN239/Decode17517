package org.woen.RobotModule.Factory.ModuleFactories;

import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.RobotModule.Interface.IRobotModuleFactory;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Impls.LocalizerDeviceListenerImpl;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Impls.LocalizerDeviceListenerMoc;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Interface.LocalizerDeviceListener;
import org.woen.RobotModule.Modules.Localizer.LocalizerImpl;
import org.woen.RobotModule.Modules.Localizer.Impls.PositionLocalizerMoc;
import org.woen.RobotModule.Modules.Localizer.Interface.PositionLocalizer;

public class LocalizerFactory implements IRobotModuleFactory {
    private ModulesActivateConfig config;

    public void setConfig(ModulesActivateConfig config) {
        this.config = config;
    }

    @Override
    public IRobotModule[] create(){
        return new IRobotModule[]{
                createDeviceListener(), createPositionLocalizer()
        };
    }

    public PositionLocalizer createPositionLocalizer(){
        if(config.localizer.position.get()){
            return new LocalizerImpl();
        }else{
            return new PositionLocalizerMoc();
        }
    }

    public LocalizerDeviceListener createDeviceListener(){
        if(config.localizer.device.get()){
            return new LocalizerDeviceListenerImpl();
        }else{
            return new LocalizerDeviceListenerMoc();
        }
    }

}
