package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules.Tags;
import com.petrolpark.destroy.content.processing.dynamo.ChargingRecipe;
import com.petrolpark.destroy.core.recipe.ingredient.fluid.MoleculeFluidIngredient;
import com.petrolpark.destroy.core.recipe.ingredient.fluid.MoleculeTagFluidIngredient;
import com.petrolpark.destroy.datagen.recipe.DestroyRecipeProvider.I;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;


public class DestroySequencedAssemblyRecipeGen extends SequencedAssemblyRecipeGen {

    GeneratedRecipe
    BLACKLIGHT = create("blacklight", b -> b.require(AllItems.STURDY_SHEET)
        .transitionTo(DestroyItems.UNFINISHED_BLACKLIGHT.get())
        .addOutput(DestroyBlocks.BLACKLIGHT, 1)
        .addStep(FillingRecipe::new, r -> r.require(MoleculeFluidIngredient.of(DestroyMolecules.MERCURY, 250)))
        .addStep(DeployerApplicationRecipe::new, r -> r.require(Items.TINTED_GLASS))
        .addStep(ChargingRecipe::new, r -> r)
        .loops(1)),

    CARD_STOCK = create("card_stock", b -> b.require(AllItems.PULP)
        .transitionTo(DestroyItems.UNFINISHED_CARD_STOCK.get())
        .addOutput(DestroyItems.CARD_STOCK, 1)
        .addStep(FillingRecipe::new, r -> r.require(MoleculeTagFluidIngredient.of(Tags.BLEACH, 5.f, 1)))
        .addStep(PressingRecipe::new, r -> r)
        .addStep(DeployerApplicationRecipe::new, r -> r.require(DestroyItems.POLYSTYRENE_BUTADIENE))
        .addStep(DeployerApplicationRecipe::new, r -> r.require(DestroyItems.CHALK_DUST))
        .loops(1)),

    CATALYTIC_CONVERTER = create("catalytic_converter", b -> b.require(DestroyBlocks.CERAMIC_MONOLITH)
        .transitionTo(DestroyItems.UNFINISHED_CATALYTIC_CONVERTER.get())
        .addOutput(DestroyBlocks.CATALYTIC_CONVERTER, 1)
        .addStep(DeployerApplicationRecipe::new, r -> r.require(I.stainlessSteelSheet()))
        .addStep(DeployerApplicationRecipe::new, r -> r.require(I.platinumDust()))
        .addStep(DeployerApplicationRecipe::new, r -> r.require(I.palladiumDust()))
        .addStep(DeployerApplicationRecipe::new, r -> r.require(I.rhodiumDust()))
        .addStep(DeployerApplicationRecipe::new, r -> r.require(I.stainlessSteelSheet()))
        .loops(1)),

    MASHED_POTATO = create("mashed_potato", b -> b.require(Items.BAKED_POTATO)
        .transitionTo(DestroyItems.UNPROCESSED_MASHED_POTATO.get())
        .addOutput(DestroyItems.MASHED_POTATO, 1)
        .addStep(CuttingRecipe::new, r -> r)
        .addStep(PressingRecipe::new, r -> r)
        .addStep(DeployerApplicationRecipe::new, r -> r.require(DestroyItems.BUTTER))
        .addStep(PressingRecipe::new, r -> r)
        .loops(1)),

    NAPALM_SUNDAE = create("napalm_sundae", b -> b.require(Items.BOWL)
        .transitionTo(DestroyItems.UNPROCESSED_NAPALM_SUNDAE.get())
        .addOutput(DestroyItems.NAPALM_SUNDAE, 1)
        .addStep(FillingRecipe::new, r -> r.require(DestroyFluids.NAPALM_SUNDAE.get(), 250))
        .addStep(DeployerApplicationRecipe::new, r -> r.require(Items.FLINT))
        .loops(1)),

    UNVARNISHED_PLYWOOD = create("unvarnished_plywood", b -> b.require(I.woodenSlabs())
        .transitionTo(DestroyItems.UNFINISHED_UNVARNISHED_PLYWOOD.get())
        .addOutput(DestroyBlocks.UNVARNISHED_PLYWOOD, 1)
        .addStep(FillingRecipe::new, r -> r.require(MoleculeTagFluidIngredient.of(Tags.ADHESIVE, 0.2f, 1.f, 1)))
        .addStep(DeployerApplicationRecipe::new, r -> r.require(I.woodenSlabs()))
        .loops(11)),

    VOLTAIC_PILE = create("voltaic_pile", b -> b.require(Items.PAPER)
        .transitionTo(DestroyItems.UNFINISHED_VOLTAIC_PILE.get())
        .addOutput(DestroyItems.VOLTAIC_PILE, 1)
        .addStep(FillingRecipe::new, r -> r.require(Fluids.WATER, 250))
        .addStep(DeployerApplicationRecipe::new, r -> r.require(I.copperSheet()))
        .addStep(FillingRecipe::new, r -> r.require(Fluids.WATER, 250))
        .addStep(DeployerApplicationRecipe::new, r -> r.require(I.zincSheet()))
        .loops(3))
    ;

    public DestroySequencedAssemblyRecipeGen(PackOutput output) {
        super(output, Destroy.MOD_ID);
    }
}
