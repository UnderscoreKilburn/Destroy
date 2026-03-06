package com.petrolpark.destroy.content.processing.centrifuge;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyAdvancementTrigger;
import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.chemistry.legacy.LegacyMixture;
import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;
import com.petrolpark.destroy.chemistry.legacy.LegacyMixture.Phases;
import com.petrolpark.destroy.chemistry.minecraft.MixtureFluid;
import com.petrolpark.destroy.client.DestroyLang;
import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.content.processing.centrifuge.CentrifugeBlockEntity.PhasedMoleculeGroup.PhasedMolecule;
import com.petrolpark.destroy.content.processing.centrifuge.potion.PotionSeparationRecipes;
import com.petrolpark.destroy.core.block.entity.IDirectionalOutputFluidBlockEntity;
import com.petrolpark.destroy.core.block.entity.IHaveLabGoggleInformation;
import com.petrolpark.destroy.core.chemistry.MixtureContentsDisplaySource;
import com.petrolpark.destroy.core.data.advancement.DestroyAdvancementBehaviour;
import com.petrolpark.destroy.core.fluid.GeniusFluidTankBehaviour;
import com.petrolpark.destroy.core.pollution.PollutingBehaviour;
import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.fluids.potion.PotionFluid.BottleType;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.infrastructure.config.AllConfigs;

import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.registries.ForgeRegistries;

public class CentrifugeBlockEntity extends KineticBlockEntity implements IDirectionalOutputFluidBlockEntity, IHaveLabGoggleInformation {

    private static final Object centrifugationRecipeKey = new Object();

    // SmartBlockEntity only supports one behaviour per type and weird things happen when multiple behaviours with the
    // same type are added, so let's make our own type for the second output.
    // The correct solution would be to have only one output behaviour with two tanks but I can't figure out an elegant way
    // to make each side access a different tank so this will have to do for now.
    public static final BehaviourType<SmartFluidTankBehaviour> OUTPUT2 = new BehaviourType<>("Output2");

    private SmartFluidTankBehaviour inputTank, denseOutputTank, lightOutputTank;
    protected LazyOptional<IFluidHandler> allFluidCapability;

    protected DestroyAdvancementBehaviour advancementBehaviour;
    protected PollutingBehaviour pollutingBehaviour;

    private Direction denseOutputTankFace;

    public int timer;
    private CentrifugationRecipe lastRecipe;

    private boolean pondering; // Whether this Centrifuge is in a Ponder Scene

