package com.petrolpark.destroy;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DestroyStats {

    public static final DeferredRegister<ResourceLocation> STATS = DeferredRegister.create(Registries.CUSTOM_STAT, Destroy.MOD_ID);

    private static Map<ResourceLocation, StatFormatter> FORMATTERS = new HashMap<>();

    public static final RegistryObject<ResourceLocation> NOVEL_COMPOUNDS_SYNTHESIZED = register("novel_compounds_synthesized", StatFormatter.DEFAULT);

    private static final RegistryObject<ResourceLocation> register(String name, StatFormatter formatter) {
        ResourceLocation rl = Destroy.asResource(name);
        RegistryObject<ResourceLocation> stat = STATS.register(name, () -> rl);
        FORMATTERS.put(rl, formatter);
        return stat;
    };

    public static final void register(IEventBus eventBus) {
        STATS.register(eventBus);
    };

    public static final void register() {
        for (Entry<ResourceLocation, StatFormatter> entry : FORMATTERS.entrySet()) {
            Stats.CUSTOM.get(entry.getKey(), entry.getValue());
        };
    };
};
