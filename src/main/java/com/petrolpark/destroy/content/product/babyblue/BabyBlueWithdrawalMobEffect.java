package com.petrolpark.destroy.content.product.babyblue;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyDamageSources;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.DestroyMobEffects;
import com.petrolpark.destroy.client.DestroyLang;
import com.petrolpark.destroy.config.DestroySubstancesConfigs;
import com.petrolpark.destroy.core.mobeffect.UncurableMobEffect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Destroy.MOD_ID)
public class BabyBlueWithdrawalMobEffect extends UncurableMobEffect {

    public BabyBlueWithdrawalMobEffect() {
        super(MobEffectCategory.HARMFUL, 0x91B1B7);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "31875c8a-f500-477c-ac52-70355c6adc12", (double)-0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, "0a7d851c-b38b-47c8-9131-348a492e3af8", (double)-0.45F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "93bfb982-7e97-472f-b2f6-a0c51b4d916f", (double)-1.0F, AttributeModifier.Operation.ADDITION);
    };

    @Override
    @SuppressWarnings("null") // We know the effect isn't null if its ticking
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide()) {
            int duration = livingEntity.getEffect(DestroyMobEffects.BABY_BLUE_WITHDRAWAL.get()).getDuration(); // This is the bit it says is null

            if (livingEntity instanceof Player) {
                livingEntity.getCapability(PlayerBabyBlueAddictionCapability.CAPABILITY).ifPresent(babyBlueAddiction -> {
                    if (duration % Math.round(100 / (Math.log(babyBlueAddiction.getBabyBlueAddiction() + 1))) == 0) { // Apply damage at a rate roughly equal to ln(baby blue addiction level)
                        livingEntity.hurt(DestroyDamageSources.babyBlueOverdose(livingEntity.level()), 1f);
                    }
                });
            } else if (duration % 50 == 0) { // For non-players, deal damage at a set rate
                livingEntity.hurt(DestroyDamageSources.babyBlueOverdose(livingEntity.level()), 1f);
            };
        };
        super.applyEffectTick(livingEntity, amplifier);
    };

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    };

    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();

        if (stack.isEdible() && DestroySubstancesConfigs.babyBlueEnabled() && stack.getItem() != DestroyItems.BABY_BLUE_POWDER.get() && player.hasEffect(DestroyMobEffects.BABY_BLUE_WITHDRAWAL.get()) && !stack.getFoodProperties(player).canAlwaysEat()) {
            player.displayClientMessage(DestroyLang.translate("tooltip.eating_prevented.baby_blue").component(), true);
            event.setCanceled(true);
        };
    };
};
