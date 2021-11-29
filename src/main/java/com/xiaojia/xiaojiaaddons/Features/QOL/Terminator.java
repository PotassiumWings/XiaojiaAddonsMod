package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.StepEvent;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;

import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class Terminator extends StepEvent {
    private static final ArrayList<String> terminatorNames = new ArrayList<String>() {{
        add("Terminator");
    }};
    private static final KeyBind useKeyBind = new KeyBind(mc.gameSettings.keyBindUseItem);

    public Terminator() {
        super(100);
    }

    @Override
    public void execute() {
        if (!Checker.enabled) return;
        if (!Configs.TerminatorAutoRightClick) return;
        if (useKeyBind.isKeyDown() && ControlUtils.checkHoldingItem(terminatorNames)) {
            ControlUtils.rightClick();
            if (XiaojiaAddons.isDebug()) ChatLib.chat("rightClick!");
        }
    }
}
