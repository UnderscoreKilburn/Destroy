package com.petrolpark.destroy;

import static com.petrolpark.destroy.Destroy.REGISTRATE;

import com.petrolpark.destroy.core.explosion.PrimedBombEntity;
import com.petrolpark.destroy.core.explosion.PrimedBombEntityRenderer;
import com.petrolpark.destroy.core.explosion.mixedexplosive.MixedExplosiveEntity;
import com.petrolpark.destroy.core.explosion.mixedexplosive.MixedExplosiveEntityRenderer;
import com.tterrag.registrate.util.entry.EntityEntry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.EntityFactory;

public class DestroyEntityTypes {

    public static final EntityEntry<MixedExplosiveEntity> PRIMED_CUSTOM_EXPLOSIVE = customBomb("primed_custom_explosive", MixedExplosiveEntity::new);

    // Legacy explosives
    public static final EntityEntry<PrimedBombEntity.Anfo> PRIMED_ANFO = bomb("primed_anfo", PrimedBombEntity.Anfo::new);
    public static final EntityEntry<PrimedBombEntity.PicricAcid> PRIMED_PICRIC_ACID = bomb("primed_picric_acid", PrimedBombEntity.PicricAcid::new);
    public static final EntityEntry<PrimedBombEntity.Cordite> PRIMED_CORDITE = bomb("primed_cordite", PrimedBombEntity.Cordite::new);
    public static final EntityEntry<PrimedBombEntity.Nitrocellulose> PRIMED_NITROCELLULOSE = bomb("primed_nitrocellulose", PrimedBombEntity.Nitrocellulose::new);

    private static <T extends PrimedBombEntity> EntityEntry<T> bomb(String name, EntityFactory<T> factory) {
        return REGISTRATE.entity(EntityType.TNT, name, factory, MobCategory.MISC)
            .renderer(() -> PrimedBombEntityRenderer::new)
            .register();
    };

    private static EntityEntry<MixedExplosiveEntity> customBomb(String name, EntityFactory<MixedExplosiveEntity> factory) {
        return REGISTRATE.entity(EntityType.TNT, name, factory, MobCategory.MISC)
            .renderer(() -> MixedExplosiveEntityRenderer::new)
            .register();
    };

    public static final void register() {};
};
