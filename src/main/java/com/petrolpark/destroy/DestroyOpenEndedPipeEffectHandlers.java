package com.petrolpark.destroy;

import com.petrolpark.destroy.core.fluid.openpipeeffect.BurningOpenEndedPipeEffectHandler;
import com.petrolpark.destroy.core.fluid.openpipeeffect.EffectApplyingOpenEndedPipeEffectHandler;
import com.petrolpark.destroy.core.pollution.PollutingOpenEndedPipeEffectHandler;
import com.simibubi.create.content.fluids.OpenEndedPipe;

import net.minecraft.world.effect.MobEffectInstance;

public class DestroyOpenEndedPipeEffectHandlers {
  
    public static final void register() {
        OpenEndedPipe.registerEffectHandler(new PollutingOpenEndedPipeEffectHandler());
        OpenEndedPipe.registerEffectHandler(new BurningOpenEndedPipeEffectHandler(DestroyFluids.NAPALM_SUNDAE.get()));
        OpenEndedPipe.registerEffectHandler(new BurningOpenEndedPipeEffectHandler(DestroyFluids.MOLTEN_CINNABAR.get()));
        OpenEndedPipe.registerEffectHandler(new EffectApplyingOpenEndedPipeEffectHandler(new MobEffectInstance(DestroyMobEffects.FRAGRANCE.get(), 21, 0, false, false, true), DestroyFluids.PERFUME.get()));
        OpenEndedPipe.registerEffectHandler(new EffectApplyingOpenEndedPipeEffectHandler(new MobEffectInstance(DestroyMobEffects.INEBRIATION.get(), 21, 0, false, false, true), DestroyFluids.UNDISTILLED_MOONSHINE.get()));
        OpenEndedPipe.registerEffectHandler(new EffectApplyingOpenEndedPipeEffectHandler(new MobEffectInstance(DestroyMobEffects.INEBRIATION.get(), 21, 2, false, false, true), DestroyFluids.MOONSHINE.get()));
    };
};
