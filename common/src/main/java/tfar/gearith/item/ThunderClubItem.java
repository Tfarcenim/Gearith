package tfar.gearith.item;

import com.google.common.base.Suppliers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import tfar.gearith.Gearith;
import tfar.gearith.ModEnchantments;
import tfar.gearith.PlayerDuck;
import tfar.gearith.ThunderClubEntity;
import tfar.gearith.client.renderer.ThunderClubItemRenderer;
import tfar.gearith.platform.Services;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ThunderClubItem extends Item implements GeoItem {

    final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ThunderClubItem(Properties properties) {
        super(properties);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (((PlayerDuck)player).getThunderClubEntity() != null) {
            if (!level.isClientSide()) {
                int i = ((PlayerDuck)player).getThunderClubEntity().retrieve(stack);
                ItemStack original = stack.copy();
                stack.hurtAndBreak(i, player, hand.asEquipmentSlot());
                if(stack.isEmpty()) {
                    Services.PLATFORM.postOnPlayerDestroyedItem(player, original, hand);
                }
            }

            level.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.FISHING_BOBBER_RETRIEVE,
                    SoundSource.NEUTRAL,
                    1.0F,
                    0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
            );
            player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            level.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.FISHING_BOBBER_THROW,
                    SoundSource.NEUTRAL,
                    0.5F,
                    0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
            );
            if (level instanceof ServerLevel serverlevel) {
                int k = Gearith.getEnchantLevel(stack, ModEnchantments.PULLING_SPIKE, player.registryAccess());
                Projectile.spawnProjectile(new ThunderClubEntity(player, level, k), serverlevel, stack);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
        }
        return super.use(level, player, hand);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            // Defer creation of our renderer then cache it so that it doesn't get instantiated too early
            private final Supplier<ThunderClubItemRenderer> renderer = Suppliers.memoize(ThunderClubItemRenderer::new);

            @Override
            @Nullable
            public GeoItemRenderer<ThunderClubItem> getGeoItemRenderer() {
                return this.renderer.get();
            }
        });
    }
}
