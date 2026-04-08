package tfar.gearith.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import tfar.gearith.Constants;

import java.util.Objects;
import java.util.function.Function;

public class CommonDataAttachment<T> {

    protected final Function<Object,T> defaultValueSupplier;
    protected final ResourceLocation name;
    protected final boolean copyOnDeath;
    protected final MapCodec<T> codec;
    private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
    protected Object attachment;

    public CommonDataAttachment(Function<Object,T> defaultValueSupplier, ResourceLocation name, boolean copyOnDeath, MapCodec<T> codec,
                                StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        this.defaultValueSupplier = defaultValueSupplier;
        this.name = name;
        this.copyOnDeath = copyOnDeath;
        this.codec = codec;
        this.streamCodec = streamCodec;
    }

    public static <T> Builder<T> create(Function<Object,T> defaultValueSupplier) {
        return new Builder<>(defaultValueSupplier);
    }

    public static <T> Builder<T> create() {
        return new Builder<>(o -> null);
    }

    public Function<Object,T> getDefaultValueSupplier() {
        return defaultValueSupplier;
    }        
    public ResourceLocation getName() {
        return name;
    }

    public MapCodec<T> getCodec() {
        return codec;
    }

    public StreamCodec<? super RegistryFriendlyByteBuf, T> getStreamCodec() {
        return streamCodec;
    }

    public boolean isCopyOnDeath() {
        return copyOnDeath;
    }

    public boolean canSync() {
        return streamCodec!=null;
    }

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        Objects.requireNonNull(attachment);
        this.attachment = attachment;
    }


    public static class Builder<T> {
        protected final Function<Object,T> defaultValueSupplier;
        protected boolean copyOnDeath;
        protected MapCodec<T> codec;
        @Nullable
        private StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;

        public Builder(Function<Object, T> defaultValueSupplier) {
            this.defaultValueSupplier = defaultValueSupplier;
        }

        public Builder<T> codec(MapCodec<T> codec) {
            Objects.requireNonNull(codec);
            this.codec = codec;
            return this;
        }

        public Builder<T> networkSynchronized(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
            Objects.requireNonNull(streamCodec);
            this.streamCodec = streamCodec;
            return this;
        }

        public Builder<T> copyOnDeath() {
            copyOnDeath = true;
            return this;
        }

        public CommonDataAttachment<T> build(String name) {
            return build(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,name));
        }

        public CommonDataAttachment<T> build(ResourceLocation name) {
            Objects.requireNonNull(name);
            return new CommonDataAttachment<>(defaultValueSupplier,name,copyOnDeath, codec,streamCodec);
        }
    }
}
