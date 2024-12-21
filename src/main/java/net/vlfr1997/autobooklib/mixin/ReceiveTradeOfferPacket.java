package net.vlfr1997.autobooklib.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import net.vlfr1997.autobooklib.core.AutoBookController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
abstract class ReceiveTradeOfferPacket {

    @Inject(at = @At("HEAD"), method = "onSetTradeOffers", cancellable = true)
    public void onSetTradeOffers(SetTradeOffersS2CPacket packet, CallbackInfo ci) {
        AutoBookController.getInstance().onPacket(packet, ci);
    }

    @Inject(at = @At("HEAD"), method = "onOpenScreen", cancellable = true)
    public void onOpenScreen(OpenScreenS2CPacket packet, CallbackInfo ci) {
        AutoBookController.getInstance().onPacket(packet, ci);
    }

    @Inject(at = @At("HEAD"), method = "onItemPickupAnimation", cancellable = true)
    public void onItemPickupAnimation(ItemPickupAnimationS2CPacket packet, CallbackInfo ci) {
        AutoBookController.getInstance().onPacket(packet, ci);
    }

}