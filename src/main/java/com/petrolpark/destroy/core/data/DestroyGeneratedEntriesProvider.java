package com.petrolpark.destroy.core.data;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyRegistries;
import com.petrolpark.destroy.content.product.periodictable.PeriodicTableBlockEntries;
import com.petrolpark.destroy.core.explosion.mixedexplosive.ExplosiveProperties;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DestroyGeneratedEntriesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(DestroyRegistries.PERIODIC_TABLE_BLOCKS, PeriodicTableBlockEntries::bootstrap)
        .add(DestroyRegistries.EXPLOSIVE_PROPERTIES, ExplosiveProperties.GeneratedEntries::bootstrap);

    public DestroyGeneratedEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Destroy.MOD_ID));
    };

    @Override
    public String getName() {
        return "Destroy's Generated Registry Entries";
    }
};
