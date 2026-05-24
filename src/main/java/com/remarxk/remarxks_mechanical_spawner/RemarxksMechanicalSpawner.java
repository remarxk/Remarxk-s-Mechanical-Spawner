package com.remarxk.remarxks_mechanical_spawner;

import com.remarxk.remarxks_mechanical_spawner.Item.MechanicalSpawnerItemRenderer;
import com.remarxk.remarxks_mechanical_spawner.config.RemarxksMechanicalSpawnerConfig;
import com.remarxk.remarxks_mechanical_spawner.registrate.ModItems;
import com.remarxk.remarxks_mechanical_spawner.registrate.ModPonder;
import com.remarxk.remarxks_mechanical_spawner.registrate.ModRegister;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

@Mod(RemarxksMechanicalSpawner.MODID)
public class RemarxksMechanicalSpawner {
    public static final String MODID = "remarxks_mechanical_spawner";

    public static final RemarxksMechanicalSpawnerConfig MAIN_CONFIG = new RemarxksMechanicalSpawnerConfig();

    public RemarxksMechanicalSpawner(IEventBus modEventBus, ModContainer modContainer) {
        ModRegister.register(modEventBus);

        ModConfigSpec.Builder mainBuilder = new ModConfigSpec.Builder();
        MAIN_CONFIG.registerAll(mainBuilder);
        ModConfigSpec mainConfigSpec = mainBuilder.build();
        modContainer.registerConfig(ModConfig.Type.SERVER, mainConfigSpec, MAIN_CONFIG.getName() + ".toml");
    }

    @OnlyIn(Dist.CLIENT)
    @EventBusSubscriber
    public static class ClientSetup {
        @SubscribeEvent
        public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
            event.registerItem(
                    new IClientItemExtensions() {
                        private MechanicalSpawnerItemRenderer renderer;

                        @Override
                        public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                            if (renderer == null) {
                                var entityRenderer = Minecraft.getInstance().getBlockEntityRenderDispatcher();
                                var modelSet = Minecraft.getInstance().getEntityModels();
                                renderer = new MechanicalSpawnerItemRenderer(entityRenderer, modelSet);
                            }
                            return renderer;
                        }
                    },
                    ModItems.MECHANICAL_SPAWNER
            );

            PonderIndex.addPlugin(new ModPonder());
        }
    }
}
