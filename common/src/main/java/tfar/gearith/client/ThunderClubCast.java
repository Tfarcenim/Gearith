package tfar.gearith.client;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import tfar.gearith.PlayerDuck;
import tfar.gearith.client.renderer.ThunderClubEntityRenderer;

public enum ThunderClubCast implements ConditionalItemModelProperty {
    INSTANCE;
    public static final MapCodec<ThunderClubCast> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<? extends ConditionalItemModelProperty> type() {
        return MAP_CODEC;
    }

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
        if (livingEntity instanceof PlayerDuck player && player.getThunderClubEntity() != null) {
            HumanoidArm humanoidarm = ThunderClubEntityRenderer.getHoldingArm((Player) player);
            return livingEntity.getItemHeldByArm(humanoidarm) == itemStack;
        } else {
            return false;
        }
    }
}
