package tfar.gearith.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import tfar.gearith.Constants;
import tfar.gearith.MItems;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, Constants.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(MItems.THUNDER_CLUB, "Thunder Club");
        add("enchantment.gearith.thunder_bagua","Thunder Bagua");
        add("enchantment.gearith.pulling_spike","Pulling Spike");
        add("enchantment.gearith.tetherlash","Tetherlash");
    }
}
