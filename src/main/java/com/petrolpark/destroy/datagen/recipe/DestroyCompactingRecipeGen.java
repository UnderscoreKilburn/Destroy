package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.DestroyTags;
import com.petrolpark.destroy.datagen.recipe.DestroyRecipeProvider.I;
import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

public class DestroyCompactingRecipeGen extends CompactingRecipeGen {

    GeneratedRecipe
    APPLE_JUICE = create("apple_juice", b -> b
        .require(Fluids.WATER, 250)
        .require(Items.APPLE)
        .require(Items.SUGAR)
        .output(DestroyFluids.APPLE_JUICE.get(), 250)),
    BLANK_MUSIC_DISC = create("blank_music_disc", b -> b
        .require(DestroyItems.POLYVINYL_CHLORIDE)
        .require(Items.CHARCOAL)
        .output(DestroyItems.BLANK_MUSIC_DISC)
        .requiresHeat(HeatCondition.HEATED)),
    BLANK_MUSIC_DISC_FROM_FRAGMENTS = create("blank_music_disc_from_fragments", b -> b
        .require(Items.DISC_FRAGMENT_5)
        .require(Items.DISC_FRAGMENT_5)
        .output(DestroyItems.BLANK_MUSIC_DISC)
        .requiresHeat(HeatCondition.HEATED)),
    BOROSILICATE_GLASS = create("borosilicate_glass", b -> b
        .require(DestroyFluids.MOLTEN_BOROSILICATE_GLASS.get(), 1000)
        .output(DestroyBlocks.BOROSILICATE_GLASS)
        .requiresHeat(I.cooled())),
    BUTTER = create("butter", b -> b
        .require(DestroyFluids.CREAM.get(), 400)
        .require(DestroyFluids.SKIMMED_MILK.get(), 100)
        .output(DestroyItems.BUTTER)),
    CARBON_FIBER = create("carbon_fiber", b -> b
        .require(DestroyItems.POLYACRYLONITRILE)
        .require(DestroyFluids.SKIMMED_MILK.get(), 100)
        .output(DestroyItems.CARBON_FIBER)
        .requiresHeat(HeatCondition.SUPERHEATED)),
    CHARCOAL_FROM_BEETROOT_ASHES = create("charcoal_from_beetroot_ashes", b -> b
        .require(I.beetrootAshes())
        .require(I.beetrootAshes())
        .require(I.beetrootAshes())
        .require(I.beetrootAshes())
        .require(I.beetrootAshes())
        .output(Items.CHARCOAL)),
    DIAMOND = create("diamond", b -> b
        .require(DestroyItems.NANODIAMONDS)
        .require(DestroyItems.NANODIAMONDS)
        .require(DestroyItems.NANODIAMONDS)
        .require(DestroyItems.NANODIAMONDS)
        .require(DestroyItems.NANODIAMONDS)
        .require(DestroyItems.BORAX)
        .output(Items.DIAMOND)
        .requiresHeat(HeatCondition.SUPERHEATED)),
    FIBERGLASS = create("fiberglass", b -> b
        .require(DestroyBlocks.BOROSILICATE_GLASS_FIBER)
        .require(DestroyTags.Items.PLASTICS.tag)
        .require(DestroyTags.Items.PLASTICS.tag)
        .output(DestroyBlocks.FIBERGLASS_BLOCK)
        .requiresHeat(HeatCondition.HEATED)),
    LIMESTONE = create("limestone", b -> b
        .require(I.lime())
        .require(I.lime())
        .require(I.lime())
        .require(I.lime())
        .require(I.lime())
        .output(AllPaletteStoneTypes.LIMESTONE.getBaseBlock().get())),
    PACKED_ICE = create("packed_ice", b -> b
        .require(Fluids.WATER, 1000)
        .output(Items.PACKED_ICE)
        .requiresHeat(I.cooled())),
    STAINLESS_STEEL_BLOCK = create("stainless_steel_block", b -> b
        .require(DestroyFluids.MOLTEN_STAINLESS_STEEL.get(), 1000)
        .output(DestroyBlocks.STAINLESS_STEEL_BLOCK)
        .requiresHeat(I.cooled())),
    TUFF = create("tuff", b -> b
        .require(Fluids.LAVA, 100)
        .require(I.beetrootAshes())
        .require(I.beetrootAshes())
        .require(I.beetrootAshes())
        .require(I.beetrootAshes())
        .require(I.beetrootAshes())
        .output(Items.TUFF))
    ;

    public DestroyCompactingRecipeGen(PackOutput output) {
        super(output, Destroy.MOD_ID);
    }
}
