package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

@Mixin({Chunk.class})
public abstract class MixinChunk {
    @Shadow
    public abstract IBlockState getBlockState(BlockPos paramBlockPos);

    @Inject(method = "setBlockState", at = @At("HEAD"))
    private void onBlockChange(BlockPos position, IBlockState newBlock, CallbackInfoReturnable<IBlockState> callbackInfoReturnable) {
        IBlockState oldBlock = getBlockState(position);
        if (oldBlock != newBlock && getWorld() != null)
            try {
                MinecraftForge.EVENT_BUS.post(new BlockChangeEvent(position, oldBlock, newBlock));
            } catch (Exception exception) {
            }
    }
}
