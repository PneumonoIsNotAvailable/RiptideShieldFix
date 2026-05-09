package net.pneumono.riptideshieldfix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @WrapOperation(
            method = "renderArmWithItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/AbstractClientPlayer;isAutoSpinAttack()Z"
            )
    )
    private boolean preventAutoSpinAttackAnimation(AbstractClientPlayer instance, Operation<Boolean> original, @Local(argsOnly = true) ItemStack stack) {
        return original.call(instance) && stack.getEnchantments().keySet().stream().anyMatch(enchantment -> enchantment.is(Enchantments.RIPTIDE));
    }
}
