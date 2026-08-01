package com.samhutchinson.dollymod.client;

import com.samhutchinson.dollymod.DollyMod;
import com.samhutchinson.dollymod.entity.DollyEntity;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Draws Dolly in the world.
 *
 * For now we reuse the vanilla {@link WolfModel} (same bones/animations as a wolf).
 * That means sit, walk, and shake already look correct before any Blockbench work.
 *
 * Texture path expected:
 *   assets/dollymod/textures/entity/dolly.png
 *
 * --- When your Blockbench model is ready ---------------------------------------
 * 1. In Blockbench, use a Java Entity / Modded Entity project for 1.20+.
 * 2. Export the Java model class → put it at
 *      client/model/DollyModel.java
 *    Fix the package to {@code com.samhutchinson.dollymod.client.model} and rename
 *    the class to DollyModel if needed.
 * 3. Export the texture PNG →
 *      assets/dollymod/textures/entity/dolly.png
 *    (replace the placeholder).
 * 4. Register a ModelLayerLocation and bake it in
 *      EntityRenderersEvent.RegisterLayerDefinitions
 *    (see ClientModEvents).
 * 5. Change this renderer to use {@code new DollyModel<>(...)} instead of WolfModel.
 *
 * Note: vanilla WolfCollarLayer is hard-typed to Wolf's renderer generics, so collar
 * dye overlays are omitted here. Easy to re-add with a small custom layer later.
 *
 * No GeckoLib needed for this workflow — plain Forge Java entity models are enough.
 */
public class DollyRenderer extends MobRenderer<DollyEntity, WolfModel<DollyEntity>> {
    /** Single texture for all Dolly states until you add angry/tame variants. */
    private static final ResourceLocation DOLLY_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(DollyMod.MOD_ID, "textures/entity/dolly.png");

    public DollyRenderer(EntityRendererProvider.Context context) {
        // 0.5F = shadow radius under the mob (wolf uses the same).
        super(context, new WolfModel<>(context.bakeLayer(ModelLayers.WOLF)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(DollyEntity entity) {
        return DOLLY_TEXTURE;
    }
}
