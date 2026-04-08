package tfar.gearith.network.server;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import tfar.gearith.Gearith;
import tfar.gearith.MiscCodecs;
import tfar.gearith.ModEnchantments;
import tfar.gearith.attachments.CommonDataAttachments;
import tfar.gearith.network.PacketHandler;
import tfar.gearith.platform.Services;

import java.util.Arrays;

public enum C2SPlayerActionPacket implements C2SModPacket {
DASH
    ;
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SPlayerActionPacket> STREAM_CODEC =
            MiscCodecs.altEnumStreamCodec(C2SPlayerActionPacket.class);


    public static final CustomPacketPayload.Type<C2SPlayerActionPacket> TYPE = new CustomPacketPayload.Type<>(
            PacketHandler.packet(C2SPlayerActionPacket.class));


    public void send() {
        Services.PLATFORM.sendToServer(this);
    }

    public void handleServer(ServerPlayer player) {
        switch (this) {
            case DASH -> {
                if (Gearith.hasEnchant(player.getMainHandItem(), ModEnchantments.THUNDER_BAGUA,player.registryAccess()) && checkCooldowns(player)) {
                    player.addDeltaMovement(player.getLookAngle());
                    player.hurtMarked = true;
                    applyCooldown(player);
                }
            }
        }
    }

    void applyCooldown(ServerPlayer player) {
        int[] cooldowns = CommonDataAttachments.getDashCooldowns(player);
        if (cooldowns == null)return;
        for (int i=0;i < cooldowns.length;i++) {
            int cooldown = cooldowns[i];
            if (cooldown <=0) {
                cooldowns[i] = 60;
                return;
            }
        }
    }

    boolean checkCooldowns(ServerPlayer player) {
        int[] cooldowns = CommonDataAttachments.getDashCooldowns(player);
        if (cooldowns == null) return true;
        return Arrays.stream(cooldowns).anyMatch(i -> i <= 0);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
