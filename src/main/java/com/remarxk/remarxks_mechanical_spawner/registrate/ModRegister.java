package com.remarxk.remarxks_mechanical_spawner.registrate;

import com.remarxk.remarxks_mechanical_spawner.RemarxksMechanicalSpawner;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.neoforged.bus.api.IEventBus;

public class ModRegister {
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(RemarxksMechanicalSpawner.MODID);
    
    public static CreateRegistrate getRegistrate() {
        return REGISTRATE;
    }

    public static void register(IEventBus bus) {
        REGISTRATE.registerEventListeners(bus);

        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
        ModTabs.register(bus);
        ModDataComponents.register(bus);
    }
}
