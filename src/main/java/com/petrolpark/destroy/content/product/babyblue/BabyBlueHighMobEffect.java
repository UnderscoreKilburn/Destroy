package com.petrolpark.destroy.content.product.babyblue;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyAdvancementTrigger;
import com.petrolpark.destroy.DestroyMobEffects;
import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.config.DestroySubstancesConfigs;
import com.petrolpark.destroy.core.mobeffect.UncurableMobEffect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Destroy.MOD_ID)
public class BabyBlueHighMobEffect extends UncurableMobEffect {

    public BabyBlueHighMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x8BDCEB);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "31875c8a-f500-477c-ac52-70355c6adc12", (double)0.3F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, "0a7d851c-b38b-47c8-9131-348a492e3af8", (double)0.9F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "93bfb982-7e97-472f-b2f6-a0c51b4d916f", (double)2.0F, AttributeModifier.Operation.ADDITION);
    };

    @Override
    @SuppressWarnings("null") // We know the effect isn't null if its ticking
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide()) {
            int duration = livingEntity.getEffect(DestroyMobEffects.BABY_BLUE_HIGH.get()).getDuration(); // This is the bit it says is null
            if (duration == 1) {
                // Apply the Baby Blue Withdrawal Effect as the BabyBlue High Effect runs out.
                if (livingEntity instanceof Player player) {
                    player.getCapability(PlayerBabyBlueAddictionCapability.CAPABILITY).ifPresent(babyBlueAddiction -> {
                        player.addEffect(new MobEffectInstance(DestroyMobEffects.BABY_BLUE_WITHDRAWAL.get(), (10 + babyBlueAddiction.getBabyBlueAddiction()) * 20)); // Change the length of the effect depending on the Addiction level
                    });
                };
            } else {
                livingEntity.removeEffect(DestroyMobEffects.BABY_BLUE_WITHDRAWAL.get());
            };

            if (livingEntity instanceof Player player) {
                Level level = player.level();
                DestroyAdvancementTrigger.TAKE_BABY_BLUE.award(level, player);
            };

            if (livingEntity instanceof Animal animal && !animal.isBaby()) {
                if (animal.getAge() > 0) animal.resetLove();
                animal.setAge(0);
            };
        };

        super.applyEffectTick(livingEntity, amplifier);
    };

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true; // Apply effects every tick
    };

    /**
     * Give the Player Haste/Mining Fatigue if they have Baby Blue High/Withdrawal respectively.
     */
    @SubscribeEvent
    public static void onPlayerBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!DestroySubstancesConfigs.babyBlueEnabled()) return;
        Player player = event.getEntity();
        if (player.hasEffect(DestroyMobEffects.BABY_BLUE_HIGH.get())) {
            event.setNewSpeed(event.getOriginalSpeed() + (DestroyAllConfigs.SERVER.substances.babyBlueMiningSpeedBonus.getF() * (player.getEffect(DestroyMobEffects.BABY_BLUE_HIGH.get()).getAmplifier() + 1))); // Increase Haste with Baby Blue High
        } else if (player.hasEffect(DestroyMobEffects.BABY_BLUE_WITHDRAWAL.get())) {
            event.setNewSpeed(event.getOriginalSpeed() + (DestroyAllConfigs.SERVER.substances.babyBlueWidthdrawalSpeedBonus.getF() * (player.getEffect(DestroyMobEffects.BABY_BLUE_WITHDRAWAL.get()).getAmplifier() + 1))); // Decrease Haste with Baby Blue Withdrawal
            if (event.getNewSpeed() <= 0f) event.setNewSpeed(0f);
        };
    };
};
