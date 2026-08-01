package com.samhutchinson.dollymod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.samhutchinson.dollymod.DollyMod;
import com.samhutchinson.dollymod.client.model.DollyModel;
import com.samhutchinson.dollymod.entity.DollyEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Draws Dolly using {@link DollyModel} (your Blockbench Beagle mesh).
 *
 * Texture: {@code assets/dollymod/textures/entity/dolly.png}
 */
public class DollyRenderer extends MobRenderer<DollyEntity, DollyModel<DollyEntity>> {
    private static final ResourceLocation DOLLY_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(DollyMod.MOD_ID, "textures/entity/dolly.png");

    public DollyRenderer(EntityRendererProvider.Context context) {
        super(context, new DollyModel<>(context.bakeLayer(DollyModel.LAYER_LOCATION)), 0.5F);
    }

    /**
     * Same as WolfRenderer: feed the tail interest/wag angle into {@code setupAnim}
     * as the {@code ageInTicks} parameter (vanilla naming is misleading here).
     */
    @Override
    protected float getBob(DollyEntity entity, float partialTicks) {
        return entity.getTailAngle();
    }

    /**
     * Darken the model while wet (shake-dry), matching wolf behaviour.
     */
    @Override
    public void render(DollyEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        if (entity.isWet()) {
            float shade = entity.getWetShade(partialTicks);
            this.model.setColor(shade, shade, shade);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        if (entity.isWet()) {
            this.model.setColor(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(DollyEntity entity) {
        return DOLLY_TEXTURE;
    }
}
