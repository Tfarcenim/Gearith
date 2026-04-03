package tfar.gearith;


import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import tfar.gearith.datagen.GearithDatagen;

@Mod(Constants.MOD_ID)
public class GearithNeoforge {

    public GearithNeoforge(IEventBus eventBus) {
        eventBus.addListener(this::register);
        eventBus.addListener(GearithDatagen::gather);
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        Gearith.init();

    }

    void register(RegisterEvent event) {
        if (event.getRegistry() == BuiltInRegistries.BLOCK) {
            MItems.init();
        }
    }
}