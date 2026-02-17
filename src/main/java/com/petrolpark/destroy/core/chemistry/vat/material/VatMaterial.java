package com.petrolpark.destroy.core.chemistry.vat.material;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.core.recipe.ingredient.BlockIngredient;
import com.petrolpark.destroy.util.GlobalRecipeAccess;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class VatMaterial implements Recipe<Container> {

    protected ResourceLocation id;
    private RecipeSerializer<?> serializer;

    protected BlockIngredient blockIngredient;

    protected float maxPressure;
    protected float thermalConductivity;
    protected boolean transparent;

    public BlockIngredient getBlock() {return blockIngredient;}
    public float getMaxPressure() {return maxPressure;}
    public float getThermalConductivity() {return thermalConductivity;}
    public boolean isTransparent() {return transparent;}

    public VatMaterial(ResourceLocation recipeId, Serializer serializer) {
        this(recipeId, serializer, BlockIngredient.EMPTY, 1000f, 0f, false);
    }

    public VatMaterial(ResourceLocation recipeId, Serializer serializer, BlockIngredient blockIngredient, float maxPressure, float thermalConductivity, boolean transparent) {
        this.id = recipeId;
        this.serializer = serializer;

        this.blockIngredient = blockIngredient;
        this.maxPressure = maxPressure;
        this.thermalConductivity = thermalConductivity;
        this.transparent = transparent;
    }

    private static List<VatMaterial> MATERIALS = new ArrayList<>();

    public static void invalidateCache() {
        MATERIALS.clear();
    }

    public static List<VatMaterial> getAllMaterials() {
        if(MATERIALS.isEmpty()) {
            RecipeManager recipeManager = GlobalRecipeAccess.get();
            if(recipeManager != null)
                recipeManager.getAllRecipesFor(DestroyRecipeTypes.VAT_MATERIAL_PROPERTIES.getType()).forEach(r -> MATERIALS.add((VatMaterial)r));
        }
        return MATERIALS;
    }

    public static boolean isValid(BlockState state) {
        return getMaterial(state).isPresent();
    }

    public static Optional<VatMaterial> getMaterial(BlockState state) {
        return getAllMaterials().stream().filter(r -> r.blockIngredient.test(state)).findFirst();
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return true;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return serializer;
    }

    @Override
    public RecipeType<?> getType() {
        return DestroyRecipeTypes.VAT_MATERIAL_PROPERTIES.getType();
    }

    public static class Serializer implements RecipeSerializer<VatMaterial> {
        @Override
        public VatMaterial fromJson(ResourceLocation id, JsonObject json) {
            BlockIngredient block = BlockIngredient.fromJson(GsonHelper.getNonNull(json, "blocks"));
            float maxPressure = GsonHelper.getNonNull(json, "max_pressure").getAsFloat();
            float thermalConductivity = GsonHelper.getNonNull(json, "thermal_conductivity").getAsFloat();
            boolean transparent = GsonHelper.getNonNull(json, "transparent").getAsBoolean();

            return new VatMaterial(id, this, block, maxPressure, thermalConductivity, transparent);
        }

        public void toJson(JsonObject json, VatMaterial recipe) {
            json.add("blocks", recipe.blockIngredient.toJson());
            json.addProperty("max_pressure", recipe.maxPressure);
            json.addProperty("thermal_conductivity", recipe.thermalConductivity);
            json.addProperty("transparent", recipe.transparent);
        }

        @Override
        public VatMaterial fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            BlockIngredient block = BlockIngredient.fromNetwork(buf);
            float maxPressure = buf.readFloat();
            float thermalConductivity = buf.readFloat();
            boolean transparent = buf.readBoolean();

            return new VatMaterial(id, this, block, maxPressure, thermalConductivity, transparent);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, VatMaterial recipe) {
            recipe.blockIngredient.toNetwork(buf);
            buf.writeFloat(recipe.maxPressure);
            buf.writeFloat(recipe.thermalConductivity);
            buf.writeBoolean(recipe.transparent);
        }
    }

    public static class Builder {
        private VatMaterial recipe;
        private List<BlockIngredient.Value> blockValues;
        protected List<ICondition> recipeConditions;

        public Builder(ResourceLocation id) {
            recipeConditions = new ArrayList<>();
            recipe = new VatMaterial(id, DestroyRecipeTypes.VAT_MATERIAL_PROPERTIES.getSerializer());
            blockValues = new ArrayList<>();
        }

        public Builder maxPressure(float v) {
            recipe.maxPressure = v;
            return this;
        }

        public Builder thermalConductivity(float v) {
            recipe.thermalConductivity = v;
            return this;
        }

        public Builder transparent(boolean v) {
            recipe.transparent = v;
            return this;
        }

        public Builder require(TagKey<Block> tag) {
            blockValues.add(new BlockIngredient.TagValue(tag));
            return this;
        }

        public Builder require(Block... blocks) {
            Arrays.stream(blocks).map(b -> new BlockIngredient.BlockValue(b)).forEach(blockValues::add);
            return this;
        }

        public VatMaterial build() {
            recipe.blockIngredient = BlockIngredient.fromValues(blockValues.stream());
            return recipe;
        }

        public void build(Consumer<FinishedRecipe> consumer) {
            consumer.accept(new DataGenResult(build(), recipeConditions));
        }

        public static class DataGenResult implements FinishedRecipe {
            private VatMaterial recipe;
            private List<ICondition> recipeConditions;
            private ResourceLocation id;
            private Serializer serializer;

            public DataGenResult(VatMaterial recipe, List<ICondition> recipeConditions) {
                this.recipeConditions = recipeConditions;
                this.recipe = recipe;
                this.id = ResourceLocation.fromNamespaceAndPath(recipe.getId().getNamespace(),
                    DestroyRecipeTypes.VAT_MATERIAL_PROPERTIES.getId().getPath() + "/" + recipe.getId().getPath());
                this.serializer = (Serializer)recipe.getSerializer();
            }

            @Override
            public void serializeRecipeData(JsonObject json) {
                serializer.toJson(json, recipe);
                if (recipeConditions.isEmpty())
                    return;

                JsonArray conds = new JsonArray();
                recipeConditions.forEach(c -> conds.add(CraftingHelper.serialize(c)));
                json.add("conditions", conds);
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return serializer;
            }

            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        }
    }
}
