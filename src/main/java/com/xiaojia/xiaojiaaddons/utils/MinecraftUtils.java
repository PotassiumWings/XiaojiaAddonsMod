package com.xiaojia.xiaojiaaddons.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaojia.xiaojiaaddons.Features.Remote.RemoteUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class MinecraftUtils {
    public static EntityPlayerSP getPlayer() {
        return mc.thePlayer;
    }

    public static World getWorld() {
        return mc.theWorld;
    }

    public static NetworkPlayerInfo getPlayerInfo(String name) {
        EntityPlayer player = getWorld().getPlayerEntityByName(name);
        if (player == null) return null;
        return mc.getNetHandler().getPlayerInfo(name);
    }

    public static BufferedImage getHeadFromMC(String name) {
        try {
            NetworkPlayerInfo info = getPlayerInfo(name);
            ITextureObject texture = mc.getTextureManager().getTexture(info.getLocationSkin());
            Field field = ThreadDownloadImageData.class.getDeclaredField("bufferedImage");
            field.setAccessible(true);
            BufferedImage image = (BufferedImage) field.get(texture);
            BufferedImage res = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
            BufferedImage bottom = image.getSubimage(8, 8, 8, 8);
            BufferedImage top = image.getSubimage(40, 8, 8, 8);
            Graphics g = res.getGraphics();
            g.drawImage(bottom, 0, 0, null);
            g.drawImage(top, 0, 0, null);
            return res;
        } catch (Exception e) {
            return null;
        }
    }

    public static BufferedImage getHead(String name) throws IOException {
        BufferedImage head = getHeadFromMC(name);
        if (head == null) {
            Image image = ImageIO.read(new URL("https://crafatar.com/avatars/" + getUUIDFromName(name)))
                    .getScaledInstance(8, 8, java.awt.Image.SCALE_SMOOTH);
            head = new BufferedImage(8, 8, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics g = head.getGraphics();
            g.drawImage(image, 0, 0, null);
        }
        return head;
    }

    public static String getUUIDFromName(String name) {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        String response = RemoteUtils.get(url, new ArrayList<>(),false);
        MojangApi apiRet = new Gson().fromJson(response, MojangApi.class);
        String uuid = apiRet.id;
        System.err.println("Got uuid for name " + name + ": " + uuid);
        return uuid;
    }

    static class MojangApi {
        String name;
        String id;
    }
}
