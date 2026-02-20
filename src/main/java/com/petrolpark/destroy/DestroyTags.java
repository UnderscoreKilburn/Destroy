package com.petrolpark.destroy;


import com.simibubi.create.Create;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import static com.petrolpark.destroy.DestroyTags.NameSpace.MOD;
import static com.petrolpark.destroy.DestroyTags.NameSpace.FORGE;

public class DestroyTags {

    // Mostly all copied from Create source code

    public enum NameSpace {

        MOD(Destroy.MOD_ID),
        CREATE(Create.ID),
        FORGE("forge");

        public final String id;

        NameSpace(String id) {
            this.id = id;
        }

        public ResourceLocation id(String path) {
            return ResourceLocation.fromNamespaceAndPath(this.id, path);
        }

        public ResourceLocation id(Enum<?> entry, @Nullable String pathOverride) {
            return this.id(pathOverride != null ? pathOverride : Lang.asId(entry.name()));
        }
    }

    public enum Items {

        ALCOHOLIC_DRINKS,
        BEETROOT_ASHES,
        BONEMEAL_BYPASSES_POLLUTION,
        DESTROY_INGOTS,
        EYES,
        FERTILIZERS,
        FLUXES,
        HEFTY_BEETROOTS,
        LIABLE_TO_CHANGE,
        SPRAY_BOTTLES,
        SYRINGES,
        TEST_TUBE_RACK_STORABLE,
        YEAST,

        CHEMICAL_PROTECTION_EYES("chemical_protection/eyes"),
        CHEMICAL_PROTECTION_NOSE("chemical_protection/nose"),
        CHEMICAL_PROTECTION_MOUTH("chemical_protection/mouth"),
        CHEMICAL_PROTECTION_HEAD("chemical_protection/head"),
        CHEMICAL_PROTECTION_CHEST("chemical_protection/chest"),
        CHEMICAL_PROTECTION_LEGS("chemical_protection/legs"),
        CHEMICAL_PROTECTION_FEET("chemical_protection/feet"),
        CONTAMINABLE,

        PLASTICS,
        RIGID_PLASTICS("plastics/rigid"),
        TEXTILE_PLASTICS("plastics/textile"),
        POROUS_PLASTICS("plastics/porous"),
        INERT_PLASTICS("plastics/inert"),
        RUBBER_PLASTICS("plastics/rubber"),
        TRANSPARENT_PLASTICS("plastics/transparent"),

        PRIMARY_EXPLOSIVES("explosives/primary"),
        SCHEMATICANNON_FUELS,
        SECONDARY_EXPLOSIVES("explosives/secondary"),
        OBLITERATION_EXPLOSIVES,  // This tag is only used to display the right Blocks in JEI.

        DUSTS_IRON(FORGE, "dusts/iron"),
        DUSTS_NICKEL(FORGE, "dusts/nickel"),
        DUSTS_CHROMIUM(FORGE, "dusts/chromium"),
        DUSTS_LIME(FORGE, "dusts/lime"),

        RAW_NICKEL(FORGE, "raw_materials/nickel"),
        ;

        public final TagKey<Item> tag;

        Items() {
            this(null);
        };
        Items(String path) {
            this(MOD, path);
        };
        Items(NameSpace namespace, String path) {
            tag = ItemTags.create(namespace.id(this, path));
        };

        @SuppressWarnings("deprecation") // Create does it therefore so can I
        public boolean matches(Item item) {
            return item.builtInRegistryHolder().containsTag(tag);
        };

        public boolean matches(ItemStack item) {
            return item.is(tag);
        };

        public static void init() {};
    };

    public enum Blocks {
        ARC_FURNACE_TRANSFORMABLE,
        BEETROOTS,
        ACID_RAIN_DESTRUCTIBLE,
        GANGUE,
        ACID_RAIN_DIRT_REPLACEABLE,

        NICKEL_ORE(FORGE, "ores/nickel"),
        FLUORITE_ORE(FORGE, "ores/fluorite"),

        RAW_NICKEL_STORAGE_BLOCKS(FORGE, "storage_blocks/raw_nickel"),
        FLUORITE_STORAGE_BLOCKS(FORGE, "storage_blocks/fluorite"),
        CARBON_FIBER_STORAGE_BLOCKS(FORGE, "storage_blocks/carbon_fiber"),
        IODINE_STORAGE_BLOCKS(FORGE, "storage_blocks/iodine"),
        CHROMIUM_STORAGE_BLOCKS(FORGE, "storage_blocks/chromium"),
        PLATINUM_STORAGE_BLOCKS(FORGE, "storage_blocks/platinum"),
        PALLADIUM_STORAGE_BLOCKS(FORGE, "storage_blocks/palladium"),
        RHODIUM_STORAGE_BLOCKS(FORGE, "storage_blocks/rhodium"),
        NICKEL_STORAGE_BLOCKS(FORGE, "storage_blocks/nickel"),
        LEAD_STORAGE_BLOCKS(FORGE, "storage_blocks/lead"),
        STAINLESS_STEEL_STORAGE_BLOCKS(FORGE, "storage_blocks/stainless_steel"),

        ;

        public final TagKey<Block> tag;
        public final Lazy<TagKey<Item>> itemTag;

        Blocks() {
            this(null);
        };
        Blocks(String path) {
            this(MOD, path);
		};
        Blocks(NameSpace namespace, String path) {
            tag = BlockTags.create(namespace.id(this, path));
            itemTag = Lazy.of(() -> ItemTags.create(tag.location()));
        };

        @SuppressWarnings("deprecation") // Create does it therefore so can I
        public boolean matches(Block block) {
            return block.builtInRegistryHolder().containsTag(tag);
        };
    };

    public enum Fluids {
        AMPLIFIES_SMOG,
        ACIDIFIES_RAIN,
        DEPLETES_OZONE,
        GREENHOUSE_GAS,
        RADIOACTIVE,
        COOLANT,

        CRUDE_OIL(FORGE, "crude_oil"),
        ;

        public final TagKey<Fluid> tag;

        Fluids() {
			this(null);
        };
        Fluids(String path) {
            this(MOD, path);
        };
        Fluids(NameSpace namespace, String path) {
            tag = FluidTags.create(namespace.id(this, path));
        };

        @SuppressWarnings("deprecation") // Create does it therefore so can I
        public boolean matches(Fluid fluid) {
            return fluid.builtInRegistryHolder().is(tag);
        };
    };

    public enum MobEffects {
        CAUSES_INFERTILITY,
        ;

        public final TagKey<MobEffect> tag;

        MobEffects() {
            tag = TagKey.create(Registries.MOB_EFFECT, Destroy.asResource(Lang.asId(name())));
        };

        public boolean matches(MobEffectInstance effect) {
            return matches(effect.getEffect());
        };

        public boolean matches(MobEffect effect) {
            return ForgeRegistries.MOB_EFFECTS.getHolder(effect).orElseThrow().is(tag);
        };
    };

    public static void register() {
        Items.init();
    };

};
