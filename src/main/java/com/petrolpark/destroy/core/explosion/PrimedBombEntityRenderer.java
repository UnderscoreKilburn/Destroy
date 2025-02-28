package com.petrolpark.destroy.core.explosion;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;

public class PrimedBombEntityRenderer<T extends PrimedBombEntity> extends EntityRenderer<T> {

    protected final BlockRenderDispatcher blockRenderer;

    public PrimedBombEntityRenderer(Context context) {
        super(context);
        blockRenderer = context.getBlockRenderDispatcher();
    };

    /**
     * Copied from the {@link net.minecraft.client.renderer.entity.TntRenderer Minecraft source code}.
     */
    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack ms, MultiBufferSource buffer, int packedLight) {
        ms.pushPose();
        ms.translate(0.0F, 0.5F, 0.0F);
        int i = entity.getFuse();
        if ((float)i - partialTicks < 9f) {
            float f = 1f + (float)Math.pow(Mth.clamp(1f - ((float)i - partialTicks + 1f) / 10f, 0.0f, 1f), 3) * 0.3f; // Grow the Entity as it is about to explode
            ms.scale(f, f, f);
        };

        ms.mulPose(Axis.YP.rotationDegrees(-90f));
        ms.translate(-0.5f, -0.5f, 0.5f);
        ms.mulPose(Axis.YP.rotationDegrees(90f));
        renderBlock(entity, ms, buffer, packedLight, i);
        ms.popPose();
    };

    public void renderBlock(T entity, PoseStack ms, MultiBufferSource buffer, int light, int fuse) {
        TntMinecartRenderer.renderWhiteSolidBlock(blockRenderer, entity.getBlockStateToRender(), ms, buffer, light, fuse / 5 % 2 == 0);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ResourceLocation getTextureLocation(T pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    };
    
};
