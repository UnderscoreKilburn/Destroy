package com.petrolpark.destroy.content.product.alcohol;

import com.petrolpark.destroy.DestroyMobEffects;
import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.config.DestroySubstancesConfigs;
import com.petrolpark.destroy.core.item.DrinkItem;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class AlcoholicDrinkItem extends DrinkItem {

    private int strength;

    /**
     * @param pProperties
     * @param strength How many levels of the Inebriation effect this item adds
     */
    public AlcoholicDrinkItem(Properties properties, int strength) {
        super(properties);
        this.strength = strength;
    };

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        super.finishUsingItem(stack, level, entityLiving);

        if (entityLiving instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        };
   
        if (!level.isClientSide) {
            if (DestroySubstancesConfigs.alcoholEnabled()) DestroyMobEffects.increaseEffectLevel(entityLiving, DestroyMobEffects.INEBRIATION.get(), strength, DestroyAllConfigs.SERVER.substances.inebriationDuration.get());
            DestroyMobEffects.increaseEffectLevel(entityLiving, DestroyMobEffects.FULL_BLADDER.get(), strength, DestroyAllConfigs.SERVER.substances.inebriationDuration.get());
        };
   
        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (entityLiving instanceof Player player && !player.getAbilities().instabuild) {
               ItemStack itemstack = new ItemStack(Items.GLASS_BOTTLE);
               if (!player.getInventory().add(itemstack)) {
                  player.drop(itemstack, false);
               }
            }
   
            return stack;
        }
    };

    /**
     * Returns the number of levels of the Inebriation effect the item should add
    */
    public int getStrength() {
        return this.strength;
    };
};
