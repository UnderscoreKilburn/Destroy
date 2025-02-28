package com.petrolpark.destroy.core.chemistry.storage.measuringcylinder;

import java.util.function.Supplier;

import com.petrolpark.destroy.core.chemistry.storage.IMixtureStorageItem;
import com.petrolpark.destroy.core.chemistry.storage.ItemMixtureTank;
import com.petrolpark.network.packet.C2SPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.network.NetworkEvent.Context;

public class TransferFluidC2SPacket extends C2SPacket {

    private final BlockPos pos;
    private final Direction face;
    private final InteractionHand hand; // The hand the Item is in

    private final boolean blockToItem; // If true, the Fluid in the Block should be transferred to the Item
    private final int transferAmount;

    public TransferFluidC2SPacket(BlockPos pos, Direction face, InteractionHand hand, boolean blockToItem, int transferAmount) {
        this.pos = pos;
        this.face = face;
        this.hand = hand;
        this.blockToItem = blockToItem;
        this.transferAmount = transferAmount;
    };

    public TransferFluidC2SPacket(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
        face = buffer.readEnum(Direction.class);
        hand = buffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        blockToItem = buffer.readBoolean();
        transferAmount = buffer.readVarInt();
    };

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeEnum(face);
        buffer.writeBoolean(hand == InteractionHand.MAIN_HAND);
        buffer.writeBoolean(blockToItem);
        buffer.writeVarInt(transferAmount);
    };

    @Override
    public boolean handle(Supplier<Context> supplier) {
        Context context = supplier.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();
            ItemStack stack = context.getSender().getItemInHand(hand);
            if (stack.getItem() instanceof IMixtureStorageItem mixtureItem) {
                Level level = player.level();
                BlockState state = level.getBlockState(pos);
                LazyOptional<IFluidHandlerItem> cap = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
                if (!cap.isPresent()) return;
                ItemMixtureTank itemTank = (ItemMixtureTank)cap.resolve().get();
                IFluidHandler otherTank = mixtureItem.getTank(level, pos, state, face, player, hand, stack, !blockToItem);
                if (otherTank == null) return;
                if (blockToItem) {
                    mixtureItem.afterFill(level, pos, state, face, player, hand, stack, mixtureItem.tryFill(stack, itemTank, otherTank, transferAmount));
                } else {
                    mixtureItem.afterEmpty(level, pos, state, face, player, hand, stack, mixtureItem.tryEmpty(stack, itemTank, otherTank, false, transferAmount));
                };
            };
        });
        return true;
    };
    
};
