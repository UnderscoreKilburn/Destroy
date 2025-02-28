package com.petrolpark.destroy.core.fluid;

import com.petrolpark.destroy.MoveToPetrolparkLibrary;
import com.petrolpark.destroy.client.DestroyParticleTypes;
import com.simibubi.create.content.equipment.bell.BasicParticleData;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;

@MoveToPetrolparkLibrary
public class RainParticle extends TintedSplashParticle {

    public RainParticle(ClientLevel level, double x, double y, double z, double r, double g, double b, SpriteSet sprites) {
        super(level, x, y, z, r, g, b, sprites);
    };

    public static class Data extends BasicParticleData<RainParticle> {
        
        @Override
        public ParticleType<?> getType() {
            return DestroyParticleTypes.RAIN.get();
        };

        @Override
        public IBasicParticleFactory<RainParticle> getBasicFactory() {
            return RainParticle::new;
        };
    };
    
};
