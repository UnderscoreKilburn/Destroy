package com.petrolpark.destroy.core.chemistry.vat.material;

import com.petrolpark.destroy.DestroyBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import com.simibubi.create.content.decoration.palettes.AllPaletteBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;

import java.util.function.UnaryOperator;

public class DestroyVatMaterialGen extends BaseRecipeProvider {

    GeneratedRecipe

    VAT_CONTROLLER = create("vat_controller", b -> b.require(DestroyBlocks.VAT_CONTROLLER.get()).maxPressure(Float.MAX_VALUE).thermalConductivity(0)),

    COPPER_CASING = create("copper_casing", b -> b.require(AllBlocks.COPPER_CASING.get()).maxPressure(500000).thermalConductivity(400)),
    IRON = create("iron", b -> b.require(AllTags.forgeBlockTag("storage_blocks/iron")).maxPressure(250000).thermalConductivity(50)),
    NETHERITE_BLOCK = create("netherite_block", b -> b.require(Blocks.NETHERITE_BLOCK).maxPressure(2000000).thermalConductivity(70)),
    STAINLESS_STEEL = create("stainless_steel", b -> b.require(AllTags.forgeBlockTag("storage_blocks/stainless_steel")).maxPressure(1000000).thermalConductivity(35)),
    INSULATED_STAINLESS_STEEL = create("insulated_stainless_steel", b -> b.require(DestroyBlocks.INSULATED_STAINLESS_STEEL_BLOCK.get()).maxPressure(800000).thermalConductivity(20)),

    FIBERGLASS = create("fiberglass", b -> b.require(DestroyBlocks.FIBERGLASS_BLOCK.get()).maxPressure(350000).thermalConductivity(17)),
    BOROSILICATE_GLASS = create("borosilicate_glass", b -> b.require(DestroyBlocks.BOROSILICATE_GLASS.get()).maxPressure(300000).thermalConductivity(15).transparent(true)),
    TINTED_GLASS = create("tinted_glass", b -> b.require(Blocks.TINTED_GLASS).maxPressure(100000).thermalConductivity(18)),
    WEAK_GLASS = create("weak_glass", b -> b
        .require(Blocks.GLASS, AllPaletteBlocks.FRAMED_GLASS.get(), AllPaletteBlocks.VERTICAL_FRAMED_GLASS.get(), AllPaletteBlocks.HORIZONTAL_FRAMED_GLASS.get(), AllPaletteBlocks.TILED_GLASS.get())
        .require(AllTags.forgeBlockTag("glass/black"))
        .require(AllTags.forgeBlockTag("glass/blue"))
        .require(AllTags.forgeBlockTag("glass/brown"))
        .require(AllTags.forgeBlockTag("glass/cyan"))
        .require(AllTags.forgeBlockTag("glass/gray"))
        .require(AllTags.forgeBlockTag("glass/green"))
        .require(AllTags.forgeBlockTag("glass/light_blue"))
        .require(AllTags.forgeBlockTag("glass/light_gray"))
        .require(AllTags.forgeBlockTag("glass/lime"))
        .require(AllTags.forgeBlockTag("glass/magenta"))
        .require(AllTags.forgeBlockTag("glass/orange"))
        .require(AllTags.forgeBlockTag("glass/pink"))
        .require(AllTags.forgeBlockTag("glass/purple"))
        .require(AllTags.forgeBlockTag("glass/red"))
        .maxPressure(100000).thermalConductivity(30).transparent(true));

    public DestroyVatMaterialGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    public String getName() {
        return modid + "'s vat material properties";
    }

    protected GeneratedRecipe create(String name, UnaryOperator<VatMaterial.Builder> transform) {
        GeneratedRecipe generatedRecipe =
            c -> transform.apply(new VatMaterial.Builder(asResource(name)))
                .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }
}
