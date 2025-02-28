package com.petrolpark.destroy.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.content.processing.centrifuge.potion.PotionFluidMixingRecipes;
import com.petrolpark.destroy.core.chemistry.basinreaction.ReactionInBasinRecipe;
import com.petrolpark.destroy.mixin.accessor.BasinOperatingBlockEntityAccessor;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.infrastructure.config.AllConfigs;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

@Mixin(MechanicalMixerBlockEntity.class)
public class MechanicalMixerBlockEntityMixin {
    
    /**
     * Injection into {@link com.simibubi.create.content.contraptions.components.mixer.MechanicalMixerBlockEntity Mechanical Mixers}
     * to allow them to recognise Mixtures that are able to React, and also stir up Potions.
     * @see com.petrolpark.destroy.core.chemistry.basinreaction.ReactionInBasinRecipe Reactions in Basins
     */
    @Inject(
        method = "getMatchingRecipes()Ljava/util/List;",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lcom/simibubi/create/content/processing/basin/BasinOperatingBlockEntity;getMatchingRecipes()Ljava/util/List;"
        ),
        cancellable = true,
        remap = false,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void inGetMatchingRecipes(CallbackInfoReturnable<List<Recipe<?>>> ci, List<Recipe<?>> matchingRecipes) {

        ((BasinOperatingBlockEntityAccessor)this).invokeGetBasin().ifPresent(basin -> {

            if (!basin.hasLevel()) return;

            IFluidHandler fluidHandler = basin.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
            IItemHandler itemHandler = basin.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
            if (fluidHandler == null || itemHandler == null) return;

            boolean containsOnlyMixtures = true;
            List<ItemStack> availableItemStacks = new ArrayList<>();
            List<FluidStack> availableFluidStacks = new ArrayList<>(); // All (Mixture) Fluid Stacks in this Basin

            for (int tank = 0; tank < fluidHandler.getTanks(); tank++) {
                FluidStack fluidStack = fluidHandler.getFluidInTank(tank);
                if (DestroyFluids.isMixture(fluidStack)) {
                    availableFluidStacks.add(fluidStack);
                } else if (!fluidStack.isEmpty()) {
                    containsOnlyMixtures = false;
                };
            };

            if (containsOnlyMixtures) { // If there are Fluids other than Mixtures, don't bother Reacting
                if (availableFluidStacks.size() <= 0) return; // If there are no Mixtures, don't bother Reacting
                    
                for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
                    availableItemStacks.add(itemHandler.getStackInSlot(slot));
                };
                    
                // Only return this if there is definitely a Reaction possible
                ReactionInBasinRecipe recipe = ReactionInBasinRecipe.create(availableFluidStacks, availableItemStacks, basin);
                if (!(recipe == null) && BasinRecipe.match(basin, recipe)) matchingRecipes.add(recipe); 
            } else if (AllConfigs.server().recipes.allowBrewingInMixer.get()) {
                matchingRecipes.addAll(PotionFluidMixingRecipes.ALL.stream().filter(recipe -> BasinRecipe.match(basin, recipe)).toList());
            };
        });
    };
    
};
