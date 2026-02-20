package org.woen.Util.Trajectory;


import org.woen.Util.Vectors.Vector2d;

public class TrajectoryPoint {
    public final Vector2d position;
    public final Vector2d velocity;
    public final Vector2d acceleration;

    public TrajectoryPoint(Vector2d position, Vector2d velocity, Vector2d acceleration) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public TrajectoryPoint(Vector2d position) {
        this.position = position;
        this.velocity = new Vector2d();
        this.acceleration = new Vector2d();
    }

}
