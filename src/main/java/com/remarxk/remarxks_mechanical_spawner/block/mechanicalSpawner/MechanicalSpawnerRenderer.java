package com.remarxk.remarxks_mechanical_spawner.block.mechanicalSpawner;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.remarxk.remarxks_mechanical_spawner.utils.Logger;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MechanicalSpawnerRenderer extends KineticBlockEntityRenderer<MechanicalSpawnerEntity> {
    private static final float SCALE = 0.4375F;

    private final EntityRenderDispatcher entityRenderer;

    private EntityType<?> entityType = null;
    private Entity displayEntity = null;

    public MechanicalSpawnerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);

        entityRenderer = context.getEntityRenderer();
    }

    @Override
    protected void renderSafe(MechanicalSpawnerEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
//        BlockState state = getRenderedBlockState(be);
//        RenderType renderType = getRenderType(be, state);
//        renderRotatingBuffer(be, getRotatedModel(be, state), ms, buffer.getBuffer(renderType), light);

        EntityType<?> type = be.getEntityType();
        if (type == null) {
            displayEntity = null;
            return;
        }

        Level level = be.getLevel();
        if (level == null)
            return;

        if (entityType != type || displayEntity == null) {
            entityType = type;
            displayEntity = type.create(level);
        }

        float rad = getAngleForBe(be, be.getBlockPos(), Direction.Axis.Y);
        renderEntityInSpawner(partialTicks, ms, buffer, light, displayEntity, entityRenderer, rad, rad);
    }

    @Override
    protected BlockState getRenderedBlockState(MechanicalSpawnerEntity be) {
        return shaft(getRotationAxisOf(be));
    }

    public static void renderEntityInSpawner(float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, Entity entity, EntityRenderDispatcher entityRenderer, double rotationX, double rotationY) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.0F, 0.5F);
        float f = 0.53125F;
        float f1 = Math.max(entity.getBbWidth(), entity.getBbHeight());
        if ((double)f1 > (double)1.0F) {
            f /= f1;
        }

        float rad = - (float) Mth.lerp(partialTicks, rotationX, rotationY);
        float radius = 0.12F;
        float offsetX = Mth.cos(rad) * radius;
        float offsetZ = Mth.sin(rad) * radius;
        poseStack.translate(offsetX, 0.0F, offsetZ);
        float lookYaw = (float)(Math.atan2(-offsetX, -offsetZ) * (180F / Math.PI));

        poseStack.translate(0.0F, 0.4F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(lookYaw));
        poseStack.translate(0.0F, -0.2F, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(-30.0F));
        poseStack.scale(f, f, f);
        entityRenderer.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, partialTicks, poseStack, buffer, light);
        poseStack.popPose();
    }
}
