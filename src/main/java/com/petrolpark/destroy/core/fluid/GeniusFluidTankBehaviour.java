package com.petrolpark.destroy.core.fluid;

import java.util.Map;
import java.util.function.Consumer;

import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.chemistry.legacy.LegacyMixture;
import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;
import com.petrolpark.destroy.chemistry.minecraft.MixtureFluid;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;

import net.createmod.catnip.data.Iterate;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class GeniusFluidTankBehaviour extends SmartFluidTankBehaviour {

    /**
     * Mostly copied from the {@link com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour Create source code}, 
     * switching the {@link com.simibubi.create.foundation.fluid.SmartFluidTank SmartFluidTanks} for {@link GeniusFluidTank GeniusFluidTanks},
     * which allow Mixtures to be added even if the NBT does not exactly match. 
     * @param type Whether this is an input or output tank
     * @param be The BlockEntity to which this behaviour is attached
     * @param tanks The number of tanks this behaviour should have
     * @param tankCapacity The capacity (in mB) of each tank
     * @param enforceVariety Whether multiple tanks should be forbidden from holding the same type of fluid or not
     * @param similarMixturesOnly When true, only allow Mixtures to be added if they contain the same species
     */
    public GeniusFluidTankBehaviour(BehaviourType<SmartFluidTankBehaviour> type, SmartBlockEntity be, int tanks, int tankCapacity, boolean enforceVariety, boolean similarMixturesOnly) {
        super(type, be, tanks, tankCapacity, enforceVariety);
        IFluidHandler[] handlers = new IFluidHandler[tanks];
        for (int i = 0; i < tanks; i++) {
			GeniusTankSegment tankSegment = new GeniusTankSegment(tankCapacity);
			this.tanks[i] = tankSegment;
			handlers[i] = tankSegment.getTank().similarMixturesOnly(similarMixturesOnly);
		};
        capability = LazyOptional.of(() -> new InternalFluidHandler(handlers, enforceVariety));
    };

    public GeniusFluidTankBehaviour(BehaviourType<SmartFluidTankBehaviour> type, SmartBlockEntity be, int tanks, int tankCapacity, boolean enforceVariety) {
        this(type, be, tanks, tankCapacity, enforceVariety, false);
    };

    public void setCapacity(int capacity) {
        for (TankSegment tank : tanks) ((GeniusTankSegment)tank).setCapacity(capacity);
    };

    public class GeniusTankSegment extends TankSegment {

        public GeniusTankSegment(int capacity) {
            super(capacity);
            tank = new GeniusFluidTank(capacity, f -> onFluidStackChanged());
        };

        protected GeniusFluidTank getTank() {
            return (GeniusFluidTank)tank;  
        };

        protected void setCapacity(int capacity) {
            tank.setCapacity(capacity);
        };

    };

    public static class GeniusFluidTank extends SmartFluidTank {

        protected boolean similarMixturesOnly = false;
        public GeniusFluidTank(int capacity, Consumer<FluidStack> updateCallback) {
            super(capacity, updateCallback);
        };

        public GeniusFluidTank similarMixturesOnly(boolean b) {
            similarMixturesOnly = b;
            return this;
        }

        public boolean acceptsFluid(FluidStack resource) {
            if(fluid.isFluidEqual(resource)) return true;

            if (!DestroyFluids.isMixture(resource) || !DestroyFluids.isMixture(fluid)) return false;
            if (!resource.getOrCreateTag().contains("Mixture", Tag.TAG_COMPOUND) || !fluid.getOrCreateTag().contains("Mixture", Tag.TAG_COMPOUND)) return false;

            if(similarMixturesOnly) {
                // If similarMixturesOnly is true and the inserted mixture contains any molecule that isn't present
                // in the target mixture, forbid them from mixing together.
                // This is not a great solution but it's a duct tape fix that allows Basins to use GeniusFluidTank instances as their input tank
                // while still retaining their ability to hold two different liquids for processing (e.g. brine electrolysis).

                // We could just make brine electrolysis require a single mixture of water, mercury and sodium chloride but that's a lot less intuitive
                // for new players and mixing two mixtures together in equal amounts can be surprisingly difficult to automate, even with redstone siphons.

                LegacyMixture existingMixture = LegacyMixture.readNBT(fluid.getOrCreateTag().getCompound("Mixture"));
                LegacyMixture addedMixture = LegacyMixture.readNBT(resource.getOrCreateTag().getCompound("Mixture"));
                for(LegacySpecies m : addedMixture.getContents(false)) {
                    if(existingMixture.getConcentrationOf(m) <= 0f) return false;
                }
            }

            return true;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            int filled = super.fill(resource, action);
            if (filled == 0 && getSpace() > 0 && acceptsFluid(resource)) { // If we wouldn't usually be able to insert, and we're not full (i.e. the Fluids are 'different')
                if (!DestroyFluids.isMixture(resource) || !DestroyFluids.isMixture(fluid)) return 0;
                if (!resource.getOrCreateTag().contains("Mixture", Tag.TAG_COMPOUND) || !fluid.getOrCreateTag().contains("Mixture", Tag.TAG_COMPOUND)) return 0;
                LegacyMixture existingMixture = LegacyMixture.readNBT(fluid.getOrCreateTag().getCompound("Mixture"));
                LegacyMixture addedMixture = LegacyMixture.readNBT(resource.getOrCreateTag().getCompound("Mixture"));

                int amountOfMixtureAdded = Math.min(getSpace(), resource.getAmount());
                int existingAmount = fluid.getAmount();
                if (!action.simulate()) { // We don't need to do anything further if we're just simulating
                    LegacyMixture newMixture = LegacyMixture.mix(Map.of(existingMixture, (double)existingAmount, addedMixture, (double)amountOfMixtureAdded));
                    setFluid(MixtureFluid.of(existingAmount + amountOfMixtureAdded, newMixture));
                };

                return amountOfMixtureAdded; 
            };
            return filled;
        };

        @Override
        public FluidStack drain(int maxDrain, FluidAction action)
        {
            FluidStack stack = super.drain(maxDrain, action);
            // Replace the held fluid with an empty fluid stack if we completely emptied this tank
            // This clears mixture data and allows empty containers of the same type to stack
            if(fluid.isEmpty() && fluid.getRawFluid() != Fluids.EMPTY)
                setFluid(FluidStack.EMPTY);
            return stack;
        }

    };

    public class InternalFluidHandler extends SmartFluidTankBehaviour.InternalFluidHandler {

        public InternalFluidHandler(IFluidHandler[] handlers, boolean enforceVariety) {
            super(handlers, enforceVariety);
        }

        public boolean acceptsFluid(IFluidHandler handler, int tank, FluidStack resource) {
            return (handler instanceof GeniusFluidTank geniusFluidTank) ? geniusFluidTank.acceptsFluid(resource) : handler.getFluidInTank(tank).isFluidEqual(resource);
        }

        /*
        * Mostly copied from {@link com.simibubi.create.foundation.fluid.CombinedTankWrapper} with a more loose fluid equality check.
        * If this handler has multiple tanks and enforceVariety is true, only one tank is allowed to hold a Mixture.
        * */
        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (!insertionAllowed)
                return 0;

            if (resource.isEmpty())
                return 0;

            int filled = 0;
            resource = resource.copy();

            boolean fittingHandlerFound = false;
            Outer: for (boolean searchPass : Iterate.trueAndFalse) {
                for (IFluidHandler iFluidHandler : itemHandler) {

                    for (int i = 0; i < iFluidHandler.getTanks(); i++)
                        if (searchPass && acceptsFluid(iFluidHandler, i, resource))
                            fittingHandlerFound = true;

                    if (searchPass && !fittingHandlerFound)
                        continue;

                    int filledIntoCurrent = iFluidHandler.fill(resource, action);
                    resource.shrink(filledIntoCurrent);
                    filled += filledIntoCurrent;

                    if (resource.isEmpty())
                        break Outer;
                    if (fittingHandlerFound && (enforceVariety || filledIntoCurrent != 0))
                        break Outer;
                }
            }

            return filled;
        }
    }

};
