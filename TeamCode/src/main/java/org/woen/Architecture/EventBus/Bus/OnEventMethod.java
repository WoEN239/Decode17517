package org.woen.Architecture.EventBus.Bus;

@FunctionalInterface
public interface OnEventMethod <K,T extends IEvent<K>> {
    void onEvent(T event);
}