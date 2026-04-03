package tfar.gearith.client;



import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import tfar.gearith.Gearith;
import tfar.gearith.ThunderClubItem;

public class ThunderClubRenderer extends GeoItemRenderer<ThunderClubItem> {
    public ThunderClubRenderer() {
        super(new DefaultedItemGeoModel<>(Gearith.id("thunder_club")));
    }
}
