package com.petrolpark.destroy.datagen;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.datagen.recipe.DestroyMechanicalCraftingRecipeGen;
import com.petrolpark.destroy.datagen.recipe.DestroyRecipeProvider;
import com.petrolpark.destroy.datagen.recipe.DestroySequencedAssemblyRecipeGen;
import com.petrolpark.destroy.datagen.recipe.DestroyStandardRecipeGen;
import com.petrolpark.destroy.datagen.recipe.DestroyVatMaterialGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class DestroyDatagen {
    public static void gatherData(GatherDataEvent event) {
        DestroyTagDatagen.addGenerators();

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        DestroyGeneratedEntriesProvider generatedEntriesProvider = new DestroyGeneratedEntriesProvider(output, lookupProvider);
        lookupProvider = generatedEntriesProvider.getRegistryProvider();

        generator.addProvider(event.includeServer(), generatedEntriesProvider);
        generator.addProvider(event.includeServer(), new DestroyStandardRecipeGen(output));
        generator.addProvider(event.includeServer(), new DestroyVatMaterialGen(output, Destroy.MOD_ID));
        generator.addProvider(event.includeServer(), new DestroySequencedAssemblyRecipeGen(output));
        generator.addProvider(event.includeServer(), new DestroyMechanicalCraftingRecipeGen(output));

        if (event.includeServer()) {
            DestroyRecipeProvider.registerAllProcessing(generator, output);
        }
    }
}
