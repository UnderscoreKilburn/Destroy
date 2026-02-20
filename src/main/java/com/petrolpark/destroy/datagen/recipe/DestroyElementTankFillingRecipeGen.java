package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.core.recipe.ingredient.fluid.PureSpeciesFluidIngredient;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;

public class DestroyElementTankFillingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe
    CHLORINE = element(DestroyMolecules.CHLORINE, DestroyBlocks.CHLORINE_PERIODIC_TABLE_BLOCK),
    HYDROGEN = element(DestroyMolecules.HYDROGEN, DestroyBlocks.HYDROGEN_PERIODIC_TABLE_BLOCK),
    MERCURY = element(DestroyMolecules.MERCURY, DestroyBlocks.MERCURY_PERIODIC_TABLE_BLOCK),
    NITROGEN = element(DestroyMolecules.NITROGEN, DestroyBlocks.NITROGEN_PERIODIC_TABLE_BLOCK),
    OXYGEN = element(DestroyMolecules.OXYGEN, DestroyBlocks.OXYGEN_PERIODIC_TABLE_BLOCK)
    ;

    public GeneratedRecipe element(LegacySpecies species, ItemLike item) {
        return create(CatnipServices.REGISTRIES.getKeyOrThrow(item.asItem()).getPath(), b -> b.require(PureSpeciesFluidIngredient.of(species, 1000)).output(item));
    }

    public DestroyElementTankFillingRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DestroyRecipeTypes.ELEMENT_TANK_FILLING;
    }
}
