package tfar.gearith.platform;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import tfar.gearith.attachments.CommonDataAttachment;
import tfar.gearith.network.server.C2SModPacket;
import tfar.gearith.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec) {

    }

    @Override
    public void sendToServer(C2SModPacket msg) {

    }

    @Override
    public <T> void registerDataAttachment(CommonDataAttachment<T> attachment) {

    }

    @Override
    public @Nullable <T> T getAttachedValue(Object object, CommonDataAttachment<T> attachment) {
        return null;
    }

    @Override
    public <T> void setAttachedValue(Object object, CommonDataAttachment<T> attachment, @Nullable T value) {

    }

    @Override
    public void postOnPlayerDestroyedItem(Player player, ItemStack itemStack, InteractionHand hand) {

    }

    @Override
    public boolean postOnProjectileImpact(Projectile projectile, HitResult hitResult) {
        return false;
    }
}
