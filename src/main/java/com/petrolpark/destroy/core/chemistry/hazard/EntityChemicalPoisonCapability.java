package com.petrolpark.destroy.core.chemistry.hazard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.petrolpark.destroy.DestroyMessages;
import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class EntityChemicalPoisonCapability {
  
    private LegacySpecies molecule;

    public static void setMolecule(Entity entity, LegacySpecies molecule) {
        if (!(entity instanceof LivingEntity)) return;
        entity.getCapability(Provider.ENTITY_CHEMICAL_POISON).ifPresent(cp -> {
            if (cp.molecule != null) return; // Don't replace existing poison
            cp.molecule = molecule;
            if (entity instanceof ServerPlayer serverPlayer) DestroyMessages.sendToClient(new ChemicalPoisonS2CPacket(molecule), serverPlayer);
        });
    };

    public static void removeMolecule(Entity entity) {
        if (!(entity instanceof LivingEntity)) return;
        entity.getCapability(Provider.ENTITY_CHEMICAL_POISON).ifPresent(cp -> {
            cp.molecule = null;
            if (entity instanceof ServerPlayer serverPlayer) DestroyMessages.sendToClient(new ChemicalPoisonS2CPacket((LegacySpecies)null), serverPlayer);
        });
    };

    @Nullable
    public LegacySpecies getMolecule() {
        return molecule;
    };

    public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static Capability<EntityChemicalPoisonCapability> ENTITY_CHEMICAL_POISON = CapabilityManager.get(new CapabilityToken<EntityChemicalPoisonCapability>() {});

        private EntityChemicalPoisonCapability chemicalPoison = null;
        private final LazyOptional<EntityChemicalPoisonCapability> optional = LazyOptional.of(this::createEntityChemicalPoison);

        private EntityChemicalPoisonCapability createEntityChemicalPoison() {
            if (chemicalPoison == null) chemicalPoison = new EntityChemicalPoisonCapability();
            return chemicalPoison;
        };

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            if (createEntityChemicalPoison().molecule != null)
                tag.putString("ToxicMolecule", createEntityChemicalPoison().molecule.getFullID());
            return tag;
        };

        @Override
        public void deserializeNBT(CompoundTag tag) {
            if (tag.contains("ToxicMolecule", Tag.TAG_STRING))
                createEntityChemicalPoison().molecule = LegacySpecies.getMolecule(tag.getString("ToxicMolecule"));
        };

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (cap == ENTITY_CHEMICAL_POISON) return optional.cast();
            return LazyOptional.empty();
        };

    };
};
