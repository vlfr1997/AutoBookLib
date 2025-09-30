package net.vlfr1997.autobooklib.util;

import java.util.List;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.sync.ItemStackHash;
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

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            List<ItemStack> inventory = player.getInventory().getMainStacks();

            for (Item item : itemList) {
                final String translationKey = item.getTranslationKey();
                int position = 0;
                for (ItemStack itemStack : inventory) {
                    if (itemStack.getItem().getTranslationKey().equals(translationKey)) {
                        return position;
                    }
                    position++;
                }
            }
        }

        return -1;
    }

    static public boolean isInHotbar(int position) {
        return position < 9;
    }

    static public void placeInHotbar(int from, int target) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player != null){
            ClientPlayNetworking.getSender()
                    .sendPacket(new ClickSlotC2SPacket(player.currentScreenHandler.syncId,
                            player.currentScreenHandler.getRevision(), (short) from, (byte) target, SlotActionType.SWAP,
                            new Int2ObjectOpenHashMap<>(),
                            ItemStackHash.fromItemStack(player.currentScreenHandler.getCursorStack(), null)));
            AutoBookUtils.setSelectedSlot(target);
        }
    }

}
