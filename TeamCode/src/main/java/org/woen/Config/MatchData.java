package org.woen.Config;

import org.woen.Util.Vectors.AbstractVector2d;
import org.woen.Util.Vectors.DoubleCoordinate;
import org.woen.Util.Vectors.Vector2d;

public class MatchData {
    public static AbstractVector2d<DoubleCoordinate, Vector2d> startPosition = new AbstractVector2d<>(
            new DoubleCoordinate(0),
            new Vector2d( new DoubleCoordinate(0),
                    new DoubleCoordinate(0))
    );
}
