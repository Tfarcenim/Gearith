package tfar.gearith;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegisterEvent;
import tfar.gearith.attachments.CommonDataAttachments;
import tfar.gearith.datagen.GearithDatagen;
import tfar.gearith.network.PacketHandler;
import tfar.gearith.platform.NeoForgePlatformHelper;
import tfar.gearith.platform.Services;

import java.util.Arrays;

@Mod(Constants.MOD_ID)
public class GearithNeoforge {

    public GearithNeoforge(IEventBus eventBus) {
        eventBus.addListener(this::register);
        eventBus.addListener(GearithDatagen::gather);
        eventBus.addListener(this::network);
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        Gearith.init();
        NeoForge.EVENT_BUS.addListener(this::playerTick);
    }

    void playerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            int[] cooldowns = CommonDataAttachments.getDashCooldowns(player);
            boolean b = Gearith.updateCooldowns(cooldowns);
            if (b) {
                CommonDataAttachments.setDashCooldowns(player, cooldowns);
                if (Services.PLATFORM.isDevelopmentEnvironment()) {
                    player.displayClientMessage(Component.literal(Arrays.toString(cooldowns) + ""), true);
                }
            }
        }
    }

    void network(RegisterPayloadHandlersEvent event) {
        NeoForgePlatformHelper.registrar = event.registrar("1");
        PacketHandler.registerPackets();
    }

    void register(RegisterEvent event) {
        if (event.getRegistry() == BuiltInRegistries.BLOCK) {
            MItems.init();
            MEntityTypes.init();
            CommonDataAttachments.init();
        }
    }
}