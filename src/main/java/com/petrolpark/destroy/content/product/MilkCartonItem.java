package com.petrolpark.destroy.content.product;

import java.util.Set;

import com.petrolpark.destroy.core.item.DrinkItem;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class MilkCartonItem extends DrinkItem {

    public MilkCartonItem(Properties properties) {
        super(properties);
    };

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {

        Set<MobEffectInstance> effects = Set.copyOf(livingEntity.getActiveEffects());
        for (MobEffectInstance effect : effects) {
            if (effect.isCurativeItem(new ItemStack(Items.MILK_BUCKET)) && effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) livingEntity.removeEffect(effect.getEffect());
        };

        return super.finishUsingItem(stack, level, livingEntity);
    };
    
};
