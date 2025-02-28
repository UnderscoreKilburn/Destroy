package com.petrolpark.destroy.core.explosion.mixedexplosive;

import java.util.List;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * You must call {@link IDyeableMixedExplosiveBlockEntity#onPlace} when the block associated with this Block Entity gets placed,
 * and {@link IDyeableMixedExplosiveBlockEntity#getFilledItemStack} for pick-block and the drop.
 * It's also good to call {@link IDyeableMixedExplosiveBlockEntity#tryDye} in the useOn method of the Block.
 */
public interface IDyeableMixedExplosiveBlockEntity extends IMixedExplosiveBlockEntity {
    
    public void setColor(int color);

    @OnlyIn(Dist.CLIENT)
    public static void reRender(Level level, BlockPos blockPos) {
        SectionPos pos = SectionPos.of(blockPos);
        if (level instanceof ClientLevel clientLevel) clientLevel.setSectionDirtyWithNeighbors(pos.x(), pos.y(), pos.z());
    };

    public int getColor();

    @Override
    public default void onPlace(ItemStack blockItemStack) {
        IMixedExplosiveBlockEntity.super.onPlace(blockItemStack);
        if (blockItemStack.getItem() instanceof DyeableLeatherItem dyeableItem) setColor(dyeableItem.getColor(blockItemStack));
    };

    @Override
    public default ItemStack getFilledItemStack(ItemStack emptyItemStack) {
        if (emptyItemStack.getItem() instanceof DyeableLeatherItem dyeableItem) dyeableItem.setColor(emptyItemStack, getColor());
        return IMixedExplosiveBlockEntity.super.getFilledItemStack(emptyItemStack);
    };

    public default InteractionResult tryDye(ItemStack dyeStack, HitResult target, Level level, BlockPos pos, Player player) {
        if (!(dyeStack.getItem() instanceof DyeItem dyeItem)) return InteractionResult.PASS;
        ItemStack stack = level.getBlockState(pos).getCloneItemStack(target, level, pos, player);
        if (stack.getItem() instanceof DyeableLeatherItem dyeableItem) {
            setColor(dyeableItem.getColor(DyeableLeatherItem.dyeArmor(stack, List.of(dyeItem))));
            if (!player.isCreative()) dyeStack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide());
        };
        return InteractionResult.PASS;
    };

    @Override
    default boolean readFromClipboard(CompoundTag tag, Player player, Direction side, boolean simulate) {
        boolean invCopied = IMixedExplosiveBlockEntity.super.readFromClipboard(tag, player, side, simulate);
        if (tag.contains("Color", Tag.TAG_INT)) {
            if (!simulate) setColor(tag.getInt("Color"));
            return true;
        };
        return invCopied;
    };

    @Override
    default boolean writeToClipboard(CompoundTag tag, Direction side) {
        IMixedExplosiveBlockEntity.super.writeToClipboard(tag, side);
        tag.putInt("Color", getColor());
        return true;
    };
    
};
