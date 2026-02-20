package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.DestroyTags;
import com.petrolpark.destroy.chemistry.legacy.LegacyMixture;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.minecraft.MixtureFluid;
import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.datagen.recipe.DestroyRecipeProvider.I;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import java.util.Map;

public class DestroyAgeingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe
    CHORUS_WINE = create("chorus_wine", b -> b
        .require(Fluids.WATER, 1000)
        .require(Items.POPPED_CHORUS_FRUIT)
        .require(DestroyTags.Items.YEAST.tag)
        .output(DestroyFluids.CHORUS_WINE.get(), 1000)
        .duration(12000)
    ),
    MOONSHINE = create("moonshine", b -> b
        .withCondition(I.configIsTrue(Destroy.MOD_ID, DestroyAllConfigs.COMMON.enableAlcohol))
        .require(Fluids.WATER, 1000)
        .require(Items.WHEAT)
        .require(DestroyTags.Items.YEAST.tag)
        .output(DestroyFluids.UNDISTILLED_MOONSHINE.get(), 1000)
        .duration(9600)
    ),
    VODKA = create("vodka", b -> b
        .withCondition(DestroyRecipeProvider.configBoolean(Destroy.MOD_ID, DestroyAllConfigs.COMMON.enableAlcohol))
        .require(Fluids.WATER, 1000)
        .require(DestroyItems.MASHED_POTATO.get())
        .require(DestroyTags.Items.YEAST.tag)
        .output(MixtureFluid.of(1000,
            LegacyMixture.mix(Map.of(
                LegacyMixture.pure(DestroyMolecules.ETHANOL), 400.0,
                LegacyMixture.pure(DestroyMolecules.WATER), 600.0
            )), "fluid.destroy.vodka"))
        .duration(9600)
    );

    public DestroyAgeingRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DestroyRecipeTypes.AGING;
    }
}
