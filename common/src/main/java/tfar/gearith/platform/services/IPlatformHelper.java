package tfar.gearith.platform.services;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import tfar.gearith.attachments.CommonDataAttachment;
import tfar.gearith.network.server.C2SModPacket;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    <MSG extends C2SModPacket> void registerServerPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf,MSG> streamCodec);
    void sendToServer(C2SModPacket msg);


    <T> void registerDataAttachment(CommonDataAttachment<T> attachment);

    @Nullable
    <T> T getAttachedValue(Object object, CommonDataAttachment<T> attachment);

    default <T> T getOrCreateAttachedValue(Entity entity, CommonDataAttachment<T> attachment) {
        T value = getAttachedValue(entity, attachment);
        if (value != null) {
            return value;
        }
        value = attachment.getDefaultValueSupplier().apply(entity);
        setAttachedValue(entity, attachment, value);
        return value;
    }

    <T> void setAttachedValue(Object object, CommonDataAttachment<T> attachment, @Nullable T value);

    void postOnPlayerDestroyedItem(Player player, ItemStack itemStack, InteractionHand hand);

    boolean postOnProjectileImpact(Projectile projectile, HitResult hitResult);
}