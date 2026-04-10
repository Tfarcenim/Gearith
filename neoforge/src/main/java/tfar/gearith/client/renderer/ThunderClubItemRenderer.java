package tfar.gearith.client.renderer;



import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import tfar.gearith.Gearith;
import tfar.gearith.item.ThunderClubItem;

public class ThunderClubItemRenderer extends GeoItemRenderer<ThunderClubItem> {
    public ThunderClubItemRenderer() {
        super(new DefaultedItemGeoModel<>(Gearith.id("thunder_club")));
    }
}
