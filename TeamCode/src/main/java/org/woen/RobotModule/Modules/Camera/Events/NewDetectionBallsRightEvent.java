package org.woen.RobotModule.Modules.Camera.Events;

import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;
import org.woen.Architecture.EventBus.IEvent;

public class NewDetectionBallsRightEvent implements IEvent<PredominantColorProcessor.Swatch> {
    private final PredominantColorProcessor.Swatch data;

    public NewDetectionBallsRightEvent(PredominantColorProcessor.Swatch data) {
        this.data = data;
    }

    public PredominantColorProcessor.Swatch getData() {
        return data;
    }
}