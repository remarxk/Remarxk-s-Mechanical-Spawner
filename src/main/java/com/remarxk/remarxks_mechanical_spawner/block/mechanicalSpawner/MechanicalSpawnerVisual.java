package com.remarxk.remarxks_mechanical_spawner.block.mechanicalSpawner;

import com.simibubi.create.content.kinetics.base.ShaftVisual;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;

public class MechanicalSpawnerVisual extends ShaftVisual<MechanicalSpawnerEntity> implements SimpleDynamicVisual {
    public MechanicalSpawnerVisual(VisualizationContext context, MechanicalSpawnerEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {

    }
}
