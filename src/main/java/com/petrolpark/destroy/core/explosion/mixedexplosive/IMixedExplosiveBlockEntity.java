package com.petrolpark.destroy.core.explosion.mixedexplosive;

import java.util.ArrayList;
import java.util.List;

import com.petrolpark.destroy.core.explosion.mixedexplosive.ExplosiveProperties.ExplosivePropertyCondition;
import com.simibubi.create.content.equipment.clipboard.ClipboardCloneable;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.ItemHelper.ExtractionCountMode;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

/**
 * You must call {@link IMixedExplosiveBlockEntity#onPlace} when the block associated with this Block Entity gets placed,
 * and {@link IMixedExplosiveBlockEntity#getFilledItemStack} for pick-block.
 */
public interface IMixedExplosiveBlockEntity extends MenuProvider, ClipboardCloneable {

    public MixedExplosiveInventory getExplosiveInventory();

    public void setExplosiveInventory(MixedExplosiveInventory inv);

    public ExplosivePropertyCondition[] getApplicableExplosionConditions();

    public default String getExplosivePropertyDescriptionTranslationKeySuffix() {
        return "";
    };
    
    public default void onPlace(ItemStack blockItemStack) {
        if (blockItemStack.getItem() instanceof IMixedExplosiveItem customMixItem) setExplosiveInventory(customMixItem.getExplosiveInventory(blockItemStack));
    };

    public default ItemStack getFilledItemStack(ItemStack emptyItemStack) {
        if (emptyItemStack.getItem() instanceof IMixedExplosiveItem customMixItem) customMixItem.setExplosiveInventory(emptyItemStack, getExplosiveInventory());
        return emptyItemStack;
    };

    @Override
    public default AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return MixedExplosiveMenu.create(containerId, playerInventory, this);
    };

    public default void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeComponent(getDisplayName());
        buffer.writeVarInt(getExplosiveInventory().getSlots());
        buffer.writeNbt(getExplosiveInventory().serializeNBT());
        buffer.writeVarInt(getApplicableExplosionConditions().length);
        for (ExplosivePropertyCondition c : getApplicableExplosionConditions())
            buffer.writeResourceLocation(c.rl);
    };

    @Override
    public default String getClipboardKey() {
        return "CustomExplosiveMix";
    };

    @Override
    default boolean readFromClipboard(CompoundTag tag, Player player, Direction side, boolean simulate) {
        if (tag.contains("TargetInventory")) {
            if (simulate) return true;
            
            //TODO check if player is creative

            MixedExplosiveInventory inv = getExplosiveInventory();
            int invSize = inv.getSlots();
            
            List<ItemStack> oldItems = new ArrayList<>(invSize);
            for (int slot = 0; slot < invSize; slot++) {
                oldItems.add(inv.getStackInSlot(slot));
                inv.setStackInSlot(slot, ItemStack.EMPTY); // Clear the Inventory
            };

            ItemStackHandler targetInventory = new ItemStackHandler();
            targetInventory.deserializeNBT(tag.getCompound("TargetInventory"));

            List<ItemStack> leftoverItems = new ArrayList<>();

            tryAddEachItem: for (int slot = 0; slot < targetInventory.getSlots(); slot++) {
                ItemStack targetStack = targetInventory.getStackInSlot(slot);
                if (targetStack.isEmpty()) continue tryAddEachItem;
                
                // Adding from existing Stacks
                for (ItemStack availableStack : oldItems) {
                    if (ItemStack.isSameItemSameTags(availableStack, targetStack)) {

                        int inserted = Math.max(availableStack.getCount(), targetStack.getCount());
                        ItemStack insertedStack = availableStack.copy();
                        insertedStack.setCount(inserted);

                        ItemStack leftoverStack = ItemHandlerHelper.insertItem(inv, insertedStack, false);
                        if (!leftoverStack.isEmpty()) { // If we couldn't fit it in, we'll need to give it back to the player
                            leftoverItems.add(leftoverStack);
                            continue tryAddEachItem; 
                        };

                        availableStack.shrink(inserted);
                        targetStack.shrink(inserted);
                        if (targetStack.isEmpty()) continue tryAddEachItem;
                    };
                };

                // Adding from the Player's inventory
                ItemStack leftoverStack = ItemHandlerHelper.insertItem(inv, ItemHelper.extract(new PlayerMainInvWrapper(player.getInventory()), s -> ItemStack.isSameItemSameTags(s, targetStack), ExtractionCountMode.UPTO, targetStack.getCount(), false), false);
                if (!leftoverStack.isEmpty()) { // If we couldn't fit it in, we'll need to give it back to the Player
                    leftoverItems.add(leftoverStack);
                    continue tryAddEachItem; 
                };
            };

            // Give any remaining Items back to the Player
            for (ItemStack remainingStack : oldItems) if (!remainingStack.isEmpty()) player.getInventory().placeItemBackInInventory(remainingStack, true);
            for (ItemStack leftoverStack : leftoverItems) player.getInventory().placeItemBackInInventory(leftoverStack, true);
            
            return true;
        };
        return false;
    };

    @Override
    default boolean writeToClipboard(CompoundTag tag, Direction side) {
        tag.put("TargetInventory", getExplosiveInventory().serializeNBT());
        return true;
    };
};
