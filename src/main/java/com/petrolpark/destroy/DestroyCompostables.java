package com.petrolpark.destroy;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

public class DestroyCompostables {

    private static Map<ItemLike, Float> DESTROY_COMPOSTABLES;

    // Shouldn't be called until it's time to register
    static {
        DESTROY_COMPOSTABLES = new HashMap<>();
        add(0.75f, DestroyItems.HEFTY_BEETROOT.get());
        add(0.85f, DestroyItems.COAL_INFUSED_BEETROOT.get());
        add(0.85f, DestroyItems.COPPER_INFUSED_BEETROOT.get());
        add(0.85f, DestroyItems.DIAMOND_INFUSED_BEETROOT.get());
        add(0.85f, DestroyItems.EMERALD_INFUSED_BEETROOT.get());
        add(0.85f, DestroyItems.FLUORITE_INFUSED_BEETROOT.get());
        add(0.85f, DestroyItems.GOLD_INFUSED_BEETROOT.get());
        add(0.85f, DestroyItems.IRON_INFUSED_BEETROOT.get());
        add(0.85f, DestroyItems.LAPIS_INFUSED_BEETROOT.get());
        add(0.85f, DestroyItems.NETHER_CROCOITE_INFUSED_BEETROOT.get());
        add(0.85f, DestroyItems.NICKEL_INFUSED_BEETROOT.get());
        add(0.85f, DestroyItems.QUARTZ_INFUSED_BEETROOT.get());
        add(0.85f, DestroyItems.REDSTONE_INFUSED_BEETROOT.get());
        add(0.85f, DestroyItems.ZINC_INFUSED_BEETROOT.get());
        add(0.7f, DestroyItems.MASHED_POTATO.get());
        add(0.4f, DestroyItems.YEAST.get());
        add(1.0f, DestroyBlocks.MASHED_POTATO_BLOCK.get());
    };

    private static void add(float chance, ItemLike item) {
        DESTROY_COMPOSTABLES.put(item.asItem(), chance);
    };

    @SuppressWarnings("deprecation")
    public static void register() {
        DESTROY_COMPOSTABLES.forEach((itemLike, chance) -> {
            ComposterBlock.COMPOSTABLES.put(itemLike.asItem(), chance);
        });
    };
};
