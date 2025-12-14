package org.woen.RobotModule.Modules.Camera;

import android.graphics.Canvas;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

@Config
public class BallDetectProcessor implements VisionProcessor {
    public static int h = 100;
    public static int w = 100;

    public static int yr = 100;
    public static int xr = 100;

    public static int yc = 100;
    public static int xc = 100;

    public static int yl = 100;
    public static int xl = 100;

    @Override
    public void init(int width, int height, CameraCalibration calibration) {

    }

    Mat right;
    Mat center;
    Mat left;

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        right = new Mat(frame, new Rect(xr,yr,h,w));
        left = new Mat(frame, new Rect(xl,yl,h,w));
        center = new Mat(frame, new Rect(xc,yc,h,w));

        Imgproc.cvtColor(right,right,Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(left,left,Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(center,center,Imgproc.COLOR_BGR2GRAY);

        return frame;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }
}
