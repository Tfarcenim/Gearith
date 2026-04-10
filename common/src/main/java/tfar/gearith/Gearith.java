package tfar.gearith;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import tfar.gearith.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class Gearith {

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {

        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.

    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,path);
    }

    public static boolean hasEnchant(ItemStack stack, ResourceKey<Enchantment> enchantment, HolderLookup.Provider provider) {
        HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
        return stack.getEnchantments().getLevel(enchantmentRegistryLookup.getOrThrow(enchantment)) > 0;
    }

    public static int getEnchantLevel(ItemStack stack, ResourceKey<Enchantment> enchantment, HolderLookup.Provider provider) {
        HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
        return EnchantmentHelper.getItemEnchantmentLevel(enchantmentRegistryLookup.getOrThrow(enchantment),stack);
    }

    public static boolean updateCooldowns(int[] cooldowns) {
        if (cooldowns != null) {
            boolean modified = false;
            for (int i = 0;i<cooldowns.length;i++) {
                int cooldown = cooldowns[i];
                if (cooldown > 0) {
                    cooldowns[i] = cooldown - 1;
                    modified = true;
                }
            }
            return modified;
        } else {
            return false;
        }
    }
}