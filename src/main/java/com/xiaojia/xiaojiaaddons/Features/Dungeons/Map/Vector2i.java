package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

public class Vector2i {
    public int x, y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public int hashCode() {
        return x * 1000 + y;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Vector2i)) return false;
        return ((Vector2i) o).x == x && ((Vector2i) o).y == y;
    }
}
