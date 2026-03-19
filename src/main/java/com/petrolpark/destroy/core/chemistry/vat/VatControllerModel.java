package com.petrolpark.destroy.core.chemistry.vat;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroySpriteShifts;
import com.petrolpark.destroy.core.chemistry.vat.VatControllerBlockEntity.CasingType;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import java.util.ArrayList;
import java.util.List;

public class VatControllerModel extends BakedModelWrapper<BakedModel> {

    public static final ModelProperty<CasingType> CASING_PROPERTY = new ModelProperty<>();

    private static final SpriteShiftEntry SPRITE_SHIFT = DestroySpriteShifts.COPPER_VAT_CONTROLLER;

    public VatControllerModel(BakedModel template) {
        super(template);
    }

    /*
    @Override
    public TextureAtlasSprite getParticleIcon(ModelData data) {
        if (!data.has(CASING_PROPERTY))
            return super.getParticleIcon(data);
        CasingType type = data.get(CASING_PROPERTY);
        if (type == CasingType.NONE || type == CasingType.BRASS)
            return super.getParticleIcon(data);
        return AllSpriteShifts.ANDESITE_CASING.getOriginal();
    }
     */

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType) {
        List<BakedQuad> quads = super.getQuads(state, side, rand, extraData, renderType);
        if(side != state.getValue(VatControllerBlock.FACING))
            return quads;

        quads = new ArrayList<>(quads);
        
        for (int i = 0; i < quads.size(); i++) {
            BakedQuad quad = quads.get(i);

            BakedQuad newQuad = BakedQuadHelper.clone(quad);
            int[] vertexData = newQuad.getVertices();

            for (int vertex = 0; vertex < 4; vertex++) {
                float u = BakedQuadHelper.getU(vertexData, vertex);
                float v = BakedQuadHelper.getV(vertexData, vertex);
                BakedQuadHelper.setU(vertexData, vertex, SPRITE_SHIFT.getTargetU(u));
                BakedQuadHelper.setV(vertexData, vertex, SPRITE_SHIFT.getTargetV(v));
            }

            quads.set(i, newQuad);
        }

        return quads;
    }

}
