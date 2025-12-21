package org.woen.Autonom;

import java.util.function.BooleanSupplier;

public class AutonomTask {
    private final Runnable[] runnable;
    private BooleanSupplier isDone;
    private boolean isFirstRun = true;

    public void run(){
        if(isFirstRun) {
            isFirstRun = false;

            for (Runnable i : runnable) {
                i.run();
            }
        }
        if(isDone.getAsBoolean()){
            isDone = ()->true;
        }

    }

    public boolean isDone(){
        return isDone.getAsBoolean() && !isFirstRun;
    }

    public boolean isFirstRun(){
        return isFirstRun;
    }

    public AutonomTask(BooleanSupplier isDone, Runnable... runnable) {
        this.runnable = runnable;
        this.isDone = isDone;
    }

    public static final AutonomTask Stub = new AutonomTask(()->true);
}
