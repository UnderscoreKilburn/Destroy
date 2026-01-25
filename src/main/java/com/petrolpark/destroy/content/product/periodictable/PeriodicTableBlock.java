package com.petrolpark.destroy.content.product.periodictable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyAdvancementTrigger;
import com.petrolpark.destroy.DestroyRegistries;

import com.simibubi.create.foundation.utility.GlobalRegistryAccess;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.*;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraftforge.event.level.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Destroy.MOD_ID)
public class PeriodicTableBlock extends HorizontalDirectionalBlock {

    public PeriodicTableBlock(Properties properties) {
        super(properties);
    };

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    };

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    };

    public static BlockPos relative(Block block, Block blockToPlace, Direction tableFacing) {
        return relative(getXY(block), getXY(blockToPlace), tableFacing);
    };

    public static BlockPos relative(int[] thisPos, int[] thatPos, Direction tableFacing) {
        int horizontalOffset = (thatPos[0] - thisPos[0]) * (tableFacing.getAxisDirection() == AxisDirection.NEGATIVE ? -1 : 1);
        return new BlockPos(tableFacing.getAxis() == Axis.X ? 0 : horizontalOffset, thisPos[1] - thatPos[1], tableFacing.getAxis() == Axis.Z ? 0 : -horizontalOffset);
    };

    public static Optional<Holder.Reference<PeriodicTableEntry>> getEntryForBlock(Block block) {
        return GlobalRegistryAccess.get().lookupOrThrow(DestroyRegistries.PERIODIC_TABLE_BLOCKS)
            .listElements()
            .filter(ref -> ref.value().blocks.contains(block.builtInRegistryHolder()))
            .findFirst();
    };

    public static Stream<Holder.Reference<PeriodicTableEntry>> getAllEntries() {
        return GlobalRegistryAccess.get().lookupOrThrow(DestroyRegistries.PERIODIC_TABLE_BLOCKS)
            .listElements();
    };

    public static boolean isPeriodicTableBlock(BlockState state) {
        return isPeriodicTableBlock(state.getBlock());
    };

    public static boolean isPeriodicTableBlock(Block block) {
        return getEntryForBlock(block).isPresent();
    };

    public static int[] getXY(Block block) {
        Optional<Holder.Reference<PeriodicTableEntry>> entry = getEntryForBlock(block);
        if (entry.isPresent()) return new int[]{entry.get().value().x, entry.get().value().y};
        return new int[2];
    };

    public record PeriodicTableEntry(HolderSet<Block> blocks, int x, int y) {
        public static final Codec<PeriodicTableEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(PeriodicTableEntry::blocks),
            Codec.INT.fieldOf("x").forGetter(PeriodicTableEntry::x),
            Codec.INT.fieldOf("y").forGetter(PeriodicTableEntry::y)
        ).apply(i, PeriodicTableEntry::new));
    };

    /**
     * Reward the Player with an advancement for assembling a full periodic table.
     */
    @SubscribeEvent
    public static void onEntityPlace(EntityPlaceEvent event) {
        BlockState state = event.getPlacedBlock();
        Level level = event.getEntity().level();

        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            // Periodic Table advancement
            if (PeriodicTableBlock.isPeriodicTableBlock(state)) {
                int[] thisPos = PeriodicTableBlock.getXY(state.getBlock());
                for (Direction direction : Iterate.horizontalDirections) {
                    boolean allPresent = getAllEntries().allMatch(r -> {
                        Optional<Holder.Reference<PeriodicTableEntry>> entry = getEntryForBlock(level.getBlockState(event.getPos().offset(PeriodicTableBlock.relative(thisPos, new int[]{r.get().x(), r.get().y()}, direction))).getBlock());
                        return entry.isPresent() && entry.get().get().x == r.get().x && entry.get().get().y == r.get().y;
                    });

                    if (allPresent) {
                        DestroyAdvancementTrigger.PERIODIC_TABLE.award(level, serverPlayer);
                        return;
                    };
                };
            };
        };

    };
    
};
