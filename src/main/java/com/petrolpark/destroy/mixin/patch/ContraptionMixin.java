package com.petrolpark.destroy.mixin.patch;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.petrolpark.compat.create.item.directional.DirectionalTransportedItemStack;
import com.petrolpark.compat.create.item.directional.IDirectionalOnBelt;
import com.petrolpark.mixin.compat.create.accessor.DepotBehaviourAccessor;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.MountedStorageManager;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Contraption.class)
public class ContraptionMixin {
    /**
     * Depots in Create 6 have a mounted storage manager that holds onto the depot's held item stack when it is being moved
     * and restores it when it is placed back in the world.
     */
    @WrapOperation(method="addBlocksToWorld", at=@At(
            value="INVOKE",
            target="Lcom/simibubi/create/content/contraptions/MountedStorageManager;unmount(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;)V",
            remap=false), remap=false)
    void fixDepotUnmount(MountedStorageManager storage, Level level, StructureTemplate.StructureBlockInfo structureBlockInfo, BlockPos pos, BlockEntity be, Operation<Void> original) {
        if(be instanceof DepotBlockEntity depot) {
            DepotBehaviour depotBehaviour = depot.getBehaviour(DepotBehaviour.TYPE);
            if(depotBehaviour != null) {
                // Grab the transported item stack the depot was holding when it was turned into a contraption
                TransportedItemStack ts = ((DepotBehaviourAccessor) depotBehaviour).getHeldItem();

                if (ts instanceof DirectionalTransportedItemStack dts) {
                    // Unmount storage
                    original.call(storage, level, structureBlockInfo, pos, be);

                    // If the item on the depot is still a directional item, restore the previously retrieved DirectionalTransportedItemStack
                    // instance and replace its contents with the new contents (unless you're doing something seriously weird with funnels,
                    // the old contents and the new contents should be identical)
                    if(depot.getHeldItem().getItem() instanceof IDirectionalOnBelt) {
                        dts.stack = depot.getHeldItem();
                        depotBehaviour.setHeldItem(dts);
                    }
                    return;
                }
            }
        }

        original.call(storage, level, structureBlockInfo, pos, be);
    }
}
