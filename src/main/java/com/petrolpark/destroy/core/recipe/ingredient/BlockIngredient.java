package com.petrolpark.destroy.core.recipe.ingredient;

import com.google.gson.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Sorry Petrol I need something simple that can be serialized as JSON and I can't wrap my head around your BlockIngredient.
 * Mostly copied from the vanilla {@link net.minecraft.world.item.crafting.Ingredient Ingredient} class with some bits and pieces
 * referenced from Team CoFH's implementation of BlockIngredient in CoFH Core.
 */
public class BlockIngredient implements Predicate<BlockState> {

    private final Value[] values;
    private Set<BlockState> blockStates;

    public static final BlockIngredient EMPTY = new BlockIngredient(Stream.empty());

    protected BlockIngredient(Stream<? extends Value> values) {
        this.values = values.toArray(n -> new Value[n]);
        this.blockStates = null;
    }

    protected BlockIngredient(Set<BlockState> states) {
        this.values = new Value[0];
        this.blockStates = states;
    }

    public Set<BlockState> getBlockStates() {
        if(blockStates == null)
            blockStates = Arrays.stream(values).flatMap(v -> v.getBlockStates()).collect(Collectors.toCollection(HashSet::new));

        return blockStates;
    }

    public List<ItemStack> getDisplayedItemStacks() {
        return getBlockStates().stream().map(state -> state.getBlock().asItem()).distinct().map(ItemStack::new).toList();
    }

    @Override
    public boolean test(BlockState blockState) {
        return getBlockStates().contains(blockState);
    }

    public static BlockIngredient of() {
        return EMPTY;
    }

    public static BlockIngredient of(Block... blocks) {
        return of(Arrays.stream(blocks));
    }

    public static BlockIngredient of(Stream<Block> blocks) {
        return fromValues(blocks.map(BlockIngredient.BlockValue::new));
    }

    public static BlockIngredient of(TagKey<Block> tag) {
        return fromValues(Stream.of(new TagValue(tag)));
    }

    public static BlockIngredient fromValues(Stream<? extends Value> stream) {
        BlockIngredient ingredient = new BlockIngredient(stream);
        return ingredient.isEmpty() ? EMPTY : ingredient;
    }

    public boolean isEmpty() {
        return values.length == 0;
    }

    public final void toNetwork(FriendlyByteBuf buf) {
        buf.writeCollection(getBlockStates(), (buffer, state) -> {
            buffer.writeVarInt(Block.getId(state));
        });
    }

    public static BlockIngredient fromNetwork(FriendlyByteBuf buf) {
        Set<BlockState> states = buf.readCollection(n -> new HashSet<>(), buffer -> Block.stateById(buffer.readVarInt()));
        return new BlockIngredient(states);
    }

    public JsonElement toJson() {
        if(values.length == 1)
            return values[0].serialize();
        else {
            JsonArray jsonArray = new JsonArray();
            for(Value value : values)
                jsonArray.add(value.serialize());

            return jsonArray;
        }
    }

    public static BlockIngredient fromJson(JsonElement json) {
        return fromJson(json, true);
    }

    public static BlockIngredient fromJson(JsonElement json, boolean canBeEmpty) {
        if(json != null && !json.isJsonNull()) {
            if(json.isJsonObject())
                return fromValues(Stream.of(valueFromJson(json.getAsJsonObject())));
            else if(json.isJsonArray()) {
                JsonArray array = json.getAsJsonArray();
                if(array.size() == 0 && !canBeEmpty)
                    throw new JsonSyntaxException("Block array cannot be empty, at least one block must be defined");
                else
                    return fromValues(StreamSupport.stream(array.spliterator(), false).map(obj -> valueFromJson(GsonHelper.convertToJsonObject(obj, "block"))));
            } else {
                throw new JsonSyntaxException("Expected block to be object or array of objects");
            }
        } else {
            throw new JsonSyntaxException("Block cannot be null");
        }
    }

    public static Value valueFromJson(JsonObject json) {
        if(json.has("block") && json.has("tag")) {
            throw new JsonParseException("A block ingredient entry is either a tag or a block, not both");
        } else if(json.has("block")) {
            ResourceLocation path = ResourceLocation.parse(GsonHelper.getAsString(json, "block"));
            if(!ForgeRegistries.BLOCKS.containsKey(path))
                throw new JsonParseException("Block '" + path + "' not found");

            return new BlockValue(ForgeRegistries.BLOCKS.getValue(path));
        } else if(json.has("tag")) {
            ResourceLocation path = ResourceLocation.parse(GsonHelper.getAsString(json, "tag"));
            TagKey<Block> tagkey = TagKey.create(Registries.BLOCK, path);
            return new TagValue(tagkey);
        } else {
            throw new JsonParseException("A block ingredient entry needs either a tag or a block");
        }
    }

    public interface Value {
        Stream<BlockState> getBlockStates();
        JsonObject serialize();
    }

    public static class BlockValue implements Value {

        private final Block block;
        public BlockValue(Block block) {this.block = block;}

        @Override
        public Stream<BlockState> getBlockStates() {
            // Don't care about specific blockstates for now
            return block.getStateDefinition().getPossibleStates().stream();
        }

        @Override
        public JsonObject serialize() {
            JsonObject json = new JsonObject();
            json.addProperty("block", ForgeRegistries.BLOCKS.getKey(block).toString());
            return json;
        }
    }

    public static class TagValue implements Value {

        private final TagKey<Block> tagKey;
        public TagValue(TagKey<Block> tagKey) {this.tagKey = tagKey;}

        @Override
        public Stream<BlockState> getBlockStates() {
            return ForgeRegistries.BLOCKS.tags().getTag(tagKey).stream().flatMap(block -> block.getStateDefinition().getPossibleStates().stream());
        }

        @Override
        public JsonObject serialize() {
            JsonObject json = new JsonObject();
            json.addProperty("tag", tagKey.location().toString());
            return json;
        }
    }
}
