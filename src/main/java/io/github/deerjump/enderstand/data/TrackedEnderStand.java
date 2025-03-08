package io.github.deerjump.enderstand.data;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.UUID;

public record TrackedEnderStand(ResourceKey<Level> level, UUID id) {
}
