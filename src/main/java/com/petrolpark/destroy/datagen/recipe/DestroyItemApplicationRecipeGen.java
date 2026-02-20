package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyItems;
import com.simibubi.create.api.data.recipe.ItemApplicationRecipeGen;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class DestroyItemApplicationRecipeGen extends ItemApplicationRecipeGen {

    GeneratedRecipe
    PLYWOOD = create("plywood", b -> b.require(DestroyBlocks.UNVARNISHED_PLYWOOD).require(DestroyItems.POLYURETHANE).output(DestroyBlocks.PLYWOOD)),
    TEAR_BOTTLE = create("tear_bottle", b -> b.require(Blocks.CRYING_OBSIDIAN).require(Items.GLASS_BOTTLE).output(Blocks.OBSIDIAN).output(DestroyItems.TEAR_BOTTLE))
    ;

    public DestroyItemApplicationRecipeGen(PackOutput output) {
        super(output, Destroy.MOD_ID);
    }
}
