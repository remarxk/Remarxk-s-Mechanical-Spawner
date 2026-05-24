package com.remarxk.remarxks_mechanical_spawner.Ponder;

import com.remarxk.remarxks_mechanical_spawner.RemarxksMechanicalSpawner;
import com.remarxk.remarxks_mechanical_spawner.registrate.ModBlockEntities;
import com.remarxk.remarxks_mechanical_spawner.registrate.ModBlocks;
import com.remarxk.remarxks_mechanical_spawner.registrate.ModItems;
import com.remarxk.remarxks_mechanical_spawner.utils.Logger;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.ParticleEmitter;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.instruction.AnimateElementInstruction;
import net.createmod.ponder.foundation.instruction.AnimateWorldSectionInstruction;
import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

public class MechanicalSpawnerScenes {
    public static void setEntity(SceneBuilder sceneBuilder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(sceneBuilder);

        scene.title("mechanical_spawner_setentity", "Set Mechanical Spawner Entity");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.idle(5);

        scene.world().showSection(util.select().layer(1), Direction.DOWN);

        BlockPos spawner = util.grid().at(2, 1, 2);
        Vec3 spawnerTop = util.vector().topOf(spawner);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("Use the Mechanical Spawner on a mob spawner while crouching to configure its entity type")
                .pointAt(spawnerTop)
                .placeNearTarget();
        scene.idle(60);

        ItemStack mechanicalSpawnerItem = ModItems.MECHANICAL_SPAWNER.asStack();

        scene.overlay().showControls(util.vector().topOf(spawner), Pointing.DOWN, 40).whileSneaking().rightClick()
                .withItem(mechanicalSpawnerItem);
        scene.idle(40);

        scene.world().destroyBlock(spawner);
        scene.idle(40);

        BlockState mechanicalSpawnerBlockEntity = ModBlocks.MECHANICAL_SPAWNER.getDefaultState();
        scene.world().setBlock(spawner, mechanicalSpawnerBlockEntity, false);
        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("Use a spawn egg on the Mechanical Spawner to set its entity type as well")
                .pointAt(spawnerTop)
                .placeNearTarget();
        scene.idle(60);

        ItemStack spawnerEgg = new ItemStack(Items.ZOMBIE_SPAWN_EGG);

        scene.overlay().showControls(util.vector().topOf(spawner), Pointing.DOWN, 40).rightClick()
                .withItem(spawnerEgg);
        scene.idle(40);
    }

    public static void working(SceneBuilder sceneBuilder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(sceneBuilder);

        scene.title("mechanical_spawner_working", "Mechanical Spawner Working");
        scene.configureBasePlate(0, 0, 7);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(5);

        BlockPos spawner = util.grid().at(3, 3, 4);

        Selection kinetics = util.select().fromTo(2, 0, 0, 3, 2, 4);

        // 初始不转
        scene.world().setKineticSpeed(kinetics, 0);

        scene.idle(20);

        // 显示结构
        scene.world().showSection(util.select().layer(1), Direction.DOWN);
        scene.world().showSection(util.select().layer(2), Direction.DOWN);
        scene.world().showSection(util.select().layer(3), Direction.DOWN);
        scene.idle(20);

        Vec3 spawnerTop = util.vector().topOf(spawner);

        // 1️⃣ 指向刷怪笼说明
        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("Providing Speed allows it to spawn mobs")
                .pointAt(spawnerTop)
                .placeNearTarget();

        scene.idle(60);

        spawnEntity(scene, util, kinetics, spawner, EntityType.ZOMBIE, 32, new Vec3(-1, 0, -1));

        // 4️⃣ 提示转速影响
        scene.overlay().showText(60)
                .attachKeyFrame()
                .colored(PonderPalette.GREEN)
                .text("Higher Speed increases spawn rate and launch distance")
                .pointAt(spawnerTop)
                .placeNearTarget();

        scene.idle(60);

        // 5️⃣ 提高转速
        spawnEntity(scene, util, kinetics, spawner, EntityType.ZOMBIE, 64, new Vec3(Mth.sin((float) Math.toRadians(-90)), 0, Mth.cos((float) Math.toRadians(-90))));
        spawnEntity(scene, util, kinetics, spawner, EntityType.ZOMBIE, 64, new Vec3(Mth.sin((float) Math.toRadians(-120)), 0, Mth.cos((float) Math.toRadians(-120))));
    }

    private final static Random randomsource = new Random();

    public static Vec3 randomParticlePos() {
        double x, y, z;
        x = y = z = 0;

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

        return new Vec3(x, y, z);
    }

    public static void spawnEntity(CreateSceneBuilder scene, SceneBuildingUtil util, Selection kinetics, BlockPos spawner, EntityType<?> entityType, int speed, Vec3 offset) {
        // 2️⃣ 启动转速
        scene.world().setKineticSpeed(kinetics, speed);
        scene.effects().indicateSuccess(spawner);
        scene.idle(10);

        // 模拟“工作中”（用成功提示代替粒子）
        for (int i = 0; i < 5; i++) {
            ParticleEmitter smokeEmitter = scene.effects().simpleParticleEmitter(ParticleTypes.SMOKE, new Vec3(0, 0.02f, 0));
            ParticleEmitter flameEmitter = scene.effects().simpleParticleEmitter(ParticleTypes.FLAME, new Vec3(0, 0.01f, 0));
            Vec3 pos = util.vector().blockSurface(spawner, Direction.UP).add(randomParticlePos());
            scene.effects().emitParticles(pos, smokeEmitter, 1, 5);
            scene.effects().emitParticles(pos, flameEmitter, 1, 5);
        }
        scene.idle(5);

        // 3️⃣ 生成生物（用展示实体方式）
        ElementLink<EntityElement> mob =
                scene.world().createEntity(level -> {
                    Vec3 pos = util.vector().blockSurface(spawner, Direction.NORTH).add(offset.x, -0.5, offset.y);
                    Zombie zombie = new Zombie(level);
                    zombie.setPos(pos);

                    Vec3 dir = offset.normalize();
                    zombie.setDeltaMovement(dir.x * (float) speed / 32, 0, dir.z * (float) speed / 32);
                    return zombie;
                });

        int updateTick = (int) (20 * (float) 32 / speed);

        // === 逐帧“甩飞” ===
        for (int i = 0; i < updateTick; i++) {
            scene.world().modifyEntity(mob, MechanicalSpawnerScenes::updateEntityDeltaMovement);
            scene.idle(1);
        }

        // 删除“占位实体”（模拟生成完成）
        scene.world().modifyEntity(mob, Entity::discard);
        scene.idle(updateTick);
    }

    public static void updateEntityDeltaMovement(Entity entity) {
        // 当前速度
        Vec3 vel = entity.getDeltaMovement();

        // 阻力衰减
        Vec3 friction = new Vec3(vel.x * 0.91, vel.y, vel.z * 0.91);

        // 重力
        Vec3 gravity = new Vec3(0, -0.08, 0);

        // 最终速度
        Vec3 newVel = friction.add(gravity);

        // 更新位置
        entity.setPos(entity.getX() + newVel.x, entity.getY() + newVel.y, entity.getZ() + newVel.z);

        // 保存速度供下一帧使用
        entity.setDeltaMovement(newVel);
    }
}
