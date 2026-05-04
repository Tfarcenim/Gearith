package tfar.gearith.client.renderer.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.phys.Vec3;

public class ThunderClubEntityRenderState extends EntityRenderState {
    public final ItemStackRenderState item = new ItemStackRenderState();
    public Vec3 lineOriginOffset = Vec3.ZERO;
}
