package com.petrolpark.destroy.core.chemistry.vat.observation.colorimeter;

import java.util.function.Supplier;

import com.petrolpark.destroy.DestroyBlockEntityTypes;
import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;
import com.petrolpark.network.packet.C2SPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public class ConfigureColorimeterC2SPacket extends C2SPacket {

    protected final boolean observingGas;
    protected final LegacySpecies species;
    protected final BlockPos pos;

    public ConfigureColorimeterC2SPacket(boolean observingGas, LegacySpecies species, BlockPos pos) {
        this.observingGas = observingGas;
        this.species = species;
        this.pos = pos;
    };

    public ConfigureColorimeterC2SPacket(FriendlyByteBuf buffer) {
        observingGas = buffer.readBoolean();
        if (buffer.readBoolean()) {
            species = LegacySpecies.getMolecule(buffer.readUtf());
        } else {
            species = null;
        }
        pos = buffer.readBlockPos();
    };

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBoolean(observingGas);
        buffer.writeBoolean(species != null);
        if (species != null) buffer.writeUtf(species.getFullID());
        buffer.writeBlockPos(pos);
    };

    @Override
    public boolean handle(Supplier<Context> supplier) {
        Context context = supplier.get();
        context.enqueueWork(() -> {
            context.getSender().level().getBlockEntity(pos, DestroyBlockEntityTypes.COLORIMETER.get()).ifPresent(be -> {
                be.configure(species, observingGas);
            });
        });
        return true;
    };
    
};
