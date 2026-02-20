package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.DestroyTags;
import com.petrolpark.destroy.chemistry.legacy.LegacyMixture;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.minecraft.MixtureFluid;
import com.petrolpark.destroy.content.processing.distillation.DistillationRecipe;
import com.petrolpark.destroy.core.chemistry.NamedMixtures;
import com.petrolpark.destroy.core.data.recipe.ExtendedProcessingRecipeBuilder;
import com.petrolpark.destroy.core.data.recipe.ExtendedProcessingRecipeGen;
import com.petrolpark.recipe.advancedprocessing.IBiomeSpecificProcessingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;

import java.util.HashSet;
import java.util.Set;

public class DestroyDistillationRecipeGen extends ExtendedProcessingRecipeGen<DistillationRecipe, DestroyDistillationRecipeGen.Builder> {

    GeneratedRecipe
    BORAX = create("borax", b -> b
        .requiresBiome(Tags.Biomes.IS_DESERT)
        .requiresBiome(BiomeTags.IS_BADLANDS)
        .require(Fluids.WATER, 1000)
        .output(NamedMixtures.BORAX_SOLUTION.get(500))
        .output(NamedMixtures.DISTILLED_WATER.get(500))
        .requiresHeat(HeatCondition.HEATED)),
    BRINE = create("brine", b -> b
        .requiresBiome(BiomeTags.IS_BEACH)
        .requiresBiome(BiomeTags.IS_OCEAN)
        .require(Fluids.WATER, 1000)
        .output(NamedMixtures.BRINE.get(500))
        .output(NamedMixtures.DISTILLED_WATER.get(500))
        .requiresHeat(HeatCondition.HEATED)),
    CINNABAR = create("cinnabar", b -> b
        .require(DestroyFluids.MOLTEN_CINNABAR.get(), 1000)
        .output(MixtureFluid.of(516, LegacyMixture.pure(DestroyMolecules.MERCURY)))
        .output(NamedMixtures.MOLTEN_SULFUR.get(614))
        .requiresHeat(HeatCondition.SUPERHEATED)),
    CRUDE_OIL = create("crude_oil", b -> b
        .require(DestroyTags.Fluids.CRUDE_OIL.tag, 100)
        .output(NamedMixtures.MOLTEN_SULFUR.get(9))
        .output(NamedMixtures.MIXED_XYLENES.get(24))
        .output(MixtureFluid.of(14, LegacyMixture.pure(DestroyMolecules.TOLUENE)))
        .output(MixtureFluid.of(53, LegacyMixture.pure(DestroyMolecules.BENZENE)))
        .output(NamedMixtures.REFINERY_GAS.get(1000))
        .requiresHeat(HeatCondition.HEATED)),
    ETHANOL_FROM_MOONSHINE = create("ethanol_from_moonshine", b -> b
        .require(DestroyFluids.MOONSHINE.get(), 400)
        .output(Fluids.WATER, 200)
        .output(NamedMixtures.ETHANOL_DISTILLATE.get(200))
        .requiresHeat(HeatCondition.HEATED)),
    MOONSHINE = create("moonshine", b -> b
        .require(DestroyFluids.UNDISTILLED_MOONSHINE.get(), 1000)
        .output(Fluids.WATER, 200)
        .output(DestroyFluids.MOONSHINE.get(), 800)
        .requiresHeat(HeatCondition.HEATED)),
    URINE = create("urine", b -> b
        .require(DestroyFluids.URINE.get(), 500)
        .output(NamedMixtures.AMMONIA_SOLUTION.get(500))
        .requiresHeat(HeatCondition.HEATED))
    ;

    public DestroyDistillationRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DestroyRecipeTypes.DISTILLATION;
    }

    @Override
    protected Builder builder(ProcessingRecipeBuilder.ProcessingRecipeFactory<DistillationRecipe> factory, ResourceLocation path) {
        return new Builder(factory, path);
    }

    public class Builder extends ExtendedProcessingRecipeBuilder<DistillationRecipe, Builder> {
        private Set<IBiomeSpecificProcessingRecipe.BiomeValue> biomes = new HashSet<>();

        public Builder(ProcessingRecipeBuilder.ProcessingRecipeFactory<DistillationRecipe> factory, ResourceLocation recipeId) {
            super(factory, recipeId);
        }

        public Builder requiresBiome(ResourceLocation biomeId) {
            biomes.add(new IBiomeSpecificProcessingRecipe.SingleBiomeValue(biomeId));
            return this;
        }

        public Builder requiresBiome(TagKey<Biome> tag) {
            biomes.add(new IBiomeSpecificProcessingRecipe.TagBiomeValue(tag));
            return this;
        }

        @Override
        public void build(DistillationRecipe recipe) {
            recipe.setAllowedBiomes(biomes);
        }
    }
}
