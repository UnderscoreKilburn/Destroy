package com.petrolpark.destroy.core.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class MoltenSolidBucketItem extends SolidBucketItem {
    public MoltenSolidBucketItem(Block pBlock, SoundEvent pPlaceSound, Supplier<? extends Fluid> fluidSupplier, Properties pProperties) {
        super(pBlock, pPlaceSound, pProperties);
        this.fluidSupplier = fluidSupplier;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundTag nbt) {
        return new Wrapper(stack);
    }

    private final Supplier<? extends Fluid> fluidSupplier;
    public Fluid getFluid() { return fluidSupplier.get(); }

    public class Wrapper extends FluidBucketWrapper {
        public Wrapper(@NotNull ItemStack container) {
            super(container);
        }

        @NotNull
        @Override
        public FluidStack getFluid()
        {
            Item item = container.getItem();
            if (item instanceof MoltenSolidBucketItem)
            {
                return new FluidStack(((MoltenSolidBucketItem)item).getFluid(), FluidType.BUCKET_VOLUME);
            }
            else
            {
                return FluidStack.EMPTY;
            }
        }
    }
}
