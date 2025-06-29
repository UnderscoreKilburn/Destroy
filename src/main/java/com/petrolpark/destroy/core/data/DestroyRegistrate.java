package com.petrolpark.destroy.core.data;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;

public class DestroyRegistrate extends CreateRegistrate {
    protected DestroyRegistrate(String modid) {
        super(modid);
    }

    public static DestroyRegistrate create(String modid) {
        return new DestroyRegistrate(modid);
    }

    @Override
    public <T extends RegistrateProvider> void genData(ProviderType<? extends T> type, T gen) {
        // Don't datagen language files for now
        if(type == ProviderType.LANG) return;

        super.genData(type, gen);
    }
}
