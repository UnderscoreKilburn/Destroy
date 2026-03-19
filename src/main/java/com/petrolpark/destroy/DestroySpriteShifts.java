package com.petrolpark.destroy;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;

public class DestroySpriteShifts {

    public static final CTSpriteShiftEntry
        NETHER_CROCOITE_BLOCK = omni("nether_crocoite_ore"),
        STAINLESS_STEEL_BLOCK = omni("stainless_steel_block"),
        BOROSILICATE_GLASS = omni("borosilicate_glass")
        ;

    public static final SpriteShiftEntry
        STAINLESS_VAT_CONTROLLER = get("block/stainless_steel_block_connected", "block/vat/vat_controller"),
        COPPER_VAT_CONTROLLER = get("block/stainless_steel_block_connected", "block/vat/copper_vat_controller");

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(Destroy.asResource(originalLocation), Destroy.asResource(targetLocation));
    }

    private static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }
    private static CTSpriteShiftEntry horizontal(String name) {
        return getCT(AllCTTypes.HORIZONTAL, name);
    }
    private static CTSpriteShiftEntry vertical(String name) {
        return getCT(AllCTTypes.VERTICAL, name);
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, Destroy.asResource("block/" + blockTextureName),
            Destroy.asResource("block/" + connectedTextureName + "_connected"));
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }

};
