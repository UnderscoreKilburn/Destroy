package com.petrolpark.destroy.core.chemistry.vat.material;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.petrolpark.destroy.client.DestroyPonderTags;
import com.petrolpark.network.packet.S2CPacket;
import com.petrolpark.recipe.ingredient.BlockIngredient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public class SyncVatMaterialsS2CPacket extends S2CPacket {

    private final Map<BlockIngredient<?>, VatMaterial> materials;

    public SyncVatMaterialsS2CPacket(Map<BlockIngredient<?>, VatMaterial> materials) {
        this.materials = materials;
    };

    public SyncVatMaterialsS2CPacket(FriendlyByteBuf buffer) {
        int count = buffer.readVarInt();
        materials = new HashMap<>(count);
        for (int i = 0; i < count; i++) {
            materials.put(BlockIngredient.read(buffer), new VatMaterial(buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), false));
        };
    };

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeVarInt(materials.size());
        materials.forEach((blockIngredient, material) -> {
            BlockIngredient.write(blockIngredient, buffer);
            buffer.writeFloat(material.maxPressure());
            buffer.writeFloat(material.thermalConductivity());
            buffer.writeBoolean(material.transparent());
        });
    };

    @Override
    public boolean handle(Supplier<Context> supplier) {
        supplier.get().enqueueWork(() -> {
            VatMaterial.clearDatapackMaterials();
            VatMaterial.BLOCK_MATERIALS.putAll(materials);
            DestroyPonderTags.refreshVatMaterialsTag();
        });
        return true;
    };
    
};
