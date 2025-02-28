package com.petrolpark.destroy.core.chemistry.storage;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.core.fluid.GeniusFluidTankBehaviour.GeniusFluidTank;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

/**
 * The Fluid Handler in which {@link com.petrolpark.destroy.core.chemistry.storage.IMixtureStorageItem IMixtureStorageItems} store Mixtures.
 */
public class ItemMixtureTank extends GeniusFluidTank implements ICapabilityProvider, IFluidHandlerItem {

    private final ItemStack container;

    public ItemMixtureTank(ItemStack container, Consumer<FluidStack> updateCallback) {
        super(0, updateCallback);
        if (!(container.getItem() instanceof IMixtureStorageItem item)) throw new IllegalArgumentException(container.getItem().getDescriptionId()+" cannot store Mixtures");
        capacity = item.getCapacity(container);
        this.container = container;
        fluid = FluidStack.loadFluidStackFromNBT(container.getOrCreateTag().getCompound("Fluid"));
    };

    public int getRemainingSpace() {
        return capacity - getFluidAmount();
    };

    @Override
    public void setFluid(FluidStack stack) {
        super.setFluid(stack);
        getContainer().getOrCreateTag().put("Fluid", stack.writeToNBT(new CompoundTag()));
    };

    @Override
    protected void onContentsChanged() {
        super.onContentsChanged();
        getContainer().getOrCreateTag().put("Fluid", fluid.writeToNBT(new CompoundTag()));
    };

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return DestroyFluids.isMixture(fluid);
    };

    @Override
    public @NotNull ItemStack getContainer() {
        return container;
    };

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap != ForgeCapabilities.FLUID_HANDLER_ITEM) return LazyOptional.empty();
        return LazyOptional.of(() -> this).cast();
    };
    
};
