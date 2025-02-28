package com.petrolpark.destroy.config;

import java.util.EnumMap;

import com.petrolpark.destroy.client.DestroyLang;

import net.minecraft.sounds.SoundSource;

public class DestroySubstancesConfigs extends DestroyConfigBase {

    public final ConfigInt chorusWineTeleportTime = i(20, 1, 60, "teleportTime", Comments.inSeconds, Comments.teleportTime);
    public final ConfigFloat aspirinHeal = f(5f, 0f, "aspirinHeal", "How many half-hearts using an Aspirin Syringe heals");
    public final ConfigBool burnIodineForDragonsBreath = b(true, "burnIodineForDragonsBreath", "Whether burning Iodine will release Dragon's Breath");

    public final ConfigInt sodiumDecayTime = i(600, 1, Integer.MAX_VALUE, "sodiumDecayTime", "How many ticks it takes for Sodium to rust");
    public final ConfigInt quicklimeBaseDecayTime = i(12000, 1, Integer.MAX_VALUE, "quicklimeBaseDecayTime", "The base number of ticks it takes for Quicklime to turn into Calcium Carbonate");

    public final ConfigGroup babyBlue = group(0, "babyBlue", Comments.babyBlue);
    public final ConfigInt babyBlueMaxAddictionLevel = i(590, 0, "maxAddictionLevel", Comments.toDisable, Comments.maxAddictionLevel);
    public final ConfigFloat babyBlueMiningSpeedBonus = f(0.5f, Float.MIN_VALUE, "babyBlueMiningSpeedBonus", "The proportion by which mining speed is boosted for every level of Baby Blue High", "For example, 0.5 gives +50% per level");
    public final ConfigFloat babyBlueWidthdrawalSpeedBonus = f(-0.3f, Float.MIN_VALUE, "babyBlueMiningSpeedBonus", "The proportion by which mining speed is \"boosted\" for every level of Baby Blue Withdrawal", "For example, -0.3 gives -30% per level");
    public final ConfigBool keepBabyBlueAddictionOnDeath = b(true, "keepBabyBlueAddictionOnDeath", "Conserve the level of Baby Blue Addiction a player has when they die", Comments.overriddenKeepInv);

    public final ConfigGroup alcohol = group(0, "alcohol", Comments.alcohol);
    public final ConfigInt inebriationDuration = i(1200, 0, "inebriationDuration", Comments.inTicks, Comments.inebriationDuration);
    public final ConfigInt hangoverDuration = i(1200, 0, "hangoverDuration", Comments.inTicks, Comments.hangoverDuration);
    public final ConfigFloat drunkenSlipping = f(0.7f, 0f, 1.0f, "drunkenSlipping", Comments.drunkenSlipping);
    public final EnumMap<SoundSource, ConfigFloat> soundSourceThresholds = enumFloatMap(SoundSource.class, SoundSource.values(), v -> "hangoverDamage" + DestroyLang.pascal(v.name()) + "Threshold", v -> new String[]{"The volume threshold for sounds from the source " + DestroyLang.pascal(v.name()) + " to damage entities with hangovers", "[1 will disable damage for that sound source]"}, 0f, 1f, 1f, 0.5f, 1f, 0.5f, 0.5f, 0.5f, 0.5f);
    public final EnumMap<SoundSource, ConfigFloat> soundSourceDamage = enumFloatMap(SoundSource.class, SoundSource.values(), v -> "hangoverDamage" + DestroyLang.pascal(v.name()), v -> new String[]{"The damage done by sounds from the source " + DestroyLang.pascal(v.name()) + " to entities with hangovers"}, 0f, Float.MAX_VALUE, 1f);
    public final ConfigFloat hangoverNoiseTriggerRadius = f(5f, 0f, 128f, "hangoverNoiseTriggerRadius", "[in blocks]", "The square radius away from a noise which will be checked for entities with hangovers");
    
    public final ConfigGroup creatine = group(0, "creatine", "Creatine");
    public final ConfigInt creatineExtraInventorySize = i(15, 0, 256, "creatineExtraInventorySize", "The number of additional Inventory Slots granted by taking Creatine");
    public final ConfigInt creatineExtraHotbarSlots = i(3, 0, 32, "creatineExtraHotbarSlots", "The maximum number of extra Hotbar Slots granted by taking Creatine");
    public final ConfigBool keepCreatineExtraInventorySizeOnDeath = b(false, "keepCreatineExtraInventorySizeOnDeath", Comments.overriddenKeepInv, "[This will only affect the extra inventory slots due to Creatine, other mods may handle it differently]");

    @Override
    public String getName() {
        return "substances";
    };

    private static class Comments {
        static String toDisable = "[0 to disable this feature]";
        static String inTicks = "[in ticks]";
        static String inSeconds = "[in seconds]";
        static String overriddenKeepInv = "[This will be overriden as true if the Game Rule keepInventory is true]";

        static String babyBlue = "Baby Blue";
        static String maxAddictionLevel = "Each level corresponds to an additional second of withdrawal.";

        static String alcohol = "Alcohol";
        static String inebriationDuration = "How long each additional level of inebriation adds.";
        static String hangoverDuration = "How long each level of inebriation adds to the Hangover effect.";
        static String drunkenSlipping = "How much Entities will slip when inebriated.";

        static String teleportTime = "How far in the past your position will be set when drinking Chorus Wine.";
    };

    public static boolean babyBlueEnabled() {
        return DestroyAllConfigs.COMMON.enableBabyBlue.get();  
    };

    public static boolean alcoholEnabled() {
        return DestroyAllConfigs.COMMON.enableAlcohol.get();
    };

    public static boolean iodineDragonsBreath() {
        return DestroyAllConfigs.SERVER.substances.burnIodineForDragonsBreath.get();
    };
};
