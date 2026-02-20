package org.woen.RobotModule.Modules.Gun.Arcitecture;

@FunctionalInterface
public interface ServoActionUnit {
    default boolean isAtTarget(){
        return true;
    }
    void run();
}
