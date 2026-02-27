package org.woen.Telemetry.ConfigurableVariables;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.ValueProvider;

import java.util.List;

public class SimpleProvider<T> implements ValueProvider<T> {
    private T data;

    public SimpleProvider(T data) {
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

    public void addToDash(String category, String name){
        FtcDashboard.getInstance().addConfigVariable(category,name,this);
    }
    public static <R> void addToDash(String category, String name, List<SimpleProvider<R>> data){
        for(int i = 0;i<data.size();i++ ){
            data.get(i).addToDash(category, name + i);
        }
    }
    public static void addDoubleArrayToDash(String category, String name, List<Double> data){
        for(int i = 0;i<data.size();i++ ){
            new SimpleProvider<>(data.get(i)).addToDash(category, name + i);
        }
    }


}