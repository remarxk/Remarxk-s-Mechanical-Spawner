package com.remarxk.remarxks_mechanical_spawner.registrate;

import com.remarxk.remarxks_mechanical_spawner.Item.MechanicalSpawnerItem;
import com.remarxk.remarxks_mechanical_spawner.RemarxksMechanicalSpawner;
import com.remarxk.remarxks_mechanical_spawner.block.mechanicalSpawner.MechanicalSpawnerBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ModBlocks {
    public static final CreateRegistrate REGISTRATE = ModRegister.getRegistrate();

    public static final BlockEntry<MechanicalSpawnerBlock> MECHANICAL_SPAWNER = REGISTRATE.block("mechanical_spawner", MechanicalSpawnerBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(properties -> properties.mapColor(MapColor.PODZOL)
                    .noOcclusion()
            )
            .transform(pickaxeOnly())
            .transform(RemarxksMechanicalSpawner.MAIN_CONFIG.setImpact(32))
            .simpleItem()
            .register();

    public static void register() {
    }
}
