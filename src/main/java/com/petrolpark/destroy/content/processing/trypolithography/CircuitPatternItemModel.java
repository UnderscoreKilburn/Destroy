package com.petrolpark.destroy.content.processing.trypolithography;

import java.util.function.Function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

/**
 * A wrapped for a regular Forge {@link ItemLayerModel Item model}, which also references a location of fragment textures, which will have models automatically created and registered.
 */
public class CircuitPatternItemModel implements IUnbakedGeometry<CircuitPatternItemModel> {

    public final ResourceLocation fragmentTextureResourceLocation;
    public final IUnbakedGeometry<?> wrapped;

    public CircuitPatternItemModel(IUnbakedGeometry<?> wrapped, ResourceLocation fragmentTextureResourceLocation) {
        this.wrapped = wrapped;
        this.fragmentTextureResourceLocation = fragmentTextureResourceLocation;
    };

    @Override
    public CircuitPatternItemModel.Baked bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        return new Baked(wrapped.bake(context, baker, spriteGetter, modelState, overrides, modelLocation));
    };

    public class Baked extends BakedModelWrapper<BakedModel> {
        
        public Baked(BakedModel originalModel) {
            super(originalModel);
        };

        public ResourceLocation getFragmentTextureResourceLocation() {
            return fragmentTextureResourceLocation;
        };

    };

    public static class Loader implements IGeometryLoader<CircuitPatternItemModel> {

        public static final Loader INSTANCE = new Loader();

        @Override
        public CircuitPatternItemModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
            ItemLayerModel itemModel = ItemLayerModel.Loader.INSTANCE.read(jsonObject, deserializationContext);
            if (jsonObject.has("fragment_textures")) {
                try {
                    return new CircuitPatternItemModel(itemModel, new ResourceLocation(jsonObject.get("fragment_textures").getAsString()));
                } catch (UnsupportedOperationException | IllegalStateException e) {
                    throw new JsonParseException("Cannot read Circuit Pattern item model", e);
                }
            } else {
                throw new JsonParseException("Circuit Pattern item models must refer to a texture of fragments");
            }
        };

    };
    
};
