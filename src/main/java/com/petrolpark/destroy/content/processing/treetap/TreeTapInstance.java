package com.petrolpark.destroy.content.processing.treetap;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.petrolpark.destroy.client.DestroyPartials;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class TreeTapInstance extends ShaftInstance<TreeTapBlockEntity> implements DynamicInstance {

    protected final ModelData arm;

    public TreeTapInstance(MaterialManager materialManager, TreeTapBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        arm = materialManager.defaultSolid()
            .material(Materials.TRANSFORMED)
            .getModel(DestroyPartials.TREE_TAP_ARM, blockState)
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
        relight(pos, arm);
    }

    @Override
    public void remove() {
        super.remove();
        arm.delete();
    }

    private void updateAnimation() {
        Direction facing = blockState.getValue(TreeTapBlock.HORIZONTAL_FACING);

        arm.loadIdentity()
            .translate(getInstancePosition())
            .centre()
            .rotate(9f * Mth.sin(KineticBlockEntityRenderer.getAngleForTe(blockEntity, blockEntity.getBlockPos(), facing.getClockWise().getAxis())), facing.getClockWise().getAxis())
            .rotateToFace(facing.getOpposite())
            .unCentre();
    }
}
