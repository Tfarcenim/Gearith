package tfar.gearith.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import tfar.gearith.PlayerDuck;
import tfar.gearith.ThunderClubEntity;

@Mixin(Player.class)
public class PlayerMixin implements PlayerDuck {

    @Unique
    ThunderClubEntity thunderClubEntity;

    @Override
    public void setThunderClubEntity(ThunderClubEntity entity) {
        thunderClubEntity = entity;
    }

    @Override
    public ThunderClubEntity getThunderClubEntity() {
        return thunderClubEntity;
    }
}
