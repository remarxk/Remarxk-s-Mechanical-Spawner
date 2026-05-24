package com.remarxk.remarxks_mechanical_spawner.registrate;

import com.remarxk.remarxks_mechanical_spawner.Ponder.MechanicalSpawnerScenes;
import com.remarxk.remarxks_mechanical_spawner.RemarxksMechanicalSpawner;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.foundation.PonderChapterRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class ModPonder implements PonderPlugin {
    @Override
    public String getModId() {
        return RemarxksMechanicalSpawner.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        ResourceLocation setEntityLoc = ResourceLocation.fromNamespaceAndPath(RemarxksMechanicalSpawner.MODID, "mechanical_spawner_setentity");
        ResourceLocation workingLoc = ResourceLocation.fromNamespaceAndPath(RemarxksMechanicalSpawner.MODID, "mechanical_spawner_working");

        HELPER.forComponents(ModBlocks.MECHANICAL_SPAWNER)
                .addStoryBoard(setEntityLoc, MechanicalSpawnerScenes::setEntity, AllCreatePonderTags.KINETIC_APPLIANCES)
                .addStoryBoard(workingLoc, MechanicalSpawnerScenes::working, AllCreatePonderTags.KINETIC_APPLIANCES);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.addToTag(AllCreatePonderTags.KINETIC_APPLIANCES)
                .add(ModBlocks.MECHANICAL_SPAWNER);
    }
}
