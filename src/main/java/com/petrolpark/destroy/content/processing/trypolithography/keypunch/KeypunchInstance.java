package com.petrolpark.destroy.content.processing.trypolithography.keypunch;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.petrolpark.destroy.client.DestroyPartials;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogInstance;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

public class KeypunchInstance extends EncasedCogInstance implements DynamicInstance {

    protected final ModelData piston;

    public KeypunchInstance(MaterialManager materialManager, KeypunchBlockEntity blockEntity) {
        super(materialManager, blockEntity, false);

        piston = materialManager.defaultSolid()
            .material(Materials.TRANSFORMED)
            .getModel(DestroyPartials.KEYPUNCH_PISTON, blockState)
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
        relight(pos, piston);
    }

    @Override
    public void remove() {
        super.remove();
        piston.delete();
    }

    @Override
    protected Instancer<RotatingData> getCogModel() {
        return materialManager.defaultSolid()
            .material(AllMaterialSpecs.ROTATING)
            .getModel(AllPartialModels.SHAFTLESS_COGWHEEL, blockEntity.getBlockState());
    }

    private void updateAnimation() {
        KeypunchBlockEntity be = (KeypunchBlockEntity)blockEntity;
        CircuitPunchingBehaviour behaviour = be.punchingBehaviour;
        float renderedHeadOffset = behaviour.getRenderedPistonOffset(AnimationTickHolder.getPartialTicks());

        int pistonPos = be.getActualPosition();

        piston.loadIdentity()
            .translate(getInstancePosition())
            .translate((4 + 2 * (pistonPos % 4)) / 16f, - (6.1f + (renderedHeadOffset * 12.5f)) / 16f, (4 + 2 * (pistonPos / 4)) / 16f);
    }
}
