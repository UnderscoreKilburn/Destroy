package com.petrolpark.destroy;

import com.petrolpark.destroy.content.product.periodictable.PeriodicTableBlock;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;
import org.jetbrains.annotations.ApiStatus;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DestroyRegistries {
    
    //public static final ResourceKey<Registry<VatMaterial>> VAT_MATERIAL = key("vat_material");
    //public static final ResourceKey<Registry<ExplosiveProperties>> EXPLOSIVE_PROPERTIES = key("explosive_properties");
    public static final ResourceKey<Registry<PeriodicTableBlock.PeriodicTableEntry>> PERIODIC_TABLE_BLOCKS = key("periodic_table_blocks");

    //TODO BM4x4 generator types (probably in library)
    //TODO Item Explosive Properties
    //TODO Seismology providers

    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(Destroy.asResource(name));
    }

    @ApiStatus.Internal
    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
            DestroyRegistries.PERIODIC_TABLE_BLOCKS,
            PeriodicTableBlock.PeriodicTableEntry.CODEC,
            PeriodicTableBlock.PeriodicTableEntry.CODEC
        );
    }
};
