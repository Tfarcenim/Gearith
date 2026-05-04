package tfar.gearith.platform;

import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;
import tfar.gearith.attachments.CommonDataAttachment;
import tfar.gearith.client.GearithNeoforgeClient;
import tfar.gearith.network.server.C2SModPacket;
import tfar.gearith.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

import java.util.function.Function;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static PayloadRegistrar registrar;

    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec) {
        registrar.playToServer(type, streamCodec, (p, t) -> p.handleServer((ServerPlayer) t.player()));
    }

    @Override
    public void sendToServer(C2SModPacket msg) {
        GearithNeoforgeClient.sendToServer(msg);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.getCurrent().isProduction();
    }

    @Override
    public <T> void registerDataAttachment(CommonDataAttachment<T> attachment) {
        AttachmentType.Builder<T> builder = AttachmentType.builder((Function<IAttachmentHolder, T>) (Object) attachment.getDefaultValueSupplier());
        if (attachment.getCodec() != null) {
            builder.serialize(attachment.getCodec());
        }
        if (attachment.isCopyOnDeath()) {
            builder.copyOnDeath();
        }
        if (attachment.canSync()) {
            builder.sync(attachment.getStreamCodec());
        }
        AttachmentType<T> type = builder.build();
        Registry.register(NeoForgeRegistries.ATTACHMENT_TYPES, attachment.getName(), type);
        attachment.setAttachment(type);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    @Nullable
    public <T> T getAttachedValue(Object object, CommonDataAttachment<T> attachment) {
        AttachmentType<T> type = (AttachmentType<T>) attachment.getAttachment();
        if (object instanceof IAttachmentHolder attachmentHolder) {
            return attachmentHolder.getData(type);
        } else {
            throw new IllegalStateException("Cannot attach data to " + object);
        }
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <T> void setAttachedValue(Object object, CommonDataAttachment<T> attachment, @Nullable T value) {
        AttachmentType<T> type = (AttachmentType<T>) attachment.getAttachment();
        if (object instanceof IAttachmentHolder attachmentHolder) {
            if (value == null) {
                attachmentHolder.removeData(type);
            } else {
                attachmentHolder.setData(type, value);
            }
        } else {
            throw new IllegalStateException("Cannot attach data to " + object);
        }
    }

    @Override
    public void postOnPlayerDestroyedItem(Player player, ItemStack itemStack, InteractionHand hand) {
        EventHooks.onPlayerDestroyItem(player, itemStack, hand);
    }

    @Override
    public boolean postOnProjectileImpact(Projectile projectile, HitResult hitResult) {
        return EventHooks.onProjectileImpact(projectile, hitResult);
    }
}