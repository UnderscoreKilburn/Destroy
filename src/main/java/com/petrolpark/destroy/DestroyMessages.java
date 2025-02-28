package com.petrolpark.destroy;

import java.util.function.Function;

import com.petrolpark.destroy.content.confetti.ConfettiBurstS2CPacket;
import com.petrolpark.destroy.content.oil.seismology.MarkSeismographC2SPacket;
import com.petrolpark.destroy.content.oil.seismology.SeismometerSpikeS2CPacket;
import com.petrolpark.destroy.content.processing.glassblowing.SelectGlassblowingRecipeC2SPacket;
import com.petrolpark.destroy.content.processing.trypolithography.CircuitPatternsS2CPacket;
import com.petrolpark.destroy.content.processing.trypolithography.keypunch.ChangeKeypunchPositionC2SPacket;
import com.petrolpark.destroy.content.processing.trypolithography.keypunch.NameKeypunchC2SPacket;
import com.petrolpark.destroy.content.processing.trypolithography.keypunch.RequestKeypunchNameS2CPacket;
import com.petrolpark.destroy.content.product.periodictable.RefreshPeriodicTablePonderSceneS2CPacket;
import com.petrolpark.destroy.content.redstone.programmer.RedstoneProgramSyncC2SPacket;
import com.petrolpark.destroy.content.redstone.programmer.RedstoneProgramSyncReplyS2CPacket;
import com.petrolpark.destroy.content.redstone.programmer.RedstoneProgrammerPowerChangedS2CPacket;
import com.petrolpark.destroy.content.tool.swissarmyknife.SwissArmyKnifeToolC2SPacket;
import com.petrolpark.destroy.core.chemistry.hazard.ChemicalPoisonS2CPacket;
import com.petrolpark.destroy.core.chemistry.hazard.mobeffect.CryingS2CPacket;
import com.petrolpark.destroy.core.chemistry.storage.measuringcylinder.TransferFluidC2SPacket;
import com.petrolpark.destroy.core.chemistry.vat.material.SyncVatMaterialsS2CPacket;
import com.petrolpark.destroy.core.chemistry.vat.observation.RedstoneQuantityMonitorThresholdChangeC2SPacket;
import com.petrolpark.destroy.core.chemistry.vat.observation.colorimeter.ConfigureColorimeterC2SPacket;
import com.petrolpark.destroy.core.explosion.SmartExplosionS2CPacket;
import com.petrolpark.destroy.core.extendedinventory.ExtraInventorySizeChangeS2CPacket;
import com.petrolpark.destroy.core.extendedinventory.RequestInventoryFullStateC2SPacket;
import com.petrolpark.destroy.core.fluid.gasparticle.EvaporatingFluidS2CPacket;
import com.petrolpark.destroy.core.pollution.LevelPollutionS2CPacket;
import com.petrolpark.destroy.core.pollution.SyncChunkPollutionS2CPacket;
import com.petrolpark.network.packet.C2SPacket;
import com.petrolpark.network.packet.S2CPacket;

import net.minecraft.core.BlockSource;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.TargetPoint;
import net.minecraftforge.network.simple.SimpleChannel;

public class DestroyMessages {

    private static SimpleChannel INSTANCE;
    private static int packetID = 0;

    private static int id() {
        return packetID++;
    };

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
            .named(Destroy.asResource("messages"))
            .networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();
    
        INSTANCE = net;

