package com.petrolpark.destroy.content.oil;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;

public class CrudeOilCommand {
  
    public CrudeOilCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("crudeoil")
            .requires(cs -> cs.hasPermission(2))
            .then(Commands.argument("position", BlockPosArgument.blockPos())
                .then(Commands.literal("query")
                    .executes(CrudeOilCommand::queryCrudeOil)
                ).then(Commands.literal("set")
                    .then(Commands.argument("amount", IntegerArgumentType.integer())
                        .executes(CrudeOilCommand::setCrudeOil)
                    )
                ).then(Commands.literal("change")
                    .then(Commands.argument("amount", IntegerArgumentType.integer())
                        .executes(CrudeOilCommand::changeCrudeOil)
                    )
                )
            )
        );
    };

    private static int queryCrudeOil(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Player player = source.getPlayer(); // May be null
        BlockPos pos = context.getArgument("position", Coordinates.class).getBlockPos(source);
        LevelChunk chunk = source.getLevel().getChunkAt(pos);
        int amount = chunk.getCapability(ChunkCrudeOil.Provider.CHUNK_CRUDE_OIL).map(crudeOil -> {
            crudeOil.generate(chunk, player);
            return crudeOil.getAmount();
        }).orElse(0);
        source.sendSuccess(() ->  Component.translatable("commands.destroy.crudeoil", amount, pos.getX(), pos.getY(), pos.getZ()), true);
        return amount;
    };

    private static int setCrudeOil(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        BlockPos pos = context.getArgument("position", Coordinates.class).getBlockPos(source);
        LevelChunk chunk = source.getLevel().getChunkAt(pos);
        int amount = chunk.getCapability(ChunkCrudeOil.Provider.CHUNK_CRUDE_OIL).map(crudeOil -> {
            crudeOil.generate(chunk, null);
            return crudeOil.setAmount(context.getArgument("amount", Integer.class));
        }).orElse(0);
        source.sendSuccess(() ->  Component.translatable("commands.destroy.crudeoil", amount, pos.getX(), pos.getY(), pos.getZ()), true);
        return amount;
    };

    private static int changeCrudeOil(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        BlockPos pos = context.getArgument("position", Coordinates.class).getBlockPos(source);
        LevelChunk chunk = source.getLevel().getChunkAt(pos);
        int amount = chunk.getCapability(ChunkCrudeOil.Provider.CHUNK_CRUDE_OIL).map(crudeOil -> {
            crudeOil.generate(chunk, null);
            return crudeOil.decreaseAmount(-context.getArgument("amount", Integer.class));
        }).orElse(0);
        source.sendSuccess(() ->  Component.translatable("commands.destroy.crudeoil", amount, pos.getX(), pos.getY(), pos.getZ()), true);
        return amount;
    };
};
