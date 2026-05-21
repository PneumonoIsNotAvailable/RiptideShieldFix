package net.pneumono.riptideshieldfix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @WrapOperation(
            method = "renderArmWithItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/AbstractClientPlayer;isAutoSpinAttack()Z"
            )
    )
    private boolean preventAutoSpinAttackAnimation(
            AbstractClientPlayer player,
            Operation<Boolean> original,
            @Local(argsOnly = true) InteractionHand hand
    ) {
        return original.call(player) && player.getUsedItemHand() == hand;
    }
}
