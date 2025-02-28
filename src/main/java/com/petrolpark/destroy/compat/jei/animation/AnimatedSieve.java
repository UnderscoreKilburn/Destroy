package com.petrolpark.destroy.compat.jei.animation;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.client.DestroyPartials;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class AnimatedSieve extends AnimatedKinetics {

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
		ms.translate(xOffset, yOffset, 200);
		ms.mulPose(Axis.XP.rotationDegrees(-25.5f));
		ms.mulPose(Axis.YP.rotationDegrees(112.5f));
		ms.scale(23, 23, 23);

        ms.pushPose();
        TransformStack.cast(ms)
            .translate(0d, -0.5d, 0.5d)
            .rotateZ(90)
            .rotateY(getCurrentAngle())
            .translateBack(0.5d, 0d, 0.5d);
		blockElement(DestroyPartials.MECHANICAL_SIEVE_SHAFT)
			.render(graphics);
        ms.popPose();

        ms.pushPose();
        TransformStack.cast(ms)
            .rotateY(90);
        ms.translate(-1f + Mth.sin(getCurrentAngle() * Mth.PI / 180f) * 2 / 16d, 0d, 0d);
        blockElement(DestroyPartials.MECHANICAL_SIEVE)
            .render(graphics);
        ms.pushPose();
        TransformStack.cast(ms)
            .translate(0.5d, -0.5d, 0d)
            .rotateZ(getCurrentAngle())
            .translateBack(0.5d, -0.5d, 0d);
        blockElement(DestroyPartials.MECHANICAL_SIEVE_LINKAGES)
            .render(graphics);
        ms.popPose();
        ms.popPose();

		blockElement(DestroyBlocks.MECHANICAL_SIEVE.getDefaultState())
			.atLocal(0, 0, 0)
			.render(graphics);

		ms.popPose();
    };
    
};
