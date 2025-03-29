package com.petrolpark.destroy;

import com.petrolpark.Petrolpark;
import com.petrolpark.destroy.client.DestroyItemDisplayContexts;
import com.petrolpark.destroy.client.DestroyPartials;
import com.petrolpark.destroy.client.DestroyParticleTypes;
import com.petrolpark.destroy.client.DestroyPonderIndex;
import com.petrolpark.destroy.client.DestroyPonderTags;
import com.petrolpark.destroy.client.DestroySpriteSource;
import com.petrolpark.destroy.client.FogHandler;
import com.petrolpark.destroy.core.extendedinventory.ExtendedInventoryClientHandler;

import com.petrolpark.item.decay.DecayingItemHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class DestroyClient {
    
    public static final FogHandler FOG_HANDLER = new FogHandler();
    public static final ExtendedInventoryClientHandler EXTENDED_INVENTORY_HANDLER = new ExtendedInventoryClientHandler();

    static {
        DestroyItemDisplayContexts.register();
    };

    public static void clientInit(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> { // Work which must be done on main thread
            DestroyItemProperties.register();

            // TODO: This is supposed to be done by the Petrolpark library, remove this once this is properly fixed
            Petrolpark.DECAYING_ITEM_HANDLER.set(new DecayingItemHandler.ClientDecayingItemHandler());
        });
        DestroyPonderTags.register();
        DestroyPonderIndex.register();
    };

    public static void clientCtor(IEventBus modEventBus, IEventBus forgeEventBus) {
        DestroySpriteSource.register();
        DestroyPartials.init();
        modEventBus.addListener(DestroyParticleTypes::registerProviders);
    };
};
