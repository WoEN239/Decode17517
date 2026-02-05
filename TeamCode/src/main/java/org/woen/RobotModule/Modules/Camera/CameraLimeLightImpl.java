package org.woen.RobotModule.Modules.Camera;


import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;
import org.woen.Architecture.EventBus.EventBus;
import org.woen.Hardware.DevicePool.DevicePool;
import org.woen.OpModes.EndOfOpModeEvent;
import org.woen.RobotModule.Modules.Camera.Enums.MOTIF;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsCenterEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsLeftEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewDetectionBallsRightEvent;
import org.woen.RobotModule.Modules.Camera.Events.NewTargetMotifEvent;
import org.woen.RobotModule.Modules.Camera.Interfaces.Camera;
import org.woen.Telemetry.Telemetry;

import java.util.List;

public class CameraLimeLightImpl implements Camera {

    private Limelight3A camera;
    private Limelight3A id;
    private LLResult llResult;

    @Override
    public void subscribeInit(){
        EventBus.getInstance().subscribe(EndOfOpModeEvent.class,this::end);
    }

    @Override
    public void init(){
        HardwareMap hardwareMap = DevicePool.getInstance().hardwareMap;

        camera = hardwareMap.get(Limelight3A.class, "Limelight");

        id = hardwareMap.get(Limelight3A.class, "Limelight");

        camera.start();
        id.start();

        camera.pipelineSwitch(0);
        id.pipelineSwitch(1);

        camera.getLatestResult();
        id.getLatestResult();

    }

    private PredominantColorProcessor.Swatch getColor(double index){
        if(index == 1){
            return PredominantColorProcessor.Swatch.ARTIFACT_PURPLE;
        }
        if(index == 2){
            return PredominantColorProcessor.Swatch.ARTIFACT_GREEN;
        }
        if(index == 0){
            return PredominantColorProcessor.Swatch.BLACK;
        }
        return null;
    }

    private void end(EndOfOpModeEvent e){
        id.close();
        camera.close();
    }

    private MOTIF latterTargetMotif = null;

    public void update(){
        camera.pipelineSwitch(0);
        id.pipelineSwitch(1);

        llResult = camera.getLatestResult();
        double[] llResultPythonOutput = llResult.getPythonOutput();

        List<LLResultTypes.FiducialResult> fiducialResult = id.getLatestResult().getFiducialResults();

        for (LLResultTypes.FiducialResult fr : fiducialResult) {
            FtcDashboard.getInstance().getTelemetry().addData("id", fr.getFiducialId());
            double id = fr.getFiducialId();
            MOTIF motif = latterTargetMotif;
            if (id == 22) {
                motif = MOTIF.PGP;
            } else if (id == 23) {
                motif = MOTIF.PPG;
            } else if (id == 21) {
                motif = MOTIF.GPP;
            }
            if (latterTargetMotif != motif) {
                EventBus.getInstance().invoke(new NewTargetMotifEvent(motif));
            }
            latterTargetMotif = motif;
        }


        Telemetry.getInstance().add("left", llResultPythonOutput[0]);
        Telemetry.getInstance().add("right", llResultPythonOutput[1]);
        Telemetry.getInstance().add("center", llResultPythonOutput[2]);
        Telemetry.getInstance().add("id", latterTargetMotif);


        EventBus.getInstance().invoke(new NewTargetMotifEvent(latterTargetMotif));
        EventBus.getInstance().invoke(new NewDetectionBallsCenterEvent(getColor(llResultPythonOutput[1])));
        EventBus.getInstance().invoke(new NewDetectionBallsLeftEvent(getColor(llResultPythonOutput[0])));
        EventBus.getInstance().invoke(new NewDetectionBallsRightEvent(getColor(llResultPythonOutput[2])));

    }

}
