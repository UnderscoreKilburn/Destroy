package com.petrolpark.destroy.core.explosion.mixedexplosive;

import java.util.*;
import java.util.stream.Collectors;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.DestroyRegistries;
import com.petrolpark.destroy.client.DestroyLang;
import com.simibubi.create.foundation.utility.GlobalRegistryAccess;
import net.createmod.catnip.lang.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ExplosiveProperties extends EnumMap<ExplosiveProperties.ExplosiveProperty, ExplosiveProperties.ExplosivePropertiesEntry> {

    public static final Codec<ExplosiveProperties> CODEC = Codec.compoundList(Codec.STRING, Codec.FLOAT).comapFlatMap(
        kv_list -> {
            ExplosiveProperties properties = new ExplosiveProperties();
            for(Pair<String, Float> kv : kv_list) {
                ExplosiveProperty property = ExplosiveProperty.valueOf(kv.getFirst().toUpperCase(Locale.ROOT));
                if(property != null) {
                    properties.put(property, new ExplosivePropertiesEntry(kv.getSecond(), property.getDefaultDescription()));
                };
            };
            return DataResult.success(properties);
        },
        properties -> Arrays.stream(ExplosiveProperty.values()).map(property -> Pair.of(property.name().toLowerCase(Locale.ROOT), Float.valueOf(properties.get(property).value))).toList()
    );

    public record RegistryEntry(HolderSet<Item> items, ExplosiveProperties properties) {
        public static final Codec<RegistryEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
            RegistryCodecs.homogeneousList(Registries.ITEM).optionalFieldOf("items", HolderSet.direct()).forGetter(RegistryEntry::items),
            ExplosiveProperties.CODEC.fieldOf("properties").forGetter(RegistryEntry::properties)
        ).apply(i, RegistryEntry::new));
    };

    public static final Map<Item, Optional<ExplosiveProperties>> ITEM_EXPLOSIVE_PROPERTIES = new HashMap<>();
    public static final Map<ResourceLocation, ExplosivePropertyCondition> EXPLOSIVE_PROPERTY_CONDITIONS = new HashMap<>();

    public static Optional<ExplosiveProperties> getEntryForItem(Item item) {
        return ITEM_EXPLOSIVE_PROPERTIES.computeIfAbsent(item, i ->
            GlobalRegistryAccess.get().lookupOrThrow(DestroyRegistries.EXPLOSIVE_PROPERTIES)
                .listElements()
                .filter(ref -> ref.value().items.contains(item.builtInRegistryHolder()))
                .findFirst().map(r -> r.get().properties)
        );
    };

    public static final ExplosivePropertyCondition

    CAN_EXPLODE = register(new ExplosivePropertyCondition(ExplosiveProperty.SENSITIVITY, 0f, Destroy.asResource("can_explode"))),
    DROPS_EXPERIENCE = register(new ExplosivePropertyCondition(ExplosiveProperty.TEMPERATURE, -4f, Destroy.asResource("drops_experience"))),
    DROPS_HEADS = register(new ExplosivePropertyCondition(ExplosiveProperty.TEMPERATURE, -8f, Destroy.asResource("drops_heads"))),
    ENTITIES_PUSHED = register(new ExplosivePropertyCondition(ExplosiveProperty.ENERGY, 7f, Destroy.asResource("entities_pushed"))),
    EXPLODES_RANDOMLY = register(new ExplosivePropertyCondition(ExplosiveProperty.SENSITIVITY, 10f, Destroy.asResource("explodes_randomly"))),
    ITEMS_DESTROYED = register(new ExplosivePropertyCondition(ExplosiveProperty.BRISANCE, 8f, Destroy.asResource("items_destroyed"))),
    EVAPORATES_FLUIDS = register(new ExplosivePropertyCondition(ExplosiveProperty.TEMPERATURE, 5f, Destroy.asResource("evaporates_fluids"))),
    OBLITERATES = register(new ExplosivePropertyCondition(ExplosiveProperty.BRISANCE, 5f, Destroy.asResource("obliterates"))),
    NO_FUSE = register(new ExplosivePropertyCondition(ExplosiveProperty.SENSITIVITY, 4f, Destroy.asResource("no_fuse"))),
    SILK_TOUCH = register(new ExplosivePropertyCondition(ExplosiveProperty.BRISANCE, -5f, Destroy.asResource("silk_touch"))),
    SOUND_ACTIVATED = register(new ExplosivePropertyCondition(ExplosiveProperty.SENSITIVITY, 8f, Destroy.asResource("sound_activated"))),
    UNDERWATER = register(new ExplosivePropertyCondition(ExplosiveProperty.OXYGEN_BALANCE, 2f, Destroy.asResource("underwater")));
  
    public ExplosiveProperties() {
        super(Arrays.stream(ExplosiveProperty.values()).collect(Collectors.toMap(p -> p, p -> new ExplosivePropertiesEntry(0f, p.getDefaultDescription()))));
    };

    public ExplosiveProperties withConditions(ExplosivePropertyCondition ...conditions) {
        forEach((p, e) -> e.conditions.clear());
        for (ExplosivePropertyCondition condition : conditions) {
            get(condition.property).conditions.add(condition);
        };
        return this;
    };

    public boolean fulfils(ExplosivePropertyCondition condition) {
        if (!hasCondition(condition)) return false;
        float value = get(condition.property).value;
        return condition.negative() ? value <= condition.threshhold : value >= condition.threshhold;
    };

    public boolean hasCondition(ExplosivePropertyCondition condition) {
        return get(condition.property).conditions.contains(condition);
    };

    public List<ExplosivePropertyCondition> getConditions() {
        List<ExplosivePropertyCondition> conditions = new ArrayList<>();
        for (ExplosivePropertiesEntry entry : values()) conditions.addAll(entry.conditions);
        return conditions;
    };

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        forEach((p, e) -> {
            if (e.value != 0f) tag.putFloat(p.name(), e.value);
        });
        return tag;
    };

    public static ExplosiveProperties read(CompoundTag tag) {
        ExplosiveProperties properties = new ExplosiveProperties();
        properties.forEach((p, e) -> e.value = tag.getFloat(p.name()));
        return properties;
    };

    public static ExplosiveProperties read(FriendlyByteBuf buffer) {
        ExplosiveProperties properties = new ExplosiveProperties();
        for (ExplosiveProperty property : ExplosiveProperty.values()) properties.get(property).value = buffer.readFloat();
        return properties.withConditions(buffer.readCollection(ArrayList<ExplosivePropertyCondition>::new, b -> EXPLOSIVE_PROPERTY_CONDITIONS.get(b.readResourceLocation())).toArray(i -> new ExplosivePropertyCondition[i]));
    };

    public void write(FriendlyByteBuf buffer) {
        for (ExplosivePropertiesEntry entry : values()) buffer.writeFloat(entry.value);
        buffer.writeCollection(getConditions(), (b, c) -> b.writeResourceLocation(c.rl));
    };

    public static class ExplosivePropertiesEntry {

        public float value;
        public final Set<ExplosivePropertyCondition> conditions;
        public Component description;

        public ExplosivePropertiesEntry(float value, Component description) {
            this.value = value;
            this.conditions = new HashSet<>();
            this.description = description;
        };

    };

    public static class ExplosivePropertyCondition implements ExplosivePropertiesTooltip.Selectable {

        public final ExplosiveProperty property;
        public final float threshhold;
        public final ResourceLocation rl;

        public ExplosivePropertyCondition(ExplosiveProperty property, float threshhold, ResourceLocation rl) {
            this.property = property;
            this.threshhold = threshhold;
            this.rl = rl;
        };

        public boolean negative() {
            return threshhold < 0f;
        };

        public Component getDescription() {
            return Component.translatable(rl.getNamespace()+".explosion_condition."+rl.getPath());
        }

        @Override
        public List<Component> getTooltip(ExplosiveProperties properties) {
            boolean active = properties.fulfils(this);
            List<Component> tooltip = new ArrayList<>(2);
            tooltip.add(getDescription().copy().withStyle(active ? ChatFormatting.WHITE : ChatFormatting.GRAY));
            tooltip.add(DestroyLang.translate("tooltip.explosion_condition_active", DestroyLang.tickOrCross(active)).style(ChatFormatting.GRAY).component());
            return tooltip;
        };
    };

    public static ExplosivePropertyCondition register(ExplosivePropertyCondition condition) {
        EXPLOSIVE_PROPERTY_CONDITIONS.put(condition.rl, condition);
        return condition;
    };

    public enum ExplosiveProperty implements ExplosivePropertiesTooltip.Selectable {

        ENERGY,
        OXYGEN_BALANCE,
        TEMPERATURE,
        BRISANCE,
        SENSITIVITY;

        public Component getName() {
            return DestroyLang.translate("explosive_property."+Lang.asId(name())).component();
        };

        public Component getSymbol() {
            return DestroyLang.translate("explosive_property."+Lang.asId(name())+".symbol").component();
        };

        public Component getDefaultDescription() {
            return Component.translatable(getDescriptionTranslationKey());
        };

        public String getDescriptionTranslationKey() {
            return "destroy.explosive_property."+Lang.asId(name())+".description";
        };

        @Override
        public List<Component> getTooltip(ExplosiveProperties properties) {
            List<Component> tooltip = new ArrayList<>(2);
            tooltip.add(getName());
            tooltip.add(properties.get(this).description.copy().withStyle(ChatFormatting.GRAY));
            return tooltip;
        };
    };

    public static class GeneratedEntries {
        public static void bootstrap(BootstapContext<RegistryEntry> ctx) {
            register(ctx, Items.GUNPOWDER, 0.5f, 0.8f, -1f, -0.7f, 0.5f);
            register(ctx, Items.FIREWORK_STAR, -0.5f, 0.3f, 0f, 0f, 0f);
            register(ctx, DestroyItems.ACETONE_PEROXIDE.get(), -0.5f, -2f, -0.5f, 0.6f, 4.4f);
            register(ctx, DestroyItems.FULMINATED_MERCURY.get(), -0.8f, -0.3f, -3f, -1.5f, 2.3f);
            register(ctx, DestroyItems.NICKEL_HYDRAZINE_NITRATE.get(), -0.1f, 2.5f, -0.7f, 0.2f, 1.6f);
            register(ctx, DestroyItems.TOUCH_POWDER.get(), -3.6f, -1f, -2f, -0.9f, 10f);
            register(ctx, DestroyItems.ANFO.get(), 1.7f, 3.3f, 1f, -1.2f, -0.8f);
            register(ctx, DestroyItems.CONFETTI.get(), -0.1f, 0f, 0f, 0f, 0f);
            register(ctx, DestroyItems.CORDITE.get(), 4.3f, -1f, 5f, -3.3f, -0.4f);
            register(ctx, DestroyItems.DYNAMITE.get(), 3f, 2.1f, 1.5f, -1.8f, -1.1f);
            register(ctx, DestroyItems.NITROCELLULOSE.get(), 3.2f, -3f, 3.4f, 4f, -0.9f);
            register(ctx, DestroyItems.PICRIC_ACID_TABLET.get(), 3.9f, -2.7f, 2.9f, 1.9f, -2.6f);
            register(ctx, DestroyItems.TNT_TABLET.get(), 4.7f, -3.1f, 4.2f, 2.5f, -3f);
            register(ctx, DestroyItems.WHITE_CONFETTI.get(), -0.1f, 0f, 0f, 0f, 0f);
        };

        private static void register(BootstapContext<RegistryEntry> ctx, Item item, float energy, float oxygenBalance, float temperature, float brisance, float sensitivity) {
            ExplosiveProperties properties = new ExplosiveProperties();
            properties.put(ExplosiveProperty.ENERGY, new ExplosivePropertiesEntry(energy, ExplosiveProperty.ENERGY.getDefaultDescription()));
            properties.put(ExplosiveProperty.OXYGEN_BALANCE, new ExplosivePropertiesEntry(oxygenBalance, ExplosiveProperty.OXYGEN_BALANCE.getDefaultDescription()));
            properties.put(ExplosiveProperty.TEMPERATURE, new ExplosivePropertiesEntry(temperature, ExplosiveProperty.TEMPERATURE.getDefaultDescription()));
            properties.put(ExplosiveProperty.BRISANCE, new ExplosivePropertiesEntry(brisance, ExplosiveProperty.BRISANCE.getDefaultDescription()));
            properties.put(ExplosiveProperty.SENSITIVITY, new ExplosivePropertiesEntry(sensitivity, ExplosiveProperty.SENSITIVITY.getDefaultDescription()));

            ctx.register(ResourceKey.create(DestroyRegistries.EXPLOSIVE_PROPERTIES, Destroy.asResource(item.builtInRegistryHolder().key().location().getPath())),
                new RegistryEntry(HolderSet.direct(item.builtInRegistryHolder()), properties));
        };
    };
};
