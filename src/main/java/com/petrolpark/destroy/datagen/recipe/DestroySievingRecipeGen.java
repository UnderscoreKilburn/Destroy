package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.content.processing.sieve.SievingRecipe;
import com.petrolpark.destroy.core.data.recipe.ExtendedProcessingRecipeBuilder;
import com.petrolpark.destroy.core.data.recipe.ExtendedProcessingRecipeGen;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class DestroySievingRecipeGen extends ExtendedProcessingRecipeGen<SievingRecipe, DestroySievingRecipeGen.Builder> {

    GeneratedRecipe
    COAL_INFUSED_BEETROOT_ASHES = beetrootAshes(DestroyItems.COAL_INFUSED_BEETROOT_ASHES,
        () -> Items.COAL, () -> Items.DIAMOND, 0.1f),
    COPPER_INFUSED_BEETROOT_ASHES = beetrootAshes(DestroyItems.COPPER_INFUSED_BEETROOT_ASHES,
        AllItems.CRUSHED_COPPER::get, AllItems.CRUSHED_IRON::get, 0.1f, AllItems.CRUSHED_PLATINUM::get, 0.02f, true),
    DIAMOND_INFUSED_BEETROOT_ASHES = beetrootAshes(DestroyItems.DIAMOND_INFUSED_BEETROOT_ASHES,
        () -> Items.DIAMOND, () -> Items.COAL, 0.2f),
    EMERALD_INFUSED_BEETROOT_ASHES = beetrootAshes(DestroyItems.EMERALD_INFUSED_BEETROOT_ASHES,
        () -> Items.EMERALD, DestroyItems.SILICA::get, 0.2f, DestroyItems.CRUSHED_RAW_CHROMIUM::get, 0.2f),
    FLUORITE_INFUSED_BEETROOT_ASHES = beetrootAshes(DestroyItems.FLUORITE_INFUSED_BEETROOT_ASHES,
        DestroyItems.FLUORITE::get, DestroyItems.CHALK_DUST::get, 0.3f, () -> Items.QUARTZ, 0.1f),
    GOLD_INFUSED_BEETROOT_ASHES = beetrootAshes(DestroyItems.GOLD_INFUSED_BEETROOT_ASHES,
        AllItems.CRUSHED_GOLD::get, AllItems.CRUSHED_COPPER::get, 0.3f, DestroyItems.CRUSHED_RAW_PALLADIUM::get, 0.03f, true),
    IRON_INFUSED_BEETROOT_ASHES = beetrootAshes(DestroyItems.IRON_INFUSED_BEETROOT_ASHES,
        AllItems.CRUSHED_IRON::get, AllItems.CRUSHED_NICKEL::get, 0.2f, DestroyItems.CRUSHED_RAW_CHROMIUM::get, 0.1f),
    LAPIS_INFUSED_BEETROOT_ASHES = beetrootAshes(DestroyItems.LAPIS_INFUSED_BEETROOT_ASHES,
        () -> Items.LAPIS_LAZULI, DestroyItems.CHALK_DUST::get, 0.3f, AllItems.CRUSHED_GOLD::get, 0.1f),
    NETHER_CROCOITE_INFUSED_BEETROOT_ASHES = beetrootAshes(DestroyItems.NETHER_CROCOITE_INFUSED_BEETROOT_ASHES,
        DestroyItems.NETHER_CROCOITE::get, DestroyItems.CRUSHED_RAW_CHROMIUM::get, 0.1f, AllItems.CRUSHED_LEAD::get, 0.1f),
    NICKEL_INFUSED_BEETROOT_ASHES = beetrootAshes(DestroyItems.NICKEL_INFUSED_BEETROOT_ASHES,
        AllItems.CRUSHED_NICKEL::get, AllItems.CRUSHED_IRON::get, 0.2f, AllItems.CRUSHED_COPPER::get, 0.2f),
    QUARTZ_INFUSED_BEETROOT_ASHES = beetrootAshes(DestroyItems.QUARTZ_INFUSED_BEETROOT_ASHES,
        () -> Items.QUARTZ, DestroyItems.NETHER_CROCOITE::get, 0.3f, AllItems.CRUSHED_GOLD::get, 0.1f),
    REDSTONE_INFUSED_BEETROOT_ASHES = beetrootAshes(DestroyItems.REDSTONE_INFUSED_BEETROOT_ASHES,
        () -> Items.REDSTONE, DestroyItems.SILICA::get, 0.2f, AllItems.CRUSHED_IRON::get, 0.1f),
    ZINC_INFUSED_BEETROOT_ASHES = beetrootAshes(DestroyItems.ZINC_INFUSED_BEETROOT_ASHES,
        AllItems.CRUSHED_ZINC::get, AllItems.CRUSHED_LEAD::get, 0.1f),

    CRUSHED_COPPER = crushedOre(AllItems.CRUSHED_COPPER, AllItems.COPPER_NUGGET::get, () -> Items.CLAY_BALL, .5f),
    CRUSHED_ZINC = crushedOre(AllItems.CRUSHED_ZINC, AllItems.ZINC_NUGGET::get, () -> Items.GUNPOWDER, .25f),
    CRUSHED_GOLD = crushedOre(AllItems.CRUSHED_GOLD, () -> Items.GOLD_NUGGET, () -> Items.QUARTZ, .5f),
    CRUSHED_IRON = crushedOre(AllItems.CRUSHED_IRON, () -> Items.IRON_NUGGET, () -> Items.REDSTONE, .75f),


    GRAVEL = create(() -> Blocks.GRAVEL, b -> b.output(.25f, Items.FLINT)
        .output(.125f, Items.IRON_NUGGET)),
    SOUL_SAND = create(() -> Blocks.SOUL_SAND, b -> b.output(.125f, Items.QUARTZ, 4)
        .output(.02f, Items.GOLD_NUGGET)),
    RED_SAND = create(() -> Blocks.RED_SAND, b -> b.output(.125f, Items.GOLD_NUGGET, 3)
        .output(.05f, Items.DEAD_BUSH).output(.75f, DestroyItems.SILICA)),
    SAND = create(() -> Blocks.SAND, b -> b.output(.75f, DestroyItems.SILICA)),

    MASHED_POTATO = create(DestroyItems.MASHED_POTATO::get, b -> b.output(.02f, DestroyItems.CRUSHED_RAW_RHODIUM)
        .output(.4f, DestroyItems.MASHED_POTATO).duration(50).luckyFirstTime()),
    RUINED_CIRCUIT_BOARD = create(DestroyItems.RUINED_CIRCUIT_BOARD::get, b -> b.output(DestroyItems.RUINED_CIRCUIT_MASK)
        .output(DestroyItems.COPPER_POWDER)),
    SLAG = create(DestroyItems.SLAG::get, b -> b
        .output(.05f, Items.FLINT)
        .output(.05f, DestroyItems.SILICA)
        .output(.1f, Items.SAND)
        .output(.05f, AllItems.CRUSHED_ZINC)
        .output(.05f, AllItems.CRUSHED_NICKEL)
        .output(.05f, AllItems.CRUSHED_LEAD))

    ;

    public GeneratedRecipe beetrootAshes(ItemEntry<? extends Item> ashes, Supplier<ItemLike> primary, Supplier<ItemLike> secondary, float secondaryChance) {
        return beetrootAshes(ashes, primary, secondary, secondaryChance, null, 0.f, false);
    }
    public GeneratedRecipe beetrootAshes(ItemEntry<? extends Item> ashes, Supplier<ItemLike> primary, Supplier<ItemLike> secondary, float secondaryChance, boolean lucky) {
        return beetrootAshes(ashes, primary, secondary, secondaryChance, null, 0.f, lucky);
    }
    public GeneratedRecipe beetrootAshes(ItemEntry<? extends Item> ashes, Supplier<ItemLike> primary,
                                         Supplier<ItemLike> secondary, float secondaryChance,
                                         @Nullable Supplier<ItemLike> tertiary, float tertiaryChance) {
        return beetrootAshes(ashes, primary, secondary, secondaryChance, tertiary, tertiaryChance, false);
    }
    public GeneratedRecipe beetrootAshes(ItemEntry<? extends Item> ashes, Supplier<ItemLike> primary,
                                         Supplier<ItemLike> secondary, float secondaryChance,
                                         @Nullable Supplier<ItemLike> tertiary, float tertiaryChance,
                                         boolean lucky) {
        return create(ashes::get, b -> {
            b.output(primary.get()).output(0.5f, primary.get())
                .output(secondaryChance, secondary.get(), 1);

            if(tertiary != null)
                b.output(tertiaryChance, tertiary.get(), 1);

            b.output(0.5f, DestroyItems.BEETROOT_ASHES).duration(12800);
            if(lucky) b.luckyFirstTime();

            return b;
        });
    }

    public GeneratedRecipe crushedOre(ItemEntry<? extends Item> crushed, Supplier<ItemLike> nugget, Supplier<ItemLike> secondary, float secondaryChance) {
        return create(crushed::get, b -> b.output(nugget.get(), 9)
            .output(secondaryChance, secondary.get(), 1));
    }

    public DestroySievingRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DestroyRecipeTypes.SIEVING;
    }

    @Override
    protected DestroySievingRecipeGen.Builder builder(ProcessingRecipeBuilder.ProcessingRecipeFactory<SievingRecipe> factory, ResourceLocation path) {
        return new Builder(factory, path);
    }

    public class Builder extends ExtendedProcessingRecipeBuilder<SievingRecipe, DestroySievingRecipeGen.Builder> {
        public boolean luckyFirstTime = false;

        public Builder(ProcessingRecipeBuilder.ProcessingRecipeFactory<SievingRecipe> factory, ResourceLocation recipeId) {
            super(factory, recipeId);
        }

        public Builder luckyFirstTime() {
            luckyFirstTime = true;
            return this;
        }

        @Override
        public void build(SievingRecipe recipe) {
            recipe.setLuckyFirstTime(luckyFirstTime);
        }
    }
}
