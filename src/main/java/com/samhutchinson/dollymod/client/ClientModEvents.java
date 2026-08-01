package com.samhutchinson.dollymod.client;

import com.samhutchinson.dollymod.DollyMod;
import com.samhutchinson.dollymod.client.model.DollyModel;
import com.samhutchinson.dollymod.init.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Client-only setup. Marked with {@link Dist#CLIENT} so this class is never loaded
 * on a dedicated server (servers have no renderer / model code).
 */
@Mod.EventBusSubscriber(modid = DollyMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    /**
     * Tells Minecraft how to build Dolly's ModelPart tree from {@link DollyModel#createBodyLayer()}.
     */
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DollyModel.LAYER_LOCATION, DollyModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.DOLLY.get(), DollyRenderer::new);
    }
}
