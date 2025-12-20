package org.woen.RobotModule.Modules.Gun.Arcitecture;

import org.woen.Architecture.EventBus.IEvent;
import org.woen.Util.Vectors.Vector2d;

public class NewAimEvent implements IEvent<Boolean> {
    private final boolean data;

    public NewAimEvent(boolean data) {
        this.data = data;
    }

    @Override
    public Boolean getData() {
        return data;
    }

    public NewAimEvent setGoal(Vector2d goal) {this.goal = goal;return this;}
    public Vector2d getGoal() {return goal;}
    private Vector2d goal = new Vector2d();

}
