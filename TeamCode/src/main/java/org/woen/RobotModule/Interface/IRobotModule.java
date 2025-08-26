package org.woen.RobotModule.Interface;

public interface IRobotModule {
    default void deviceUpdate(){}
    default void update(){}
    default void lateUpdate(){}
    default void init(){}
    default void subscribeInit(){}
}
