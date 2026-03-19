package com.petrolpark.destroy.core.chemistry.vat;

import java.util.Map;
import java.util.function.Consumer;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.chemistry.legacy.LegacyMixture;
import com.petrolpark.destroy.chemistry.legacy.LegacyMixture.Phases;
import com.petrolpark.destroy.chemistry.legacy.ReadOnlyMixture;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.minecraft.MixtureFluid;
import com.petrolpark.destroy.core.fluid.GeniusFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import org.jetbrains.annotations.NotNull;

public class VatFluidTankBehaviour extends SmartFluidTankBehaviour {

    /**
     * Hold both the liquids and gases present in the Vat as a single Mixture
     * with an implied volume equal to the {@link VatFluidTankBehaviour#vatCapacity volume of the whole Vat}.
     */
    protected LegacyMixture combinedMixture;

    protected int vatCapacity;
    protected boolean liquidFull;

    public VatFluidTankBehaviour(VatControllerBlockEntity be, int vatCapacity) {
        super(SmartFluidTankBehaviour.TYPE, be, 2, vatCapacity, false);

        IFluidHandler[] handlers = new IFluidHandler[2];
        for (int i = 0; i < 2; i++) {
			VatTankSegment tankSegment = new VatTankSegment(vatCapacity, i == 1);
			this.tanks[i] = tankSegment;
			handlers[i] = tankSegment.getTank();
		};
        capability = LazyOptional.of(() -> new VatFluidHandler(handlers));

        this.vatCapacity = vatCapacity;
        this.liquidFull = false;
        this.combinedMixture = new LegacyMixture();
    };

    public VatFluidTank getLiquidHandler() {
        return getLiquidTank().getTank();
    };

    public VatFluidTank getGasHandler() {
        return getGasTank().getTank();
    };

    public VatTankSegment getLiquidTank() {
        return (VatTankSegment)super.getPrimaryTank();
    };

    public VatTankSegment getGasTank() {
        return (VatTankSegment)tanks[1];
    };

    public void disturbEquilibrium() {
        combinedMixture.disturbEquilibrium();
    }

    public void setCapacity(int capacity) {
        vatCapacity = capacity;
        for (TankSegment tankSegment : tanks) {
            ((VatTankSegment)tankSegment).getTank().setCapacity(capacity);
        };
    };

    public boolean isFull() {
        return liquidFull;
    };

    public LegacyMixture getCombinedMixture() { return combinedMixture; }
    public ReadOnlyMixture getCombinedReadOnlyMixture() { return combinedMixture; }
    
    /**
     * Replace all the gas in the gas tank with room temperature and pressure air.
     * @return The gas that was previously stored
     */
    public FluidStack flush(float temperature) {
        Phases phases = combinedMixture.separatePhases(vatCapacity);
        double gasVolume = vatCapacity - phases.liquidVolume();
        combinedMixture.drainGas(vatCapacity, vatCapacity).mixWith(vatCapacity, MixtureFluid.airMixture(temperature), (float)gasVolume);
        updateTankContents();
        getGasHandler().flushed = true;

        LegacyMixture oldGasMixture = phases.gasMixture();
        int oldGasVolume = Math.max(1, (int)gasVolume);
        oldGasMixture.scale(oldGasVolume);
        return MixtureFluid.of(oldGasVolume, oldGasMixture);
    };

    @Override
	public void write(CompoundTag nbt, boolean clientPacket) {
		super.write(nbt, clientPacket);
        nbt.put("Mixture", combinedMixture.writeNBT());

        if (clientPacket) return;
		nbt.putBoolean("Full", liquidFull);
	};

	@Override
	public void read(CompoundTag nbt, boolean clientPacket) {
		super.read(nbt, clientPacket);
        combinedMixture = LegacyMixture.readNBT(nbt.getCompound("Mixture"));
        updateTankContents();

        if (clientPacket) return;

        liquidFull = nbt.getBoolean("Full");
        vatCapacity = getLiquidHandler().getCapacity();
	};

    public void updateTankContents() {
        Phases phases = combinedMixture.separatePhases(vatCapacity);

        int liquidVolume = phases.liquidVolume().intValue();
        int gasVolume = vatCapacity - liquidVolume;

        if(liquidVolume == 0 || phases.gasMixture().isEmpty())
            getLiquidHandler().setFluid(FluidStack.EMPTY);
        else {
            LegacyMixture liquidMixture = phases.liquidMixture();
            liquidMixture.scale((float) (liquidVolume / phases.liquidVolume()));
            getLiquidHandler().setFluid(MixtureFluid.of(liquidVolume, liquidMixture));
        }

        if(gasVolume == 0 || phases.gasMixture().isEmpty())
            getGasHandler().setFluid(FluidStack.EMPTY);
        else {
            LegacyMixture gasMixture = phases.gasMixture();
            gasMixture.scale(gasVolume);
            getGasHandler().setFluid(MixtureFluid.of(gasVolume, gasMixture));
        }
    }

