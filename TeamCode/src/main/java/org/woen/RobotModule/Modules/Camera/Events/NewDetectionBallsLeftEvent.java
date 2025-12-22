package org.woen.RobotModule.Modules.Camera.Events;

import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;
import org.woen.Architecture.EventBus.IEvent;
import org.woen.RobotModule.Modules.Camera.Enums.BALL_COLOR;

public class NewDetectionBallsLeftEvent implements IEvent<PredominantColorProcessor.Swatch> {
    private final PredominantColorProcessor.Swatch data;

    public NewDetectionBallsLeftEvent(PredominantColorProcessor.Swatch data) {
        this.data = data;
    }

    public PredominantColorProcessor.Swatch getData() {
        return data;
    }
}