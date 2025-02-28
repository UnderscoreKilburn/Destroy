package com.petrolpark.destroy.core.pollution;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.petrolpark.destroy.core.pollution.Pollution.PollutionType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraftforge.server.command.EnumArgument;

public class PollutionCommand {

    public PollutionCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("pollution")
            .requires(cs -> cs.hasPermission(2))
            .then(Commands.argument("position", BlockPosArgument.blockPos())
                .then(Commands.argument("type", EnumArgument.enumArgument(PollutionType.class))
                    .then((Commands.literal("query").executes(context -> {
                        return queryLevelPollution(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "position"), context.getArgument("type", PollutionType.class));
                    }))).then((Commands.literal("set").then(Commands.argument("value", IntegerArgumentType.integer(0)).executes(context -> {
                        return setLevelPollution(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "position"), context.getArgument("type", PollutionType.class), IntegerArgumentType.getInteger(context, "value"));
                    })))).then((Commands.literal("add").then(Commands.argument("change", IntegerArgumentType.integer()).executes(context -> {
                        return addLevelPollution(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "position"), context.getArgument("type", PollutionType.class), IntegerArgumentType.getInteger(context, "change"));
                    }))))
                )
            )
        );
    };

    private int queryLevelPollution(CommandSourceStack source, BlockPos pos, PollutionType pollutionType) {
        int pollutionLevel = PollutionHelper.getPollution(source.getLevel(), pos, pollutionType);
        source.sendSuccess(() ->  Component.translatable("commands.destroy.pollution.query", pollutionType.name(), pollutionLevel), true);
        return pollutionLevel;
    };

    private int setLevelPollution(CommandSourceStack source, BlockPos pos, PollutionType pollutionType, int value) {
        int pollutionLevel = PollutionHelper.setPollution(source.getLevel(), pos, pollutionType, value);
        source.sendSuccess(() ->  Component.translatable("commands.destroy.pollution.set", pollutionType.name(), pollutionLevel), true);
        return pollutionLevel;
    };

    private int addLevelPollution(CommandSourceStack source, BlockPos pos, PollutionType pollutionType, int change) {
        int pollutionLevel = PollutionHelper.changePollution(source.getLevel(), pos, pollutionType, change);
        source.sendSuccess(() ->  Component.translatable("commands.destroy.pollution.set", pollutionType.name(), pollutionLevel), true);
        return pollutionLevel;
    };
};
