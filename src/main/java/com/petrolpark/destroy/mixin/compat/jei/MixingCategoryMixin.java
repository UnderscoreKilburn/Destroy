package com.petrolpark.destroy.mixin.compat.jei;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.petrolpark.destroy.compat.jei.animation.AnimatedCooler;
import com.petrolpark.destroy.content.processing.centrifuge.potion.PotionFluidMixingRecipes;
import com.petrolpark.destroy.content.processing.cooler.CoolerBlockEntity.ColdnessLevel;
import com.simibubi.create.Create;
import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.compat.jei.category.MixingCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedMixer;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.infrastructure.config.AllConfigs;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.gui.GuiGraphics;

@Mixin(MixingCategory.class)
public abstract class MixingCategoryMixin extends BasinCategory {

    public MixingCategoryMixin(Info<BasinRecipe> info, boolean needsHeating) {
        super(info, needsHeating); // Never called
    };

    private static final AnimatedMixer newMixer = new AnimatedMixer();
    private static final AnimatedCooler cooler = new AnimatedCooler();

    /**
     * Injection into {@link com.simibubi.create.compat.jei.category.MixingCategory#draw MixingCategory}.
     * This renders the Cooler instead of the Blaze Burner and sets the text, if required.
     */
    @Inject(
        method = "Lcom/simibubi/create/compat/jei/category/MixingCategory;draw(Lcom/simibubi/create/content/processing/basin/BasinRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V",
        at = @At(
            value = "INVOKE",
            target = "Lcom/simibubi/create/content/processing/basin/BasinRecipe;getRequiredHeat()Lcom/simibubi/create/content/processing/recipe/HeatCondition;"
        ),
        cancellable = true,
        remap = false
    )
    private void inDraw(BasinRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY, CallbackInfo ci) {
        if ("COOLED".equals(recipe.getRequiredHeat().name())) {
            cooler.withColdness(ColdnessLevel.FROSTING)
                .draw(graphics, 177 / 2 + 3, 55); // I have replaced the dynamic access getBackground() with just a constant hopefully that shouldn't matter too much
            newMixer.draw(graphics, 177 / 2 + 3, 34); // We also need to render the Press here seeing as that gets cancelled
            ci.cancel();
        };
    };

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        super.registerRecipes(registration);
        if (AllConfigs.server().recipes.allowBrewingInMixer.get() && getRecipeType().getUid().equals(Create.asResource("automatic_brewing"))) {
            registration.addRecipes(getRecipeType(), PotionFluidMixingRecipes.ALL.stream().map(recipe -> (BasinRecipe)recipe).toList());
        };
    };
};
