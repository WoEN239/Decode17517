package org.firstinspires.ftc.teamcode.Modules.Shooter;


import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Config
@Configurable
public class ShooterConst {

    /// first param - motor vel
    /// second param - angle servo controller pos
    public static double[] leftS = {1600, 0.88, 1800, 0.88};
    public static double[] centerS = {1600, 0.9, 1800, 0.9};
    public static double[] rightS = {1600, 0.2, 1800, 0.2};
}
