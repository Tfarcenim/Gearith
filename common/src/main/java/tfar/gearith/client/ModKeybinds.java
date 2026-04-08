package tfar.gearith.client;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import tfar.gearith.Gearith;

public class ModKeybinds {
    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Gearith.id("keybinds"));
    public static final KeyMapping DASH = new KeyMapping("Dash", GLFW.GLFW_KEY_LEFT_CONTROL,CATEGORY);
}
