package io.github.deerjump.enderstand.entity.custom;

import io.github.deerjump.enderstand.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class EnderStand extends ArmorStand {
    public EnderStand(EntityType<? extends ArmorStand> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ArmorStand.createAttributes();
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.ENDER_STAND.get());
    }

    protected void brokenByPlayer(ServerLevel level, DamageSource damageSource) {
        ItemStack itemstack = new ItemStack(ModItems.ENDER_STAND.get());
        itemstack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
        Block.popResource(this.level(), this.blockPosition(), itemstack);
        this.brokenByAnything(level, damageSource);
    }

    protected void brokenByAnything(ServerLevel level, DamageSource damageSource) {
        this.playBrokenSound();
        this.dropAllDeathLoot(level, damageSource);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = this.getItemBySlot(slot);
            if (itemStack.isEmpty()) continue;
            Block.popResource(this.level(), this.blockPosition().above(), itemStack);
            this.setItemSlot(slot, ItemStack.EMPTY);
        }
    }

    protected void playBrokenSound() {
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ARMOR_STAND_BREAK, this.getSoundSource(), 1.0F, 1.0F);
    }

    protected void causeDamage(ServerLevel level, DamageSource damageSource, float damageAmount) {
        float f = this.getHealth();
        f -= damageAmount;
        if (f <= 0.5F) {
            this.brokenByAnything(level, damageSource);
            this.kill();
        } else {
            this.setHealth(f);
            this.gameEvent(GameEvent.ENTITY_DAMAGE, damageSource.getEntity());
        }
    }

    public boolean hurt(DamageSource source, float amount) {
        if (this.isRemoved()) {
            return false;
        } else if (this.level() instanceof ServerLevel serverlevel) {
            if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                this.kill();
                return false;
            } else if (this.isInvulnerableTo(source) || this.isInvisible() || this.isMarker()) {
                return false;
            } else if (source.is(DamageTypeTags.IS_EXPLOSION)) {
                this.brokenByAnything(serverlevel, source);
                this.kill();
                return false;
            } else if (source.is(DamageTypeTags.IGNITES_ARMOR_STANDS)) {
                if (this.isOnFire()) {
                    this.causeDamage(serverlevel, source, 0.15F);
                } else {
                    this.igniteForSeconds(5.0F);
                }

                return false;
            } else if (source.is(DamageTypeTags.BURNS_ARMOR_STANDS) && this.getHealth() > 0.5F) {
                this.causeDamage(serverlevel, source, 4.0F);
                return false;
            } else {
                boolean flag1 = source.is(DamageTypeTags.CAN_BREAK_ARMOR_STAND);
                boolean flag = source.is(DamageTypeTags.ALWAYS_KILLS_ARMOR_STANDS);
                if (!flag1 && !flag) {
                    return false;
                } else {
                    if (source.getEntity() instanceof Player player && !player.getAbilities().mayBuild) {
                        return false;
                    }

                    if (source.isCreativePlayer()) {
                        this.playBrokenSound();
                        this.showBreakingParticles();
                        this.kill();
                        return true;
                    } else {
                        long i = serverlevel.getGameTime();
                        if (i - this.lastHit > 5L && !flag) {
                            serverlevel.broadcastEntityEvent(this, (byte)32);
                            this.gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
                            this.lastHit = i;
                        } else {
                            this.brokenByPlayer(serverlevel, source);
                            this.showBreakingParticles();
                            this.kill();
                        }

                        return true;
                    }
                }
            }
        } else {
            return false;
        }
    }

    protected void showBreakingParticles() {
        if (this.level() instanceof ServerLevel) {
            ((ServerLevel)this.level())
                    .sendParticles(
                            new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_PLANKS.defaultBlockState()),
                            this.getX(),
                            this.getY(0.6666666666666666),
                            this.getZ(),
                            10,
                            (this.getBbWidth() / 4.0F),
                            (this.getBbHeight() / 4.0F),
                            (this.getBbWidth() / 4.0F),
                            0.05
                    );
        }
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(itemStack.is(ModItems.REMOTE_SWAPPER.get())) {
            return InteractionResult.PASS;
        }
        if (player.level().isClientSide()) return InteractionResult.sidedSuccess(true);
        return super.interactAt(player, vec, hand);
    }
}
