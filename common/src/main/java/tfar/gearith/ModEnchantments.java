package tfar.gearith;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {

    public static final ResourceKey<Enchantment> THUNDER_BAGUA = key("thunder_bagua");
    public static final ResourceKey<Enchantment> PULLING_SPIKE = key("pulling_spike");
    public static final ResourceKey<Enchantment> TETHERLASH = key("tetherlash");

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Enchantment> holdergetter1 = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> holdergetter2 = context.lookup(Registries.ITEM);

        register(
                context,
                THUNDER_BAGUA,
                Enchantment.enchantment(
                                Enchantment.definition(
                                        holdergetter2.getOrThrow(ModItemTags.THUNDER_BAGUA_ENCHANTABLE),
                                        1,
                                        1,
                                        Enchantment.constantCost(15),
                                        Enchantment.constantCost(65),
                                        8,
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
//                        .withEffect(EnchantmentEffectComponents.BLOCK_EXPERIENCE, new SetValue(LevelBasedValue.constant(0.0F)))
        );

        register(
                context,
                PULLING_SPIKE,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ModItemTags.PULLING_SPIKE_ENCHANTABLE),
                                1,
                                1,
                                Enchantment.constantCost(15),
                                Enchantment.constantCost(65),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
//                        .withEffect(EnchantmentEffectComponents.BLOCK_EXPERIENCE, new SetValue(LevelBasedValue.constant(0.0F)))
        );

        register(
                context,
                TETHERLASH,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ModItemTags.TETHERLASH_ENCHANTABLE),
                                1,
                                1,
                                Enchantment.constantCost(15),
                                Enchantment.constantCost(65),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
//                        .withEffect(EnchantmentEffectComponents.BLOCK_EXPERIENCE, new SetValue(LevelBasedValue.constant(0.0F)))
        );
    }

        private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, Gearith.id(name));
    }
}
//BUT it'll also have 3 enchants.
// the first enchant is a triple dash,
// they can be used separately and they have their separate cooldowns (which is around 3-2 seconds),
// once the player uses his last dash, he will crash into the ground and deal area damage around itself

//the second enchant works the same as the first but it can reel in mobs with itself
//but the third (which i think will be the hardest of them all) is a swing
//by swing i mean the player swings the weapon in front of him, dealing great damage in a short range to multiple enemies,
//it's a fast swing too, basically the same speed as a regular sword sweep
//the player can also crouch while pressing it, the weapon will be swung by the chain, making it have less damage slower speed, but more range
//basically like tying a rope to something heavy and swinging it