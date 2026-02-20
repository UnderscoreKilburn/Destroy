package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.DestroyTags;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.core.chemistry.NamedMixtures;
import com.petrolpark.destroy.core.recipe.ingredient.fluid.MoleculeFluidIngredient;
import com.petrolpark.destroy.core.recipe.ingredient.fluid.MoleculeTagFluidIngredient;
import com.petrolpark.destroy.core.recipe.ingredient.fluid.SaltFluidIngredient;
import com.petrolpark.destroy.datagen.recipe.DestroyRecipeProvider.I;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;

import java.util.stream.Stream;

public class DestroyMixingRecipeGen extends MixingRecipeGen {

    GeneratedRecipe
    ALCHEMY = create("alchemy", b -> b
        .require(DestroyFluids.URINE.get(), 250)
        .require(I.lead())
        .require(I.lead())
        .require(Tags.Items.DYES_BLACK)
        .require(Tags.Items.DYES_WHITE)
        .require(Tags.Items.DYES_YELLOW)
        .require(Tags.Items.DYES_RED)
        .output(Items.GOLD_INGOT, 2)
        .requiresHeat(HeatCondition.HEATED)),
    ANDESITE_ALLOY_FROM_CHROMIUM = create("andesite_alloy_from_chromium", b -> b // worth
        .require(Items.ANDESITE)
        .require(I.chromiumNugget())
        .output(AllItems.ANDESITE_ALLOY)),
    ANFO = create("anfo", b -> b
        .require(DestroyFluids.CRUDE_OIL.get(), 5)
        .require(SaltFluidIngredient.of(DestroyMolecules.AMMONIUM, DestroyMolecules.NITRATE, 3.f, 5))
        .output(DestroyItems.ANFO)),

    BOROSILICATE_GLASS = create("borosilicate_glass", b -> b
        .require(DestroyItems.SILICA)
        .require(DestroyItems.SILICA)
        .require(DestroyItems.SILICA)
        .require(DestroyItems.BORAX)
        .require(Ingredient.fromValues(Stream.of(
            new Ingredient.TagValue(DestroyTags.Items.DUSTS_LIME.tag),
            new Ingredient.ItemValue(DestroyItems.OXIDIZED_SODIUM_INGOT.asStack())
        )))
        .output(DestroyFluids.MOLTEN_BOROSILICATE_GLASS.get(), 1000)),

    CREAM_FROM_MAGMA_CREAM = create("cream_from_magma_cream", b -> b // now you're really pushing it
        .require(Items.MAGMA_CREAM)
        .requiresHeat(I.cooled())
        .output(DestroyFluids.CREAM.get(), 200)),
    MAGMA_CREAM_FROM_CREAM = create("magma_cream_from_cream", b -> b
        .require(DestroyFluids.CREAM.get(), 200)
        .require(Items.MAGMA_BLOCK)
        .requiresHeat(HeatCondition.HEATED)
        .output(Items.MAGMA_CREAM)),

    SLIME_BALL = create("slime_ball", b -> b
        .require(Tags.Items.DUSTS)
        .require(MoleculeTagFluidIngredient.of(DestroyMolecules.Tags.ADHESIVE, 0.2f, 1))
        .output(Items.SLIME_BALL)),

    STAINLESS_STEEL = create("stainless_steel", b -> b
        .require(Items.CHARCOAL)
        .require(I.ironDust())
        .require(I.ironDust())
        .require(I.ironDust())
        .require(I.ironDust())
        .require(I.ironDust())
        .require(I.nickelDust())
        .require(I.nickelDust())
        .require(I.chromiumDust())
        .output(DestroyFluids.MOLTEN_STAINLESS_STEEL.get(), 500)
        .duration(1)), // Mixing recipes do not allow you to specify an actual duration - only fast (0) or slow (any other number)
    STAINLESS_STEEL_FLUXED = create("stainless_steel_fluxed", b -> b
        .require(Items.CHARCOAL)
        .require(I.ironDust())
        .require(I.ironDust())
        .require(I.ironDust())
        .require(I.ironDust())
        .require(I.ironDust())
        .require(I.nickelDust())
        .require(I.nickelDust())
        .require(I.chromiumDust())
        .require(I.fluxes())
        .output(DestroyFluids.MOLTEN_STAINLESS_STEEL.get(), 500)
        .output(DestroyItems.SLAG)),

    STAINLESS_STEEL_INEFFICIENT = create("stainless_steel_inefficient", b -> b
        .require(Items.CHARCOAL)
        .require(I.iron())
        .require(I.iron())
        .require(I.nickel())
        .require(I.chromiumNugget())
        .require(I.chromiumNugget())
        .output(DestroyItems.STAINLESS_STEEL_INGOT)
        .output(0.5f, DestroyItems.STAINLESS_STEEL_INGOT)
        .duration(1)),
    STAINLESS_STEEL_INEFFICIENT_FLUXED = create("stainless_steel_inefficient_fluxed", b -> b
        .require(Items.CHARCOAL)
        .require(I.iron())
        .require(I.iron())
        .require(I.nickel())
        .require(I.chromiumNugget())
        .require(I.chromiumNugget())
        .require(I.fluxes())
        .output(DestroyItems.STAINLESS_STEEL_INGOT, 2)),

