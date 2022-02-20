package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

public class Vector3i {
    public int x, y, z;

    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString() {
        return x + ", " + y + ", " + z;
    }

    public int hashCode() {
        return x * 1000000 + y * 1000 + z;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Vector3i) {
            Vector3i o = (Vector3i) obj;
            return x == o.x && y == o.y && z == o.z;
        }
        return false;
    }
}
