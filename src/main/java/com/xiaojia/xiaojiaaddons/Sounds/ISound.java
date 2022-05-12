package com.xiaojia.xiaojiaaddons.Sounds;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.util.ResourceLocation;

public class ISound implements ITickableSound {
    public final ResourceLocation sound;
    public float volume = 1.0F;
    public float pitch = 1.0F;
    public float xPos = 0.0F;
    public float yPos = 0.0F;
    public float zPos = 0.0F;
    public boolean repeat = true;
    public int delay = 0;
    public ISound.AttenuationType type;
    public boolean donePlaying = false;

    public ISound(ResourceLocation sound, float volume, float pitch) {
        this.type = AttenuationType.NONE;
        this.volume = volume;
        this.pitch = pitch;
        this.sound = sound;
    }

    @Override
    public boolean isDonePlaying() {
        return donePlaying;
    }

    @Override
    public ResourceLocation getSoundLocation() {
        return sound;
    }

    @Override
    public boolean canRepeat() {
        return repeat;
    }

    @Override
    public int getRepeatDelay() {
        return delay;
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public float getXPosF() {
        return xPos;
    }

    @Override
    public float getYPosF() {
        return yPos;
    }

    @Override
    public float getZPosF() {
        return zPos;
    }

    @Override
    public AttenuationType getAttenuationType() {
        return type;
    }

    @Override
    public void update() {

    }
}
