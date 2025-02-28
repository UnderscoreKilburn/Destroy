package com.petrolpark.destroy;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@MoveToPetrolparkLibrary
public class DestroyAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, Destroy.MOD_ID);

    public static final RegistryObject<Attribute>

    EXTRA_INVENTORY_SIZE = registerRanged("extra_inventory_size", 0d, 0d, 256d, true),
    EXTRA_HOTBAR_SLOTS = registerRanged("extra_hotbar_slots", 0d, 0d, 32d, true);

    private static RegistryObject<Attribute> registerRanged(String name, double defaultValue, double min, double max, boolean syncable) {
        return ATTRIBUTES.register(name, () -> new RangedAttribute(name, defaultValue, min, max).setSyncable(syncable));
    };

    public static final void register(IEventBus bus) {
        ATTRIBUTES.register(bus);
    };
    
};
