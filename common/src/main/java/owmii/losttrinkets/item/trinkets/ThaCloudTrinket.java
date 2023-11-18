package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import owmii.losttrinkets.api.trinket.ITickableTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.lib.util.math.V3d;

public class ThaCloudTrinket extends Trinket<ThaCloudTrinket> implements ITickableTrinket {
    public ThaCloudTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    @Override
    public void tick(World world, BlockPos pos, PlayerEntity player) {
        if (player.fallDistance > 3.0F) {
            if (!world.isAir(player.getBlockPos().down(3))) {
                Vec3d v3d = player.getVelocity();
                player.setVelocity(v3d.x, 0.0D, v3d.z);
                if (world.isClient) {
                    for (V3d v3d1 : V3d.from(player.getPos()).circled(8, 0.3D)) {
                        world.addParticle(ParticleTypes.CLOUD, v3d1.x, v3d1.y, v3d1.z, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
            player.fallDistance = 0.0F;
        }
    }
}
