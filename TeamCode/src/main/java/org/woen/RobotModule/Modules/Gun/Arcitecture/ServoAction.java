package org.woen.RobotModule.Modules.Gun.Arcitecture;

import org.woen.Telemetry.Telemetry;

import java.util.function.BooleanSupplier;

public class ServoAction {
    public ServoActionUnit [] action;
    private int i = 0;
    private boolean firstRun = true;
    public boolean isDone() {return isDone;}
    private boolean isDone = false;
    public void update(){
        if(i >= action.length){
            isDone = true;
            return;
        }

        if(firstRun){
            action[i].run();
            firstRun = false;
        }
        if(action[i].isAtTarget()){
            firstRun = true;
            i++;
        }
    }

    public ServoAction(BooleanSupplier[] isAtTargets, Runnable[] runnables){
        ServoActionUnit[] action = new ServoActionUnit[isAtTargets.length];
        for (int i = 0; i < action.length; i++) {
            int finalI = i;
            action[i] = new ServoActionUnit() {
                public void run() {
                    runnables[finalI].run();
                }
                public boolean isAtTarget() {
                    return isAtTargets[finalI].getAsBoolean();
                }
            };
        }
        this.action = action;
    }
    public ServoAction(ServoActionUnit ... action) {
        this.action = action;
    }
    public ServoAction copy(){
        return new ServoAction(action);
    }
}
