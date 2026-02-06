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
    private LLResult llResult;
    private int pipeLineId = 0;
    @Override
    public void subscribeInit() {
        EventBus.getInstance().subscribe(EndOfOpModeEvent.class, this::switchPipeLine);
        EventBus.getInstance().subscribe(PipeLineSwitchEvent.class, this::switchPipeLine);
    }

    @Override
    public void init() {
        HardwareMap hardwareMap = DevicePool.getInstance().hardwareMap;
        camera = hardwareMap.get(Limelight3A.class, "Limelight");
        camera.pipelineSwitch(0);
        camera.start();
    }

    private PredominantColorProcessor.Swatch getColor(double index) {
        if (index == 1) {
            return PredominantColorProcessor.Swatch.ARTIFACT_PURPLE;
        }
        if (index == 2) {
            return PredominantColorProcessor.Swatch.ARTIFACT_GREEN;
        }
        if (index == 0) {
            return PredominantColorProcessor.Swatch.BLACK;
        }
        return null;
    }

    private void switchPipeLine(EndOfOpModeEvent e) {
        camera.stop();
    }
    private void switchPipeLine(PipeLineSwitchEvent e) {
        pipeLineId = e.getData();
    }

    private MOTIF latterTargetMotif = null;

    private void tagUpdate(){
        camera.pipelineSwitch(1);
        llResult = camera.getLatestResult();
        if (llResult.isValid()) {

            List<LLResultTypes.FiducialResult> fiducialResults = llResult.getFiducialResults();

            for (LLResultTypes.FiducialResult fr : fiducialResults) {
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
                Telemetry.getInstance().add("id", id);

              latterTargetMotif = motif;
            }
        }

    }
    private void ballUpdate(){
        camera.pipelineSwitch(0);
        llResult = camera.getLatestResult();

        if (llResult.getPythonOutput().length > 0) {
            double[] llResultPythonOutput = llResult.getPythonOutput();

            EventBus.getInstance().invoke(new NewDetectionBallsCenterEvent(getColor(llResultPythonOutput[1])));
            EventBus.getInstance().invoke(new NewDetectionBallsLeftEvent(getColor(llResultPythonOutput[2])));
            EventBus.getInstance().invoke(new NewDetectionBallsRightEvent(getColor(llResultPythonOutput[0])));
        }

        Telemetry.getInstance().add("isLLValid", llResult.getPythonOutput().length);
        Telemetry.getInstance().add("left", llResult.getPythonOutput()[2]);
        Telemetry.getInstance().add("right", llResult.getPythonOutput()[0]);
        Telemetry.getInstance().add("center", llResult.getPythonOutput()[1]);

    }

    public void update() {
        if(pipeLineId == 1){
            tagUpdate();
        }else{
            ballUpdate();
        }
    }
}
