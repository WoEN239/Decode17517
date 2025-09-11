package org.woen.Architecture.Observers;

@FunctionalInterface
public interface IListener <T> {
    void set(T data);
}
