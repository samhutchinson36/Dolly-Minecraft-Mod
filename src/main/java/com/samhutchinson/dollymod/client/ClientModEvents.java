package com.samhutchinson.dollymod.client;

import com.samhutchinson.dollymod.DollyMod;
import com.samhutchinson.dollymod.init.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Client-only setup. Marked with {@link Dist#CLIENT} so this class is never loaded
 * on a dedicated server (servers have no renderer code).
 *
 * When you add a custom DollyModel from Blockbench, also subscribe to
 * {@link EntityRenderersEvent.RegisterLayerDefinitions} here and call
 * {@code event.registerLayerDefinition(DollyModel.LAYER_LOCATION, DollyModel::createBodyLayer)}.
 */
@Mod.EventBusSubscriber(modid = DollyMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.DOLLY.get(), DollyRenderer::new);
    }
}
