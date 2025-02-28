package com.petrolpark.destroy.core.block.copycat;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.foundation.model.BakedQuadHelper;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;

public class CopycatFullBlockModel extends CopycatModel {

    public CopycatFullBlockModel(BakedModel originalModel) {
        super(originalModel);
    };

    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, RandomSource rand, BlockState material, ModelData wrappedData, RenderType renderType) {
        return getModelOf(material).getQuads(material, side, rand, wrappedData, renderType).stream().map(BakedQuadHelper::clone).toList();
    };

    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return originalModel.getRenderTypes(state, rand, data);
    };
    
};
