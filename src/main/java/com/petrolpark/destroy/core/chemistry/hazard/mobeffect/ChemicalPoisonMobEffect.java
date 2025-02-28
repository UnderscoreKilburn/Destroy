package com.petrolpark.destroy.core.chemistry.hazard.mobeffect;

import java.util.List;
import java.util.function.Consumer;

import com.petrolpark.destroy.DestroyDamageSources;
import com.petrolpark.destroy.DestroyMobEffects;
import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;
import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.core.chemistry.hazard.EntityChemicalPoisonCapability;
import com.petrolpark.destroy.core.mobeffect.UncurableMobEffect;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import net.minecraftforge.common.util.LazyOptional;

public class ChemicalPoisonMobEffect extends UncurableMobEffect {

    public ChemicalPoisonMobEffect() {
        super(MobEffectCategory.HARMFUL, 0xFFFFFF);
    };
    
    @Override
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        consumer.accept(new ChemicalPoisonMobEffectExtensions());
    };

    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(livingEntity, attributeMap, amplifier);
        EntityChemicalPoisonCapability.removeMolecule(livingEntity);
    };

    @Override
    @SuppressWarnings("null") // We know the effect isn't null if its ticking
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide()) {
            int duration = livingEntity.getEffect(DestroyMobEffects.CHEMICAL_POISON.get()).getDuration(); // This is the bit it says is null
            if (duration % 50 == 0) {
                LegacySpecies molecule = null;
                LazyOptional<EntityChemicalPoisonCapability> cap = getCap(livingEntity);
                if (cap.isPresent()) molecule = cap.resolve().get().getMolecule();
                livingEntity.hurt(DestroyDamageSources.chemicalPoison(livingEntity.level(), molecule), 1f);
            };
        };
        super.applyEffectTick(livingEntity, amplifier);
    };

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    };

    private static LazyOptional<EntityChemicalPoisonCapability> getCap(LivingEntity livingEntity) {
        return livingEntity.getCapability(EntityChemicalPoisonCapability.Provider.ENTITY_CHEMICAL_POISON);
    };

    public static class ChemicalPoisonMobEffectExtensions extends DestroyMobEffectExtensions {

        @Override
        public void addToTooltip(List<Component> tooltip, MobEffectInstance instance) {
            super.addToTooltip(tooltip, instance);
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
                minecraft.player.getCapability(EntityChemicalPoisonCapability.Provider.ENTITY_CHEMICAL_POISON).ifPresent(cp -> {
                    LegacySpecies molecule = cp.getMolecule();
                    if (molecule != null) tooltip.set(0, Component.translatable("effect.destroy.chemical_poison.molecule", cp.getMolecule().getName(DestroyAllConfigs.CLIENT.chemistry.iupacNames.get())));
                });
            };
        };
    };
    
};
