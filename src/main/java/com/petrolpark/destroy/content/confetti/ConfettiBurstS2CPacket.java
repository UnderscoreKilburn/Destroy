package com.petrolpark.destroy.content.confetti;

import java.util.function.Supplier;

import org.joml.Vector3f;

import com.petrolpark.network.packet.S2CPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent.Context;

public class ConfettiBurstS2CPacket extends S2CPacket {

    protected final ItemStack confetti;
    protected final Vector3f pos;
    protected final Vector3f velocity;

    public ConfettiBurstS2CPacket(FriendlyByteBuf buffer) {
        confetti = buffer.readItem();
        pos = buffer.readVector3f();
        velocity = buffer.readVector3f();
    };

    public ConfettiBurstS2CPacket(ItemStack confetti, Vec3 pos, Vec3 v) {
        this.confetti = confetti;
        this.pos = new Vector3f((float)pos.x, (float)pos.y, (float)pos.z);
        this.velocity = new Vector3f((float)v.x, (float)v.y, (float)v.z);
    };

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeItem(confetti);
        buffer.writeVector3f(pos);
        buffer.writeVector3f(velocity);
    };

    @Override
    public boolean handle(Supplier<Context> supplier) {
        supplier.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            RandomSource rand = mc.level.random;
            if (!(confetti.getItem() instanceof ConfettiItem item)) return;
            for (int i = 0; i < 256; i++) {
                Vec3 v = new Vec3(velocity).add(new Vec3(-0.05d + rand.nextFloat() * 0.1d, -0.05d + rand.nextFloat() * 0.1d, -0.05d + rand.nextFloat() * 0.1d));
                mc.level.addParticle(item.particleFactory.get(), pos.x(), pos.y(), pos.z(), v.x(), v.y(), v.z());
            };
        });
        return true;
    };
    

};
