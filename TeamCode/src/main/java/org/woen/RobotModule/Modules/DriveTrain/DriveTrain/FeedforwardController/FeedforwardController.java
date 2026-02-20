package org.woen.RobotModule.Modules.DriveTrain.DriveTrain.FeedforwardController;

import org.woen.RobotModule.Modules.DriveTrain.VoltageController.Architecture.WheelValueMap;

public class FeedforwardController {
    private final WheelValueMap kVMap;
    private final WheelValueMap kAMap;

    public FeedforwardController(WheelValueMap kVMap, WheelValueMap kAMap) {
        this.kVMap = kVMap;
        this.kAMap = kAMap;
    }

    public WheelValueMap computeU(WheelValueMap targetVel,WheelValueMap targetA){
        return targetVel.dot(kVMap).plus(targetA.dot(kAMap));
    }
}
