package com.petrolpark.destroy.client;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.core.chemistry.vat.material.VatMaterial;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.foundation.ponder.PonderTagRegistry.TagBuilder;
import com.simibubi.create.infrastructure.ponder.AllPonderTags;

public class DestroyPonderTags {

    public static final PonderTag

    CHEMISTRY = new PonderTag(Destroy.asResource("chemistry"))
        .item(DestroyItems.TEST_TUBE)
        .addToIndex(),
    
    DESTROY = new PonderTag(Destroy.asResource("destroy"))
        .item(DestroyItems.LOGO)
        .addToIndex(),

    VAT_SIDE_BLOCKS = new PonderTag(Destroy.asResource("vat_side_blocks"))
        .item(DestroyBlocks.VAT_CONTROLLER);

    public static void register() {

        PonderRegistry.TAGS.forTag(DestroyPonderTags.CHEMISTRY)
            .add(AllBlocks.BASIN)
            .add(DestroyBlocks.BLACKLIGHT)
            .add(AllBlocks.BLAZE_BURNER)
            .add(DestroyBlocks.BUBBLE_CAP)
            .add(DestroyBlocks.CENTRIFUGE)
            .add(DestroyBlocks.COOLER)
            .add(AllBlocks.MECHANICAL_MIXER)
            .add(DestroyBlocks.VAT_CONTROLLER)
        ;
        
        PonderRegistry.TAGS.forTag(DestroyPonderTags.DESTROY)
            .add(DestroyBlocks.AGING_BARREL)
            .add(DestroyBlocks.BLACKLIGHT)
            .add(DestroyBlocks.BUBBLE_CAP)
            .add(DestroyBlocks.CATALYTIC_CONVERTER)
            .add(DestroyBlocks.CENTRIFUGE)
            .add(DestroyBlocks.COOLER)
            .add(DestroyBlocks.CREATIVE_PUMP)
            .add(DestroyBlocks.CUSTOM_EXPLOSIVE_MIX)
            .add(DestroyBlocks.DYNAMO)
            .add(DestroyBlocks.EXTRUSION_DIE)
            .add(DestroyItems.HYPERACCUMULATING_FERTILIZER)
            .add(DestroyBlocks.KEYPUNCH)
            .add(DestroyItems.POLLUTION_SYMBOL)
            .add(DestroyBlocks.PUMPJACK)
            .add(DestroyBlocks.REDSTONE_PROGRAMMER)
            .add(DestroyItems.SEISMOMETER)
            .add(DestroyItems.SEISMOGRAPH)
            .add(DestroyBlocks.SIPHON)
            .add(DestroyBlocks.TREE_TAP)
            .add(DestroyBlocks.VAT_CONTROLLER)
        ;

        TagBuilder vatSideBlockBuilder = PonderRegistry.TAGS.forTag(DestroyPonderTags.VAT_SIDE_BLOCKS);
        VatMaterial.BLOCK_MATERIALS.forEach((blockIngredient, material) -> blockIngredient.getDisplayedItemStacks().forEach(stack -> vatSideBlockBuilder.add(stack.getItem())));

        PonderRegistry.TAGS.forTag(AllPonderTags.FLUIDS)
            .add(DestroyBlocks.BUBBLE_CAP)
            .add(DestroyBlocks.CATALYTIC_CONVERTER)
            .add(DestroyBlocks.CENTRIFUGE)
            .add(DestroyBlocks.CREATIVE_PUMP)
            .add(DestroyBlocks.PUMPJACK)
            .add(DestroyBlocks.SIPHON)
            .add(DestroyBlocks.TREE_TAP)
            .add(DestroyBlocks.VAT_CONTROLLER)
        ;

        PonderRegistry.TAGS.forTag(AllPonderTags.KINETIC_APPLIANCES)
            .add(DestroyBlocks.CENTRIFUGE)
            .add(DestroyBlocks.DYNAMO)
            .add(DestroyBlocks.KEYPUNCH)
            .add(DestroyBlocks.MECHANICAL_SIEVE)
            .add(DestroyBlocks.PUMPJACK)
            .add(DestroyBlocks.TREE_TAP)
        ;

        PonderRegistry.TAGS.forTag(AllPonderTags.ARM_TARGETS)
            .add(DestroyBlocks.AGING_BARREL)
        ;

        PonderRegistry.TAGS.forTag(AllPonderTags.REDSTONE)
            .add(DestroyBlocks.DYNAMO)
            .add(DestroyBlocks.REDSTONE_PROGRAMMER)
            .add(DestroyBlocks.SIPHON)
        ;

        PonderRegistry.TAGS.forTag(AllPonderTags.DISPLAY_SOURCES)
            .add(DestroyBlocks.BUBBLE_CAP)
            .add(DestroyBlocks.CENTRIFUGE)
            .add(DestroyBlocks.COLORIMETER)
            .add(DestroyBlocks.POLLUTOMETER)
            .add(DestroyBlocks.VAT_CONTROLLER)
        ;

        PonderRegistry.TAGS.forTag(AllPonderTags.CONTRAPTION_ACTOR)
            .add(DestroyBlocks.EXTRUSION_DIE)
        ;

        PonderRegistry.TAGS.forTag(AllPonderTags.CREATIVE)
            .add(DestroyBlocks.CREATIVE_PUMP)
        ;
    };

    public static final void refreshVatMaterialsTag() {
        VatMaterial.BLOCK_MATERIALS.keySet().forEach(blockIngredient -> blockIngredient.getDisplayedItemStacks().forEach(stack -> PonderRegistry.TAGS.forTag(VAT_SIDE_BLOCKS).add(stack.getItem())));
    };
    
};
