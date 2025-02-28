package com.petrolpark.destroy;

import com.petrolpark.destroy.content.processing.trypolithography.CircuitPatternItemAttribute;
import com.simibubi.create.content.logistics.filter.ItemAttribute;

public abstract class DestroyItemAttributes implements ItemAttribute {
    
    public static final ItemAttribute

    isCircuitPatternPunched = ItemAttribute.register(new CircuitPatternItemAttribute(0, true));

    public static void register() {};
};
