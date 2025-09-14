package org.woen.Architecture.EventBus;

@FunctionalInterface
public interface OnEventMethod <K,T extends IEvent<K>> {
    void onEvent(T event);
}