package com.petrolpark.destroy.content.processing.sieve;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.petrolpark.destroy.client.DestroyPartials;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class MechanicalSieveInstance extends SingleRotatingInstance<MechanicalSieveBlockEntity> implements DynamicInstance {

    protected final ModelData sieve;
    protected final ModelData linkages;

    public MechanicalSieveInstance(MaterialManager materialManager, MechanicalSieveBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        sieve = materialManager.defaultCutout()
            .material(Materials.TRANSFORMED)
            .getModel(DestroyPartials.MECHANICAL_SIEVE, blockState)
            .createInstance();

        linkages = materialManager.defaultSolid()
            .material(Materials.TRANSFORMED)
            .getModel(DestroyPartials.MECHANICAL_SIEVE_LINKAGES, blockState)
            .createInstance();

        updateAnimation();
    }

    @Override
    public void beginFrame() {
        updateAnimation();
    }

    @Override
    public void updateLight() {
        super.updateLight();
        relight(pos, sieve, linkages);
    }

    @Override
    public void remove() {
        super.remove();
        sieve.delete();
        linkages.delete();
    }

    private void updateAnimation() {
        Direction.Axis axis = KineticBlockEntityRenderer.getRotationAxisOf(blockEntity);
        float angle = KineticBlockEntityRenderer.getAngleForTe(blockEntity, blockEntity.getBlockPos(), axis);
        Direction facing = blockState.getValue(MechanicalSieveBlock.X) ? Direction.EAST : Direction.SOUTH;

        float offset = (float)(Mth.sin(angle) * 2 / 16d);

        sieve.loadIdentity()
            .translate(getInstancePosition())
            .centre()
            .rotateY(AngleHelper.horizontalAngle(facing))
            .unCentre()
            .translate(offset, 0d , 0d);

        linkages.loadIdentity()
            .translate(getInstancePosition())
            .centre()
            .rotateY(AngleHelper.horizontalAngle(facing))
            .translate(offset, 0d , 0d)
            .rotateZRadians(-angle)
            .unCentre();
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        Direction facing = blockState.getValue(MechanicalSieveBlock.X) ? Direction.EAST : Direction.SOUTH;
        return getRotatingMaterial().getModel(DestroyPartials.MECHANICAL_SIEVE_SHAFT, blockState, facing);
    }
}
