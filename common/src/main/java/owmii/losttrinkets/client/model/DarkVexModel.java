package owmii.losttrinkets.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import owmii.losttrinkets.entity.DarkVexEntity;

@Environment(EnvType.CLIENT)
public class DarkVexModel extends BipedEntityModel<DarkVexEntity> {
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public DarkVexModel(ModelPart modelPart) {
        super(modelPart);
        this.leftLeg.visible = false;
        this.hat.visible = false;
        this.rightWing = modelPart.getChild("right_wing");
        this.leftWing = modelPart.getChild("left_wing");
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return Iterables.concat(super.getBodyParts(), ImmutableList.of(this.rightWing, this.leftWing));
    }

    @Override
    public void setAngles(DarkVexEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (entityIn.isCharging()) {
            if (entityIn.getMainArm() == Arm.RIGHT) {
                this.rightArm.pitch = 3.7699115F;
            } else {
                this.leftArm.pitch = 3.7699115F;
            }
        }

        this.rightLeg.pitch += ((float) Math.PI / 5F);
        this.rightWing.pivotZ = 2.0F;
        this.leftWing.pivotZ = 2.0F;
        this.rightWing.pivotY = 1.0F;
        this.leftWing.pivotY = 1.0F;
        this.rightWing.yaw = 0.47123894F + MathHelper.cos(ageInTicks * 0.8F) * (float) Math.PI * 0.05F;
        this.leftWing.yaw = -this.rightWing.yaw;
        this.leftWing.roll = -0.47123894F;
        this.leftWing.pitch = 0.47123894F;
        this.rightWing.pitch = 0.47123894F;
        this.rightWing.roll = 0.47123894F;
    }
}
