package net.vlfr1997.autobooklib.core;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.text.Text;
import net.vlfr1997.autobooklib.data.AutoBookData;

public class WaitingLecternState extends AutoBookState {

    public WaitingLecternState(AutoBookData data) {
        super(data);
    }

    @Override
    public AutoBookState onPacket(Packet<ClientPlayPacketListener> packet, CallbackInfo ci) {
        if (packet instanceof ItemPickupAnimationS2CPacket) {
            ItemPickupAnimationS2CPacket itemPickupAnimationS2CPacket = (ItemPickupAnimationS2CPacket) packet;
            MinecraftClient client = MinecraftClient.getInstance();
            Entity pickedItem = client.world.getEntityById(itemPickupAnimationS2CPacket.getEntityId());

            if(pickedItem == null) {
                client.player.sendMessage(
                    Text.translatable("error.autobooklib.pickedIsNull"),
                    false
                );
            }

            if (data.getLecternBlock() == null || client.world == null) {
                client.player.sendMessage(
                    Text.translatable("error.autobooklib.lecternIsNull"),
                    false
                );
            }

            if (data.getLecternBlock().getBlockPos().isWithinDistance(pickedItem.getBlockPos(), 2)) {// 1 = detection range
                return new WaitingInventoryUpdateState(data);
            }

            return super.onPacket(packet, ci);
        }
        return super.onPacket(packet, ci);
    }

}
