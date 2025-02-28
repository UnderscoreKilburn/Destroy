package com.petrolpark.destroy.compat.jei.category;

import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.compat.jei.animation.AnimatedDynamo;
import com.petrolpark.destroy.content.processing.discstamping.DiscElectroplatingRecipe;
import com.simibubi.create.compat.jei.CreateJEI;
import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.content.processing.basin.BasinRecipe;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ElectrolysisCategory extends BasinCategory {

    private final AnimatedDynamo dynamo;

    protected final RecipeType<BasinRecipe> type;

    public ElectrolysisCategory(Info<BasinRecipe> info) {
        super(info, false);
        dynamo = new AnimatedDynamo(true, false);
        type = info.recipeType();
    };

    @Override
    public void draw(BasinRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, graphics, mouseX, mouseY);
        dynamo.draw(graphics, getBackground().getWidth() / 2 + 3, 34);
    };

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        super.registerRecipes(registration);
        CreateJEI.<DiscElectroplatingRecipe>consumeTypedRecipes(recipe -> {
            if (recipe.original) registration.addRecipes(type, ForgeRegistries.ITEMS.tags().getTag(ItemTags.MUSIC_DISCS).stream().filter(item -> !item.equals(DestroyItems.BLANK_MUSIC_DISC.get())).map(
                item -> recipe.copyWithDisc(new ItemStack(item))
            ).toList());
        }, DestroyRecipeTypes.DISC_ELECTROPLATING.getType());
    };
    
};
