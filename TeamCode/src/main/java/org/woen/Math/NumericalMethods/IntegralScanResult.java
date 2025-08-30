package org.woen.Math.NumericalMethods;

import java.util.List;

public class IntegralScanResult {

    public final List<Double> values;
    public final List<Double> sums;

    public IntegralScanResult(List<Double> values, List<Double> sums) {
        this.values = values;
        this.sums = sums;
    }
}
