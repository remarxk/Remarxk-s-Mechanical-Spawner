package com.remarxk.remarxks_mechanical_spawner.Item;

import com.remarxk.remarxks_mechanical_spawner.registrate.ModBlocks;
import com.remarxk.remarxks_mechanical_spawner.registrate.ModDataComponents;
import com.remarxk.remarxks_mechanical_spawner.registrate.ModItems;
import com.remarxk.remarxks_mechanical_spawner.utils.SpawnerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import org.jetbrains.annotations.NotNull;

public class MechanicalSpawnerItem extends BlockItem {

    public MechanicalSpawnerItem(Properties properties) {
        super(ModBlocks.MECHANICAL_SPAWNER.get(), properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Player player = ctx.getPlayer();
        ItemStack stack = ctx.getItemInHand();

        if (player == null)
            return InteractionResult.FAIL;

        var entityId = stack.get(ModDataComponents.SPAWNER_ENTITY.get());
        if (entityId != null) {
            return super.useOn(ctx);
        }

        if (player.isCrouching()) {
            if (!(level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner))
                return super.useOn(ctx);

            if (!level.isClientSide) {
                SpawnData data = SpawnerHelper.getNextSpawnData(spawner.getSpawner());
                if (data == null)
                    return InteractionResult.FAIL;

                CompoundTag entityTag = data.getEntityToSpawn();

                if (!entityTag.contains("id", Tag.TAG_STRING))
                    return InteractionResult.FAIL;

                entityId = ResourceLocation.tryParse(entityTag.getString("id"));
                if (entityId == null || !BuiltInRegistries.ENTITY_TYPE.containsKey(entityId))
                    return InteractionResult.FAIL;

                ItemStack bound = new ItemStack(ModItems.MECHANICAL_SPAWNER.get());
                bound.set(ModDataComponents.SPAWNER_ENTITY.get(), entityId);

                stack.shrink(1);

                if (!player.getInventory().add(bound)) {
                    player.drop(bound, false);
                }

                level.levelEvent(
                        LevelEvent.PARTICLES_DESTROY_BLOCK,
                        pos,
                        Block.getId(level.getBlockState(pos))
                );

                level.removeBlock(pos, false);

                return InteractionResult.CONSUME;
            }

            return InteractionResult.SUCCESS;
        }

        return super.useOn(ctx);
    }
}
