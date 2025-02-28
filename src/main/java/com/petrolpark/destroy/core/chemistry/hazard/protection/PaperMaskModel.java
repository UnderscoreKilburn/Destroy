package com.petrolpark.destroy.core.chemistry.hazard.protection;

import com.mojang.blaze3d.vertex.PoseStack;
import com.petrolpark.destroy.client.DestroyPartials;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.BakedModelWrapper;

public class PaperMaskModel extends BakedModelWrapper<BakedModel> {

    public PaperMaskModel(BakedModel template) {
        super(template);
    };

    @Override
	public BakedModel applyTransform(ItemDisplayContext itemDisplayContext, PoseStack matrix, boolean leftHanded) {
		if (itemDisplayContext == ItemDisplayContext.HEAD) {
			return DestroyPartials.PAPER_MASK.get().applyTransform(itemDisplayContext, matrix, leftHanded);
        };
		return super.applyTransform(itemDisplayContext, matrix, leftHanded);
	};
    
};
