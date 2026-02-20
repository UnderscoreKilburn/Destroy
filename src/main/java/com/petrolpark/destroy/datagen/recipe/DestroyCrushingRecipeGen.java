package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.datagen.recipe.DestroyRecipeProvider.I;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

public class DestroyCrushingRecipeGen extends CrushingRecipeGen {

    GeneratedRecipe
    FLUORITE_ORE = stoneOre(DestroyBlocks.FLUORITE_ORE::get, DestroyItems.FLUORITE::get, 1.75f, 250),
    NICKEL_ORE = stoneOre(DestroyBlocks.NICKEL_ORE::get, DestroyItems.RAW_NICKEL::get, 1.75f, 250),

    DEEPSLATE_FLUORITE_ORE = deepslateOre(DestroyBlocks.DEEPSLATE_FLUORITE_ORE::get, DestroyItems.FLUORITE::get, 2.25f, 350),
    DEEPSLATE_NICKEL_ORE = deepslateOre(DestroyBlocks.DEEPSLATE_NICKEL_ORE::get, DestroyItems.RAW_NICKEL::get, 2.25f, 350),

    NETHER_CROCOITE_ORE = netherOre(DestroyBlocks.NETHER_CROCOITE_ORE::get, DestroyItems.NETHER_CROCOITE::get, 7.25f, 350),

    END_FLUORITE_ORE = endOre(DestroyBlocks.END_FLUORITE_ORE::get, DestroyItems.FLUORITE::get, 2.25f, 350),

    COPPER = metalPowder("copper", DestroyItems.COPPER_POWDER::get, true),
    IRON = metalPowder("iron", DestroyItems.IRON_POWDER::get, true),
    CHROMIUM = metalPowder("chromium", DestroyItems.CHROMIUM_POWDER::get, false),
    LEAD = metalPowder("lead", DestroyItems.LEAD_POWDER::get, false),
    PLATINUM = metalPowder("platinum", DestroyItems.PLATINUM_POWDER::get, false),
    PALLADIUM = metalPowder("palladium", DestroyItems.PALLADIUM_POWDER::get, false),
    RHODIUM = metalPowder("rhodium", DestroyItems.RHODIUM_POWDER::get, false),

    AMETHYST = create("amethyst", b -> b.duration(100).require(I.amethyst()).output(0.75f,DestroyItems.SILICA, 6)),
    CALCITE = create(() -> Items.CALCITE, b -> b.duration(225).output(DestroyItems.CHALK_DUST, 2).output(0.5f, DestroyItems.CHALK_DUST)),
    LIMESTONE = create("limestone", b -> b.duration(150).require(I.limestone()).output(DestroyItems.CHALK_DUST, 5)),

    NETHER_CROCOITE = create(DestroyItems.NETHER_CROCOITE::get, b -> b.duration(250)
        .output(0.25f, AllItems.CRUSHED_LEAD)
        .output(0.25f, DestroyItems.CRUSHED_RAW_CHROMIUM)
        .output(0.75f, AllItems.EXP_NUGGET)),

    ROSE_QUARTZ = create(AllItems.ROSE_QUARTZ::get, b -> b.duration(100)
        .output(DestroyItems.SILICA)
        .output(0.25f, DestroyItems.SILICA)
        .output(0.5f, Items.REDSTONE, 4)),
    POLISHED_ROSE_QUARTZ = create(AllItems.POLISHED_ROSE_QUARTZ::get, b -> b.duration(100)
        .output(DestroyItems.SILICA)
        .output(0.25f, DestroyItems.SILICA)
        .output(0.5f, Items.REDSTONE, 4)),

    QUARTZ = create("quartz", b -> b.duration(100).require(I.quartz()).output(DestroyItems.SILICA).output(0.25f, DestroyItems.SILICA))
    // not doing quartz blocks to silica - most mods provide a way to break quartz blocks and their variants into quartz
    ;

    protected GeneratedRecipe endOre(Supplier<ItemLike> ore, Supplier<ItemLike> raw, float expectedAmount,
                                        int duration) {
        return ore(Blocks.END_STONE, ore, raw, expectedAmount, duration);
    }

    protected GeneratedRecipe metalPowder(String name, Supplier<ItemLike> powder, boolean generateSheet) {
        if(generateSheet) create(name + "_powder_from_sheet", b -> b.duration(100).require(AllTags.forgeItemTag("plates/" + name)).output(powder.get()));
        return create(name + "_powder", b -> b.duration(100).require(AllTags.forgeItemTag("ingots/" + name)).output(powder.get()));
    }

    public DestroyCrushingRecipeGen(PackOutput output) {
        super(output, Destroy.MOD_ID);
    }
}