        addS2CPacket(net, CryingS2CPacket.class, CryingS2CPacket::new);
        addS2CPacket(net, LevelPollutionS2CPacket.class, LevelPollutionS2CPacket::new);
        addS2CPacket(net, EvaporatingFluidS2CPacket.class, EvaporatingFluidS2CPacket::new);
        addS2CPacket(net, SeismometerSpikeS2CPacket.class, SeismometerSpikeS2CPacket::new);
        addS2CPacket(net, ChemicalPoisonS2CPacket.class, ChemicalPoisonS2CPacket::new);
        addS2CPacket(net, RefreshPeriodicTablePonderSceneS2CPacket.class, RefreshPeriodicTablePonderSceneS2CPacket::new);
        addS2CPacket(net, CircuitPatternsS2CPacket.class, CircuitPatternsS2CPacket::read);
        addS2CPacket(net, RequestKeypunchNameS2CPacket.class, RequestKeypunchNameS2CPacket::new);
        addS2CPacket(net, RedstoneProgramSyncReplyS2CPacket.class, RedstoneProgramSyncReplyS2CPacket::new);
        addS2CPacket(net, SyncVatMaterialsS2CPacket.class, SyncVatMaterialsS2CPacket::new);
        addS2CPacket(net, RedstoneProgrammerPowerChangedS2CPacket.class, RedstoneProgrammerPowerChangedS2CPacket::new);
        addS2CPacket(net, SyncChunkPollutionS2CPacket.class, SyncChunkPollutionS2CPacket::new);
        addS2CPacket(net, ExtraInventorySizeChangeS2CPacket.class, ExtraInventorySizeChangeS2CPacket::new);
        addS2CPacket(net, SmartExplosionS2CPacket.class, SmartExplosionS2CPacket::read);
        addS2CPacket(net, ConfettiBurstS2CPacket.class, ConfettiBurstS2CPacket::new);

        addC2SPacket(net, SwissArmyKnifeToolC2SPacket.class, SwissArmyKnifeToolC2SPacket::new);
        addC2SPacket(net, RedstoneProgramSyncC2SPacket.class, RedstoneProgramSyncC2SPacket::new);
        addC2SPacket(net, NameKeypunchC2SPacket.class, NameKeypunchC2SPacket::new);
        addC2SPacket(net, ChangeKeypunchPositionC2SPacket.class, ChangeKeypunchPositionC2SPacket::new);
        addC2SPacket(net, RedstoneQuantityMonitorThresholdChangeC2SPacket.class, RedstoneQuantityMonitorThresholdChangeC2SPacket::new);
        addC2SPacket(net, MarkSeismographC2SPacket.class, MarkSeismographC2SPacket::new);
        addC2SPacket(net, TransferFluidC2SPacket.class, TransferFluidC2SPacket::new);
        addC2SPacket(net, ConfigureColorimeterC2SPacket.class, ConfigureColorimeterC2SPacket::new);
        addC2SPacket(net, SelectGlassblowingRecipeC2SPacket.class, SelectGlassblowingRecipeC2SPacket::new);
        addC2SPacket(net, RequestInventoryFullStateC2SPacket.class, b -> new RequestInventoryFullStateC2SPacket());
    };

    public static <T extends S2CPacket> void addS2CPacket(SimpleChannel net, Class<T> clazz, Function<FriendlyByteBuf, T> decoder) {
        net.messageBuilder(clazz, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(decoder)
            .encoder(T::toBytes)
            .consumerMainThread(T::handle)
            .add();
    };

    public static <T extends C2SPacket> void addC2SPacket(SimpleChannel net, Class<T> clazz, Function<FriendlyByteBuf, T> decoder) {
        net.messageBuilder(clazz, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(decoder)
            .encoder(T::toBytes)
            .consumerMainThread(T::handle)
            .add();
    };

    public static void sendToServer(C2SPacket message) {
        INSTANCE.sendToServer(message);
    };

    public static void sendToClient(S2CPacket message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    };

    public static void sendToAllClientsInDimension(S2CPacket message, ServerLevel level) {
        INSTANCE.send(PacketDistributor.DIMENSION.with(level::dimension), message);
    };

    public static void sendToAllClients(S2CPacket message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    };

    public static void sendToAllClientsNear(S2CPacket message, BlockSource location) {
        INSTANCE.send(PacketDistributor.NEAR.with(TargetPoint.p(location.x(), location.y(), location.z(), 32d, location.getLevel().dimension())), message);
    };

    public static void sendToClientsTrackingEntity(S2CPacket message, Entity trackedEntity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> trackedEntity), message);
    };

    public static void sendToClientsTrackingChunk(S2CPacket message, LevelChunk chunk) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), message);
    };
}
