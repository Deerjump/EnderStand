package io.github.deerjump.enderstand;

import io.github.deerjump.enderstand.data.DataRegistry;
import io.github.deerjump.enderstand.entity.ModEntities;
import io.github.deerjump.enderstand.entity.client.EnderStandRenderer;
import io.github.deerjump.enderstand.item.ModItems;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(EnderStandMod.MOD_ID)
public class EnderStandMod
{
    public static final String MOD_ID = "enderstand";

    public EnderStandMod(IEventBus modEventBus, ModContainer modContainer)
    {
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        DataRegistry.register(modEventBus);
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.ENDER_STAND.get(), EnderStandRenderer::new);
        }
    }
}
