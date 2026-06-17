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
            1850,
            0.4,
            2150,
            0.4
    };
    public static double[] centerS = {
            1400,
            0.9,
            1700,
            0.6,
            1850,
            0.33,
            2140,
            0.3
    };
    public static double[] rightS = {
            1400,
            0.1,
            1800,
            0.4,
            1850,
            0.6,
            2150,
            0.6
    };
}
