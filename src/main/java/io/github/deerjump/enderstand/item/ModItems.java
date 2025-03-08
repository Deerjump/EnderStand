package io.github.deerjump.enderstand.item;

import io.github.deerjump.enderstand.EnderStandMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModItems {
    public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(EnderStandMod.MOD_ID);

    public static DeferredItem<Item> REMOTE_SWAPPER = ITEMS.registerItem("remote_swapper", RemoteSwapper::new, new Item.Properties().stacksTo(1));
    public static DeferredItem<Item> ENDER_STAND = ITEMS.registerItem("ender_stand", EnderStandItem::new, new Item.Properties().stacksTo(16));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
