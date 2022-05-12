package com.xiaojia.xiaojiaaddons.Sounds;

public class CustomSound extends Sound {
    public CustomSound(String name) {
        this(name, 1F);
    }

    public CustomSound(String name, float volume) {
        super(name, volume);
    }
}
