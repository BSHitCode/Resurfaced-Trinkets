package owmii.losttrinkets.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.item.Itms;

@Mixin(LivingEntityRenderer.class)
abstract class LivingEntityRendererMixin {

    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;isSpectator()Z"
        )
    )
    private boolean shouldRenderLayers(LivingEntity living) {
        // The return value determines if layer's should be rendered or not;
        // a value of "true" renders no layers while "false" renders all layers.

        // How it works:
        // we check if the entity to be rendered is a player; if so, we check if
        // they have the invisibility potion active, the THA_GHOST trinket equipped
        // and check isInvisibleTo(<current client's player>); if all is true,
        // we do not render the layers.
        //
        // We insert the check to isInvisibleTo() to also check if the current
        // player of the client has the MINDS_EYE trinket equiped, which negates THA_GHOST.

        // TODO: improve visuals by modifing all layers for players to render translucent instead
        // net.minecraft.client.renderer.entity.PlayerRenderer
        //  -> net.minecraft.client.renderer.entity.layers.BipedArmorLayer
        //  -> HeldItemLayer
        //  -> ArrowLayer
        //  -> Deadmau5HeadLayer
        //  -> CapeLayer
        //  -> HeadLayer
        //  -> ElytraLayer
        //  -> ParrotVariantLayer
        //  -> SpinAttackEffectLayer
        //  -> BeeStingerLayer

        if (living instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) living;
            if (
                playerEntity.hasStatusEffect(StatusEffects.INVISIBILITY)
                && LostTrinketsAPI.getTrinkets(playerEntity).isActive(Itms.THA_GHOST)
                && playerEntity.isInvisibleTo(MinecraftClient.getInstance().player)
            ) {
                return true;
            }
        }
        return living.isSpectator();
    }

}
