package com.xiaojia.xiaojiaaddons.Objects;

import java.awt.Color;

public class Cube {
    public double x, y, z;
    public double h, w;
    public Color color = new Color(0, 255, 0);

    public Cube(double x, double y, double z, double h, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.h = h;
        this.w = w;
    }
}
