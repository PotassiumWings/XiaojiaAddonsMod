package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.HashSet;

public class AutoQuestion {
    public HashMap<String, String> answer = new HashMap<String, String>() {{
        put("如果你有更强大的副本武器，那么你如何处理你的主线武器？ [单选题]", "[以副本武器为辅，来赚钱提升主线武器]");
        put("如果想催更怎么办？ [单选题]", "[忍住不去催更]");
        put("临渊之殿的等级限制为多少？ [单选题]", "[3000级]");
        put("迷雾森林需要多少级才可进入？ [单选题]", "[2600级]");
        put("如果有人在公屏上宣传服务器，拉在线玩家，你怎么做？ [单选题]", "[举报]");
        put("如果遇到BUG怎么办？ [单选题]", "[私聊管理反馈Bug]");
        put("临渊之殿为几城？ [单选题]", "[6城]");
        put("最好的变强的方法是什么？ [单选题]", "[享受游戏过程，点点滴滴间提升]");
        put("关于交易，你该怎么做？ [单选题]", "[不轻易相信别人，在全球市场买东西]");
        put("关于自身财产问题，你怎么做？ [单选题]", "[剧情什么的都保存好]");
        put("挂机的地方叫什么名字 [简答题]", "甘泉挂机池");
        put("服务器周年庆时间？ [简答题] [x月x日]", "1月7日");
    }};
    public String currentQuestion = "";

    public static HashSet<String> newQuestions = new HashSet<>();

    @SubscribeEvent
    public void onReceive(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoQuestion) return;
        String message = ChatLib.removeFormatting(event.message.getUnformattedText());
        if (message.equals(answer.get(currentQuestion))) {
            new Thread(() -> {
                try {
                    Thread.sleep(Configs.AutoQuestionCD);
                    click(event.message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } else if (answer.containsKey(message)) {
            currentQuestion = message;
            if (message.contains("[简答题]")) {
                new Thread(() -> {
                    try {
                        Thread.sleep(Configs.AutoQuestionCD);
                        ChatLib.say(answer.get(message));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } else if (message.contains("[单选题]") || message.contains("[简答题]")) {
            newQuestions.add(message);
        }
    }

    private boolean click(IChatComponent component) {
        ChatStyle style = component.getChatStyle();
        if (style != null && style.getChatClickEvent() != null) {
            if (style.getChatClickEvent().getAction() == ClickEvent.Action.RUN_COMMAND) {
                ChatLib.say(style.getChatClickEvent().getValue());
                return true;
            }
        }
        for (IChatComponent sub : component.getSiblings()) if (click(sub)) return true;
        return false;
    }

    public static void display() {
        for (String question : newQuestions) {
            ChatLib.chat("Q: " + question);
        }
    }
}
