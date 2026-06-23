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
            1420,
            0.06,
            1820,
            0.55,

            1970,
            1-0.25,
            2220,
            1-0.4
    };
    public static double[] centerS = {
            1400,
            0.1,
            1800,
            1-0.4,

            1970,
            0.8,
            2220,
            0.92
    };
    public static double[] rightS = {
            1420,
            0.06,
            1820,
            0.55,

            1970,
            0.65,
            2220,
            0.69
    };
}
