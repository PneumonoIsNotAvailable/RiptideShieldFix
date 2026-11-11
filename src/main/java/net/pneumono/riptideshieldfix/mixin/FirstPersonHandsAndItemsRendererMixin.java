package net.pneumono.riptideshieldfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
//? if >=26.3 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.objectweb.asm.Opcodes;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class FirstPersonHandsAndItemsRendererMixin {
    @WrapOperation(
            method = "submitArmWithItem",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;isAutoSpinAttack:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean preventAutoSpinAttackAnimation(
            AvatarRenderState state,
            Operation<Boolean> original,
            @Local(argsOnly = true) InteractionHand hand
    ) {
        return original.call(state) && state.useItemHand == hand;
    }
}
//?} else {
/*@Mixin()
public abstract class FirstPersonHandsAndItemsRendererMixin {}*/
//?}
