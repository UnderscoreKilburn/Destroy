package com.petrolpark.destroy.content.processing.treetap;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.petrolpark.destroy.client.DestroyPartials;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class TreeTapInstance extends ShaftInstance<TreeTapBlockEntity> implements DynamicInstance {

    final Direction facing;
    protected final OrientedData arm;

    public TreeTapInstance(MaterialManager materialManager, TreeTapBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        facing = blockState.getValue(HORIZONTAL_FACING);
        arm = getOrientedMaterial().getModel(DestroyPartials.TREE_TAP_ARM, blockState, facing).createInstance()
            .setPivot(0.5f, 0.5f, 0.5f);

        updateAnimation();
    }

    @Override
    public void beginFrame() {
        updateAnimation();
    }

    @Override
    public void updateLight() {
        super.updateLight();
        relight(pos, arm);
    }

    @Override
    public void remove() {
        super.remove();
        arm.delete();
    }

    private void updateAnimation() {
        float angle = 9f * Mth.sin((float)Math.toRadians((AnimationTickHolder.getRenderTime() * blockEntity.getSpeed() * 3f / 10) % 360));

        Vector3f axis = facing.getCounterClockWise().step();

        arm.setPosition(getInstancePosition())
            .setRotation(new Quaternionf().rotationAxis((float)Math.toRadians(angle), axis));
    }
}
