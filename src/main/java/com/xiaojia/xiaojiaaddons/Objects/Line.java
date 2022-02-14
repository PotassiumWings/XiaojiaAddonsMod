package com.xiaojia.xiaojiaaddons.Objects;

import javax.vecmath.Vector3d;
import java.awt.Color;

public class Line {
    public Vector3d from;
    public Vector3d to;
    public Color color;

    public Line(Vector3d from, Vector3d to, boolean can) {
        this.from = from;
        this.to = to;
        this.color = can ? new Color(0, 255, 0) : new Color(255, 0, 0);
    }
}
