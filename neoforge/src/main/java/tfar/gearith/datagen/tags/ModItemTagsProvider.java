package tfar.gearith.datagen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import tfar.gearith.Constants;
import tfar.gearith.MItems;
import tfar.gearith.ModItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Constants.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModItemTags.PULLING_SPIKE_ENCHANTABLE).add(MItems.THUNDER_CLUB);
        tag(ModItemTags.TETHERLASH_ENCHANTABLE).add(MItems.THUNDER_CLUB);
        tag(ModItemTags.THUNDER_BAGUA_ENCHANTABLE).add(MItems.THUNDER_CLUB);
    }
}
