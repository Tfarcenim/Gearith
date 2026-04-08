package tfar.gearith.network;

import net.minecraft.resources.ResourceLocation;
import tfar.gearith.Gearith;
import tfar.gearith.network.server.C2SPlayerActionPacket;
import tfar.gearith.platform.Services;

import java.util.Locale;

public class PacketHandler {

    public static void registerPackets() {

        Services.PLATFORM.registerServerPacket(C2SPlayerActionPacket.TYPE, C2SPlayerActionPacket.STREAM_CODEC);


        ///////server to client

    }

    public static ResourceLocation packet(Class<?> clazz) {
        return Gearith.id(clazz.getName().toLowerCase(Locale.ROOT));
    }

}
