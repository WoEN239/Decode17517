package org.woen.Hardware.ServiseActivationConfig;

import com.acmerobotics.dashboard.config.ValueProvider;

public class SimpleProvider<T> implements ValueProvider<T> {
    private T data;

    public SimpleProvider(T defaultValue) {
        this.data = defaultValue;
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