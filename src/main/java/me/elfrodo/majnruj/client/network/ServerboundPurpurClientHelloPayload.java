package me.elfrodo.majnruj.client.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import me.elfrodo.majnruj.client.util.Constants;

public record ServerboundPurpurClientHelloPayload(int protocol) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, ServerboundPurpurClientHelloPayload> STREAM_CODEC = CustomPacketPayload.codec(ServerboundPurpurClientHelloPayload::write, ServerboundPurpurClientHelloPayload::new);
    public static final Type<ServerboundPurpurClientHelloPayload> TYPE = new Type<>(Constants.PURPUR);

    public ServerboundPurpurClientHelloPayload(){
        this(Constants.PROTOCOL);
    }

    public ServerboundPurpurClientHelloPayload(FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readInt());
    }

    private void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(Constants.PROTOCOL);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
