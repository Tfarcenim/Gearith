package tfar.gearith.attachments;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import tfar.gearith.MiscCodecs;
import tfar.gearith.platform.Services;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommonDataAttachments {

    private static final Map<ResourceLocation,CommonDataAttachment<?>> MAP =new HashMap<>();

    public static final CommonDataAttachment<int[]> DASH_COOLDOWNS =
            register(CommonDataAttachment.create(o -> new int[3])
                    .networkSynchronized(MiscCodecs.intArrayCodec())
                    .build("dash_cooldowns"));

    public static CommonDataAttachment<?> lookup(ResourceLocation location) {
        return MAP.get(location);
    }

    static <T> CommonDataAttachment<T> register(CommonDataAttachment<T> type) {
        Services.PLATFORM.registerDataAttachment(type);
        Objects.requireNonNull(type.getAttachment());
        MAP.put(type.name,type);
        return type;
    }


    ////////////Helpers

    public static int[] getDashCooldowns(Entity entity) {
        return Services.PLATFORM.getAttachedValue(entity,DASH_COOLDOWNS);
    }

    public static void setDashCooldowns(Entity entity,int[] cooldowns) {
        Services.PLATFORM.setAttachedValue(entity,DASH_COOLDOWNS,cooldowns);
    }

    public static void init() {

    }
}