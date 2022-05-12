package com.xiaojia.xiaojiaaddons.Sounds;

public class CustomSound extends Sound {
    public CustomSound(String name) {
        this(name, 10F);
    }

    public CustomSound(String name, float volume) {
        super(name, volume);
    }
}
