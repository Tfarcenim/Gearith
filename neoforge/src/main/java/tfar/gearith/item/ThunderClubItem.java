package tfar.gearith.item;

import com.google.common.base.Suppliers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import tfar.gearith.client.ThunderClubRenderer;

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
            private final Supplier<ThunderClubRenderer> renderer = Suppliers.memoize(ThunderClubRenderer::new);

            @Override
            @Nullable
            public GeoItemRenderer<ThunderClubItem> getGeoItemRenderer() {
                return this.renderer.get();
            }
        });
    }
}
