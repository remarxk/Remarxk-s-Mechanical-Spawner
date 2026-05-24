package com.remarxk.remarxks_mechanical_spawner.block.mechanicalSpawner;

import com.remarxk.remarxks_mechanical_spawner.RemarxksMechanicalSpawner;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

import static com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer.getAngleForBe;

public class MechanicalSpawnerEntity extends KineticBlockEntity {
    private EntityType<?> entityType;

    private float spawnProgress = 0;

    public MechanicalSpawnerEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);

        if (getSpeed() == 0)
            resetSpawnProgress();
    }

    @Override
    public float calculateStressApplied() {
        if (entityType == null)
            return 0;

        return super.calculateStressApplied();
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null || entityType == null)
            return;

        if (getSpeed() == 0)
            return;

        spawnProgress += Mth.clamp(getSpawnSpeed(), 0.1f, 50);

        if (spawnProgress >= 50) {
            resetSpawnProgress();

            if (level.isClientSide) {
                clientSpawn();
            }
            else {
                serverSpawn();
            }
        }
    }

    private void clientSpawn() {
        if (level == null)
            return;

        RandomSource randomsource = level.getRandom();
        BlockPos pos = getBlockPos();

        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.5;
        double centerZ = pos.getZ() + 0.5;

        int particleCount = 5;

        for (int i = 0; i < particleCount; i++) {

            double x = centerX;
            double y = centerY;
            double z = centerZ;

            double offset = 0.6; // 外壳半径
            double rand = randomsource.nextDouble() - 0.5;

            int face = randomsource.nextInt(6);

            switch (face) {
                case 0 -> { // 上
                    y += offset;
                    x += rand;
                    z += rand;
                }
                case 1 -> { // 下
                    y -= offset;
                    x += rand;
                    z += rand;
                }
                case 2 -> { // 北
                    z -= offset;
                    x += rand;
                    y += rand;
                }
                case 3 -> { // 南
                    z += offset;
                    x += rand;
                    y += rand;
                }
                case 4 -> { // 西
                    x -= offset;
                    y += rand;
                    z += rand;
                }
                case 5 -> { // 东
                    x += offset;
                    y += rand;
                    z += rand;
                }
            }

            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0.02, 0);
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0.01, 0);
        }
    }

    private void serverSpawn() {
        if (!(level instanceof ServerLevel serverLevel))
            return;

        BlockPos pos = getBlockPos();

        float angle = - getAngleForBe(this, pos, Direction.Axis.Y);

        float radius = 0.5f + entityType.getWidth() + 0.1f; // 外围半径

        float centerX = worldPosition.getX() + 0.5f;
        float centerY = worldPosition.getY() + 0.5f;
        float centerZ = worldPosition.getZ() + 0.5f;

        float spawnX = centerX + (float) Math.cos(angle) * radius;
        float spawnZ = centerZ + (float) Math.sin(angle) * radius;
        float spawnY = centerY;

        AABB box = entityType.getSpawnAABB(spawnX, spawnY, spawnZ);
        if (!level.noCollision(box)) {
            return;
        }

        float checkRadius = RemarxksMechanicalSpawner.MAIN_CONFIG.checkRadius.getF();
        int maxCount = RemarxksMechanicalSpawner.MAIN_CONFIG.maxEntities.get();

        AABB checkBox = new AABB(worldPosition).inflate(checkRadius);
        int k = serverLevel.getEntities(entityType, checkBox, EntitySelector.NO_SPECTATORS).size();
        if (k >= maxCount) {
            return;
        }

        BlockPos spawnPos = BlockPos.containing(spawnX, spawnY, spawnZ);
        Entity entity = entityType.spawn(serverLevel, spawnPos, MobSpawnType.SPAWNER);
        if (entity == null)
            return;

        double absSpeed = Math.abs(getSpeed());
        double maxVelocity = 1.25;
        double power = maxVelocity * (absSpeed / (absSpeed + 128.0));

        Vec3 velocity = new Vec3(
                Math.cos(angle),
                0,
                Math.sin(angle)
        ).normalize().scale(power);

        entity.push(velocity);
    }

    private float getSpawnSpeed() {
        return Math.abs(getSpeed()) / 100;
    }

    private void resetSpawnProgress() {
        spawnProgress = 0;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean status = super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        if (entityType != null) {
            MutableComponent entityName = MutableComponent.create(entityType.getDescription().getContents());

            CreateLang.translate("gui.goggle.spawner_content")
                    .space()
                    .add(entityName.withStyle(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip);
            ;
            status = true;
        }

        return status;
    }

    public void setEntityType(EntityType<?> type) {
        this.entityType = type;
        setChanged();

        if (level != null && !level.isClientSide()) {
            sendData();
        }
    }

    @Nullable
    public EntityType<?> getEntityType() {
        return entityType;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);

        if (entityType != null) {
            compound.putString("entity_id", BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString());
            compound.putFloat("spawn_progress", spawnProgress);
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);

        if (compound.contains("entity_id")) {
            ResourceLocation resourceLocation = ResourceLocation.parse(compound.getString("entity_id"));
            if (BuiltInRegistries.ENTITY_TYPE.containsKey(resourceLocation)) {
                entityType = BuiltInRegistries.ENTITY_TYPE.get(resourceLocation);
            }
        }

        if (compound.contains("spawn_progress")) {
            spawnProgress = compound.getFloat("spawn_progress");
        }
    }
}
