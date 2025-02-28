package com.petrolpark.destroy.content.product;

import com.petrolpark.destroy.core.explosion.SmartExplosion;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WaterSensitiveSpontaneouslyCombustingItem extends SpontaneouslyCombustingItem {

    public WaterSensitiveSpontaneouslyCombustingItem(Properties properties) {
        super(properties);
    };
    
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (checkForWater(stack, entity, isSelected)) stack.setCount(0);
    };

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (checkForWater(stack, entity, true)) entity.kill();
        return super.onEntityItemUpdate(stack, entity);
    };

    public boolean checkForWater(ItemStack stack, Entity entity, boolean rainSensitive) {
        if (entity.isInWaterOrBubble() || (entity.isInWaterRainOrBubble() && (rainSensitive || (entity instanceof LivingEntity livingEntity && livingEntity.getOffhandItem() == stack)))) {
            SmartExplosion.explode(entity.level(), new SmartExplosion(entity.level(), entity, null, null, entity.position(), 2f, 0.7f));
            return true;
        };
        return false;
    };
};
