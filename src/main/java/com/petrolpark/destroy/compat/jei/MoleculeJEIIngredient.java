package com.petrolpark.destroy.compat.jei;

import java.util.List;

import javax.annotation.Nullable;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.chemistry.legacy.LegacyMixture;
import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;
import com.petrolpark.destroy.chemistry.legacy.ReadOnlyMixture;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.minecraft.MixtureFluid;
import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.core.chemistry.MoleculeDisplayItem;
import com.petrolpark.destroy.core.chemistry.MoleculeDisplayItem.MoleculeTooltip;
import com.petrolpark.destroy.core.chemistry.storage.testtube.TestTubeItem;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

import java.util.Collections;

public class MoleculeJEIIngredient {

    private static final ItemStack illegalFish;
    static {
        illegalFish = new ItemStack(Items.COD);
        illegalFish.setHoverName(Component.literal("Impossible Fish"));
    };

    public static final IIngredientType<LegacySpecies> TYPE = new IIngredientType<LegacySpecies>() {

        @Override
        public Class<? extends LegacySpecies> getIngredientClass() {
            return LegacySpecies.class;
        };

    };

    public static final IIngredientHelper<LegacySpecies> HELPER = new IIngredientHelper<LegacySpecies>() {

        @Override
        public IIngredientType<LegacySpecies> getIngredientType() {
            return TYPE;
        };

        @Override
        public String getDisplayName(LegacySpecies ingredient) {
            return ingredient.getName(false).getString()+ingredient.getName(true).getString();
        };

        @Override
        public String getUniqueId(LegacySpecies ingredient, UidContext context) {
            return ingredient.getFullID();
        };

        @Override
        public ResourceLocation getResourceLocation(LegacySpecies ingredient) {
            if (ingredient.isNovel()) return Destroy.asResource("novel_molecule");
            return new ResourceLocation(ingredient.getFullID());
        };

        @Override
        public String getDisplayModId(LegacySpecies ingredient) {
            if (ingredient.isNovel()) return "Destroy";
            return IIngredientHelper.super.getDisplayModId(ingredient);
        };

        @Override
        public LegacySpecies copyIngredient(LegacySpecies ingredient) {
            return ingredient; // There should be no need to copy Molecules as they cannot be modified
        };

        @Override
        public String getErrorInfo(@Nullable LegacySpecies ingredient) {
            return ingredient == null ? "Molecule ingredient is null" : "Something is wrong with: " + ingredient.getFullID();
        };

        @Override
        public ItemStack getCheatItemStack(LegacySpecies ingredient) {
            if (ingredient.isHypothetical() || ingredient == DestroyMolecules.PROTON) return illegalFish;
            ReadOnlyMixture mixture;
            if (ingredient.getBoilingPoint() > 273f) { // Liquids at RTP 
                mixture = LegacyMixture.pure(ingredient);
            } else { // Gases at RTP
                mixture = new ReadOnlyMixture();
                mixture.addMolecule(ingredient, (float)DestroyFluids.AIR_MOLAR_DENSITY);
            };
            return DestroyItems.TEST_TUBE.get().of(MixtureFluid.of(TestTubeItem.CAPACITY, mixture, ""));
        };
    };

    public static final IIngredientRenderer<LegacySpecies> RENDERER = new IIngredientRenderer<LegacySpecies>() {

        @Override
        public void render(GuiGraphics graphics, LegacySpecies ingredient) {
            graphics.renderItem(MoleculeDisplayItem.with(ingredient), 0, 0); // TODO check positioning
        };

        @Override
        public void getTooltip(ITooltipBuilder tooltip, LegacySpecies ingredient, TooltipFlag tooltipFlag) {
            tooltip.add(ingredient.getName(DestroyAllConfigs.CLIENT.chemistry.iupacNames.get()));
            tooltip.add(new MoleculeTooltip(ingredient));
            tooltip.addAll(MoleculeDisplayItem.getLore(ingredient));
        }

        @Override
        @Deprecated
        public List<Component> getTooltip(LegacySpecies ingredient, TooltipFlag tooltipFlag) {
            return Collections.emptyList();
        };

    };
};
