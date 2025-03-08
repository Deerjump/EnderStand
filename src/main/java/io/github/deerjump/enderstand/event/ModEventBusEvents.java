package io.github.deerjump.enderstand.event;

import io.github.deerjump.enderstand.EnderStandMod;
import io.github.deerjump.enderstand.entity.ModEntities;
import io.github.deerjump.enderstand.entity.custom.EnderStand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = EnderStandMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ENDER_STAND.get(), EnderStand.createAttributes().build());
    }
}
