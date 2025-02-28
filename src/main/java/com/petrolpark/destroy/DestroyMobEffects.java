package com.petrolpark.destroy;

import com.petrolpark.destroy.content.product.alcohol.HangoverMobEffect;
import com.petrolpark.destroy.content.product.alcohol.InebriationMobEffect;
import com.petrolpark.destroy.content.product.babyblue.BabyBlueHighMobEffect;
import com.petrolpark.destroy.content.product.babyblue.BabyBlueWithdrawalMobEffect;
import com.petrolpark.destroy.core.chemistry.hazard.mobeffect.ChemicalPoisonMobEffect;
import com.petrolpark.destroy.core.chemistry.hazard.mobeffect.CryingMobEffect;
import com.petrolpark.destroy.core.chemistry.hazard.mobeffect.LeadPoisoningMobEffect;
import com.petrolpark.destroy.core.mobeffect.DestroyMobEffect;
import com.petrolpark.destroy.core.mobeffect.UncurableMobEffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DestroyMobEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Destroy.MOD_ID);

    public static final RegistryObject<MobEffect> 

    CANCER = MOB_EFFECTS.register("cancer",
        () -> new UncurableMobEffect(MobEffectCategory.NEUTRAL, 0)
    ),
    CHEMICAL_POISON = MOB_EFFECTS.register("chemical_poison", ChemicalPoisonMobEffect::new),
    CRYING = MOB_EFFECTS.register("crying", CryingMobEffect::new),
    FRAGRANCE = MOB_EFFECTS.register("fragrance",
        () -> new DestroyMobEffect(MobEffectCategory.BENEFICIAL, 0xF294D9)
    ),
    FULL_BLADDER = MOB_EFFECTS.register("full_bladder",
        () -> new DestroyMobEffect(MobEffectCategory.NEUTRAL, 0xF7F75D)
    ),
    HANGOVER = MOB_EFFECTS.register("hangover", HangoverMobEffect::new),
    INEBRIATION = MOB_EFFECTS.register("inebriation", InebriationMobEffect::new),
    LEAD_POISONING = MOB_EFFECTS.register("lead_poisoning", LeadPoisoningMobEffect::new),
    BABY_BLUE_HIGH = MOB_EFFECTS.register("baby_blue_high", BabyBlueHighMobEffect::new),
    BABY_BLUE_WITHDRAWAL = MOB_EFFECTS.register("baby_blue_withdrawal", BabyBlueWithdrawalMobEffect::new),
    SUN_PROTECTION = MOB_EFFECTS.register("sun_protection",
        () -> new DestroyMobEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFE)
    );

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    };

    public static MobEffectInstance cancerInstance() {
        return new MobEffectInstance(CANCER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false, true);
    };

    @SuppressWarnings("null")
    public static void increaseEffectLevel(LivingEntity entity, final MobEffect effect, int level, int addedDurationPerLevel) {
        boolean infinite = addedDurationPerLevel == -1;
        if (entity.hasEffect(effect)) {
            int currentAmplifier = entity.getEffect(effect).getAmplifier(); // This is warned as being null
            int currentDuration = entity.getEffect(effect).getDuration(); // So is this
            entity.removeEffect(effect);
            int newLevel = currentAmplifier + level;
            if (newLevel <= 0) return;
            entity.addEffect(new MobEffectInstance(effect, infinite ? -1 : Math.max(currentDuration + (addedDurationPerLevel * level), 0), Math.max(currentAmplifier + level, 0), false, false, true));
        } else if (level >= 1) { // If the Entity is not already under the effect and we are attempting to add levels
            entity.addEffect(new MobEffectInstance(effect, infinite ? -1 : addedDurationPerLevel * level, level - 1, false, false, true));
        };
    };
};
