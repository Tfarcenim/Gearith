package tfar.gearith;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class MiscCodecs {
    public static <E extends Enum<E>> StreamCodec<RegistryFriendlyByteBuf,E> altEnumStreamCodec(Class<E> eClass) {
        return StreamCodec.of(FriendlyByteBuf::writeEnum, buffer -> buffer.readEnum(eClass));
    }

    public static <E extends Enum<E>> StreamCodec<FriendlyByteBuf,E> enumStreamCodec(Class<E> eClass) {
        return StreamCodec.of(FriendlyByteBuf::writeEnum, buffer -> buffer.readEnum(eClass));
    }

    public static <E extends Enum<E>> Codec<E> enumCodec(Class<E> eClass) {
        return Codec.STRING.xmap(string -> Enum.valueOf(eClass,string), Enum::name);
    }

    public static StreamCodec<RegistryFriendlyByteBuf,int[]> intArrayCodec() {
        return StreamCodec.of(FriendlyByteBuf::writeVarIntArray, FriendlyByteBuf::readVarIntArray);
    }
}
