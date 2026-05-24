package com.remarxk.remarxks_mechanical_spawner.registrate;

import com.remarxk.remarxks_mechanical_spawner.Item.MechanicalSpawnerItem;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;

public class ModItems {
    public static final CreateRegistrate REGISTRATE = ModRegister.getRegistrate();

    public static final ItemEntry<MechanicalSpawnerItem> MECHANICAL_SPAWNER = REGISTRATE.item("mechanical_spawner", MechanicalSpawnerItem::new)
            .register();

    public static void register() {
    }
}
