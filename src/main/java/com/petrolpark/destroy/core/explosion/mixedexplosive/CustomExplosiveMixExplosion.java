package com.petrolpark.destroy.core.explosion.mixedexplosive;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.core.explosion.SmartExplosion;
import com.petrolpark.destroy.core.explosion.mixedexplosive.ExplosiveProperties.ExplosiveProperty;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.FireworkStarItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;

public class CustomExplosiveMixExplosion extends SmartExplosion {

    public static final Serializer SERIALIZER = new Serializer(Destroy.asResource("custom_mix"));

    protected static final Lazy<ItemStack> SILK_TOUCH_TOOL = Lazy.of(() -> {
        ItemStack stack = new ItemStack(Items.NETHERITE_PICKAXE);
        stack.enchant(Enchantments.SILK_TOUCH, 1);
        return stack;
    });

    protected final ExplosiveProperties properties;
    protected final List<ItemStack> specialItems;

    protected CustomExplosiveMixExplosion(Level level, ExplosiveProperties properties, List<ItemStack> specialItems, Entity source, DamageSource damageSource, ExplosionDamageCalculator damageCalculator, Vec3 position, float radius, float irregularity) {
        super(level, source, damageSource, damageCalculator, position, radius, irregularity);
        this.properties = properties;
        this.specialItems = specialItems;
    };

    public static CustomExplosiveMixExplosion create(Level level, MixedExplosiveInventory inv, @Nullable Entity source, Vec3 position) {
        return create(level, inv.getExplosiveProperties(), inv.getSpecialItems(), source, position);
    };

    protected static CustomExplosiveMixExplosion create(Level level, ExplosiveProperties properties, List<ItemStack> specialItems, @Nullable Entity source, Vec3 position) {
        float oxygenBalance = Math.abs(properties.get(ExplosiveProperty.OXYGEN_BALANCE).value) / 10f;
        if (properties.fulfils(ExplosiveProperties.DROPS_HEADS)) source = new DummyChargedCreeper(level, source);
        
        return new CustomExplosiveMixExplosion(level, properties, specialItems, source, null, new DamageCalculator(properties), position, (4f + (properties.get(ExplosiveProperty.ENERGY).value / 3f)) * (1f - oxygenBalance * oxygenBalance), 0.5f);
    };

    @Override
    public boolean shouldDoObliterationDrops() {
        return properties.fulfils(ExplosiveProperties.OBLITERATES);
    };

    @Override
    public boolean shouldAlwaysDropExperience() {
        return properties.fulfils(ExplosiveProperties.DROPS_EXPERIENCE);
    };

    @Override
    public boolean shouldAlwaysDropExperienceFromMobs() {
        return properties.fulfils(ExplosiveProperties.DROPS_EXPERIENCE);
    };

    @Override
    public void modifyLoot(BlockPos pos, Builder builder) {
        if (properties.fulfils(ExplosiveProperties.SILK_TOUCH)) builder.withParameter(LootContextParams.TOOL, SILK_TOUCH_TOOL.get());
    };

    @Override
    public void explodeEntity(Entity entity, float strength) {
        super.explodeEntity(entity, strength * (properties.hasCondition(ExplosiveProperties.ENTITIES_PUSHED) && !properties.fulfils(ExplosiveProperties.ENTITIES_PUSHED) ? 0.1f : 1f));
    };

    @Override
    public void effects(boolean clientSide) {
        super.effects(clientSide);
        ListTag fireworkExplosionsTag = new ListTag();
        if (clientSide) for (ItemStack stack : specialItems) {
            if (stack.getItem() instanceof FireworkStarItem) fireworkExplosionsTag.add(stack.getTagElement("Explosion"));
            else if (stack.getItem() instanceof ISpecialEffectExplosiveItem specialItem) specialItem.explode(this, level, toBlow, stack);
        };
        if (!fireworkExplosionsTag.isEmpty()) {
            CompoundTag fireworkTag = new CompoundTag();
            fireworkTag.put("Explosions", fireworkExplosionsTag);
            level.createFireworks(x, y, z, 0d, 0d, 0d, fireworkTag);
        };
    };

    public static class DamageCalculator extends ExplosionDamageCalculator {

        protected final ExplosiveProperties properties;

        public DamageCalculator(ExplosiveProperties properties) {
            this.properties = properties;
        };

        @Override
        public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, FluidState fluid) {
            if (!fluid.isEmpty() && (properties.fulfils(ExplosiveProperties.EVAPORATES_FLUIDS) || properties.fulfils(ExplosiveProperties.UNDERWATER))) return Optional.of(0f);
            return super.getBlockExplosionResistance(explosion, level, pos, state, fluid);
        };

        @Override
        public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) {
            return level.getFluidState(pos).isEmpty() || !properties.fulfils(ExplosiveProperties.UNDERWATER) || properties.fulfils(ExplosiveProperties.EVAPORATES_FLUIDS);
        };
        
    };

    public static class DummyChargedCreeper extends Creeper {

        protected Entity trueSource;

        public DummyChargedCreeper(Level level, Entity trueSource) {
            super(EntityType.CREEPER, level);
        };

        @Override
        public Component getName() {
            return trueSource == null ? Component.translatable("entity.destroy.dummy_charged_creeper") : trueSource.getName();
        };

        @Override
        public boolean isPowered() {
            return true;
        };

        @Override
        public boolean canDropMobsSkull() {
            return true;
        };

    };

    @Override
    public Serializer getSerializer() {
        return SERIALIZER;
    };

    @Override
    public void write(FriendlyByteBuf buffer) {
        properties.write(buffer);
        buffer.writeCollection(specialItems, FriendlyByteBuf::writeItem);
        buffer.writeDouble(getPosition().x());
        buffer.writeDouble(getPosition().y());
        buffer.writeDouble(getPosition().z());
    };

    public static class Serializer extends SmartExplosion.Serializer<CustomExplosiveMixExplosion> {

        public Serializer(ResourceLocation id) {
            super(id);
        };

        @Override
        @OnlyIn(Dist.CLIENT)
        public SmartExplosion read(FriendlyByteBuf buffer) {
            Minecraft mc = Minecraft.getInstance();
            return CustomExplosiveMixExplosion.create(mc.level, ExplosiveProperties.read(buffer), buffer.readCollection(ArrayList<ItemStack>::new, FriendlyByteBuf::readItem), null, new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()));
        };

    };
    
};
