package org.firstinspires.ftc.teamcode.Modules.ShooterConst;


import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Config
@Configurable
public class ShooterConst {

    /// first param - motor vel
    /// second param - angle servo controller pos
    public static double[] leftS = {100, 0.5};
    public static double[] centerS = {100, 0.5};
    public static double[] rightS = {100, 0.5};
}
