package com.petrolpark.destroy.mixin.compat.jei;

import org.spongepowered.asm.mixin.Mixin;

import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.content.processing.discstamping.DiscStamperItem;
import com.petrolpark.destroy.content.processing.discstamping.DiscStampingRecipe;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.DeployingCategory;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;

import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

@Mixin(DeployingCategory.class)
public abstract class DeployingCategoryMixin extends CreateRecipeCategory<DeployerApplicationRecipe> {

    public DeployingCategoryMixin(Info<DeployerApplicationRecipe> info) {
        super(info); // Never called
    };

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        super.registerRecipes(registration);
        registration.addRecipes(getRecipeType(), ForgeRegistries.ITEMS.tags().getTag(ItemTags.MUSIC_DISCS).stream().filter(item -> !item.equals(DestroyItems.BLANK_MUSIC_DISC.get())).map(item -> DiscStampingRecipe.create(DiscStamperItem.of(new ItemStack(item)))).toList());
    };
};
