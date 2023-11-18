package owmii.losttrinkets.entity;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class DarkVexEntity extends DarkEntity {
    protected static final TrackedData<Byte> VEX_FLAGS = DataTracker.registerData(DarkVexEntity.class, TrackedDataHandlerRegistry.BYTE);
    @Nullable
    private BlockPos boundOrigin;

    public DarkVexEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
        this.moveControl = new DarkVexEntity.MoveHelperController(this);
    }

    public static DefaultAttributeContainer.Builder getAttribute() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(4, new DarkVexEntity.ChargeAttackGoal());
        this.goalSelector.add(8, new DarkVexEntity.MoveRandomGoal());
        this.goalSelector.add(9, new LookAtEntityGoal(this, MobEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.targetSelector.add(1, (new RevengeGoal(this, PlayerEntity.class)).setGroupRevenge());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VEX_FLAGS, (byte) 0);
    }

    @Override
    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        setNoGravity(true);
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos boundOriginIn) {
        this.boundOrigin = boundOriginIn;
    }

    private boolean getVexFlag(int mask) {
        int i = this.dataTracker.get(VEX_FLAGS);
        return (i & mask) != 0;
    }

    private void setVexFlag(int mask, boolean value) {
        int i = this.dataTracker.get(VEX_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }
        this.dataTracker.set(VEX_FLAGS, (byte) (i & 255));
    }

    public boolean isCharging() {
        return getVexFlag(1);
    }

    public void setCharging(boolean charging) {
        setVexFlag(1, charging);
    }

    @Override
    public void playAmbientSound() {
        if (this.random.nextInt(7) == 0) {
            super.playAmbientSound();
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VEX_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VEX_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_VEX_HURT;
    }

    @Override
    public float getBrightnessAtEyes() {
        return 1.0F;
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason reason, @Nullable EntityData data, @Nullable NbtCompound nbt) {
        initEquipment(difficulty);
        updateEnchantments(difficulty);
        return super.initialize(world, difficulty, reason, data, nbt);
    }

    @Override
    protected void initEquipment(LocalDifficulty difficulty) {
        equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    class ChargeAttackGoal extends Goal {
        public ChargeAttackGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        public boolean canStart() {
            if (DarkVexEntity.this.getTarget() != null && !DarkVexEntity.this.getMoveControl().isMoving() && DarkVexEntity.this.random.nextInt(7) == 0) {
                return DarkVexEntity.this.squaredDistanceTo(DarkVexEntity.this.getTarget()) > 4.0D;
            } else {
                return false;
            }
        }

        public boolean shouldContinue() {
            return DarkVexEntity.this.getMoveControl().isMoving() && DarkVexEntity.this.isCharging() && DarkVexEntity.this.getTarget() != null && DarkVexEntity.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = DarkVexEntity.this.getTarget();
            if (livingentity != null) {
                Vec3d vector3d = livingentity.getCameraPosVec(1.0F);
                DarkVexEntity.this.moveControl.moveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                DarkVexEntity.this.setCharging(true);
                DarkVexEntity.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
            }
        }

        public void stop() {
            DarkVexEntity.this.setCharging(false);
        }

        public void tick() {
            LivingEntity livingentity = DarkVexEntity.this.getTarget();
            if (livingentity != null) {
                if (DarkVexEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                    DarkVexEntity.this.tryAttack(livingentity);
                    DarkVexEntity.this.setCharging(false);
                } else {
                    double d0 = DarkVexEntity.this.squaredDistanceTo(livingentity);
                    if (d0 < 9.0D) {
                        Vec3d vector3d = livingentity.getCameraPosVec(1.0F);
                        DarkVexEntity.this.moveControl.moveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                    }
                }
            }
        }
    }

    class MoveHelperController extends MoveControl {
        public MoveHelperController(DarkVexEntity vex) {
            super(vex);
        }

        public void tick() {
            if (this.state == MoveControl.State.MOVE_TO) {
                Vec3d vector3d = new Vec3d(this.targetX - DarkVexEntity.this.getX(), this.targetY - DarkVexEntity.this.getY(), this.targetZ - DarkVexEntity.this.getZ());
                double d0 = vector3d.length();
                if (d0 < DarkVexEntity.this.getBoundingBox().getAverageSideLength()) {
                    this.state = MoveControl.State.WAIT;
                    DarkVexEntity.this.setVelocity(DarkVexEntity.this.getVelocity().multiply(0.5D));
                } else {
                    DarkVexEntity.this.setVelocity(DarkVexEntity.this.getVelocity().add(vector3d.multiply(this.speed * 0.05D / d0)));
                    if (DarkVexEntity.this.getTarget() == null) {
                        Vec3d vector3d1 = DarkVexEntity.this.getVelocity();
                        DarkVexEntity.this.yaw = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
                        DarkVexEntity.this.bodyYaw = DarkVexEntity.this.yaw;
                    } else {
                        double d2 = DarkVexEntity.this.getTarget().getX() - DarkVexEntity.this.getX();
                        double d1 = DarkVexEntity.this.getTarget().getZ() - DarkVexEntity.this.getZ();
                        DarkVexEntity.this.yaw = -((float) MathHelper.atan2(d2, d1)) * (180F / (float) Math.PI);
                        DarkVexEntity.this.bodyYaw = DarkVexEntity.this.yaw;
                    }
                }

            }
        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        public boolean canStart() {
            return !DarkVexEntity.this.getMoveControl().isMoving() && DarkVexEntity.this.random.nextInt(7) == 0;
        }

        public boolean shouldContinue() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = DarkVexEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = DarkVexEntity.this.getBlockPos();
            }

            for (int i = 0; i < 3; ++i) {
                BlockPos pos = blockpos.add(DarkVexEntity.this.random.nextInt(15) - 7, DarkVexEntity.this.random.nextInt(11) - 5, DarkVexEntity.this.random.nextInt(15) - 7);
                if (DarkVexEntity.this.world.isAir(pos)) {
                    DarkVexEntity.this.moveControl.moveTo((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 0.25D);
                    if (DarkVexEntity.this.getTarget() == null) {
                        DarkVexEntity.this.getLookControl().lookAt((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }
}
