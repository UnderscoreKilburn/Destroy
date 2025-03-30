package com.petrolpark.destroy.content.oil.pumpjack;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.petrolpark.destroy.client.DestroyPartials;
import com.simibubi.create.foundation.utility.AngleHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class PumpjackInstance extends BlockEntityInstance<PumpjackBlockEntity> implements DynamicInstance {

    protected final ModelData cam;
	protected final ModelData linkage;
	protected final ModelData beam;
    protected final ModelData pump;

    private static final double beamRotation = Math.asin(5 / 17d);

    public PumpjackInstance(MaterialManager materialManager, PumpjackBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        cam = materialManager.defaultSolid()
            .material(Materials.TRANSFORMED)
            .getModel(DestroyPartials.PUMPJACK_CAM, blockState)
            .createInstance();

        linkage = materialManager.defaultSolid()
            .material(Materials.TRANSFORMED)
            .getModel(DestroyPartials.PUMPJACK_LINKAGE, blockState)
            .createInstance();

        beam = materialManager.defaultCutout()
            .material(Materials.TRANSFORMED)
            .getModel(DestroyPartials.PUMPJACK_BEAM, blockState)
            .createInstance();

        pump = materialManager.defaultSolid()
            .material(Materials.TRANSFORMED)
            .getModel(DestroyPartials.PUMPJACK_PUMP, blockState)
            .createInstance();
    };

    @Override
    public void beginFrame() {
        float angle = blockEntity.getRenderAngle();

        Direction facing = PumpjackBlock.getFacing(blockState);

        transformed(cam, facing)
            .translate(0d, 0d, 1d)
            .centre()
            .rotateXRadians(angle - Mth.HALF_PI)
            .centre()
            .translate(0d, 0d, -1d)
            .unCentre()
            .unCentre();

        transformed(linkage, facing)
            .translate(0d, -4.5 / 16d, 1d)
            .translate(0d, Mth.sin(angle) * 5 / 16d, -Mth.cos(angle) * 5 / 16d)
            .centre()
            .rotateXRadians((Mth.cos(angle)) * beamRotation * 0.73d)
            .centre()
            .translate(0d, 0d, -1d)
            .unCentre()
            .unCentre();

        transformed(beam, facing)
            .translate(0d, 1d, 0d)
            .centre()
            .rotateXRadians((Mth.sin(angle)) * -beamRotation)
            .centre()
            .translate(0d, -1d, 0d)
            .unCentre()
            .unCentre();

        transformed(pump, facing)
            .translate(0d, (3 / 16) - (Mth.sin(angle) * 3 / 16d), 0d);
    };

    protected ModelData transformed(ModelData modelData, Direction facing) {
		return modelData.loadIdentity()
			.translate(getInstancePosition())
			.centre()
			.rotateY(AngleHelper.horizontalAngle(facing))
			.unCentre();
	};

    @Override
	public void updateLight() {
		relight(pos.above(), cam, linkage, beam, pump);
	};

    @Override
    protected void remove() {
        cam.delete();
        linkage.delete();
        beam.delete();
        pump.delete();
    };
    
};
