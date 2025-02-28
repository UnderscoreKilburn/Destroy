package com.petrolpark.destroy.core.chemistry.hazard.protection;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyArmorMaterials;
import com.petrolpark.destroy.core.chemistry.hazard.ChemistryHazardHelper;
import com.simibubi.create.content.equipment.armor.BaseArmorItem;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HazmatSuitArmorItem extends BaseArmorItem {

    public HazmatSuitArmorItem(Type type, Properties properties) {
        super(DestroyArmorMaterials.HAZMAT, type, properties, Destroy.asResource("hazmat"));
    };

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (entity instanceof LivingEntity livingEntity) {
            if (!ItemStack.matches(livingEntity.getItemBySlot(this.getEquipmentSlot()), stack))
            ChemistryHazardHelper.decontaminate(stack);
        };
    };
    
};
