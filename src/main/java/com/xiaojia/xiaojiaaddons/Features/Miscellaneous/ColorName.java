package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class ColorName {
    private static final HashMap<String, String> cachedColorName = new HashMap<>();
    public static HashMap<String, String> colorMap = new HashMap<>();
    public static HashMap<String, String> rankMap = new HashMap<>();

    public static void setColorMap(String rank, String color) {
        Type type = (new TypeToken<HashMap<String, String>>() {
        }).getType();
        rankMap = (new Gson()).fromJson(rank, type);
        colorMap = (new Gson()).fromJson(color, type);
        cachedColorName.clear();
    }

    public static void showCache() {
        HashMap<String, String> map = new HashMap<>(cachedColorName);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.err.println(entry.getKey() + " -> " + entry.getValue());
        }
        System.err.println();
        for (Map.Entry<String, String> entry : rankMap.entrySet()) {
            System.err.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static IChatComponent convert(IChatComponent message) {
        List<IChatComponent> siblings = message.getSiblings();
        ChatStyle style = message.getChatStyle();
        if (siblings.isEmpty()) {
            if (!(message instanceof ChatComponentTranslation)) {
                IChatComponent component = new ChatComponentText(addColorName(message.getFormattedText()));
                component.setChatStyle(style);
                return component;
            } else {
                System.out.println(message.getFormattedText());
//                ChatLib.chat("what is this ???");
                return message;
            }
        } else {
            int index = message.getFormattedText().indexOf(siblings.get(0).getFormattedText());
            IChatComponent component = new ChatComponentText(message.getFormattedText().substring(0, index));
            component.setChatStyle(style);
            siblings.add(0, component);

            siblings = compactSiblings(siblings);

            IChatComponent res = null;
            for (IChatComponent component1 : siblings) {
                if (res == null) res = convert(component1);
                else res.appendSibling(convert(component1));
            }
            return res;
        }
    }

    private static List<IChatComponent> compactSiblings(List<IChatComponent> siblings) {
        StringBuilder str = new StringBuilder();
        List<IChatComponent> res = new ArrayList<>();
        for (int i = 0; i < siblings.size(); i++) {
            IChatComponent component = siblings.get(i);
            ClickEvent clickEvent = component.getChatStyle().getChatClickEvent();
            HoverEvent hoverEvent = component.getChatStyle().getChatHoverEvent();
            if (clickEvent == null && hoverEvent == null) {
                str.append(component.getFormattedText());
            } else {
                res.add(new ChatComponentText(str.toString()));
                res.add(component);
                str = new StringBuilder();
            }
        }
        if (!str.toString().equals("")) res.add(new ChatComponentText(str.toString()));
        return res;
    }

    private static void addToCache(String src, String dst) {
        if (cachedColorName.size() > 10000) {
            System.err.println("Color name cache too big! Clearing cache...");
            cachedColorName.clear();
        }
        cachedColorName.put(src, dst);
    }

    public static int getCacheSize() {
        return cachedColorName.size();
    }

    // message: \u00a7
    public static String addColorName(String message) {
        if (message == null) return null;
        if (colorMap == null) return message;
        String originMessage = message;
        if (cachedColorName.containsKey(message)) return cachedColorName.get(message);

//        System.err.println("msg: " + message);
        for (String name : colorMap.keySet()) {
            String color = colorMap.get(name);

            String reg = "(\u00a77|\u00a7.\\[(MVP|VIP)] |\u00a7.\\[(MVP|VIP)(\u00a7.)*\\++(\u00a7.)*] )(\u00a7.)*" + name;
//            System.err.println("reg: " + reg);

            message = message.replaceAll(reg, "\u1105");
            message = message.replaceAll("(?<=\u00a7bXJC > \u00a7r\u00a78)" + name, "\u1105");

            message = message.replace(name, color + name + "&r");

            String dst = colorMap.get(name) + name + "&r";
            if (!rankMap.get(name).equals("")) dst = rankMap.get(name) + " " + dst;
            message = message.replace("\u1105", dst);
        }
        String res = ChatLib.addColor(message);
        addToCache(originMessage, res);
        return res;
    }

    public static String addColorNameWithPrefix(String message) {
        if (message == null) return null;
        if (colorMap == null) return message;
        if (cachedColorName.containsKey(message)) return cachedColorName.get(message);
        if (!message.contains("'s")) return addColorName(message);

        String unformatted = ChatLib.removeFormatting(message);
        for (String name : colorMap.keySet()) {
            String color = colorMap.get(name);

            if (unformatted.matches("\\[Lv[0-9]+] " + name + "'s.*")) {
                String res = message.replace(name, color + name +
                        ChatLib.getPrefix(ChatLib.removeColor(message.replaceAll(".*\\[.*] ", ""))));
                res = ChatLib.addColor(res);
                addToCache(message, res);
                return res;
            }
        }
        return addColorName(message);
    }

    private static Pair<String, String> addColorNameToScoreboard(String prefix, String suffix) {
        // prefix: \u00a77WingP suffix: \u00a771234
        // prefix: \u00a7e[M] \u00a7bPotassium suffix: \u00a7bWin \u00a7c(DEAD)
        String noFormat = ChatLib.removeFormatting(prefix + suffix);
        if (!noFormat.startsWith("[") || !noFormat.contains(" ")) return new Pair<>(prefix, suffix);
        String part = noFormat.split(" ")[1];
        String prefixPart = ChatLib.removeFormatting(prefix).split(" ")[1];
        boolean shouldRemoveSuffixPrefixFormat = prefixPart.length() < part.length();
//        ChatLib.chat("prefix: " + prefix);
//        ChatLib.chat("suffix: " + suffix);
//        ChatLib.chat("part: " + part);
//        ChatLib.chat("prefixPart: " + prefixPart);
        for (String name : colorMap.keySet()) {
            if (name.contains(part)) {
                String color = ChatLib.addColor(colorMap.get(name));
                int index = prefix.indexOf(prefixPart);

                String resPrefix = prefix.substring(0, index) + color + prefix.substring(index);
                String resSuffix = suffix;
                if (shouldRemoveSuffixPrefixFormat)
                    resSuffix = color + suffix.substring(ChatLib.getPrefix(ChatLib.removeColor(suffix)).length());

//                ChatLib.chat("resprefix: " + resPrefix);
//                ChatLib.chat("ressuffix: " + resSuffix);
                return new Pair<>(resPrefix, resSuffix);
            }
        }
        return new Pair<>(prefix, suffix);
    }

    // colorname chat
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onMessageReceived(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (event.type != 0) return;
        if (!Configs.ColorNameChat) return;
        IChatComponent message = event.message.createCopy();
        event.message = convert(message);
//        event.setCanceled(true);
////        System.err.println(message);
//        ChatLib.addComponent(convert(message), false);
    }

    // colorname nametag
    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
//    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.ColorNameNameTag) return;
//        if (getWorld() == null) return;
//        for (Entity entity: getWorld().loadedEntityList) {
        if (event.entity instanceof EntityPlayer) {
            EntityPlayer entity = (EntityPlayer) event.entity;
//            if (!(entity instanceof EntityPlayer)) return;
            Field nameField = null;
            try {
                nameField = EntityPlayer.class.getDeclaredField("displayname");
            } catch (NoSuchFieldException e) {
                return;
            }
            nameField.setAccessible(true);
            try {
                String displayName = entity.getDisplayName().getFormattedText();
                String res = addColorName(displayName);
                if (res.equals(displayName)) return;

                nameField.set(entity, res);
                ScorePlayerTeam team = mc.getNetHandler().getPlayerInfo(entity.getName()).getPlayerTeam();
                team.setNamePrefix("");
                team.setNameSuffix("");
            } catch (Exception ignored) {
            }
        }
    }

    // colorname scoreboard
    @SubscribeEvent
    public void onScoreBoardPacketReceived(PacketReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.ColorNameScoreboard) return;

        if (event.packet instanceof S3EPacketTeams) {
            S3EPacketTeams packet = (S3EPacketTeams) event.packet;

            Field prefixField = null;
            Field suffixField = null;
            try {
                prefixField = S3EPacketTeams.class.getDeclaredField("field_149319_c");
                suffixField = S3EPacketTeams.class.getDeclaredField("field_149316_d");
            } catch (NoSuchFieldException e) {
                return;
            }
            prefixField.setAccessible(true);
            suffixField.setAccessible(true);
            try {
                String prefix = (String) prefixField.get(packet);
                String suffix = (String) suffixField.get(packet);
                Pair<String, String> res = addColorNameToScoreboard(prefix, suffix);

                prefixField.set(packet, res.getKey());
                suffixField.set(packet, res.getValue());
            } catch (Exception ignored) {
            }
        }
    }

    // colorname title
    @SubscribeEvent
    public void onTitlePacketReceived(PacketReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.ColorNameTitle) return;

        if (event.packet instanceof S45PacketTitle) {
            S45PacketTitle packet = (S45PacketTitle) event.packet;

            Field field = null;
            try {
                field = S45PacketTitle.class.getDeclaredField("field_179810_b");
            } catch (NoSuchFieldException e) {
                try {
                    field = S45PacketTitle.class.getDeclaredField("message");
                } catch (NoSuchFieldException e1) {
                    return;
                }
            }
            field.setAccessible(true);
            try {
                IChatComponent component = (IChatComponent) field.get(packet);
                if (component != null) {
                    field.set(packet, convert(component));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // colorname tooltip
    @SubscribeEvent
    public void onToolTip(ItemTooltipEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.ColorNameItem) return;
        String uuid = NBTUtils.getUUID(event.itemStack);
        if (uuid.equals("")) return;
        List<String> lore = event.toolTip.stream().map(ColorName::addColorName).collect(Collectors.toList());
        event.toolTip.clear();
        event.toolTip.addAll(lore);
    }

    @SubscribeEvent
    public void onTabPacketReceived(PacketReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.ColorNameTab) return;

        if (event.packet instanceof S38PacketPlayerListItem) {
            S38PacketPlayerListItem packet = (S38PacketPlayerListItem) event.packet;
            List<S38PacketPlayerListItem.AddPlayerData> list = packet.func_179767_a();
            List<S38PacketPlayerListItem.AddPlayerData> newList = new ArrayList<>();
//            StringBuilder out = new StringBuilder();
            for (S38PacketPlayerListItem.AddPlayerData player : list) {
                if (player.getDisplayName() == null) {
                    newList.add(player);
//                    out.append("null").append(", ");
                    continue;
                }
                String name = player.getDisplayName().getUnformattedText();
                String[] splits = name.split(" ");
                if (splits.length == 0) return;

                // [YOUTUBE] Mxmimi (Mage I)
                // PotassiumWings (DEAD)
                int index = 0;
                if (splits[0].matches("^\\[.*]$")) index = 1;
                if (splits.length <= index) return;
                name = splits[index];

                if (colorMap.containsKey(name)) {
                    String s = player.getDisplayName().getFormattedText().replaceAll(name, colorMap.get(name) + name);
                    IChatComponent component = new ChatComponentText(ChatLib.addColor(s));
                    S38PacketPlayerListItem.AddPlayerData newPlayer = packet.new AddPlayerData(
                            player.getProfile(), player.getPing(), player.getGameMode(), component
                    );
                    newList.add(newPlayer);
                } else {
                    newList.add(player);
                }
//                out.append(name).append(", ");
            }

            try {
                Field field = null;
                try {
                    field = S38PacketPlayerListItem.class.getDeclaredField("players");
                } catch (NoSuchFieldException e) {
                    field = S38PacketPlayerListItem.class.getDeclaredField("field_179769_b");
                }

                field.setAccessible(true);
                field.set(event.packet, newList);
            } catch (Exception ignored) {
            }
//            ChatLib.chat(packet.func_179768_b() + ": " + out.toString());
        }
    }
}
