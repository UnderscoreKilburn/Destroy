package com.petrolpark.destroy.content.processing.centrifuge.potion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyFluids;
import com.simibubi.create.content.fluids.potion.PotionFluid.BottleType;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionFluidMixingRecipes {

    static List<Item> VANILLA_CONTAINERS = List.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION);

    /**
     * The Fluid equivalents of Potion Items, in order of priority for which one gets made when Centrifuging
     */
    static Map<Item, FluidStack> FLUID_EQUIVALENTS = createFluidEquivalents();

    public static final List<MixingRecipe> ALL = createRecipes();

    public static Map<Item, FluidStack> createFluidEquivalents() {
        return Map.of(
            Items.DRAGON_BREATH, new FluidStack(DestroyFluids.LINGERING_POTION.getSource(), 100),
            Items.GUNPOWDER, new FluidStack(DestroyFluids.SPLASH_POTION.getSource(), 100),
            Items.REDSTONE, new FluidStack(DestroyFluids.LONG_POTION.getSource(), 100),
            Items.GLOWSTONE_DUST, new FluidStack(DestroyFluids.STRONG_POTION.getSource(), 100),
            Items.FERMENTED_SPIDER_EYE, new FluidStack(DestroyFluids.CORRUPTING_POTION.getSource(), 100)
        );
    };

    private static List<MixingRecipe> createRecipes() {
		List<MixingRecipe> mixingRecipes = new ArrayList<>();

		int recipeIndex = 0;

		for (Item container : VANILLA_CONTAINERS) {
			BottleType bottleType = PotionFluidHandler.bottleTypeFromItem(container);
			for (PotionBrewing.Mix<Potion> mix : PotionBrewing.POTION_MIXES) {
                for (Entry<Item, FluidStack> entry : FLUID_EQUIVALENTS.entrySet()) {
                    if (mix.ingredient.test(new ItemStack(entry.getKey()))) {
                        FluidStack fromFluid = PotionFluidHandler.getFluidFromPotion(mix.from.get(), bottleType, 1000);
                        FluidStack toFluid = PotionFluidHandler.getFluidFromPotion(mix.to.get(), bottleType, 1000);
                        mixingRecipes.add(createRecipe("potion_mixing_with_fluid_" + recipeIndex++, entry.getValue(), fromFluid, toFluid));
                    };
                };
			};
		};

		for (PotionBrewing.Mix<Item> mix : PotionBrewing.CONTAINER_MIXES) {
			Item from = mix.from.get();
			if (!VANILLA_CONTAINERS.contains(from)) continue;

			Item to = mix.to.get();
			BottleType fromBottleType = PotionFluidHandler.bottleTypeFromItem(from);
			BottleType toBottleType = PotionFluidHandler.bottleTypeFromItem(to);

            for (Entry<Item, FluidStack> entry : FLUID_EQUIVALENTS.entrySet()) {
                if (mix.ingredient.test(new ItemStack(entry.getKey()))) {
                    addEachPotion: for (Potion potion : ForgeRegistries.POTIONS.getValues()) {
                        if (potion == Potions.EMPTY) continue addEachPotion;
        
                        FluidStack fromFluid = PotionFluidHandler.getFluidFromPotion(potion, fromBottleType, 1000);
                        FluidStack toFluid = PotionFluidHandler.getFluidFromPotion(potion, toBottleType, 1000);
        
                        mixingRecipes.add(createRecipe("potion_mixing_with_fluid_" + recipeIndex++, entry.getValue(), fromFluid, toFluid));
                    };
                };
            };
		};

		for (IBrewingRecipe recipe : BrewingRecipeRegistry.getRecipes()) {
			if (recipe instanceof BrewingRecipe brewingRecipe) {
				ItemStack output = brewingRecipe.getOutput();
				if (!VANILLA_CONTAINERS.contains(output.getItem())) continue;

				Ingredient input = brewingRecipe.getInput();
				Ingredient ingredient = brewingRecipe.getIngredient();
				FluidStack outputFluid = null;

                for (Item item : VANILLA_CONTAINERS) {
                    if (input.test(new ItemStack(item))) {
                        for (Entry<Item, FluidStack> entry : FLUID_EQUIVALENTS.entrySet()) {
                            ItemStack stack = new ItemStack(entry.getKey());
                            if (ingredient.test(stack)) {
                                FluidStack fromFluid = PotionFluidHandler.getFluidFromPotionItem(stack);
                                if (outputFluid == null) outputFluid = PotionFluidHandler.getFluidFromPotionItem(output);
                    
                                mixingRecipes.add(createRecipe("potion_mixing_with_fluid_" + recipeIndex++, entry.getValue(), fromFluid, outputFluid)); 
                            };
                        };
                    };
                };
			};
		};

		return mixingRecipes;
	};

	private static MixingRecipe createRecipe(String id, FluidStack itemSubstitute, FluidStack fromFluid, FluidStack toFluid) {
		return new ProcessingRecipeBuilder<>(MixingRecipe::new, Destroy.asResource(id))
                .require(FluidIngredient.fromFluidStack(itemSubstitute))
				.require(FluidIngredient.fromFluidStack(fromFluid))
				.output(toFluid)
				.requiresHeat(HeatCondition.HEATED)
				.build();
	};
};
