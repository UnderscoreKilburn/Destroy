package com.petrolpark.destroy.core.chemistry.vat.ponder;

import com.petrolpark.destroy.DestroyBlockEntityTypes;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.instruction.PonderInstruction;
import com.simibubi.create.foundation.utility.animation.LerpedFloat.Chaser;

import net.minecraft.core.BlockPos;

public class SetVatPressurePonderInstruction extends PonderInstruction {

    public final BlockPos vatControllerPos;
    public final float targetPressure;
    public final float chaseSpeed;

    public SetVatPressurePonderInstruction(BlockPos vatControllerPos, float targetPressure, float chaseSpeed) {
        this.vatControllerPos = vatControllerPos;
        this.targetPressure = targetPressure;
        this.chaseSpeed = chaseSpeed;
    };

    @Override
    public boolean isComplete() {
        return true;
    };

    @Override
    public void tick(PonderScene scene) {
        scene.getWorld().getBlockEntity(vatControllerPos, DestroyBlockEntityTypes.VAT_CONTROLLER.get()).ifPresent(vc -> {
            vc.pressure.chase(targetPressure, chaseSpeed, Chaser.EXP);
        });
    };
    
};
