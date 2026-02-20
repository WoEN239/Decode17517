package org.woen.RobotModule.Modules.Camera.Util;

import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

public enum BALL_COLOR {
    G, P;
    public PredominantColorProcessor.Swatch toSwatch(){
        if (this == G){
            return PredominantColorProcessor.Swatch.ARTIFACT_GREEN;
        }else{
            return PredominantColorProcessor.Swatch.ARTIFACT_PURPLE;
        }
    }
}
