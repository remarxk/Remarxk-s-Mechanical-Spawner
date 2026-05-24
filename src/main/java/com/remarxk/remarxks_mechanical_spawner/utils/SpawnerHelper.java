package com.remarxk.remarxks_mechanical_spawner.utils;

import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class SpawnerHelper {
    private static Field NEXT_SPAWN_DATA;

    static {
        try {
            NEXT_SPAWN_DATA = BaseSpawner.class.getDeclaredField("nextSpawnData");
            NEXT_SPAWN_DATA.setAccessible(true);
        } catch (Exception e) {
            NEXT_SPAWN_DATA = null;
        }
    }

    @Nullable
    public static SpawnData getNextSpawnData(BaseSpawner spawner) {
        if (NEXT_SPAWN_DATA == null)
            return null;

        try {
            return (SpawnData) NEXT_SPAWN_DATA.get(spawner);
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
