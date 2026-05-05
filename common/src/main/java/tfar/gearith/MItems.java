package tfar.gearith;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.Weapon;
import tfar.gearith.item.ThunderClubItem;

public class MItems {

    public static final ThunderClubItem THUNDER_CLUB = new ThunderClubItem(new Item.Properties().setId(key("thunder_club"))
            .rarity(Rarity.EPIC)
            .durability(5000)
            .component(DataComponents.TOOL, MaceItem.createToolProperties())
            .repairable(Items.BREEZE_ROD)
            .attributes(MaceItem.createAttributes())
            .enchantable(15)
            .component(DataComponents.WEAPON, new Weapon(1))
    );

    static {
        Registry.register(BuiltInRegistries.ITEM,Gearith.id("thunder_club"),THUNDER_CLUB);
    }

    static ResourceKey<Item> key(String s) {
        return ResourceKey.create(Registries.ITEM,Gearith.id(s));
    }

    public static void init() {

    }
}
