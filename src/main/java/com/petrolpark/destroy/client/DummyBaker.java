package com.petrolpark.destroy.client;

import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.petrolpark.destroy.MoveToPetrolparkLibrary;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;

@MoveToPetrolparkLibrary
public class DummyBaker implements ModelBaker {

    public static final DummyBaker BAKER = new DummyBaker();

    public static BakedModel bake(BlockModel model, ResourceLocation location) {
        return bake(model, model, location);
    };

    public static BakedModel bake(BlockModel model, BlockModel modelOwner, ResourceLocation location) {
        return UnbakedGeometryHelper.bake(model, BAKER, model, Material::sprite, BlockModelRotation.X0_Y0, location, false);
    };

    @Override
    public @Nullable BakedModel bake(ResourceLocation location, ModelState state, Function<Material, TextureAtlasSprite> sprites) {
        return null;
    };

    @Override
    public Function<Material, TextureAtlasSprite> getModelTextureGetter() {
        return Material::sprite;
    };

    @Override
    public UnbakedModel getModel(ResourceLocation pLocation) {
        return null;
    };

    @Override
    public BakedModel bake(ResourceLocation location, ModelState state) {
        return bake(location, state, getModelTextureGetter());
    };
    
};
