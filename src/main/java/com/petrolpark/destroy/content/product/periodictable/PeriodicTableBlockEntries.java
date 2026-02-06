package com.petrolpark.destroy.content.product.periodictable;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyRegistries;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public class PeriodicTableBlockEntries {
    public static void bootstrap(BootstapContext<PeriodicTableBlock.PeriodicTableEntry> ctx) {
        register(ctx, DestroyBlocks.HYDROGEN_PERIODIC_TABLE_BLOCK.get(), 0, 0);
        register(ctx, DestroyBlocks.CARBON_PERIODIC_TABLE_BLOCK.get(), 13, 1);
        register(ctx, DestroyBlocks.NITROGEN_PERIODIC_TABLE_BLOCK.get(), 14, 1);
        register(ctx, DestroyBlocks.OXYGEN_PERIODIC_TABLE_BLOCK.get(), 15, 1);
        register(ctx, DestroyBlocks.CHLORINE_PERIODIC_TABLE_BLOCK.get(), 16, 2);
        register(ctx, DestroyBlocks.CHROMIUM_PERIODIC_TABLE_BLOCK.get(), 5, 3);
        register(ctx, DestroyBlocks.IRON_PERIODIC_TABLE_BLOCK.get(), 7, 3);
        register(ctx, DestroyBlocks.NICKEL_PERIODIC_TABLE_BLOCK.get(), 9, 3);
        register(ctx, DestroyBlocks.COPPER_PERIODIC_TABLE_BLOCK.get(), 10, 3);
        register(ctx, DestroyBlocks.ZINC_PERIODIC_TABLE_BLOCK.get(), 11, 3);
        register(ctx, DestroyBlocks.RHODIUM_PERIODIC_TABLE_BLOCK.get(), 8, 4);
        register(ctx, DestroyBlocks.PALLADIUM_PERIODIC_TABLE_BLOCK.get(), 9, 4);
        register(ctx, DestroyBlocks.IODINE_PERIODIC_TABLE_BLOCK.get(), 16, 4);
        register(ctx, DestroyBlocks.PLATINUM_PERIODIC_TABLE_BLOCK.get(), 9, 5);
        register(ctx, DestroyBlocks.GOLD_PERIODIC_TABLE_BLOCK.get(), 10, 5);
        register(ctx, DestroyBlocks.MERCURY_PERIODIC_TABLE_BLOCK.get(), 11, 5);
        register(ctx, DestroyBlocks.LEAD_PERIODIC_TABLE_BLOCK.get(), 13, 5);
    };

    private static void register(BootstapContext<PeriodicTableBlock.PeriodicTableEntry> ctx, Block block, int x, int y) {
        ctx.register(ResourceKey.create(DestroyRegistries.PERIODIC_TABLE_BLOCKS, Destroy.asResource(block.builtInRegistryHolder().key().location().getPath())),
            new PeriodicTableBlock.PeriodicTableEntry(HolderSet.direct(block.builtInRegistryHolder()), x, y));
    };
};
