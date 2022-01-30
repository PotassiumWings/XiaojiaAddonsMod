package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ColorName;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ItemRename;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ItemStack.class})
public class MixinItemStack {
    @Shadow
    private NBTTagCompound stackTagCompound;

    @Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
    public void getDisplayName(CallbackInfoReturnable<String> ci) {
        try {
            if (stackTagCompound == null || !stackTagCompound.hasKey("ExtraAttributes")) return;
            NBTTagCompound subCompound = stackTagCompound.getCompoundTag("ExtraAttributes");
            if (subCompound == null || !subCompound.hasKey("uuid")) return;
            String uuid = subCompound.getString("uuid");
            if (!uuid.equals("") && ItemRename.renameMap.containsKey(uuid)) {
                String prefix = "&r";
                String rename = ItemRename.renameMap.get(uuid);
                if (stackTagCompound.hasKey("display", 10)) {
                    NBTTagCompound display = stackTagCompound.getCompoundTag("display");
                    if (display.hasKey("Name", 8)) {
                        String name = ChatLib.removeColor(display.getString("Name"));
                        prefix = ChatLib.getPrefix(name);
                    }
                }
                ci.setReturnValue(ChatLib.addColor(prefix + rename));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "getDisplayName", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void getReturnValue(CallbackInfoReturnable<String> ci, String ret) {
        if (!Checker.enabled) return;
        if (!Configs.ColorNameItem) return;
        ret = ColorName.addColorName(ret);
        ci.setReturnValue(ret);
    }
}
