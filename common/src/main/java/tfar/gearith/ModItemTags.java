package tfar.gearith;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface ModItemTags {

    TagKey<Item> THUNDER_BAGUA_ENCHANTABLE = bind("thunder_club_enchantable");
    TagKey<Item> PULLING_SPIKE_ENCHANTABLE = bind("pulling_spike_enchantable");
    TagKey<Item> TETHERLASH_ENCHANTABLE = bind("tetherlash_enchantable");

    private static TagKey<Item> bind(String name) {
        return TagKey.create(Registries.ITEM, Gearith.id(name));
    }
}
