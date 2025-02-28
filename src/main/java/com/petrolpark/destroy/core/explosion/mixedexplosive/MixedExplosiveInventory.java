package com.petrolpark.destroy.core.explosion.mixedexplosive;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.petrolpark.destroy.core.explosion.mixedexplosive.ExplosiveProperties.ExplosiveProperty;
import com.petrolpark.destroy.core.explosion.mixedexplosive.ExplosiveProperties.ExplosivePropertyCondition;

import net.minecraft.util.Mth;
import net.minecraft.world.item.FireworkStarItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class MixedExplosiveInventory extends ItemStackHandler {

    protected ExplosivePropertyCondition[] conditions;
    
    public MixedExplosiveInventory(int size, ExplosivePropertyCondition... conditions) {
        super(size);
        this.conditions = conditions;
    };

    public static boolean canBeAdded(ItemStack stack) {
        return ExplosiveProperties.ITEM_EXPLOSIVE_PROPERTIES.get(stack.getItem()) != null; // Must have explosive properties
    };

    public ExplosiveProperties getExplosiveProperties() {
        ExplosiveProperties properties = new ExplosiveProperties();
        for (int slot = 0; slot < getSlots(); slot++) {
            ItemStack stack = getStackInSlot(slot);
            ExplosiveProperties itemProperties = ExplosiveProperties.ITEM_EXPLOSIVE_PROPERTIES.getOrDefault(stack.getItem(), new ExplosiveProperties());
            for (ExplosiveProperty property : ExplosiveProperty.values()) properties.merge(property, itemProperties.get(property), (e1, e2) -> {
                e1.value += e2.value;
                return e1;
            });
        };
        properties.forEach((ep, e) -> e.value = Mth.clamp(e.value, -10f, 10f));
        return properties.withConditions(conditions);
    };

    public boolean isEmpty() {
        return stacks.isEmpty() || stacks.stream().allMatch(ItemStack::isEmpty);
    };

    /**
     * Items which have special behaviour when exploded
     * @return
     */
    public List<ItemStack> getSpecialItems() {
        return stacks.stream().filter(s -> s.getItem() instanceof FireworkStarItem || s.getItem() instanceof ISpecialEffectExplosiveItem).toList();
    };

    @Override
    public final boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return canBeAdded(stack);
    };

    @Override
    public final int getSlotLimit(int slot) {
        return 1;
    };
};
