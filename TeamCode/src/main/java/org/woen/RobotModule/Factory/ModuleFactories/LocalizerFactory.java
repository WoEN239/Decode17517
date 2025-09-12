package org.woen.RobotModule.Factory.ModuleFactories;

import org.woen.RobotModule.Factory.ModulesActivateConfig;
import org.woen.RobotModule.Interface.IRobotModule;
import org.woen.RobotModule.Interface.IRobotModuleFactory;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Impls.LocalizerDeviceListenerImpl;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Impls.LocalizerDeviceListenerMoc;
import org.woen.RobotModule.Modules.Localizer.DeviceListener.Interface.LocalizerDeviceListener;
import org.woen.RobotModule.Modules.Localizer.Position.Impls.PositionLocalizerImpl;
import org.woen.RobotModule.Modules.Localizer.Position.Impls.PositionLocalizerMoc;
import org.woen.RobotModule.Modules.Localizer.Position.Interface.PositionLocalizer;
import org.woen.RobotModule.Modules.Localizer.Velocity.Impls.VelocityLocalizerImpl;
import org.woen.RobotModule.Modules.Localizer.Velocity.Impls.VelocityLocalizerMoc;
import org.woen.RobotModule.Modules.Localizer.Velocity.Interface.VelocityLocalizer;

public class LocalizerFactory implements IRobotModuleFactory {
    private ModulesActivateConfig config;

    public void setConfig(ModulesActivateConfig config) {
        this.config = config;
    }

    @Override
    public IRobotModule[] create(){
        return new IRobotModule[]{
                createDeviceListener(), createPositionLocalizer(), createVelocityLocalizer()
        };
    }

    public PositionLocalizer createPositionLocalizer(){
        if(config.localizer.position.get()){
            return new PositionLocalizerImpl();
        }else{
            return new PositionLocalizerMoc();
        }
    }

    public VelocityLocalizer createVelocityLocalizer(){
        if(config.localizer.velocity.get()){
            return new VelocityLocalizerImpl();
        }else{
            return new VelocityLocalizerMoc();
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
