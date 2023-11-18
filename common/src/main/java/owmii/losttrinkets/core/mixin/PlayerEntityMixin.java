package owmii.losttrinkets.core.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.core.IPlayerEntityExtended;
import owmii.losttrinkets.item.trinkets.IceShardTrinket;
import owmii.losttrinkets.item.trinkets.ThaSpiderTrinket;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements IPlayerEntityExtended {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void applyMovementEffects(BlockPos pos) {
        IceShardTrinket.frostWalk(this, pos);
        super.applyMovementEffects(pos);
    }

    @Override
    public boolean isClimbing() {
        if (this.isSpectator()) {
            return false;
        }

        if (super.isClimbing()) {
            return true;
        }

        return ThaSpiderTrinket.doClimb(this);
    }

    // ==================== Data storage ====================
    @Unique
    private PlayerData playerData = new PlayerData();

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    public void writeCustomDataToNbt(NbtCompound tag, CallbackInfo ci) {
        sunkentrinkets_toTag(tag);
    }

    public void sunkentrinkets_toTag(NbtCompound tag) {
        tag.put(DATA_KEY, playerData.serializeNBT());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    public void readCustomDataFromNbt(NbtCompound tag, CallbackInfo ci) {
        sunkentrinkets_fromTag(tag);
    }

    public void sunkentrinkets_fromTag(NbtCompound tag) {
        NbtCompound data = null;
        if (tag.contains(DATA_KEY, 10)) {
            data = tag.getCompound(DATA_KEY);
        }

        if (tag.contains("ForgeCaps", 10)) {
            NbtCompound forgeCaps = tag.getCompound("ForgeCaps");
            if (forgeCaps.contains("losttrinkets:player_data", 10)) {
                if (data != null) {
                    throw new RuntimeException("Malformed data: got old losttrinkets data and new sunken_trinkets data as well!");
                }
                data = forgeCaps.getCompound("losttrinkets:player_data");
                LOGGER.warn("Got old playerdata for losttrinkets; upgrade to be used with sunken_trinkets!");
            }
        }

        if (data != null) {
            playerData.deserializeNBT(data);
        }
    }

    public PlayerData sunkentrinkets_getPlayerData() {
        return playerData;
    }
}
