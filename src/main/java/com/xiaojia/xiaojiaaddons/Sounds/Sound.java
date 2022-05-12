package com.xiaojia.xiaojiaaddons.Sounds;

import net.minecraft.util.ResourceLocation;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.MODID;

public abstract class Sound {
    public String name;
    public ISound sound;
    public float volume;

    public Sound(String name, float volume) {
        Sounds.sounds.add(this);
        this.name = name;
        this.sound = new ISound(new ResourceLocation(MODID + ":" + name), volume, 1F);
        this.volume = volume;
    }
}
