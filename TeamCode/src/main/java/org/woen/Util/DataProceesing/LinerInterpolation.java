package org.woen.Util.DataProceesing;

import static com.acmerobotics.roadrunner.Math.lerpLookup;

import java.util.ArrayList;

public class LinerInterpolation {
    private ArrayList<Double> dist  = new ArrayList<>();
    private ArrayList<Double> value = new ArrayList<>();

    public ArrayList<Double> getDist() {
        return dist;
    }

    public ArrayList<Double> getValue() {
        return value;
    }

    public void add(double dist, double value){
        this.dist.add(dist);
        this.value.add(value);
    }

    public double get(double dist){
        return lerpLookup(this.dist,value,dist);
    }
}
