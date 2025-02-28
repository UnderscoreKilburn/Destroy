package com.petrolpark.destroy.core.chemistry.vat.material;

import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Optional;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.petrolpark.destroy.DestroyMessages;
import com.petrolpark.recipe.ingredient.BlockIngredient;
import com.petrolpark.recipe.ingredient.BlockIngredient.BlockTagIngredient;
import com.petrolpark.recipe.ingredient.BlockIngredient.SingleBlockIngredient;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition.IContext;

public class VatMaterialResourceListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new Gson();
    private final IContext loadingContext;

    public VatMaterialResourceListener(IContext loadingContext) {
        super(GSON, "destroy_compat/vat_materials");
        this.loadingContext = loadingContext;
    };

    @Override
    @SuppressWarnings("deprecation")
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        // Remove any previously added Vat Materials
        VatMaterial.clearDatapackMaterials();

        final Map<BlockIngredient<?>, VatMaterial> datapackMaterials = new HashMap<>(object.size());

        // Add new ones
        for (Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            JsonElement json = entry.getValue();
            ResourceLocation rl = entry.getKey();
            if (!json.isJsonObject()) throw new JsonSyntaxException("Cannot read Vat Material: "+rl);
            JsonObject jobj = json.getAsJsonObject();

            // Check if this should be loaded
            if (!CraftingHelper.processConditions(jobj, "conditions", loadingContext)) return;

            // Default values
            float maxPressureDifference = 100000f;
            float conductivity = 100f;
            boolean transparent = false;

            // Pressure
            if (jobj.has("max_pressure_difference")) {
                try {
                    maxPressureDifference = jobj.get("max_pressure_difference").getAsFloat();
                    if (maxPressureDifference <= 0f) throw new IllegalArgumentException();
                } catch(Throwable e) {
                    throw new JsonSyntaxException("Vat Material "+rl+" specifies an invalid maximum pressure difference");
                };
            };

            // Conductivity
            if (jobj.has("conductivity")) {
                try {
                    conductivity = jobj.get("conductivity").getAsFloat();
                    if (conductivity <= 0f) throw new IllegalArgumentException();
                } catch(Throwable e) {
                    throw new JsonSyntaxException("Vat Material "+rl+" specifies an invalid conductivity");
                };
            };

            // Transparency
            if (jobj.has("conductivity")) {
                try {
                    transparent = jobj.get("transparent").getAsBoolean();
                } catch(Throwable e) {
                    throw new JsonSyntaxException("Vat Material "+rl+" specifies transparency");
                };
            };

            VatMaterial material = new VatMaterial(maxPressureDifference, conductivity, transparent, false);

            // Register the materials
            if (!jobj.has("blocks") || !jobj.get("blocks").isJsonArray()) throw new JsonSyntaxException("Vat Material "+rl+" must specify at least one block.");
            jobj.get("blocks").getAsJsonArray().forEach(e -> {
                try {
                    String id = e.getAsString();
                    if (id.startsWith("#")) { // Tags
                        id = id.substring(1);
                        datapackMaterials.put(new BlockTagIngredient(TagKey.create(Registries.BLOCK, new ResourceLocation(id))), material);
                    } else { // Individual blocks
                        Optional<? extends Holder<Block>> blockOptional = BuiltInRegistries.BLOCK.asLookup().get(ResourceKey.create(Registries.BLOCK, new ResourceLocation(id)));
                        if (blockOptional.isEmpty()) throw new IllegalArgumentException();
                        datapackMaterials.put(new SingleBlockIngredient(blockOptional.get().value()), material);
                    };
                } catch (Throwable error) {
                    throw new JsonSyntaxException("Vat material "+rl+" specifies an invalid block or block tag: "+e.toString());
                };
            });


        };

        // Add new materials server-side too
        VatMaterial.BLOCK_MATERIALS.putAll(datapackMaterials);

        // Send to clients, if possible
        try {DestroyMessages.sendToAllClients(new SyncVatMaterialsS2CPacket(datapackMaterials));} catch (NullPointerException e) {};
    };
    
};
