package com.petrolpark.destroy.content.processing.glassblowing;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class GlassblowingRecipe extends ProcessingRecipe<RecipeWrapper> {

    public List<BlowingShape> blowingShapes;

    public GlassblowingRecipe(ProcessingRecipeParams params) {
        super(DestroyRecipeTypes.GLASSBLOWING, params);
        blowingShapes = new ArrayList<>();
    };

    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return true;
    };

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    };

    @Override
    protected int getMaxInputCount() {
        return 0;
    };

    @Override
    protected int getMaxOutputCount() {
        return 1;
    };

    @Override
    public void readAdditional(FriendlyByteBuf buffer) {
        super.readAdditional(buffer);
        int shapeCount = buffer.readVarInt();
        blowingShapes = new ArrayList<>(shapeCount);
        for (int i = 0; i < shapeCount; i++) {
            blowingShapes.add(new BlowingShape(buffer.readFloat(), buffer.readFloat()));
        };
    };

    @Override
    public void writeAdditional(FriendlyByteBuf buffer) {
        super.writeAdditional(buffer);
        buffer.writeVarInt(blowingShapes.size());
        for (BlowingShape shape : blowingShapes) {
            buffer.writeFloat(shape.length());
            buffer.writeFloat(shape.radius());
        };
    };

    @Override
    public void readAdditional(JsonObject json) {
        super.readAdditional(json);
        if (json.has("shape") && json.get("shape").isJsonArray()) {
            json.get("shape").getAsJsonArray().forEach(e -> {
                if (!e.isJsonArray()) throw new JsonSyntaxException("Shape must specify a list of pairs of shapes");
                JsonArray shape = e.getAsJsonArray();
                if (shape.size() != 2) throw new JsonSyntaxException("Each entry in the shape must specify two values: length and radius");
                try {
                    blowingShapes.add(new BlowingShape(shape.get(0).getAsFloat() / 16f, shape.get(1).getAsFloat() / 16f));
                } catch (Throwable error) {
                    throw new JsonSyntaxException("Length and radius of shape components must be numbers");
                };
            });
        };
    };

    @Override
    public void writeAdditional(JsonObject json) {
        super.writeAdditional(json);
        json.add("shape", blowingShapes.stream().map(bs -> {
            JsonArray shape = new JsonArray();
            shape.set(0, new JsonPrimitive(bs.length()));
            shape.set(1, new JsonPrimitive(bs.radius()));
            return shape;
        }).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
    };

    public static record BlowingShape(float length, float radius) {};
    
};
