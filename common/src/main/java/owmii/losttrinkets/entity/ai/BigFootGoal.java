package owmii.losttrinkets.entity.ai;

import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.entity.Entities;
import owmii.losttrinkets.item.Itms;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BigFootGoal extends Goal {
    public static final double SPEED = 1.4;
    protected final EntityNavigation navigation;
    private PathAwareEntity entity;
    @Nullable
    protected Path path;
    @Nullable
    protected PlayerEntity player;

    public BigFootGoal(PathAwareEntity entity) {
        this.setControls(EnumSet.of(Control.MOVE));
        this.navigation = entity.getNavigation();
        this.entity = entity;
    }

    @Override
    public void start() {
        this.navigation.startMovingAlong(this.path, SPEED);
    }

    @Override
    public boolean shouldContinue() {
        return !this.navigation.isIdle();
    }

    @Override
    public void stop() {
        this.player = null;
    }

    @Override
    public void tick() {
        // Speed gets set very slow sometimes without this
        this.navigation.setSpeed(SPEED);
    }

    @Override
    public boolean canStart() {
        if (Entities.isNonBossEntity(this.entity) && this.entity.isBaby()) {
            this.player = this.entity.world.getClosestPlayer(this.entity.getX(), this.entity.getY(), this.entity.getZ(), 8.0,
                    target -> target instanceof PlayerEntity && LostTrinketsAPI.getTrinkets((PlayerEntity) target).isActive(Itms.BIG_FOOT));
            if (this.player != null) {
                Vec3d vector3d = NoPenaltyTargeting.find(this.entity, 16, 7, this.player.getPos());
                if (vector3d == null) {
                    return false;
                } else if (this.player.squaredDistanceTo(vector3d.x, vector3d.y, vector3d.z) < this.player.squaredDistanceTo(this.entity)) {
                    return false;
                } else {
                    this.path = this.navigation.findPathTo(vector3d.x, vector3d.y, vector3d.z, 0);
                    return this.path != null;
                }
            }
        }
        return false;
    }
}
