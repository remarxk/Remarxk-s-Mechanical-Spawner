package com.remarxk.remarxks_mechanical_spawner.utils;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Method;

public class BlazeBurnerItemHelper {
    private static Method SPAWN_CAPTURE_EFFECTS;

    private static Method GIVE_BURNER_ITEM_TO;

    static {
        try {
            SPAWN_CAPTURE_EFFECTS = BlazeBurnerBlockItem.class.getDeclaredMethod(
                    "spawnCaptureEffects", Level.class, Vec3.class);
        } catch (NoSuchMethodException e) {
            SPAWN_CAPTURE_EFFECTS = null;
        }

        try {
            GIVE_BURNER_ITEM_TO = BlazeBurnerBlockItem.class.getDeclaredMethod(
                    "giveBurnerItemTo", Player.class, ItemStack.class, InteractionHand.class);
        } catch (NoSuchMethodException e) {
            GIVE_BURNER_ITEM_TO = null;
        }
    }

    public static void spawnCaptureEffects(BlazeBurnerBlockItem item, Level world, Vec3 vec) {
        if (SPAWN_CAPTURE_EFFECTS == null)
            return;

        try {
            SPAWN_CAPTURE_EFFECTS.setAccessible(true);
            SPAWN_CAPTURE_EFFECTS.invoke(item, world, vec);
        }
        catch (Exception e) {
            return;
        }
    }

    public static void giveBurnerItemTo(BlazeBurnerBlockItem item, Player player, ItemStack heldItem, InteractionHand hand) {
        if (GIVE_BURNER_ITEM_TO == null)
            return;

        try {
            GIVE_BURNER_ITEM_TO.setAccessible(true);
            GIVE_BURNER_ITEM_TO.invoke(item, player, heldItem, hand);
        }
        catch (Exception e) {
            return;
        }
    }
}