    public class VatFluidHandler extends InternalFluidHandler {

        public VatFluidHandler(IFluidHandler[] handlers) {
            super(handlers, false);
        };

        @Override
		public int fill(FluidStack resource, FluidAction action) {
            if (!DestroyFluids.isMixture(resource)) return 0;

            boolean simulate = action == FluidAction.SIMULATE;
            LegacyMixture addedMixture = LegacyMixture.readNBT(resource.getOrCreateChildTag("Mixture"));

            float existingLiquidVolume = combinedMixture.getLiquidVolume(vatCapacity);
            float addedLiquidVolume = addedMixture.getLiquidVolume(resource.getAmount());

            double amountScale = 1d;
            if (addedLiquidVolume > 0 && existingLiquidVolume + addedLiquidVolume > vatCapacity - 1) {
                if(!simulate) liquidFull = true;
                amountScale = (vatCapacity - 1 - existingLiquidVolume) / addedLiquidVolume;
            }

            int transferAmount = (int)(resource.getAmount() * amountScale);
            if(!simulate) {
                combinedMixture.mixWith((float)vatCapacity, addedMixture, (float)transferAmount);
                updateTankContents();
            }

            return transferAmount;
		};

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return DestroyFluids.isMixture(stack);
        };

    };

    public class VatFluidTank extends SmartFluidTank {

        private final boolean isForGas;
        protected boolean flushed;

        public VatFluidTank(int capacity, boolean isForGas, Consumer<FluidStack> updateCallback) {
            super(capacity, updateCallback);
            this.isForGas = isForGas;
            flushed = false;
        };

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return DestroyFluids.isMixture(stack);
        };

        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            flushed = false;
            if (fluid.getAmount() < getCapacity() && !isForGas) liquidFull = false;
        };

        @Override
        public void setFluid(FluidStack stack) {
            super.setFluid(stack);
            flushed = false;
            if (stack.getAmount() < getCapacity() && !isForGas) liquidFull = false;
        };

        public boolean isFullOfAir() {
            return isForGas && flushed;
        };

        @NotNull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            boolean simulate = action == FluidAction.SIMULATE;
            Phases phases = combinedMixture.separatePhases(vatCapacity);
            double liquidVolume = phases.liquidVolume();
            double gasVolume = vatCapacity - liquidVolume;

            if(isForGas) {
                double drained = Math.min(gasVolume, maxDrain);
                if(drained < 1.0) return FluidStack.EMPTY;

                if(!simulate) {
                    phases.gasMixture().scale((float)gasVolume);
                    combinedMixture.drainGas(vatCapacity, (float)drained);
                    updateTankContents();
                }

                int amount = Math.max((int)Math.round(drained), 1);
                phases.gasMixture().scale((float)(amount / drained));

                return MixtureFluid.of(amount, phases.gasMixture());
            } else {
                double drained = Math.min(liquidVolume, maxDrain);
                if(drained < 1.0) return FluidStack.EMPTY;

                if(!simulate) {
                    combinedMixture.drainLiquid(vatCapacity, (float)drained);
                    updateTankContents();
                }

                int amount = Math.max((int)Math.round(drained), 1);
                phases.liquidMixture().scale((float)(amount / drained));

                return MixtureFluid.of(amount, phases.liquidMixture());
            }
        };

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return capability.map(handler -> handler.fill(resource, action)).orElse(0);
        };
    };

    public class VatTankSegment extends SmartFluidTankBehaviour.TankSegment {

        public VatTankSegment(int capacity, boolean isForGas) {
            super(capacity);
            tank = new VatFluidTank(capacity, isForGas, f -> onFluidStackChanged());
        };

        public VatFluidTank getTank() {
            return (VatFluidTank)tank;
        };

    };

    /**
     * @deprecated Use {@link VatFluidTankBehaviour#getLiquidHandler()} and {@link VatFluidTankBehaviour#getGasHandler()} instead.
     */
    @Deprecated
    @Override
    public SmartFluidTank getPrimaryHandler() {
        return null;
    };

    /**
     * @deprecated Use {@link VatFluidTankBehaviour#getLiquidTank()} and {@link VatFluidTankBehaviour#getGasTank()} instead.
     */
    @Deprecated
    @Override
    public TankSegment getPrimaryTank() {
        return null;
    };
    
};
