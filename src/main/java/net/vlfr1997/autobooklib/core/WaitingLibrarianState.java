package net.vlfr1997.autobooklib.core;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.village.VillagerProfession;
import net.vlfr1997.autobooklib.data.AutoBookData;

public class WaitingLibrarianState extends AutoBookState {

    public WaitingLibrarianState(AutoBookData data) {
        super(data);
    }

    @Override
    public AutoBookState onTick(MinecraftClient client) {
        VillagerEntity villager = (VillagerEntity) client.world.getEntityById(data.getVillager().getId());
        if (villager != null) {
            if (villager.getVillagerData().profession().matchesKey(VillagerProfession.LIBRARIAN)) {
                ClientPlayNetworking.getSender()
                        .sendPacket(PlayerInteractEntityC2SPacket.interact(data.getVillager(),
                                client.player.isSneaking(),
                                Hand.MAIN_HAND));
                return new WaitingInteractionState(data);
            }
        } else {
            data.setVillager(null);
            client.player.sendMessage(
                    Text.translatable("error.autobooklib.villager"), false);
            return new InitialState(data);
        }
        return super.onTick(client);
    }
}