    public CentrifugeBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        denseOutputTankFace = state.getValue(CentrifugeBlock.DENSE_OUTPUT_FACE);
        pondering = false;
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        inputTank = new GeniusFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 1, getEachTankCapacity(), true)
            .whenFluidUpdates(this::onFluidStackChanged);
        denseOutputTank = new GeniusFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 1, getEachTankCapacity(), true)
            .whenFluidUpdates(this::onFluidStackChanged)
            .forbidInsertion();
        lightOutputTank = new GeniusFluidTankBehaviour(OUTPUT2, this, 1, getEachTankCapacity(), true)
            .whenFluidUpdates(this::onFluidStackChanged)
            .forbidInsertion();
        behaviours.addAll(List.of(inputTank, denseOutputTank, lightOutputTank));

        allFluidCapability = LazyOptional.of(() -> {
            return new CombinedTankWrapper(inputTank.getCapability().orElse(null), denseOutputTank.getCapability().orElse(null), lightOutputTank.getCapability().orElse(null));
        });

        advancementBehaviour = new DestroyAdvancementBehaviour(this, DestroyAdvancementTrigger.USE_CENTRIFUGE);
        behaviours.add(advancementBehaviour);

        pollutingBehaviour = new PollutingBehaviour(this);
        behaviours.add(pollutingBehaviour);
    };

    /**
     * Attempts to rotate the Centrifuge so that it faces a new face which also has a Pipe. If no Pipe is available, just rotates it anyway.
     * @param shouldSwitch Whether the rotation should prioritise switching faces or staying on the current face
     * @return Whether the Centrifuge was rotated
     */
    @SuppressWarnings("null")
    public boolean attemptRotation(boolean shouldSwitch) {
        if (!hasLevel()) return false;
        if (getLevel().setBlock(getBlockPos(), getBlockState().setValue(CentrifugeBlock.DENSE_OUTPUT_FACE, refreshDirection(this, shouldSwitch ? denseOutputTankFace.getClockWise() : denseOutputTankFace, getDenseOutputTank(), true)), 6)) { // If the output Direction can be successfully changed
            denseOutputTankFace = getBlockState().getValue(CentrifugeBlock.DENSE_OUTPUT_FACE);
            notifyUpdate(); // Block State has changed
            return true;
        };
        return false;
    };

    @Override
    @SuppressWarnings("null")
    public void tick() {
        super.tick();
        if (!hasLevel()) return; // Don't do anything if we're not in a Level
        if (getSpeed() == 0) return; // Don't do anything without rotational power
        if (isTankFull(getDenseOutputTank()) || isTankFull(getLightOutputTank())) return; // Don't do anything if output is full
        if (timer > 0) {
            timer -= getProcessingSpeed();
            if (getLevel().isClientSide()) { // It thinks getLevel() can be null (it can't)
                spawnParticles();
                return;
            };
            if (timer <= 0) {
                process();
            };
            sendData();
            return;
        };
        if (inputTank.isEmpty()) return; // Don't do anything more if input Tank is empty

        if (lastRecipe == null || !lastRecipe.getRequiredFluid().test(getInputTank().getFluid())) { // If the Recipe has changed
            FluidStack inputFluidStack = getInputTank().getFluid();

            // Standard recipes
            List<Recipe<?>> possibleRecipes = RecipeFinder.get(centrifugationRecipeKey, getLevel(), r -> r.getType() == DestroyRecipeTypes.CENTRIFUGATION.getType()).stream().filter(r -> {
                CentrifugationRecipe recipe = (CentrifugationRecipe) r;
                if (!recipe.isValidAt(getLevel(), getBlockPos())) return false; // Biome-specific recipes
                if (!recipe.getRequiredFluid().test(inputFluidStack)) return false; // If there is insufficient input Fluid
                if (!canFitFluidInTank(recipe.getDenseOutputFluid(), getDenseOutputTank()) || !canFitFluidInTank(recipe.getLightOutputFluid(), getLightOutputTank())) return false; // If the outputs can't fit
                return true;
            }).collect(Collectors.toList());

            // Potion separation
            if (AllConfigs.server().recipes.allowBrewingInMixer.get() && inputFluidStack.getFluid().isSame(AllFluids.POTION.get()) && inputFluidStack.hasTag()) {
                Potion potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(inputFluidStack.getOrCreateTag().getString("Potion")));
                BottleType bottleType = NBTHelper.readEnum(inputFluidStack.getOrCreateTag(), "BottleType", BottleType.class);
                if (potion != null) {
                    CentrifugationRecipe potionSeparationRecipe = PotionSeparationRecipes.ALL.get(Pair.of(potion, bottleType));
                    if (potionSeparationRecipe != null)
                    possibleRecipes.add(potionSeparationRecipe);
                };
            };

            if (possibleRecipes.size() >= 1) {
                lastRecipe = (CentrifugationRecipe)possibleRecipes.get(0);
            } else { // If no recipe could be found
                lastRecipe = null;
            };
        };

        if (lastRecipe == null) {
            timer = 100; // If we have no Recipe, don't try checking again for another 100 ticks
        } else {
            timer = lastRecipe.getProcessingDuration();
        };

        sendData();
    };

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        timer = compound.getInt("Timer");
        getInputTank().readFromNBT(compound.getCompound("InputTank"));
        getDenseOutputTank().readFromNBT(compound.getCompound("DenseOutputTank"));
        getLightOutputTank().readFromNBT(compound.getCompound("LightOutputTank"));
        denseOutputTankFace = getBlockState().getValue(CentrifugeBlock.DENSE_OUTPUT_FACE);
        super.read(compound, clientPacket);
    };

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("Timer", timer);
        compound.put("InputTank", getInputTank().writeToNBT(new CompoundTag()));
        compound.put("DenseOutputTank", getDenseOutputTank().writeToNBT(new CompoundTag()));
        compound.put("LightOutputTank", getLightOutputTank().writeToNBT(new CompoundTag()));
        super.write(compound, clientPacket);
    };

    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 512);
    };

    public SmartFluidTank getInputTank() {
        return inputTank.getPrimaryHandler();
    };

    public SmartFluidTank getDenseOutputTank() {
        return denseOutputTank.getPrimaryHandler();
    };

    public SmartFluidTank getLightOutputTank() {
        return lightOutputTank.getPrimaryHandler();
    };

    protected static class PhasedMoleculeGroup {
        float totalVolume = 0f;
        float totalCharge = 0f;

        List<PhasedMolecule> molecules = new ArrayList<>();
        record PhasedMolecule(LegacySpecies molecule, float moles, float volume, boolean gas, float charge) {}

        void add(LegacySpecies molecule, float moles, float volume, boolean gas) {
            float charge = moles * molecule.getCharge();
            molecules.add(new PhasedMolecule(molecule, moles, volume, gas, charge));
            totalVolume += volume;
            totalCharge += charge;
        }

        boolean tryBalanceCharge(PhasedMoleculeGroup other) {
            if(totalCharge == 0f) return true;

            ListIterator<PhasedMolecule> it = other.molecules.listIterator();
            while(it.hasNext()) {
                PhasedMolecule m = it.next();
                if(m.charge != 0f && (totalCharge > 0f) != (m.charge > 0f)) {
                    boolean finished = Math.abs(m.charge) >= Math.abs(totalCharge);
                    float toTransfer = Math.min(1f, -totalCharge / m.charge);
                    float toKeep = 1f - toTransfer;
                    add(m.molecule, m.moles * toTransfer, m.volume * toTransfer, m.gas);

                    if(toKeep <= 0f) {
                        other.totalCharge -= m.charge;
                        other.totalVolume -= m.volume;
                        it.remove();
                    } else {
                        other.totalCharge -= m.charge * toTransfer;
                        other.totalVolume -= m.volume * toTransfer;
                        it.set(new PhasedMolecule(m.molecule, m.moles * toKeep, m.volume * toKeep, m.gas, m.charge * toKeep));
                    }

                    if(finished)
                        return true;
                }
            }

            return false;
        }

        void forceChargeToZero() {
            if(totalCharge == 0f) return;

            ListIterator<PhasedMolecule> it = molecules.listIterator();
            while(it.hasNext()) {
                PhasedMolecule m = it.next();
                if (m.charge != 0f && (totalCharge > 0f) == (m.charge > 0f)) {
                    boolean finished = Math.abs(m.charge) >= Math.abs(totalCharge);
                    float toRemove = Math.min(1f, totalCharge / m.charge);
                    float toKeep = 1f - toRemove;

                    if(toKeep <= 0f) {
                        totalCharge -= m.charge;
                        totalVolume -= m.volume;
                        it.remove();
                    } else {
                        totalCharge -= m.charge * toRemove;
                        totalVolume -= m.volume * toRemove;
                        it.set(new PhasedMolecule(m.molecule, m.moles * toKeep, m.volume * toKeep, m.gas, m.charge * toKeep));
                    }

                    if(finished)
                        return;
                }
            }
        }
    }

    protected static class MixtureBuilder {
        Map<LegacySpecies, Couple<Float>> contents;
        float volume;
        float temperature;

        MixtureBuilder(float volume, float temperature) {
            this.volume = volume;
            this.temperature = temperature;
            this.contents = new HashMap<>();
        }

        void add(LegacySpecies molecule, float moles, boolean gas) {
            Couple<Float> c = contents.computeIfAbsent(molecule, m -> Couple.create(0f, 0f));
            c.set(!gas, c.get(!gas) + moles);
        }

        LegacyMixture build() {
            LegacyMixture mixture = new LegacyMixture();
            mixture.setTemperature(temperature);
            for(Entry<LegacySpecies, Couple<Float>> e : contents.entrySet()) {
                float totalMoles = e.getValue().getFirst() + e.getValue().getSecond();
                float state = e.getValue().getSecond() / totalMoles;
                mixture.addMolecule(e.getKey(), totalMoles / volume);
                mixture.setState(e.getKey(), state);
            }
            return mixture;
        }
    }

    public void process() {
        if(lastRecipe == null) {
            if(DestroyFluids.isMixture(getInputTank().getFluid())) {
                LegacyMixture mixture = LegacyMixture.readNBT(getInputTank().getFluid().getOrCreateChildTag("Mixture"));
                boolean debug = false;

                if (mixture.isEmpty()) return;

                if(debug) {
                    float totalCharge = 0f;
                    Destroy.LOGGER.info("== raw mixture ==");
                    for (var m : mixture.getContents(false)) {
                        Destroy.LOGGER.info("    {}: {}", m.getName(false), mixture.getConcentrationOf(m) * getInputTank().getFluidAmount());
                        totalCharge += m.getCharge() * mixture.getConcentrationOf(m) * getInputTank().getFluidAmount();
                    }
                    Destroy.LOGGER.info("    total charge: {}", totalCharge);
                }

                // Don't go any further if either output tank can't take Mixture
                if (!(DestroyFluids.isMixture(getDenseOutputTank().getFluid()) || getDenseOutputTank().isEmpty()) || !(DestroyFluids.isMixture(getLightOutputTank().getFluid()) || getLightOutputTank().isEmpty())) return;

                // Determine how much can be processed
                int amount = IntStream.of(2*(getInputTank().getFluidAmount()/2), getDenseOutputTank().getSpace() * 2, getLightOutputTank().getSpace() * 2).min().getAsInt();
                if (amount == 0) return; // If either of the two output tanks can't fit anything at all, give up

                Phases phases = mixture.separatePhases(amount);
                LegacyMixture liquidMixture = phases.liquidMixture();
                LegacyMixture gasMixture = phases.gasMixture();

                float liquidVolume = phases.liquidVolume().floatValue();
                float gasVolume = amount - liquidVolume;

                if(debug) {
                    float totalCharge = 0f;
                    Destroy.LOGGER.info("== phase separated mixture ==");
                    for (var m : mixture.getContents(false)) {
                        Destroy.LOGGER.info("    {}: liquid {} | gas {} | total {}", m.getName(false),
                            liquidMixture.getConcentrationOf(m) * liquidVolume,
                            gasMixture.getConcentrationOf(m),
                            liquidMixture.getConcentrationOf(m) * liquidVolume + gasMixture.getConcentrationOf(m)
                        );
                        totalCharge += m.getCharge() * (liquidMixture.getConcentrationOf(m) * liquidVolume + gasMixture.getConcentrationOf(m));
                    }
                    Destroy.LOGGER.info("    total charge: {}", totalCharge);
                }

                // Sort all the molecules in this Mixture by density (from highest to lowest)
                // Molecules with the exact same density are grouped into the same bucket
                TreeMap<Float, PhasedMoleculeGroup> sortedMolecules = new TreeMap<>(Comparator.reverseOrder());

                for(LegacySpecies molecule : liquidMixture.getContents(false)) {
                    float moles = liquidMixture.getConcentrationOf(molecule) * liquidVolume;
                    float volume = moles / molecule.getPureConcentration();
                    //float density = moles * molecule.getMass() / volume;
                    float density = molecule.getDensity(); // Skip the math and avoid floating point errors by directly getting the density from the molecule itself

                    sortedMolecules.computeIfAbsent(density, d -> new PhasedMoleculeGroup()).add(molecule, moles, volume, false);
                }

                float totalGasMoles = gasMixture.getTotalConcentration();
                for(LegacySpecies molecule : gasMixture.getContents(false)) {
                    float moles = gasMixture.getConcentrationOf(molecule); // gasMixture is returned with an implied volume of 1 so number of moles = concentration
                    float volume = gasVolume * moles / totalGasMoles;
                    float density = moles * molecule.getMass() / volume;

                    sortedMolecules.computeIfAbsent(density, d -> new PhasedMoleculeGroup()).add(molecule, moles, volume, true);
                }

                if(debug) {
                    float totalCharge = 0f;
                    Destroy.LOGGER.info("== sorted molecules ==");
                    for (var e : sortedMolecules.entrySet()) {
                        Destroy.LOGGER.info("  {}: volume={}, charge={}", e.getKey(), e.getValue().totalVolume, e.getValue().totalCharge);
                        for (var m : e.getValue().molecules) {
                            Destroy.LOGGER.info("    {}: volume={}, moles={}, charge={}", m.molecule.getName(false), m.volume, m.moles, m.charge);
                        }
                        totalCharge += e.getValue().totalCharge;
                    }
                    Destroy.LOGGER.info("  total charge: {}", totalCharge);
                }

                // If any groups contain ions and have a non-zero charge, pull in counter-ions from the next groups to balance it out
                List<PhasedMoleculeGroup> sortedMoleculeList = sortedMolecules.values().stream().toList();
                ListIterator<PhasedMoleculeGroup> it = sortedMoleculeList.listIterator();

                while(it.hasNext()) {
                    PhasedMoleculeGroup group = it.next();
                    ListIterator<PhasedMoleculeGroup> it2 = sortedMoleculeList.listIterator(it.nextIndex());
                    boolean balanced = false;
                    while(it2.hasNext()) {
                        if(group.tryBalanceCharge(it2.next())) {
                            balanced = true;
                            break;
                        }
                    }

                    // If there is any leftover charge, cheat a little and remove ions from this group to ensure the total charge is zero
                    // If the original mixture didn't have unbalanced ions, this should usually result in a negligible loss
                    if(!balanced)
                        group.forceChargeToZero();
                }

                if(debug) {
                    float totalCharge = 0f;
                    Destroy.LOGGER.info("== sorted molecules (balanced) ==");
                    for (var g : sortedMoleculeList) {
                        Destroy.LOGGER.info("  volume={}, charge={}", g.totalVolume, g.totalCharge);
                        for (var m : g.molecules) {
                            Destroy.LOGGER.info("    {}: volume={}, moles={}, charge={}", m.molecule.getName(false), m.volume, m.moles, m.charge);
                        }
                        totalCharge += g.totalCharge;
                    }
                    Destroy.LOGGER.info("  total charge: {}", totalCharge);
                }

                // Distribute the sorted molecules into the dense output first, then the light output
                float denseMixtureVolume = (float)Math.ceil(amount / 2f);
                float lightMixtureVolume = amount - denseMixtureVolume;
                float remaining = denseMixtureVolume;

                MixtureBuilder denseMixtureBuilder = new MixtureBuilder(denseMixtureVolume, mixture.getTemperature());
                MixtureBuilder lightMixtureBuilder = new MixtureBuilder(lightMixtureVolume, mixture.getTemperature());

                it = sortedMoleculeList.listIterator();
                while(it.hasNext()) {
                    PhasedMoleculeGroup group = it.next();
                    if(remaining > 0f && remaining <= group.totalVolume) {
                        float fraction = remaining / group.totalVolume;
                        if(group.totalVolume - remaining < 1e-3f) fraction = 1f;

                        if(remaining > 1e-3f) {
                            float f = fraction;
                            group.molecules.replaceAll(m -> {
                                denseMixtureBuilder.add(m.molecule, m.moles * f, m.gas);
                                return new PhasedMolecule(m.molecule, m.moles * (1f - f), m.volume * (1f - f), m.gas, m.charge * (1f - f));
                            });
                            group.totalVolume *= 1f - f;
                            group.totalCharge *= 1f - f;
                        }

                        if(group.totalVolume > 0.001f)
                            it.previous();

                        remaining = 0f;
                    } else {
                        MixtureBuilder currentMixtureBuilder = remaining > 0f ? denseMixtureBuilder : lightMixtureBuilder;
                        group.molecules.forEach(m -> {
                            currentMixtureBuilder.add(m.molecule, m.moles, m.gas);
                        });
                        remaining -= group.totalVolume;
                    }
                }

                LegacyMixture denseMixture = denseMixtureBuilder.build();
                LegacyMixture lightMixture = lightMixtureBuilder.build();

                if(debug) {
                    float totalCharge = 0f;
                    Destroy.LOGGER.info("== final mixtures ==");
                    float denseCharge = 0f;
                    float lightCharge = 0f;
                    for (var m : mixture.getContents(false)) {
                        Destroy.LOGGER.info("    {}: dense {} | light {} | total {}", m.getName(false),
                            denseMixture.getConcentrationOf(m) * denseMixtureVolume,
                            lightMixture.getConcentrationOf(m) * lightMixtureVolume,
                            denseMixture.getConcentrationOf(m) * denseMixtureVolume + lightMixture.getConcentrationOf(m) * lightMixtureVolume
                        );
                        denseCharge += m.getCharge() * denseMixture.getConcentrationOf(m) * denseMixtureVolume;
                        lightCharge += m.getCharge() * lightMixture.getConcentrationOf(m) * lightMixtureVolume;
                        totalCharge += m.getCharge() * (denseMixture.getConcentrationOf(m) * denseMixtureVolume + lightMixture.getConcentrationOf(m) * lightMixtureVolume);
                    }
                    Destroy.LOGGER.info("    total charge: dense {} | light {} | total {}", denseCharge, lightCharge, totalCharge);
                }

                // If we got to this point, the Fluid can be successfully processed
                getInputTank().drain(amount, FluidAction.EXECUTE);
                getDenseOutputTank().fill(MixtureFluid.of((int)denseMixtureVolume, denseMixture), FluidAction.EXECUTE);
                getLightOutputTank().fill(MixtureFluid.of((int)lightMixtureVolume, lightMixture), FluidAction.EXECUTE);
            } else { // If there is no Mixture to Centrifuge
                return;
            }
        } else { // If there is a Recipe
            if (!canFitFluidInTank(lastRecipe.getDenseOutputFluid(), getDenseOutputTank()) || !canFitFluidInTank(lastRecipe.getLightOutputFluid(), getLightOutputTank()) || hasFluidInTank(lastRecipe.getRequiredFluid(), getLightOutputTank())) return; // Ensure the Recipe can still be Processed
            getInputTank().drain(lastRecipe.getRequiredFluid().getRequiredAmount(), FluidAction.EXECUTE);
            getDenseOutputTank().fill(lastRecipe.getDenseOutputFluid(), FluidAction.EXECUTE);
            getLightOutputTank().fill(lastRecipe.getLightOutputFluid(), FluidAction.EXECUTE);
        }
        advancementBehaviour.awardDestroyAdvancement(DestroyAdvancementTrigger.USE_CENTRIFUGE);
        notifyUpdate();
    }

    @SuppressWarnings("null")
    public void spawnParticles() {
        FluidStack fluidStack = inputTank.getPrimaryHandler().getFluid();
        if (fluidStack.isEmpty() || !hasLevel()) return;

        RandomSource random = getLevel().getRandom(); // It thinks getLevel() might be null

        ParticleOptions data = FluidFX.getFluidParticle(fluidStack);
        float angle = random.nextFloat() * 360;
        Vec3 offset = new Vec3(0, 0, 0.7f);
        offset = VecHelper.rotate(offset, angle, Axis.Y);
        Vec3 target = VecHelper.rotate(offset, getSpeed() > 0 ? 25 : -25, Axis.Y);

        Vec3 center = offset.add(VecHelper.getCenterOf(worldPosition));
        target = VecHelper.offsetRandomly(target.subtract(offset), random, 1/ 128f);
        getLevel().addParticle(data, center.x, center.y, center.z, target.x, target.y, target.z); // It thinks getLevel() might be null
    };

    @Nonnull
    @Override
    @SuppressWarnings("null")
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            if (side == Direction.UP) {
                return inputTank.getCapability().cast();
            } else if (side == Direction.DOWN) {
                return lightOutputTank.getCapability().cast();
            } else if (side == denseOutputTankFace || pondering) {
                return denseOutputTank.getCapability().cast();
            } else if (side == null) { // For the PollutingBehaviour, it needs all tanks
                return allFluidCapability.cast();
            };
        };
        return super.getCapability(cap, side);
    };

    @Override
    public void invalidate() {
        super.invalidate();
        allFluidCapability.invalidate();
    };

    public int getEachTankCapacity() {
        return DestroyAllConfigs.SERVER.blocks.centrifugeCapacity.get();
    };

    private void onFluidStackChanged() {
        notifyUpdate();
    };

    /**
     * Let this Centrifuge know we're in a Ponder.
     * This makes it so the dense Fluid can be pulled from any side.
     */
    public void setPondering() {
        pondering = true;
    };

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        // if (MAX_LUBRICATION_LEVEL != 0) {
        //     DestroyLang.translate("tooltip.centrifuge.lubrication")
        //         .style(ChatFormatting.WHITE)
        //         .space()
        //         .add(DestroyLang.barMeterComponent(lubricationLevel, MAX_LUBRICATION_LEVEL, Math.min(MAX_LUBRICATION_LEVEL, 20)))
        //         .forGoggles(tooltip);
        // };

        DestroyLang.fluidContainerInfoHeader(tooltip);
        DestroyLang.tankInfoTooltip(tooltip, DestroyLang.translate("tooltip.centrifuge.input_tank"), getInputTank());
        DestroyLang.tankInfoTooltip(tooltip, DestroyLang.translate("tooltip.centrifuge.dense_output_tank"), getDenseOutputTank());
        DestroyLang.tankInfoTooltip(tooltip, DestroyLang.translate("tooltip.centrifuge.light_output_tank"), getLightOutputTank());
        
        return true;
    };

    public static class CentrifugeDisplaySource extends MixtureContentsDisplaySource {

        private final Function<CentrifugeBlockEntity, SmartFluidTank> tankGetter;
        private final String tankId;

        private CentrifugeDisplaySource(String tankId, Function<CentrifugeBlockEntity, SmartFluidTank> tankGetter) {
            super(false);
            this.tankId = tankId;
            this.tankGetter = tankGetter;
        };

        public static CentrifugeDisplaySource createInput() {
            return new CentrifugeDisplaySource("input", CentrifugeBlockEntity::getInputTank);
        }

        public static CentrifugeDisplaySource createDenseOutput() {
            return new CentrifugeDisplaySource("dense_output", CentrifugeBlockEntity::getDenseOutputTank);
        }

        public static CentrifugeDisplaySource createLightOutput() {
            return new CentrifugeDisplaySource("light_output", CentrifugeBlockEntity::getLightOutputTank);
        }

        @Override
        public FluidStack getFluidStack(DisplayLinkContext context) {
            if (context.getSourceBlockEntity() instanceof CentrifugeBlockEntity centrifuge) {
                return tankGetter.apply(centrifuge).getFluid();
            };
            return FluidStack.EMPTY;
        };

        @Override
        public Component getName() {
            return DestroyLang.translate("display_source.centrifuge."+tankId).component();
        };
    };
};
