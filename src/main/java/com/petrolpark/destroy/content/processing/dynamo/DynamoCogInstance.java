package com.petrolpark.destroy.content.processing.dynamo;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.petrolpark.destroy.client.DestroyPartials;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;

import net.minecraft.world.level.block.state.BlockState;

public class DynamoCogInstance extends SingleRotatingInstance<KineticBlockEntity> {

    public DynamoCogInstance(MaterialManager modelManager, KineticBlockEntity blockEntity) {
        super(modelManager, blockEntity);
    };

    @Override
    public void updateLight() {
        super.updateLight();
        relight(pos);
    };

    @Override
    protected Instancer<RotatingData> getModel() {
        BlockState state = blockEntity.getBlockState();
        return getRotatingMaterial().getModel(state.getValue(DynamoBlock.ARC_FURNACE) ? DestroyPartials.ARC_FURNACE_SHAFT : DestroyPartials.DYNAMO_SHAFT, blockEntity.getBlockState());
    };
};
