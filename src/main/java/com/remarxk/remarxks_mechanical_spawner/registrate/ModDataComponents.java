package com.remarxk.remarxks_mechanical_spawner.registrate;

import com.remarxk.remarxks_mechanical_spawner.RemarxksMechanicalSpawner;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, RemarxksMechanicalSpawner.MODID);

    // 用来存刷怪笼绑定的生物类型
    public static final DeferredHolder<
                DataComponentType<?>,
                DataComponentType<ResourceLocation>
                > SPAWNER_ENTITY = DATA_COMPONENTS.register(
            "spawner_entity",
            () -> DataComponentType.<ResourceLocation>builder()
                    // 世界保存
                    .persistent(ResourceLocation.CODEC)
                    // 网络同步（客户端显示 / tooltip / 放置）
                    .networkSynchronized(ResourceLocation.STREAM_CODEC)
                    .build()
    );

    public static void register(IEventBus bus) {
        DATA_COMPONENTS.register(bus);
    }
}
