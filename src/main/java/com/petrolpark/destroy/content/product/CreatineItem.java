package com.petrolpark.destroy.content.product;

import java.util.UUID;

import com.petrolpark.destroy.DestroyAttributes;
import com.petrolpark.destroy.config.DestroyAllConfigs;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CreatineItem extends Item {

    public static final UUID EXTRA_INVENTORY_ATTRIBUTE_MODIFIER = UUID.fromString("6f73b338-70ae-4d2e-9fd9-a57e56ac5c97");
    public static final UUID EXTRA_HOTBAR_ATTRIBUTE_MODIFIER = UUID.fromString("0547fc1c-ee9e-4bf5-b95a-00f61802976f");
    
    public CreatineItem(Properties properties) {
        super(properties);
    };

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!player.isCreative() && player.getFoodData().getFoodLevel() > 6f) return InteractionResultHolder.fail(player.getItemInHand(hand));
        return super.use(level, player, hand);
    };

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (livingEntity.getAttributes().hasAttribute(DestroyAttributes.EXTRA_INVENTORY_SIZE.get())) {
            AttributeInstance extraInventory = livingEntity.getAttribute(DestroyAttributes.EXTRA_INVENTORY_SIZE.get());
            extraInventory.removeModifier(EXTRA_INVENTORY_ATTRIBUTE_MODIFIER);
            extraInventory.addPermanentModifier(new AttributeModifier(EXTRA_INVENTORY_ATTRIBUTE_MODIFIER, "Extra Inventory", DestroyAllConfigs.SERVER.substances.creatineExtraInventorySize.get(), AttributeModifier.Operation.ADDITION));
        };
        if (livingEntity.getAttributes().hasAttribute(DestroyAttributes.EXTRA_HOTBAR_SLOTS.get())) {
            AttributeInstance extraHotbar = livingEntity.getAttribute(DestroyAttributes.EXTRA_HOTBAR_SLOTS.get());
            extraHotbar.removeModifier(EXTRA_HOTBAR_ATTRIBUTE_MODIFIER);
            extraHotbar.addPermanentModifier(new AttributeModifier(EXTRA_HOTBAR_ATTRIBUTE_MODIFIER, "Extra Hotbar", DestroyAllConfigs.SERVER.substances.creatineExtraHotbarSlots.get(), AttributeModifier.Operation.ADDITION));
        };
        return super.finishUsingItem(stack, level, livingEntity);
    };
    
};
