package com.petrolpark.destroy.content.processing.glassblowing;

import com.mojang.blaze3d.vertex.PoseStack;
import com.petrolpark.destroy.DestroyBlocks;

import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BlowpipeItemRenderLayer<T extends LivingEntity, M extends EntityModel<T> & ArmedModel & HeadedModel> extends ItemInHandLayer<T, M> {

    private ItemInHandRenderer itemRenderer;

    public BlowpipeItemRenderLayer(RenderLayerParent<T, M> renderer, ItemInHandRenderer itemInHandRenderer) {
        super(renderer, itemInHandRenderer);
        this.itemRenderer = itemInHandRenderer;
    };

    @Override
    protected void renderArmWithItem(LivingEntity livingEntity, ItemStack stack, ItemDisplayContext displayContext, HumanoidArm arm, PoseStack ms, MultiBufferSource buffer, int light) {
        if (DestroyBlocks.BLOWPIPE.isIn(stack) && livingEntity.swingTime == 0 && stack.getOrCreateTag().getBoolean("Blowing")) {
            ms.pushPose();
            ModelPart modelpart = getParentModel().getHead();
            float f = modelpart.xRot;
            modelpart.xRot = Mth.clamp(modelpart.xRot, -Mth.PI / 6f, Mth.PI / 2f);
            modelpart.translateAndRotate(ms);
            modelpart.xRot = f;
            CustomHeadLayer.translateToHead(ms, false);

            itemRenderer.renderItem(livingEntity, stack, ItemDisplayContext.HEAD, false, ms, buffer, light);
            ms.popPose();
        };
    };

    public static void registerOnAll(EntityRenderDispatcher renderManager) {
		for (EntityRenderer<? extends Player> renderer : renderManager.getSkinMap().values()) registerOn(renderer, renderManager.getItemInHandRenderer());
		for (EntityRenderer<?> renderer : renderManager.renderers.values()) registerOn(renderer, renderManager.getItemInHandRenderer());
	};

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void registerOn(EntityRenderer<?> entityRenderer, ItemInHandRenderer itemRenderer) {
		if (!(entityRenderer instanceof LivingEntityRenderer)) return;
		LivingEntityRenderer<?, ?> livingRenderer = (LivingEntityRenderer<?, ?>) entityRenderer;
		if (!(livingRenderer.getModel() instanceof HumanoidModel)) return;
		BlowpipeItemRenderLayer<?, ?> layer = new BlowpipeItemRenderLayer(livingRenderer, itemRenderer);
		livingRenderer.addLayer((BlowpipeItemRenderLayer) layer);
	};
    
};
