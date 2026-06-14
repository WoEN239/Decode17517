package org.firstinspires.ftc.teamcode.Modules.Shooter;


import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Config
@Configurable
public class ShooterConst {

    /// first param - motor vel near
    /// second param - angle servo controller pos near
    /// third param - motor vel mid
    /// forth param - angle servo controller mid
    /// fifth param - vel near far
    /// sixth param - angle servo controller near far
    /// seventh param - vel far
    /// eight param - angle servo controller far
    public static double[] leftS = {
            1400,
            0.88,
            1800,
            0.6,
            2100,
            0.5,
            2200,
            0.4
    };
    public static double[] centerS = {
            1400,
            0.95,
            1800,
            0.6,
            2100,
            0.5,
            2200,
            0.4
    };
    public static double[] rightS = {
            1400,
            0.2,
            1800,
            0.4,
            2100,
            0.6,
            2200,
            0.7
    };
}
