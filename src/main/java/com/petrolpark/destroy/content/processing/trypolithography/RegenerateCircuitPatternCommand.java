package com.petrolpark.destroy.content.processing.trypolithography;

import static com.petrolpark.destroy.Destroy.CIRCUIT_PATTERN_HANDLER;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyMessages;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class RegenerateCircuitPatternCommand {
  
    public RegenerateCircuitPatternCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("regeneratecircuitpattern")
            .requires(cs -> cs.hasPermission(2))
            .then(Commands.argument("pattern", CircuitPatternIdArgument.create())
                .executes(context -> regenerateCircuitPattern(context.getSource(), context.getArgument("pattern", ResourceLocation.class)))
            )
        );
    };

    public static int regenerateCircuitPattern(CommandSourceStack source, ResourceLocation pattern) {
        Destroy.CIRCUIT_PATTERN_HANDLER.removePattern(pattern);
        Destroy.CIRCUIT_PATTERN_HANDLER.setDirty();
        DestroyMessages.sendToAllClients(new CircuitPatternsS2CPacket(CIRCUIT_PATTERN_HANDLER.getAllPatterns()));
        source.sendSuccess(() -> Component.translatable("commands.destroy.regeneratecircuitpattern", pattern), true);
        return 1;
    };

    public static class CircuitPatternIdArgument extends ResourceLocationArgument {

        private static final Function<ResourceLocation, SimpleCommandExceptionType> ERROR_UNKNOWN = rl -> new SimpleCommandExceptionType(Component.translatable("argument.id.unknown", rl));

        public static CircuitPatternIdArgument create() {
            return new CircuitPatternIdArgument();
        };
        
        @Override
        public ResourceLocation parse(StringReader reader) throws CommandSyntaxException {
            ResourceLocation rl = super.parse(reader);
            if (!Destroy.CIRCUIT_PATTERN_HANDLER.getPatternsWithGenerators().contains(rl)) throw ERROR_UNKNOWN.apply(rl).createWithContext(reader);
            return rl;
        };

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
            return SharedSuggestionProvider.suggest(Destroy.CIRCUIT_PATTERN_HANDLER.getPatternsWithGenerators().stream().map(ResourceLocation::toString).toList(), builder);
        };

        @Override
        public Collection<String> getExamples() {
            return Destroy.CIRCUIT_PATTERN_HANDLER.getPatternsWithGenerators().stream().map(ResourceLocation::toString).toList();
        };
    };  
};
