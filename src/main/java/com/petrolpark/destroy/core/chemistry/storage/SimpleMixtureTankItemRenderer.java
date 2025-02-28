package com.petrolpark.destroy.core.chemistry.storage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.petrolpark.destroy.core.chemistry.storage.SimpleMixtureTankRenderer.ISimpleMixtureTankRenderInformation;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SimpleMixtureTankItemRenderer extends CustomRenderedItemModelRenderer {

    private final ISimpleMixtureTankRenderInformation<ItemStack> renderInfo;

    public SimpleMixtureTankItemRenderer(ISimpleMixtureTankRenderInformation<ItemStack> renderInfo) {
        this.renderInfo = renderInfo;
    };

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        mc.getItemRenderer().render(stack, ItemDisplayContext.NONE, false, ms, buffer, light, overlay, model.getOriginalModel());
        ms.popPose();
        SimpleMixtureTankRenderer.render(renderInfo, stack, 0f, ms, buffer, light, overlay);
        ms.pushPose();
    };
    
};
