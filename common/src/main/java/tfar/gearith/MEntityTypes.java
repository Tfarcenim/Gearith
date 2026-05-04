package tfar.gearith;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class MEntityTypes {
    public static final EntityType<ThunderClubEntity> THUNDER_CLUB = EntityType.Builder.<ThunderClubEntity>of(ThunderClubEntity::new, MobCategory.MISC)
            .noLootTable()
            .noSave()
            .noSummon()
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(5)
            .build(key("thunder_club"));

    static {
        Registry.register(BuiltInRegistries.ENTITY_TYPE,Gearith.id("thunder_club"),THUNDER_CLUB);
    }

    public static void init(){

    }

    static ResourceKey<EntityType<?>> key(String s) {
        return ResourceKey.create(Registries.ENTITY_TYPE,Gearith.id(s));
    }

}
