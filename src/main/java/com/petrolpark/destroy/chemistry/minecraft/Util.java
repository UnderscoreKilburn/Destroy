package com.petrolpark.destroy.chemistry.minecraft;

import com.petrolpark.destroy.chemistry.api.species.NamespacedId;

import net.minecraft.resources.ResourceLocation;

public class Util {
    
    public static final ResourceLocation toRl(NamespacedId speciesId) {
        return ResourceLocation.parse(speciesId.toString());
    };
};
