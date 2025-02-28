package com.petrolpark.destroy.chemistry.legacy.reactionresult;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.petrolpark.destroy.chemistry.legacy.LegacyReaction;
import com.petrolpark.destroy.chemistry.legacy.ReactionResult;
import com.petrolpark.destroy.core.chemistry.vat.VatControllerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;

public class PrecipitateReactionResult extends ReactionResult {
    
    private final Supplier<ItemStack> precipitate;

    public static BiFunction<Float, LegacyReaction, ReactionResult> of(Supplier<ItemStack> precipitate) {
        return (m, r) -> new PrecipitateReactionResult(m, r, precipitate);
    };

    public PrecipitateReactionResult(float moles, LegacyReaction reaction, Supplier<ItemStack> precipitate) {
        super(moles, reaction);
        this.precipitate = precipitate;
    };

    public ItemStack getPrecipitate() {
        return precipitate.get();
    };

    @Override
    public void onBasinReaction(Level level, BasinBlockEntity basin) {
        // Do nothing, this is handled in ReactionInBasinRecipe
    };

    @Override
    public void onVatReaction(Level level, VatControllerBlockEntity vatController) {
        ItemHandlerHelper.insertItemStacked(vatController.inventory, precipitate.get(), false);
    };

    @Override
    public Collection<PrecipitateReactionResult> getAllPrecipitates() {
        return Collections.singleton(this);
    };

};
