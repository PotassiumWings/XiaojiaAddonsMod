package com.xiaojia.xiaojiaaddons.Sounds;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;

import java.util.ArrayList;

public class SoundHandler {
    public static ArrayList<Sound> playing = new ArrayList<>();

    public static void playSound(Sound sound) {
        XiaojiaAddons.mc.getSoundHandler().playSound(sound.sound);
    }
}
