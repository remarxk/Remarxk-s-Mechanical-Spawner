package com.remarxk.remarxks_mechanical_spawner.registrate;

import com.remarxk.remarxks_mechanical_spawner.utils.Logger;
import com.simibubi.create.AllCreativeModeTabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

import static com.remarxk.remarxks_mechanical_spawner.RemarxksMechanicalSpawner.MODID;

public class ModTabs {
    public static void register(IEventBus bus) {
        bus.addListener(ModTabs::addTab);
    }

    public static void addTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey()) {
            event.accept(ModItems.MECHANICAL_SPAWNER.get());
        }
    }
}
