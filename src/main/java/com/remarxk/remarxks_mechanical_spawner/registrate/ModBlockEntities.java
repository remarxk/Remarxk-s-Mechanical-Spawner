package com.remarxk.remarxks_mechanical_spawner.registrate;

import com.remarxk.remarxks_mechanical_spawner.block.mechanicalSpawner.MechanicalSpawnerEntity;
import com.remarxk.remarxks_mechanical_spawner.block.mechanicalSpawner.MechanicalSpawnerRenderer;
import com.remarxk.remarxks_mechanical_spawner.block.mechanicalSpawner.MechanicalSpawnerVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class ModBlockEntities {
    public static final CreateRegistrate REGISTRATE = ModRegister.getRegistrate();

    public static final BlockEntityEntry<MechanicalSpawnerEntity> MECHANICAL_SPAWNER = REGISTRATE.blockEntity("mechanical_spawner", MechanicalSpawnerEntity::new)
            .visual(() -> MechanicalSpawnerVisual::new)
            .validBlock(ModBlocks.MECHANICAL_SPAWNER)
            .renderer(() -> MechanicalSpawnerRenderer::new)
            .register();

    public static void register() {
    }
}
