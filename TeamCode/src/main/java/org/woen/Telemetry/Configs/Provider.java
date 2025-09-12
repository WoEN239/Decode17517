package org.woen.Telemetry.Configs;

import com.acmerobotics.dashboard.config.ValueProvider;

public class Provider<T> implements ValueProvider<T> {
    private T data;

    public Provider(T data) {
        this.data = data;
    }

    @Override
    public T get() {
        return data;
    }

    @Override
    public void set(T value) {
        if (value != null) {
            data = value;
        }
    }
}