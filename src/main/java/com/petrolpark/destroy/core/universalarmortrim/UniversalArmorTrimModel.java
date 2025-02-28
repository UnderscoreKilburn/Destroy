package com.petrolpark.destroy.core.universalarmortrim;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.petrolpark.destroy.Destroy;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Destroy.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class UniversalArmorTrimModel extends BakedModelWrapper<BakedModel> {

    private final UniversalArmorTrimItemOverrides trimOverrides;

    public UniversalArmorTrimModel(BakedModel originalModel) {
        super(originalModel);
        trimOverrides = new UniversalArmorTrimItemOverrides(originalModel.getOverrides());
    };

    @Override
    public ItemOverrides getOverrides() {
        return trimOverrides;
    };

    public static final ResourceLocation TRIM_TYPE_PREDICATE_LOCATION = new ResourceLocation("trim_type");

    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult event) {

        // Armor Trim stuff
        List<Entry<ResourceLocation, BakedModel>> modelsToReplace = event.getModels().entrySet()
            .stream()
            .filter(entry ->
                entry.getKey().toString().endsWith("#inventory")
                && entry.getValue() instanceof SimpleBakedModel // Don't override anything complicated
                && Stream.of(entry.getValue().getOverrides().properties).anyMatch(TRIM_TYPE_PREDICATE_LOCATION::equals) // Check if this item is likely to support trims
            ).toList();
        for (Entry<ResourceLocation, BakedModel> entry : modelsToReplace) 
            event.getModels().put(entry.getKey(), new UniversalArmorTrimModel(entry.getValue())); // Replace the model with one which wraps the old one but also provides the additional Armor Trims
    };
    
};
