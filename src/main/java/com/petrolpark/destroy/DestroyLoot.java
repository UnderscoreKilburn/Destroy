package com.petrolpark.destroy;

import net.minecraftforge.eventbus.api.IEventBus;

public class DestroyLoot {
    
    public static void register(IEventBus eventBus) {
        DestroyLootConditions.register(eventBus);
        DestroyNumberProviders.register(eventBus);
    };
};
