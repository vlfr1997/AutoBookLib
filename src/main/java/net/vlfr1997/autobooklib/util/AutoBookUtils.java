package net.vlfr1997.autobooklib.util;

import java.util.List;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PickFromInventoryC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoBookUtils {

    public AutoBookUtils() {

    }

    static public void setSelectedSlot(int target) {
        ClientPlayNetworking.getSender().sendPacket(new UpdateSelectedSlotC2SPacket(target));
    }

    static public void placeBlock(BlockHitResult block) {
        ClientPlayNetworking.getSender().sendPacket(
                new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, block, 0));
    }

    static public void mineBlock(BlockPos blockPos) {
        Direction side = Direction.NORTH;
        ClientPlayNetworking.getSender()
                .sendPacket(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, blockPos, side));
        ClientPlayNetworking.getSender()
                .sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, blockPos, side));
    }

    static public int getItemPosition(List<Item> itemList) {

        MinecraftClient client = MinecraftClient.getInstance();
        List<ItemStack> inventory = client.player.getInventory().main;

        for (Item item : itemList) {
            final String translationKey = item.getTranslationKey();
            int position = 0;
            for (ItemStack itemStack : inventory) {
                if (itemStack.getTranslationKey() == translationKey) {
                    return position;
                }
                position++;
            }
        }

        return -1;
    }

    static public boolean isInHotbar(int position) {
        return position < 9;
    }

    static public void placeInHotbar(int from, int target) {
        setSelectedSlot(target);
        ClientPlayNetworking.getSender().sendPacket(new PickFromInventoryC2SPacket(from));
    }
}
