package com.petrolpark.destroy.core.chemistry.vat.material;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Iterator;

import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.recipe.ingredient.BlockIngredient;

import net.minecraft.world.level.block.state.BlockState;

/**
 * Information about a Block from which a Vat can be construced.
 * @param maxPressure The maxmimum pressure (in pascals) this Material can withstand before the Vat explodes
 * @param thermalConductivity The thermal conductivity (in watts per block-side-length-kelvin) of this Material
 * @param transparent Whether Blocks of this Material are permeable to sunlight and UV
 * @param builtin Whether this Vat Material must exist and was not added by a datapack
 */
public record VatMaterial(float maxPressure, float thermalConductivity, boolean transparent, boolean builtIn) {

    public static final Map<BlockIngredient<?>, VatMaterial> BLOCK_MATERIALS = new HashMap<>();

    public static final VatMaterial UNBREAKABLE = new VatMaterial(Float.MAX_VALUE, 0f, false, true);

    /**
     * Whether the given Block can be used to construct a Vat.
     * @param block
     */
    public static boolean isValid(BlockState state) {
        return BLOCK_MATERIALS.keySet().stream().anyMatch(ingredient -> ingredient.isValid(state));
    };

    public static Optional<VatMaterial> getMaterial(BlockState state) {
        return BLOCK_MATERIALS.entrySet().stream().filter(entry -> entry.getKey().isValid(state)).map(Entry::getValue).findFirst();
    };

    public static void clearDatapackMaterials() {
        for (Iterator<Entry<BlockIngredient<?>, VatMaterial>> iterator = BLOCK_MATERIALS.entrySet().iterator(); iterator.hasNext();) {
            if (!iterator.next().getValue().builtIn()) iterator.remove();
        };
    };

    public static void registerDestroyVatMaterials() {
        BLOCK_MATERIALS.put(new BlockIngredient.SingleBlockIngredient(DestroyBlocks.VAT_CONTROLLER.get()), UNBREAKABLE);
    };
};
