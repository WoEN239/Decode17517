package org.woen.RobotModule.Modules.Camera;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class BallsDetectProcessor implements VisionProcessor {
    Mat rMat;
    Mat cMat;
    Mat lMat;

    Rect rRoi;
    Rect lRoi;
    Rect cRoi;
    @Override
    public void init(int width, int height, CameraCalibration calibration) {

    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        lMat = frame.submat(lRoi);
        cMat = frame.submat(cRoi);
        rMat = frame.submat(rRoi);
        return null;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }
}
