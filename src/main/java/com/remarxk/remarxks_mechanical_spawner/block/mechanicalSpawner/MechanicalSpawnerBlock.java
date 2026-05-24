package com.remarxk.remarxks_mechanical_spawner.block.mechanicalSpawner;

import com.remarxk.remarxks_mechanical_spawner.registrate.ModBlockEntities;
import com.remarxk.remarxks_mechanical_spawner.registrate.ModDataComponents;
import com.remarxk.remarxks_mechanical_spawner.registrate.ModItems;
import com.remarxk.remarxks_mechanical_spawner.utils.BlazeBurnerItemHelper;
import com.simibubi.create.AllEntityTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockItem;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MechanicalSpawnerBlock extends KineticBlock implements IBE<MechanicalSpawnerEntity> {
    public static final Property<Direction> VERTICAL_FACING = BlockStateProperties.VERTICAL_DIRECTION;

    public MechanicalSpawnerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VERTICAL_FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState blockState) {
        return blockState.getValue(VERTICAL_FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(VERTICAL_FACING)
                .getAxis();
    }

    @Override
    public Class<MechanicalSpawnerEntity> getBlockEntityClass() {
        return MechanicalSpawnerEntity.class;
    }

    @Override
    public BlockEntityType<? extends MechanicalSpawnerEntity> getBlockEntityType() {
        return ModBlockEntities.MECHANICAL_SPAWNER.get();
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> itemStacks = super.getDrops(state, params);

        BlockEntity blockEntity = params.getParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof MechanicalSpawnerEntity be) {
            EntityType<?> entityType = be.getEntityType();

            if (entityType != null) {
                ResourceLocation entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);

                for (ItemStack itemStack : itemStacks) {
                    if (itemStack.is(ModItems.MECHANICAL_SPAWNER)) {
                        itemStack.set(ModDataComponents.SPAWNER_ENTITY.get(), entityId);
                        break;
                    }
                }
            }
        }

        return itemStacks;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);

        // 仅在服务端处理（避免客户端/服务端数据不一致）
        if (!worldIn.isClientSide() && placer != null && !stack.isEmpty()) {
            // 1. 获取放置后生成的方块实体
            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
            if (blockEntity instanceof MechanicalSpawnerEntity be) {
                // 2. 从手持道具中读取自定义NBT数据
                ResourceLocation entityId = stack.get(ModDataComponents.SPAWNER_ENTITY.get());
                if (entityId != null && BuiltInRegistries.ENTITY_TYPE.containsKey(entityId)) {
                    be.setEntityType(BuiltInRegistries.ENTITY_TYPE.get(entityId));
                }
            }
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!(level.getBlockEntity(pos) instanceof MechanicalSpawnerEntity spawnerEntity)) {
            return super.useItemOn(stack, state, level, pos, player, hand, result);
        }

        EntityType<?> entityType = spawnerEntity.getEntityType();

        if (stack.getItem() instanceof SpawnEggItem eggItem) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MechanicalSpawnerEntity be) {
                if (entityType == null) {
                    if (!level.isClientSide()) {
                        be.setEntityType(eggItem.getType(stack));
                        stack.consume(1, player);
                        return ItemInteractionResult.CONSUME;
                    }
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        else if (stack.getItem() instanceof BlazeBurnerBlockItem blazeBurnerItem) {
            if (entityType != null && !blazeBurnerItem.hasCapturedBlaze() && AllTags.AllEntityTags.BLAZE_BURNER_CAPTURABLE.matches(entityType)) {
                BlazeBurnerItemHelper.spawnCaptureEffects(blazeBurnerItem, level, VecHelper.getCenterOf(pos));

                if (level.isClientSide)
                    return ItemInteractionResult.SUCCESS;

                BlazeBurnerItemHelper.giveBurnerItemTo(blazeBurnerItem, player, player.getItemInHand(hand), hand);
                return ItemInteractionResult.SUCCESS;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, result);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, components, flag);

        ResourceLocation id =
                stack.get(ModDataComponents.SPAWNER_ENTITY.get());

        if (id != null && BuiltInRegistries.ENTITY_TYPE.containsKey(id)) {
            EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(id);
            components.add(
                    Component.translatable(
                            "tooltip.remarxks_mechanical_spawner.entity",
                            type.getDescription()
                    ).withStyle(ChatFormatting.GRAY)
            );
        }
    }
}
