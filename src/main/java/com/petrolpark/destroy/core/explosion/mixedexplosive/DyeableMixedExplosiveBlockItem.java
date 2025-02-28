package com.petrolpark.destroy.core.explosion.mixedexplosive;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
 
public abstract class DyeableMixedExplosiveBlockItem extends BlockItem implements DyeableLeatherItem, IMixedExplosiveItem {

    public DyeableMixedExplosiveBlockItem(Block block, Properties properties) {
        super(block, properties);
    };

    @Override
    public int getColor(ItemStack pStack) {
        CompoundTag compoundtag = pStack.getTagElement("display");
        return compoundtag != null && compoundtag.contains("color", Tag.TAG_INT) ? compoundtag.getInt("color") : 0xFFFFFF;
    };

    @Override
    public int getExplosiveInventorySize() {
        return 16;
    };

    public ItemStack fromStructureInfo(StructureBlockInfo info) {
        ItemStack stack = new ItemStack(this);
        setColor(stack, info.nbt().getInt("Color"));
        MixedExplosiveInventory inv = new MixedExplosiveInventory(getExplosiveInventorySize());
        inv.deserializeNBT(info.nbt().getCompound("ExplosiveMix"));
        setExplosiveInventory(stack, inv);
        return stack;
    };

    public StructureBlockInfo toStructureInfo(BlockPos localPos, BlockState state, ItemStack stack) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Color", getColor(stack));
        tag.put("ExplosiveMix", getExplosiveInventory(stack).serializeNBT());
        return new StructureBlockInfo(localPos, state, tag);
    };
    
};
