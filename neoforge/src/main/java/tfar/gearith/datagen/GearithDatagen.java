package tfar.gearith.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class GearithDatagen {

    public static void gather(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput=generator.getPackOutput();
        event.addProvider(new ModModelProvider(packOutput));
    }

}
