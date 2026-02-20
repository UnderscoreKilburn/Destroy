package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.DestroyTags;
import com.petrolpark.destroy.datagen.recipe.DestroyRecipeProvider.I;
import com.simibubi.create.api.data.recipe.PressingRecipeGen;
import net.minecraft.data.PackOutput;

public class DestroyPressingRecipeGen extends PressingRecipeGen {

    GeneratedRecipe
    CIRCUIT_MASK_FROM_PLASTIC = create("circuit_mask_from_plastic", b -> b.require(DestroyTags.Items.RIGID_PLASTICS.tag).output(DestroyItems.CIRCUIT_MASK)),
    CIRCUIT_MASK_FROM_RUINED = create("circuit_mask_from_ruined", b -> b.require(DestroyItems.RUINED_CIRCUIT_MASK).output(DestroyItems.CIRCUIT_MASK)),
    STAINLESS_STEEL_SHEET = create("stainless_steel_sheet", b -> b.require(I.stainlessSteel()).output(DestroyItems.STAINLESS_STEEL_SHEET)),
    ZINC_SHEET = create("zinc_sheet", b -> b.require(I.zinc()).output(DestroyItems.ZINC_SHEET));

    public DestroyPressingRecipeGen(PackOutput output) {
        super(output, Destroy.MOD_ID);
    }
}
