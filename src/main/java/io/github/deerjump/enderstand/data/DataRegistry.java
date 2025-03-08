package io.github.deerjump.enderstand.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.deerjump.enderstand.EnderStandMod;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DataRegistry {
    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, EnderStandMod.MOD_ID);

    public static final Codec<TrackedEnderStand> TRACKED_ENDER_STAND_CODEC =
            RecordCodecBuilder.create(
            instance -> instance.group(
            ResourceKey.codec(Registries.DIMENSION).fieldOf("level").forGetter(TrackedEnderStand::level),
            UUIDUtil.CODEC.fieldOf("id").forGetter(TrackedEnderStand::id)
            ).apply(instance, TrackedEnderStand::new));

    public static final Supplier<DataComponentType<TrackedEnderStand>> TRACKED_ENDER_STAND =
            REGISTRAR.registerComponentType(
                    "tracked_ender_stand",
                    builder -> builder.persistent(TRACKED_ENDER_STAND_CODEC)
            );

    public static void register(IEventBus eventBus) {
        REGISTRAR.register(eventBus);
    }
}
