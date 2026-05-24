package com.remarxk.remarxks_mechanical_spawner.Item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.remarxk.remarxks_mechanical_spawner.registrate.ModBlockEntities;
import com.remarxk.remarxks_mechanical_spawner.registrate.ModBlocks;
import com.remarxk.remarxks_mechanical_spawner.registrate.ModDataComponents;
import com.remarxk.remarxks_mechanical_spawner.block.mechanicalSpawner.MechanicalSpawnerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class MechanicalSpawnerItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final MechanicalSpawnerEntity dummyBE;

    public MechanicalSpawnerItemRenderer(BlockEntityRenderDispatcher entityRender, EntityModelSet modelSet) {
        super(entityRender, modelSet);

        this.dummyBE = new MechanicalSpawnerEntity(
                ModBlockEntities.MECHANICAL_SPAWNER.get(),
                BlockPos.ZERO,
                ModBlocks.MECHANICAL_SPAWNER.get().defaultBlockState()
        );
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        net.minecraft.resources.ResourceLocation entityId = stack.get(ModDataComponents.SPAWNER_ENTITY.get());

        if (entityId == null)
            return;

        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(entityId);
        dummyBE.setEntityType(type);

        Minecraft.getInstance()
                .getBlockEntityRenderDispatcher()
                .renderItem(
                        dummyBE,
                        poseStack,
                        buffer,
                        light,
                        overlay
                );
    }
}
