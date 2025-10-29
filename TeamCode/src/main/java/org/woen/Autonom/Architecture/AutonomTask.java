package org.woen.Autonom.Architecture;

import java.util.function.BooleanSupplier;

public class AutonomTask {
    private final Runnable[] runnable;
    private final BooleanSupplier isDone;
    private boolean isRunOnce = false;

    public void run(){
        if(!isRunOnce) {
            for (Runnable i : runnable) {
                i.run();
            }
            isRunOnce = true;
        }
    }

    public boolean isDone(){
        return isDone.getAsBoolean();
    }

    public boolean isRunOnce(){
        return isRunOnce;
    }

    public AutonomTask(BooleanSupplier isDone, Runnable... runnable) {
        this.runnable = runnable;
        this.isDone = isDone;
    }

    public static final AutonomTask Stub = new AutonomTask(()->false,new Runnable[]{});
}
