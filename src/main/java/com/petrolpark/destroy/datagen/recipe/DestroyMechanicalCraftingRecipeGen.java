package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.datagen.recipe.DestroyRecipeProvider.I;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeGen;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;


public class DestroyMechanicalCraftingRecipeGen extends MechanicalCraftingRecipeGen {

    GeneratedRecipe
        GAS_MASK = create(DestroyItems.GAS_MASK::get).recipe(b -> b
            .key('N', I.textilePlastic())
            .key('P', I.inertPlastic())
            .key('G', AllItems.GOGGLES)
            .key('Y', Tags.Items.DYES_YELLOW)
            .key('F', DestroyItems.GAS_FILTER)
            .patternLine(" NNN ")
            .patternLine("NPGPN")
            .patternLine("NPYPN")
            .patternLine(" NFN ")
            .disallowMirrored()),
        HAZMAT_LEGGINGS = create(DestroyItems.HAZMAT_LEGGINGS::get).recipe(b -> b
            .key('N', I.textilePlastic())
            .key('P', I.inertPlastic())
            .key('Y', Tags.Items.DYES_YELLOW)
            .patternLine(" NNN ")
            .patternLine("NPPPN")
            .patternLine("NPYPN")
            .patternLine("NPNPN")
            .patternLine(" N N ")
            .disallowMirrored()),
        HAZMAT_SUIT = create(DestroyItems.HAZMAT_SUIT::get).recipe(b -> b
            .key('N', I.textilePlastic())
            .key('P', I.inertPlastic())
            .key('Y', Tags.Items.DYES_YELLOW)
            .patternLine(" N N ")
            .patternLine("NPYPN")
            .patternLine("NPPPN")
            .patternLine("NPPPN")
            .patternLine(" NNN ")
            .disallowMirrored()),
        INSULATED_STAINLESS_STEEL_BLOCK = create(DestroyBlocks.INSULATED_STAINLESS_STEEL_BLOCK::get).returns(4).recipe(b -> b
            .key('S', I.stainlessSteelSheet())
            .key('R', I.stainlessSteelRod())
            .key('G', DestroyBlocks.BOROSILICATE_GLASS_FIBER)
            .patternLine("SRS")
            .patternLine("SGS")
            .patternLine("SGS")
            .patternLine("SRS")
            .disallowMirrored()),
        PUMPJACK = create(DestroyBlocks.PUMPJACK::get).recipe(b -> b
            .key('B', I.brassBlock())
            .key('S', AllBlocks.SHAFT)
            .key('P', I.brassSheet())
            .key('L', AllBlocks.LARGE_COGWHEEL)
            .key('M', AllItems.PRECISION_MECHANISM)
            .key('C', AllBlocks.BRASS_CASING)
            .key('F', AllBlocks.FLUID_PIPE)
            .patternLine("PSSSB")
            .patternLine("LMCFF")
            .disallowMirrored()),
        STAINLESS_STEEL_RODS_BLOCK = create(DestroyBlocks.STAINLESS_STEEL_RODS::get).recipe(b -> b
            .key('R', I.stainlessSteelRod())
            .patternLine("RRRR")
            .patternLine("RRRR")
            .patternLine("RRRR")
            .patternLine("RRRR")
            .disallowMirrored()),
        SWISS_ARMY_KNIFE = create(DestroyItems.SWISS_ARMY_KNIFE::get).recipe(b -> b
            .key('P', I.rigidPlastic())
            .key('M', AllItems.PRECISION_MECHANISM)
            .key('I', Items.IRON_PICKAXE)
            .key('A', Items.IRON_AXE)
            .key('S', Items.SHEARS)
            .key('H', Items.IRON_SHOVEL)
            .key('O', Items.IRON_HOE)
            .key('D', DestroyItems.NANODIAMONDS)
            .patternLine("PPPPPPP")
            .patternLine("MIASHOD")
            .patternLine("PPPPPPP")
            .disallowMirrored())
    ;

    public DestroyMechanicalCraftingRecipeGen(PackOutput output) {
        super(output, Destroy.MOD_ID);
    }
}
