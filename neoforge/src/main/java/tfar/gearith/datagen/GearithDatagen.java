package tfar.gearith.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tfar.gearith.ModEnchantments;
import tfar.gearith.datagen.tags.ModEnchantmentTagsProvider;
import tfar.gearith.datagen.tags.ModItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class GearithDatagen {

    public static void gather(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput=generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        //event.addProvider(new ModModelProvider(packOutput));
        DatapackBuiltinEntriesProvider provider = new ModDatapackProvider(packOutput,lookupProvider,new RegistrySetBuilder().add(Registries.ENCHANTMENT, ModEnchantments::bootstrap));
        event.addProvider(provider);
        lookupProvider = provider.getRegistryProvider();
        event.addProvider(new ModEnchantmentTagsProvider(packOutput,lookupProvider));
        event.addProvider(new ModItemTagsProvider(packOutput,lookupProvider));
    }

}
