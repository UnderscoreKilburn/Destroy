package com.petrolpark.destroy.content.product;

import com.petrolpark.destroy.DestroyAdvancementTrigger;
import com.petrolpark.destroy.DestroyMobEffects;
import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.content.tool.syringe.SyringeItem;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AspirinSyringeItem extends SyringeItem {

    public AspirinSyringeItem(Properties properties) {
        super(properties);
    };

    @Override
    public void onInject(ItemStack itemStack, Level level, LivingEntity target) {
        target.heal(DestroyAllConfigs.SERVER.substances.aspirinHeal.getF());
        if (!target.removeEffect(DestroyMobEffects.HANGOVER.get())) return;
        if (target instanceof Player player) {
            DestroyAdvancementTrigger.CURE_HANGOVER.award(level, player);
        };
    };
    
};
