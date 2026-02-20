package org.woen.RobotModule.Interface;

public interface IRobotModule {
    default void deviceReadUpdate(){}
    default void deviceSetUpdate(){}
    default void update(){}
    default void lateUpdate(){}
    default void init(){}
    default void subscribeInit(){}
}
