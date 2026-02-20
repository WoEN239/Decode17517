package org.woen.RobotModule.Modules.Camera.Util;

import static org.woen.RobotModule.Modules.Camera.Util.BALL_COLOR.G;
import static org.woen.RobotModule.Modules.Camera.Util.BALL_COLOR.P;

public enum MOTIF {
    GPP(G,P,P),PGP(P,G,P),PPG(P,P,G);

    public final BALL_COLOR[] colors;

    MOTIF(BALL_COLOR... colors) {
        this.colors = colors;
    }
}
