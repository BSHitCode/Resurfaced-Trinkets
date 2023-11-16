package owmii.losttrinkets.core.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
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
    protected void frostWalk(BlockPos pos) {
        IceShardTrinket.frostWalk(this, pos);
        super.frostWalk(pos);
    }

    @Override
    public boolean isOnLadder() {
        if (this.isSpectator()) {
            return false;
        }

        if (super.isOnLadder()) {
            return true;
        }

        return ThaSpiderTrinket.doClimb(this);
    }

    // ==================== Data storage ====================
    @Unique
    private PlayerData playerData = new PlayerData();

    // TODO: change to writeCustomDataToNbt once we use YARN mappings...
    @Inject(method = "writeAdditional", at = @At("RETURN"))
    public void writeAdditional(CompoundNBT tag, CallbackInfo ci) {
        sunkentrinkets_toTag(tag);
    }

    public void sunkentrinkets_toTag(CompoundNBT tag) {
        tag.put(DATA_KEY, playerData.serializeNBT());
    }

    // TODO: change to readCustomDataFromNbt once we use YARN mappings...
    @Inject(method = "readAdditional", at = @At("RETURN"))
    public void readAdditional(CompoundNBT tag, CallbackInfo ci) {
        sunkentrinkets_fromTag(tag);
    }

    public void sunkentrinkets_fromTag(CompoundNBT tag) {
        CompoundNBT data = null;
        if (tag.contains(DATA_KEY, 10)) {
            data = tag.getCompound(DATA_KEY);
        }

        if (tag.contains("ForgeCaps", 10)) {
            CompoundNBT forgeCaps = tag.getCompound("ForgeCaps");
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
