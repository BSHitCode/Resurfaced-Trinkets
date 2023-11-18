package owmii.losttrinkets.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import owmii.losttrinkets.client.model.DarkVexModel;
import owmii.losttrinkets.entity.DarkVexEntity;

@Environment(EnvType.CLIENT)
public class DarkVexRenderer extends BipedEntityRenderer<DarkVexEntity, DarkVexModel> {
    private static final Identifier VEX_TEXTURE = new Identifier("textures/entity/illager/vex.png");
    private static final Identifier VEX_CHARGING_TEXTURE = new Identifier("textures/entity/illager/vex_charging.png");

    public DarkVexRenderer(EntityRenderDispatcher renderManagerIn) {
        super(renderManagerIn, new DarkVexModel(), 0.3F);
    }

    @Override
    protected int getBlockLight(DarkVexEntity entityIn, BlockPos partialTicks) {
        return 15;
    }

    @Override
    public Identifier getTexture(DarkVexEntity entity) {
        return entity.isCharging() ? VEX_CHARGING_TEXTURE : VEX_TEXTURE;
    }

    @Override
    protected void scale(DarkVexEntity vexEntity, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.4F, 0.4F, 0.4F);
    }
}
