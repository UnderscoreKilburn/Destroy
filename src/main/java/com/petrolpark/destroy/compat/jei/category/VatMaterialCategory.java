package com.petrolpark.destroy.compat.jei.category;

import java.util.List;
import java.util.Collections;

import com.petrolpark.client.rendering.PetrolparkGuiTexture;
import com.petrolpark.compat.jei.category.PetrolparkRecipeCategory;
import com.petrolpark.destroy.client.DestroyLang;
import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.core.chemistry.vat.material.VatMaterial;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.createmod.catnip.lang.FontHelper.Palette;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class VatMaterialCategory extends PetrolparkRecipeCategory<VatMaterial> {

    public static final Palette DARK_GRAY = Palette.ofColors(ChatFormatting.DARK_GRAY, ChatFormatting.DARK_GRAY);

    private final Minecraft mc;

    public VatMaterialCategory(Info<VatMaterial> info, IJeiHelpers helpers) {
        super(info, helpers);
        mc = Minecraft.getInstance();
    };

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, VatMaterial recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 2, 2)
            .setBackground(getRenderedSlot(), -1, -1)
            .addItemStacks(recipe.getBlock().getDisplayedItemStacks());
    };

    @Override
    public List<Component> getTooltipStrings(VatMaterial recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseY > textY && mouseY < textY + textSeparation * 5) {
            if (mouseY < textY + textSeparation * 2) {
                return TooltipHelper.cutStringTextComponent(DestroyLang.translate("tooltip.vat_material.pressure.description").string(), DestroyLang.WHITE_AND_WHITE);
            } else if (mouseY < textY + textSeparation * 4) {
                return TooltipHelper.cutStringTextComponent(DestroyLang.translate("tooltip.vat_material.conductivity.description").string(), DestroyLang.WHITE_AND_WHITE);
            } else {
                return TooltipHelper.cutStringTextComponent(DestroyLang.translate("tooltip.vat_material.transparent.description").string(), DestroyLang.WHITE_AND_WHITE);
            }
        };
        return Collections.emptyList();
    };

    private final int textX = 2;
    private final int textY = 28;
    private final int textSeparation = 11;

    @Override
    public void draw(VatMaterial recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.drawString(mc.font,recipeSlotsView.getSlotViews(RecipeIngredientRole.INPUT).get(0).getDisplayedItemStack().get().getHoverName(), 24, 7, 0xFFFFFF);
        PetrolparkGuiTexture.JEI_LINE.render(guiGraphics, 2, 22);
        guiGraphics.drawString(mc.font, DestroyLang.vatMaterialMaxPressure(recipe, Palette.GRAY_AND_WHITE), textX, textY, 0xFFFFFF);
        guiGraphics.drawString(mc.font, DestroyLang.vatMaterialConductivity(recipe, Palette.GRAY_AND_WHITE), textX, textY + textSeparation * 2, 0xFFFFFF);
        guiGraphics.drawString(mc.font, DestroyLang.vatMaterialTransparent(recipe, Palette.GRAY_AND_WHITE), textX, textY + textSeparation * 4, 0xFFFFFF);
        if (DestroyAllConfigs.CLIENT.chemistry.nerdMode.get()) {
            guiGraphics.drawString(mc.font, DestroyLang.translate("tooltip.vat_material.pressure.nerd_mode", recipe.getMaxPressure() / 1000f).style(ChatFormatting.WHITE).component(), textX, textY + textSeparation, 0xFFFFFF);
            guiGraphics.drawString(mc.font, DestroyLang.translate("tooltip.vat_material.conductivity.nerd_mode", recipe.getThermalConductivity()).style(ChatFormatting.WHITE).component(), textX, textY + textSeparation * 3, 0xFFFFFF);
        };
    };

    /*
    public static List<VatMaterialPropertiesRecipe> getAllRecipes()  {

        return VatMaterial.BLOCK_MATERIALS.entrySet().stream().filter(entry -> entry.getKey().getDisplayedItemStacks().size() != 0 && !entry.getKey().getDisplayedItemStacks().stream().anyMatch(DestroyBlocks.VAT_CONTROLLER::isIn)).map(entry -> new VatMaterialRecipe(entry.getValue(), entry.getKey())).toList();
    };

    public static class VatMaterialRecipe extends ShapelessRecipe {

        private static int id = 0;

        public final BlockIngredient<?> blockIngredient;
        public final VatMaterial material;

        public VatMaterialRecipe(VatMaterial material, BlockIngredient<?> blockIngredient) {
            super(Destroy.asResource("vat_material_"+id++), "", CraftingBookCategory.MISC, ItemStack.EMPTY, NonNullList.create());
            this.blockIngredient = blockIngredient;
            this.material = material;
        };

    };

     */
};
