package com.petrolpark.destroy.mixin.patch;

import com.petrolpark.PetrolparkRegistries;
import com.petrolpark.contamination.Contaminant;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Contaminant.class)
public class ContaminantMixin {
    @Shadow
    protected ResourceLocation rl;

    /**
     * @author _Kilburn
     * @reason Temporarily patching this here so I don't have to maintain a separate fork of the Library. It's not pretty but it'll have to do for now.
     */
    @Overwrite(remap = false)
    public ResourceLocation getLocation() {
        if (this.rl == null)
        {
            this.rl = PetrolparkRegistries.getDataRegistry(PetrolparkRegistries.Keys.CONTAMINANT).getKey((Contaminant) (Object) this);
        }

        return this.rl;
    }
}