    EMPTY_BOMB_BON = create("empty_bomb_bon", b -> b
        .require(MoleculeFluidIngredient.of(DestroyMolecules.GLYCEROL, 1.f, 2))
        .require(AllTags.AllFluidTags.CHOCOLATE.tag, 200)
        .require(I.primaryExplosives())
        .require(I.secondaryExplosives())
        .requiresHeat(I.cooled())
        .output(DestroyItems.EMPTY_BOMB_BON)),

    GUNPOWDER = create("gunpowder", b -> b
        .withCondition(I.sulfurExists())
        .require(SaltFluidIngredient.of(DestroyMolecules.POTASSIUM_ION, DestroyMolecules.NITRATE, 1.f, 5.f, 250))
        .require(Items.CHARCOAL)
        .require(I.anySulfur())
        .output(Items.GUNPOWDER, 4)),
    GUNPOWDER_FROM_MOLTEN_SULFUR = create("gunpowder_from_molten_sulfur", b -> b
        .require(SaltFluidIngredient.of(DestroyMolecules.POTASSIUM_ION, DestroyMolecules.NITRATE, 1.f, 5.f, 250))
        .require(MoleculeFluidIngredient.of(DestroyMolecules.OCTASULFUR, 250))
        .require(Items.CHARCOAL)
        .output(Items.GUNPOWDER, 4)),
    SULFUR_MELTING = create("sulfur_melting", b -> b
        .withCondition(I.sulfurExists())
        .require(I.anySulfur())
        .requiresHeat(HeatCondition.HEATED)
        .output(NamedMixtures.MOLTEN_SULFUR.get(250))),

    HYPERACCUMULATING_FERTILIZER = create("hyperaccumulating_fertilizer", b -> b
        .require(I.fertilizers())
        .require(I.fertilizers())
        .require(SaltFluidIngredient.of(DestroyMolecules.AMMONIUM, DestroyMolecules.NITRATE, 3.f, 5.f, 2))
        .output(DestroyItems.HYPERACCUMULATING_FERTILIZER, 4)),
    HYPERACCUMULATING_FERTILIZER_NITROLIME = create("hyperaccumulating_fertilizer_nitrolime", b -> b
        .require(I.fertilizers())
        .require(I.fertilizers())
        .require(SaltFluidIngredient.of(DestroyMolecules.CALCIUM_ION, DestroyMolecules.CYANAMIDE_ION, 3.f, 5.f, 2))
        .output(DestroyItems.HYPERACCUMULATING_FERTILIZER, 4)),

    MOLTEN_CINNABAR = create("molten_cinnabar_from_redstone", b -> b
        .require(Items.REDSTONE)
        .requiresHeat(HeatCondition.SUPERHEATED)
        .output(DestroyFluids.MOLTEN_CINNABAR.get(), 100)),
    MOLTEN_CINNABAR_FROM_BLOCK = create("molten_cinnabar_from_redstone_block", b -> b
        .require(Items.REDSTONE_BLOCK)
        .requiresHeat(HeatCondition.SUPERHEATED)
        .output(DestroyFluids.MOLTEN_CINNABAR.get(), 900)),

    NAPALM_SUNDAE = create("napalm_sundae", b -> b
        .require(DestroyTags.Items.POROUS_PLASTICS.tag)
        .require(DestroyFluids.CRUDE_OIL.get(), 250)
        .require(DestroyFluids.CREAM.get(), 250)
        .requiresHeat(I.cooled())
        .output(DestroyFluids.NAPALM_SUNDAE.get(), 500)),
    PERFUME = create("perfume", b -> b
        .require(I.flowers())
        .require(I.flowers())
        .require(MoleculeTagFluidIngredient.of(DestroyMolecules.Tags.FRAGRANT, 0.2f, 50))
        .require(Fluids.WATER, 200)
        .output(DestroyFluids.PERFUME.get(), 250)),

    THERMITE_BROWNIE = create("thermite_brownie", b -> b
        .require(AllItems.CRUSHED_IRON)
        .require(DestroyItems.POLYTETRAFLUOROETHENE)
        .require(Items.COCOA_BEANS)
        .require(DestroyItems.BUTTER)
        .require(AllItems.CINDER_FLOUR)
        .requiresHeat(HeatCondition.HEATED)
        .output(DestroyItems.THERMITE_BROWNIE, 2))
    ;

    public DestroyMixingRecipeGen(PackOutput output) {
        super(output, Destroy.MOD_ID);
    }
}
