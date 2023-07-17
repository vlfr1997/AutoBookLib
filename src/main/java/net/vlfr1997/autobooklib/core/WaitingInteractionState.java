package net.vlfr1997.autobooklib.core;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import net.vlfr1997.autobooklib.data.AutoBookData;

public class WaitingInteractionState extends AutoBookState {

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

}
