package com.petrolpark.destroy.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.petrolpark.destroy.content.processing.trypolithography.CircuitPatternItem;
import com.petrolpark.destroy.content.processing.trypolithography.recipe.CircuitSequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

@Mixin(SequencedAssemblyRecipe.class)
public abstract class SequencedAssemblyRecipeMixin implements Recipe<RecipeWrapper> {

    @Shadow
    protected ResourceLocation id;

    @Shadow
    abstract int getStep(ItemStack stack);

    @Shadow
    public abstract ItemStack rollResult();

    @Shadow
    protected List<SequencedRecipe<?>> sequence;

    @Shadow
    protected int loops;

    @Shadow
    public abstract ItemStack getTransitionalItem();
    
    /**
     * Mostly copied from {@link com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe#advance Create source code}.
     * @param input The stack being processed
     * @return The result of processing, which, unlike the default behaviour, conserves other NBTs
     */
    @Overwrite(remap = false)
    public ItemStack advance(ItemStack input) {
        int step = getStep(input);
        ItemStack result;
		if ((step + 1) / sequence.size() >= loops) {
            result = rollResult().copy();
        } else {
            result = step == 0 ? ItemHandlerHelper.copyStackWithSize(getTransitionalItem(), 1) : ItemHandlerHelper.copyStackWithSize(input, 1);
            CompoundTag itemTag = result.getOrCreateTag();
            CompoundTag tag = new CompoundTag();
            tag.putString("id", id.toString());
            tag.putInt("Step", step + 1);
            tag.putFloat("Progress", (step + 1f) / (sequence.size() * loops));
            itemTag.put("SequencedAssembly", tag);
            result.setTag(itemTag);
        };

        if (getSerializer() instanceof CircuitSequencedAssemblyRecipe.Serializer) {
            CircuitPatternItem.putPattern(result, CircuitPatternItem.getPattern(input));
        };

		return result;
    };
};
