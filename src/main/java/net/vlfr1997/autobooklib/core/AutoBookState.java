package net.vlfr1997.autobooklib.core;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import net.vlfr1997.autobooklib.data.AutoBookData;

public abstract class AutoBookState {
    final AutoBookData data;

    public AutoBookState(AutoBookData data) {
        this.data = data;
    }

    public AutoBookState onKey(MinecraftClient client) {
        client.player.sendMessage(
                Text.translatable("info.autobooklib.stopped"),
                false);
        return new InitialState(data);
    }

    public AutoBookState onTick(MinecraftClient client) {
        return this;
    }

    public AutoBookState onPacket(Packet<ClientPlayPacketListener> packet, CallbackInfo ci) {
        return this;
    };
}