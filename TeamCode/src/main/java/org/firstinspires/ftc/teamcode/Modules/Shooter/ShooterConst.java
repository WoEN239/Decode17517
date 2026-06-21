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
            0.95,
            1740,
            0.65,
            1970,
            0.25,
            2150,
            0.4
    };
    public static double[] centerS = {
            1400,
            0.9,
            1740,
            0.55,
            1970,
            0.2,
            2150,
            0.1
    };
    public static double[] rightS = {
            1400,
            0.05,
            1740,
            0.33,
            1970,
            0.62,
            2150,
            0.85
    };
}
