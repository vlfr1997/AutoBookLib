package net.vlfr1997.autobooklib.core;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.vlfr1997.autobooklib.data.AutoBookData;

public class WaitingInteractionState extends AutoBookState {

    private static final int TIMEOUT_TICKS = 20 * 5; // 5 seconds
    private int ticksRemaining = TIMEOUT_TICKS;

    public WaitingInteractionState(AutoBookData data) {
        super(data);
    }

    @Override
    public AutoBookState onPacket(Packet<ClientPlayPacketListener> packet, CallbackInfo ci) {
        if (packet instanceof OpenScreenS2CPacket) {
            OpenScreenS2CPacket openScreenS2CPacket = (OpenScreenS2CPacket) packet;
            var type = openScreenS2CPacket.getScreenHandlerType();
            if (type == ScreenHandlerType.MERCHANT) {
                ClientPlayNetworking.getSender()
                        .sendPacket(new CloseHandledScreenC2SPacket(openScreenS2CPacket.getSyncId()));
                ci.cancel();
                return new WaitingOfferState(data);
            }
        }
        return super.onPacket(packet, ci);
    }

    @Override
    public AutoBookState onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            return this;
        }

        ticksRemaining--;

        if (ticksRemaining <= 0) {
            // timeout: back to previous state to retry interaction
            client.player.sendMessage(
                    Text.translatable("info.autobooklib.timeout"), false);
            return new WaitingLibrarianState(data);
        }

        return this;
    }

}
