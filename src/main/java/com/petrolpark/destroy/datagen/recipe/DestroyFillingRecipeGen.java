package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.core.recipe.ingredient.fluid.MoleculeFluidIngredient;
import com.petrolpark.destroy.datagen.recipe.DestroyRecipeProvider.I;
import com.simibubi.create.AllFluids;
import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;

import java.util.List;
import java.util.function.Supplier;

public class DestroyFillingRecipeGen extends FillingRecipeGen {

    GeneratedRecipe
    CHORUS_WINE = bottle(DestroyItems.CHORUS_WINE_BOTTLE::get, DestroyFluids.CHORUS_WINE.get(), 250),
    MOONSHINE = bottle(DestroyItems.MOONSHINE_BOTTLE::get, DestroyFluids.MOONSHINE.get(), 250),
    UNDISTILLED_MOONSHINE = bottle(DestroyItems.UNDISTILLED_MOONSHINE_BOTTLE::get, DestroyFluids.UNDISTILLED_MOONSHINE.get(), 250),
    URINE = bottle(DestroyItems.URINE_BOTTLE::get, DestroyFluids.URINE.get(), 250),

    APPLE_JUICE = carton(DestroyItems.APPLE_JUICE_CARTON::get, DestroyFluids.APPLE_JUICE.get(), 250),
    MILK = carton(DestroyItems.MILK_CARTON::get, FluidIngredient.fromTag(Tags.Fluids.MILK, 250)),

    ASPIRIN = syringe(DestroyItems.ASPIRIN_SYRINGE::get, MoleculeFluidIngredient.of(DestroyMolecules.ASPIRIN, 1.f, 1)),
    // originally took 2 mB of methyl salicylate, changed it to 1 so we can say syringes universally hold 1 mB of fluid
    // if we want to add more of them later
    BABY_BLUE = syringe(DestroyItems.BABY_BLUE_SYRINGE::get, MoleculeFluidIngredient.of(DestroyMolecules.METHYL_SALICYLATE, 5.f, 1)),
    CISPLATIN = syringe(DestroyItems.CISPLATIN_SYRINGE::get, MoleculeFluidIngredient.of(DestroyMolecules.CISPLATIN, 1.f, 1)),

    PERFUME = sprayBottle(DestroyItems.PERFUME_BOTTLE::get, DestroyFluids.PERFUME.get(), 250),
    SUNSCREEN = sprayBottle(DestroyItems.SUNSCREEN_BOTTLE::get, MoleculeFluidIngredient.of(DestroyMolecules.SALICYLIC_ACID, 1.f, 2)),

    BOMB_BON = create("bomb_bon", b -> b.require(DestroyItems.EMPTY_BOMB_BON).require(DestroyFluids.MOONSHINE.get(), 200).output(DestroyItems.BOMB_BON)
        .withCondition(I.configIsTrue(Destroy.MOD_ID, DestroyAllConfigs.COMMON.enableAlcohol))),
    BOMB_BON_ALCOHOL_FREE = create("bomb_bon_alcohol_free", b -> b.require(DestroyItems.EMPTY_BOMB_BON).require(AllFluids.CHOCOLATE.get(), 200).output(DestroyItems.BOMB_BON)
        .withCondition(I.configIsFalse(Destroy.MOD_ID, DestroyAllConfigs.COMMON.enableAlcohol))),

    DYNAMITE = create("dynamite", b -> b
        .require(DestroyBlocks.CLAY_MONOLITH)
        .require(MoleculeFluidIngredient.of(DestroyMolecules.NITROGLYCERINE, 7.f, 2))
        .output(DestroyItems.DYNAMITE)),
    PICRIC_ACID = create("picric_acid", b -> b
        .require(Items.SAND)
        .require(MoleculeFluidIngredient.of(DestroyMolecules.PICRIC_ACID, 7.7f, 2))
        .output(DestroyItems.PICRIC_ACID_TABLET)),
    TNT = create("tnt", b -> b
        .require(Items.SAND)
        .require(MoleculeFluidIngredient.of(DestroyMolecules.TRINITROTOLUENE, 7.3f, 2))
        .output(DestroyItems.TNT_TABLET)),

