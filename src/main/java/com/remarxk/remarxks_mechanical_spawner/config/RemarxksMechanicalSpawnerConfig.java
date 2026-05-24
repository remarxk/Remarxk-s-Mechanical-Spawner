package com.remarxk.remarxks_mechanical_spawner.config;

import com.remarxk.remarxks_mechanical_spawner.RemarxksMechanicalSpawner;
import com.simibubi.create.api.stress.BlockStressValues;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.createmod.catnip.config.ConfigBase;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

public class RemarxksMechanicalSpawnerConfig extends ConfigBase {
    private static final String modId = RemarxksMechanicalSpawner.MODID;

    public final ConfigFloat checkRadius =
            f(6, 1, 32, "checkRadius", Comments.radius);

    public final ConfigInt maxEntities =
            i(10, 1, 200, "maxEntities", Comments.maxEntities);

    // 这些是非静态的，每个StressConfig实例管理自己的默认值
    private final Map<ResourceLocation, Double> defaultImpacts = new HashMap<>();
    private final Map<ResourceLocation, Double> defaultCapacities = new HashMap<>();

    // 这些用于存储从配置文件加载的值
    private final Map<ResourceLocation, ModConfigSpec.ConfigValue<Double>> impacts = new HashMap<>();
    private final Map<ResourceLocation, ModConfigSpec.ConfigValue<Double>> capacities = new HashMap<>();

    @Override
    public String getName() {
        return modId + "-server";
    }

    @Override
    public void registerAll(ModConfigSpec.Builder builder) {
        super.registerAll(builder);

        builder.comment("Stress impact configurations for " + modId, "[in Stress Units]")
                .push("impact");
        // defaultImpacts 将包含由 setImpact 添加的条目
        defaultImpacts.forEach((id, defaultValue) -> {
            // 确保路径正确，不含命名空间
            impacts.put(id, builder.define(id.getPath(), defaultValue));
        });
        builder.pop();

        // 如果你有容量配置
        if (!defaultCapacities.isEmpty()) {
            builder.comment("Stress capacity configurations for " + modId, "[in Stress Units]")
                    .push("capacity");
            defaultCapacities.forEach((id, defaultValue) -> {
                capacities.put(id, builder.define(id.getPath(), defaultValue));
            });
            builder.pop();
        }

        // 关键：注册应力值提供者
        BlockStressValues.IMPACTS.registerProvider(this::getImpact);
        BlockStressValues.CAPACITIES.registerProvider(this::getCapacity); // 总是注册，getCapacity可以返回null
    }

    @Nullable
    public DoubleSupplier getImpact(Block block) {
        ResourceLocation id = RegisteredObjectsHelper.getKeyOrThrow(block);
        if (!id.getNamespace().equals(modId)) {
            return null;
        }
        ModConfigSpec.ConfigValue<Double> configValue = this.impacts.get(id);
        return configValue == null ? null : configValue::get;
    }

    @Nullable
    public DoubleSupplier getCapacity(Block block) {
        ResourceLocation id = RegisteredObjectsHelper.getKeyOrThrow(block);
        if (!id.getNamespace().equals(modId)) {
            return null;
        }
        ModConfigSpec.ConfigValue<Double> configValue = this.capacities.get(id);
        return configValue == null ? null : configValue::get;
    }

    public <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setImpact(double value) {
        return builder -> {
            if (!builder.getOwner().getModid().equals(modId)) {
                throw new IllegalStateException("Attempting to set stress impact for block '" + builder.getName()
                        + "' from mod '" + builder.getOwner().getModid() + "' using config for mod '" + modId + "'.");
            }
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modId, builder.getName());
            this.defaultImpacts.put(id, value);
            return builder;
        };
    }

    public <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setCapacity(double value) {
        return builder -> {
            if (!builder.getOwner().getModid().equals(modId)) {
                throw new IllegalStateException("Attempting to set stress capacity for block '" + builder.getName()
                        + "' from mod '" + builder.getOwner().getModid() + "' using config for mod '" + modId + "'.");
            }
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modId, builder.getName());
            this.defaultCapacities.put(id, value);
            return builder;
        };
    }

    public <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setNoImpact() {
        return setImpact(0);
    }

    private static class Comments {
        static String radius =
                "Radius used to check nearby entities before spawning.";

        static String maxEntities =
                "Maximum allowed nearby entities before spawning fails.";
    }
}
