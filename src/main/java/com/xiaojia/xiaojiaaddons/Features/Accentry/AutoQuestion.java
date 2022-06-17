package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class AutoQuestion {
    public HashMap<String, String[]> answer = new HashMap<String, String[]>() {{
        put("如果你有更强大的副本武器，那么你如何处理你的主线武器？ [单选题]", new String[]{"[以副本武器为辅，来赚钱提升主线武器]"});
        put("如果想催更怎么办？ [单选题]", new String[]{"[忍住不去催更]"});
        put("临渊之殿的等级限制为多少？ [单选题]", new String[]{"[3000级]"});
        put("迷雾森林需要多少级才可进入？ [单选题]", new String[]{"[2600级]", "[2600]"});
        put("如果有人在公屏上宣传服务器，拉在线玩家，你怎么做？ [单选题]", new String[]{"[举报]"});
        put("如果遇到BUG怎么办？ [单选题]", new String[]{"[私聊管理反馈Bug]"});
        put("临渊之殿为几城？ [单选题]", new String[]{"[6城]"});
        put("最好的变强的方法是什么？ [单选题]", new String[]{"[享受游戏过程，点点滴滴间提升]"});
        put("关于交易，你该怎么做？ [单选题]", new String[]{"[不轻易相信别人，在全球市场买东西]"});
        put("关于自身财产问题，你怎么做？ [单选题]", new String[]{"[剧情什么的都保存好]"});
        put("挂机的地方叫什么名字 [简答题]", new String[]{"甘泉挂机池"});
        put("服务器周年庆时间？ [简答题] [x月x日]", new String[]{"1月7日"});
        put("如何用2个字母打开拍卖行？<不用打/号>？ [简答题]", new String[]{"ya"});
        put("怎么展示武器？<不用打/号>？ [简答题]", new String[]{"mkshow"});
        put("白蚁一击多少伤害？ [简答题]", new String[]{"2"});
        put("有人跟你抢怪怎么办？ [单选题]", new String[]{"[要么不要打，要么平分]"});
        put("白羽鸡有几滴血？ [简答题]", new String[]{"3"});
        put("远古主城是第几城 [单选题]", new String[]{"[4]"});
        put("如果有人在公屏上骂人，你怎么做? [单选题]", new String[]{"[屏蔽他]"});
        put("如果有人蹭经验怎么办？ [单选题]", new String[]{"[找管理举报并按制度处罚该玩家]"});
        put("梦境遇事找谁处理？ [简答题]", new String[]{"管理"});
        put("服务器允许注册小号么？ [单选题]", new String[]{"[不允许，只能注册1个]"});
    }};
    public String currentQuestion = "";

    public static HashSet<String> newQuestions = new HashSet<>();

    @SubscribeEvent
    public void onReceive(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoQuestion) return;
        String message = ChatLib.removeFormatting(event.message.getUnformattedText());
        if (Arrays.asList(answer.get(currentQuestion)).contains(message)) {
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
                        ChatLib.say(answer.get(message)[0]);
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
