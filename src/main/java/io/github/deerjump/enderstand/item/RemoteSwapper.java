package io.github.deerjump.enderstand.item;

import io.github.deerjump.enderstand.data.DataRegistry;
import io.github.deerjump.enderstand.data.TrackedEnderStand;
import io.github.deerjump.enderstand.entity.custom.EnderStand;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class RemoteSwapper extends Item {
    private static final List<EquipmentSlot> ARMOR_SLOTS = List.of(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD);

    public RemoteSwapper(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(level.isClientSide()) return InteractionResultHolder.sidedSuccess(itemStack, true);
        TrackedEnderStand trackedEnderStand = itemStack.get(DataRegistry.TRACKED_ENDER_STAND);

        if (trackedEnderStand == null) {
            player.displayClientMessage(Component.translatable("firstmod.messages.no_tracked_stand"), true);
            return InteractionResultHolder.consume(itemStack);
        }

        EnderStand enderStand = (EnderStand) ((ServerLevel) level).getEntities().get(trackedEnderStand.id());

        if (enderStand == null) {
            player.displayClientMessage(Component.translatable("firstmod.messages.stand_not_found"), true);
            return InteractionResultHolder.consume(itemStack);
        }

        Vec3 coords = enderStand.position();

        this.swapPlayerArmor(player, enderStand);

        player.displayClientMessage(Component.translatable("firstmod.messages.tracked_stand_location", coords.x, coords.y, coords.z), true);
        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        Level level = interactionTarget.level();

        if(!(interactionTarget instanceof  EnderStand enderStand)) {
            return InteractionResult.PASS;
        }

        if (!player.isCrouching()) {
            return InteractionResult.CONSUME;
        }

        TrackedEnderStand trackedEnderStand = new TrackedEnderStand(level.dimension(), enderStand.getUUID());
        stack.set(DataRegistry.TRACKED_ENDER_STAND, trackedEnderStand);
        player.setItemInHand(usedHand, stack);

        if(!level.isClientSide()) {
            player.displayClientMessage(Component.translatable("enderstand.messages.bind_successful"), true);
        }

        return InteractionResult.SUCCESS;
    }

    private void swapPlayerArmor(Player player, EnderStand enderStand) {
        ARMOR_SLOTS.forEach(slot -> {
            ItemStack playerItem = player.getItemBySlot(slot);
            ItemStack standItem = enderStand.getItemBySlot(slot);
            player.setItemSlot(slot, standItem);
            enderStand.setItemSlot(slot, playerItem);
        });
    }
}
