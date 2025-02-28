package com.petrolpark.destroy.compat.createbigcannons;

public class DestroyBlobEffects {
    
    public static void registerBlobEffects() {
        //FluidBlobEffectRegistry.registerAllHit(DestroyFluids.MIXTURE.get(), DestroyBlobEffects::onMixtureHit);
    };

    // public static void onMixtureHit(EndFluidStack fstack, FluidBlob projectile, Level level, HitResult result) {
    //     FluidStack stack = new FluidStack(fstack.fluid(), fstack.amount());
    //     stack.setTag(fstack.data());
    //     PollutionHelper.pollute(level, BlockPos.containing(result.getLocation()), stack);
    // };
};
