package tfar.gearith.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.NeoForge;
import tfar.gearith.Constants;
import tfar.gearith.network.server.C2SModPacket;
import tfar.gearith.network.server.C2SPlayerActionPacket;

@Mod(value = Constants.MOD_ID,dist = Dist.CLIENT)
public class GearithNeoforgeClient {
    public GearithNeoforgeClient(IEventBus bus) {
        bus.addListener(this::keybinds);
        NeoForge.EVENT_BUS.addListener(this::keyPress);
    }

    void keybinds(RegisterKeyMappingsEvent event){
        event.register(ModKeybinds.DASH);
    }

    void keyPress(InputEvent.Key event) {
        if (ModKeybinds.DASH.matches(event.getKeyEvent()) && event.getAction() == InputConstants.PRESS) {
            C2SPlayerActionPacket.DASH.send();
        }
    }

    public static void sendToServer(C2SModPacket packet) {
        ClientPacketDistributor.sendToServer(packet);
    }
}
