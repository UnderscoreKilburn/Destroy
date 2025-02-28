package com.petrolpark.destroy.content.confetti;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;

public class ConfettoParticle extends TextureSheetParticle {

    public float swayAngle;
    public float swayMagnitude;
    public int swayOffset;

    public ConfettoParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z);
        xd = vx;
        yd = vy;
        zd = vz;
        this.gravity = 0.04f;
        setSize(0.4f, 0.4f);
        pickSprite(sprites);

        swayAngle = random.nextFloat() * 360f;
        swayMagnitude = 0.02f + random.nextFloat() * 0.04f;
        swayOffset = random.nextInt(360);

        lifetime = 1600;
    };

    @Override
    public void tick() {
        super.tick();

        // Sway through descent
        float offset = Mth.sin((float)(age + swayOffset) / 10f);
        if (yd <= 0 && (xd * xd + yd * yd + zd * zd) < 1d && !onGround) {
            move(Mth.cos(swayAngle) * offset * swayMagnitude, Mth.sin((float)(age + swayOffset) / 5f) * yd, Mth.sin(swayAngle) * offset * swayMagnitude);
        };
        // Begin fading if landed
        if (onGround && age < lifetime - 40) age = lifetime - 40;
        // Fade
        if (lifetime - age <= 40) setAlpha((float)(lifetime - age) / 40f);

        oRoll = roll;
        if (!onGround) roll = offset + swayOffset;
    };

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    };
};
