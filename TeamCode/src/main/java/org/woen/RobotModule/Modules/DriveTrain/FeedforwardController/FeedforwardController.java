package org.woen.RobotModule.Modules.DriveTrain.FeedforwardController;

import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelValueMap;

public class FeedforwardController {
    private final WheelValueMap kMap;

    public FeedforwardController(WheelValueMap kMap) {
        this.kMap = kMap;
    }

    public WheelValueMap computeU(WheelValueMap targetVel){
        return new WheelValueMap(
                targetVel.lf*kMap.lf,
                targetVel.rf*kMap.rf,
                targetVel.rb*kMap.rb,
                targetVel.lb*kMap.lb
        );
    }
}