    CB_OX = oxidizationChain(List.of(() -> Blocks.COPPER_BLOCK, () -> Blocks.EXPOSED_COPPER, () -> Blocks.WEATHERED_COPPER, () -> Blocks.OXIDIZED_COPPER)),
    CCB_OX = oxidizationChain(List.of(() -> Blocks.CUT_COPPER, () -> Blocks.EXPOSED_CUT_COPPER, () -> Blocks.WEATHERED_CUT_COPPER, () -> Blocks.OXIDIZED_CUT_COPPER)),
    CCST_OX = oxidizationChain(List.of(() -> Blocks.CUT_COPPER_STAIRS, () -> Blocks.EXPOSED_CUT_COPPER_STAIRS, () -> Blocks.WEATHERED_CUT_COPPER_STAIRS, () -> Blocks.OXIDIZED_CUT_COPPER_STAIRS)),
    CCS_OX = oxidizationChain(List.of(() -> Blocks.CUT_COPPER_SLAB, () -> Blocks.EXPOSED_CUT_COPPER_SLAB, () -> Blocks.WEATHERED_CUT_COPPER_SLAB, () -> Blocks.OXIDIZED_CUT_COPPER_SLAB));

    ;

    protected GeneratedRecipe bottle(Supplier<ItemLike> output, Fluid fluid, int amount) {
        return bottle(output, FluidIngredient.fromFluid(fluid, amount));
    }
    protected GeneratedRecipe sprayBottle(Supplier<ItemLike> output, Fluid fluid, int amount) {
        return sprayBottle(output, FluidIngredient.fromFluid(fluid, amount));
    }
    protected GeneratedRecipe carton(Supplier<ItemLike> output, Fluid fluid, int amount) {
        return carton(output, FluidIngredient.fromFluid(fluid, amount));
    }

    protected GeneratedRecipe bottle(Supplier<ItemLike> output, FluidIngredient fluid) {
        return bottle(() -> Items.GLASS_BOTTLE, output, fluid);
    }
    protected GeneratedRecipe sprayBottle(Supplier<ItemLike> output, FluidIngredient fluid) {
        return bottle(DestroyItems.SPRAY_BOTTLE::get, output, fluid);
    }
    protected GeneratedRecipe carton(Supplier<ItemLike> output, FluidIngredient fluid) {
        return bottle(DestroyItems.EMPTY_CARTON::get, output, fluid);
    }
    protected GeneratedRecipe syringe(Supplier<ItemLike> output, FluidIngredient fluid) {
        return bottle(DestroyItems.SYRINGE::get, output, fluid);
    }

    protected GeneratedRecipe bottle(Supplier<ItemLike> bottle, Supplier<ItemLike> output, FluidIngredient fluid) {
        return create(CatnipServices.REGISTRIES.getKeyOrThrow(output.get().asItem()), b -> {
            b.require(bottle.get()).require(fluid).output(output.get());

            // ehh
            // why does this have to be a config anyway?
            // people can just datapack/kubejs the recipe out if they don't want fake meth for whatever reason
            if(output.get().asItem() == DestroyItems.BABY_BLUE_SYRINGE.get())
                b.withCondition(DestroyRecipeProvider.configBoolean(Destroy.MOD_ID, DestroyAllConfigs.COMMON.enableBabyBlue));

            return b;
        });
    }

    public GeneratedRecipe oxidizationChain(List<Supplier<ItemLike>> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            Supplier<ItemLike> from = chain.get(i);
            Supplier<ItemLike> to = chain.get(i + 1);
            create("oxidation/" + CatnipServices.REGISTRIES.getKeyOrThrow(to.get().asItem()).getPath(), b -> b
                .require(from.get())
                .require(MoleculeFluidIngredient.of(DestroyMolecules.ACETIC_ACID, 1.f, 5))
                .output(to.get()));
        }
        return null;
    }


    public DestroyFillingRecipeGen(PackOutput output) {
        super(output, Destroy.MOD_ID);
    }
}
