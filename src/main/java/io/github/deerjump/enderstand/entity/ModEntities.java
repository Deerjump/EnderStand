package io.github.deerjump.enderstand.entity;

import io.github.deerjump.enderstand.EnderStandMod;
import io.github.deerjump.enderstand.entity.custom.EnderStand;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, EnderStandMod.MOD_ID);

    public static final Supplier<EntityType<EnderStand>> ENDER_STAND = ENTITY_TYPES.register(
            "ender_stand",
            () -> EntityType.Builder.of(EnderStand::new, MobCategory.MISC)
                    .sized(0.5F, 1.975F)
                    .eyeHeight(1.7775F)
                    .clientTrackingRange(10)
                    .build(EnderStandMod.MOD_ID)
    );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
