package net.vlfr1997.autobooklib.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.village.TradeOffer;
import net.vlfr1997.autobooklib.data.AutoBookData;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ClientPlayNetworkHandler.class)
abstract class ReceiveTradeOfferPacket {
    @Inject(at = @At("HEAD"), method = "onSetTradeOffers", cancellable = true)
    public void onSetTradeOffers(SetTradeOffersS2CPacket packet, CallbackInfo ci) {
        if (AutoBookData.getInfo().getWorking()) {
            AutoBookData.getInfo().setOffers(packet.getOffers());
            int ebookcount = 0;
            int level = 0;
            int price = 1;
            Identifier id = new Identifier("minecraft:iron_ingot");
            for (TradeOffer offer : packet.getOffers()) {
                if (offer.getSellItem().getItem() instanceof EnchantedBookItem) {
                    price = offer.getOriginalFirstBuyItem().getCount();
                    ebookcount++;
                    ItemStack itemStack = offer.getSellItem();
                    NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack);
                    for (int i = 0; i < nbtList.size(); ++i) {
                        NbtCompound nbtCompound = nbtList.getCompound(i);
                        level = EnchantmentHelper.getLevelFromNbt(nbtCompound);
                        id = EnchantmentHelper.getIdFromNbt(nbtCompound);               
                    }
                }
            }
            if (AutoBookData.getInfo().getEnchantedData().containsKey(id) && ebookcount > 0 && level == AutoBookData.getInfo().getEnchantedData().get(id).getLevel() && AutoBookData.getInfo().getEnchantedData().get(id).getPrice() >= price) {
                AutoBookData.getInfo().setWorking(false); 
                AutoBookData.getInfo().setDone(false);
                MinecraftClient instance = MinecraftClient.getInstance();
                instance.player.sendMessage(Text.literal("Found: " + id.toString() + Integer.toString(level) + " for " + price + " emeralds"), false);
                instance.player.sendMessage(Text.literal("Done"), false); 
            } else {
                Direction side = Direction.NORTH;
                BlockPos blockPos = AutoBookData.getInfo().getLecternBlock().getBlockPos();            
                ClientPlayNetworking.getSender().sendPacket(new UpdateSelectedSlotC2SPacket(0));
                ClientPlayNetworking.getSender().sendPacket(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, blockPos, side));
                ClientPlayNetworking.getSender().sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, blockPos, side));
            }
        }
    }
    
    @Inject(at = @At("HEAD"), method = "onOpenScreen", cancellable = true)
    public void onOpenScreen(OpenScreenS2CPacket packet, CallbackInfo ci) {
        var type = packet.getScreenHandlerType();
        if (AutoBookData.getInfo().getWorking() && type == ScreenHandlerType.MERCHANT) {
            ClientPlayNetworking.getSender()
                    .sendPacket(new CloseHandledScreenC2SPacket(packet.getSyncId()));
            ci.cancel();
        }
    }
    @Inject(at = @At("HEAD"), method = "onItemPickupAnimation", cancellable = true)
    public void onItemPickupAnimation(ItemPickupAnimationS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity pickedItem = client.world.getEntityById(packet.getEntityId());
        if (AutoBookData.getInfo().getWorking() && pickedItem!=null && (AutoBookData.getInfo().getLecternBlock().getBlockPos().compareTo(pickedItem.getBlockPos()) <= 1)) {//1 = range
            ClientPlayNetworking.getSender().sendPacket(new UpdateSelectedSlotC2SPacket(1));
            ClientPlayNetworking.getSender().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, AutoBookData.getInfo().getLecternBlock(), 0));
            AutoBookData.getInfo().setDone(false);
        }
    }
}