package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.core.chemistry.NamedMixtures;
import com.simibubi.create.api.data.recipe.EmptyingRecipeGen;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Supplier;

public class DestroyEmptyingRecipeGen extends EmptyingRecipeGen {

    GeneratedRecipe
    CHORUS_WINE = bottle(DestroyItems.CHORUS_WINE_BOTTLE::get, DestroyFluids.CHORUS_WINE.get(), 250),
    MOONSHINE = bottle(DestroyItems.MOONSHINE_BOTTLE::get, DestroyFluids.MOONSHINE.get(), 250),
    UNDISTILLED_MOONSHINE = bottle(DestroyItems.UNDISTILLED_MOONSHINE_BOTTLE::get, DestroyFluids.UNDISTILLED_MOONSHINE.get(), 250),
    TEAR = bottle(DestroyItems.TEAR_BOTTLE::get, NamedMixtures.BRINE.get(250)),
    URINE = bottle(DestroyItems.URINE_BOTTLE::get, DestroyFluids.URINE.get(), 250)
    ;

    protected GeneratedRecipe bottle(Supplier<ItemLike> bottle, Fluid fluid, int amount) {
        return create(bottle, b -> b.output(fluid, amount).output(Items.GLASS_BOTTLE));
    }

    protected GeneratedRecipe bottle(Supplier<ItemLike> bottle, FluidStack fluid) {
        return create(bottle, b -> b.output(fluid).output(Items.GLASS_BOTTLE));
    }

    public DestroyEmptyingRecipeGen(PackOutput output) {
        super(output, Destroy.MOD_ID);
    }
}
