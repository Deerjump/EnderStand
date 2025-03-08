package io.github.deerjump.enderstand.entity.client;

import io.github.deerjump.enderstand.EnderStandMod;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ArmorStand;

public class EnderStandRenderer extends ArmorStandRenderer {
    private static final ResourceLocation DEFAULT_SKIN_LOCATION =
            ResourceLocation.fromNamespaceAndPath(EnderStandMod.MOD_ID, "textures/entity/ender_stand/ender_stand.png");

    public EnderStandRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ArmorStand entity) {
        return DEFAULT_SKIN_LOCATION;
    }
}
