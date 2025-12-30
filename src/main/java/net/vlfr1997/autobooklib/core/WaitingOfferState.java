package net.vlfr1997.autobooklib.core;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.TradeOffer;
import net.vlfr1997.autobooklib.data.AutoBookData;
import net.vlfr1997.autobooklib.util.AutoBookUtils;

public class WaitingOfferState extends AutoBookState {

    public WaitingOfferState(AutoBookData data) {
        super(data);
    }

    @Override
    public AutoBookState onPacket(Packet<ClientPlayPacketListener> packet, CallbackInfo ci) {
        if (packet instanceof SetTradeOffersS2CPacket) {
            SetTradeOffersS2CPacket tradeOffersS2CPacket = (SetTradeOffersS2CPacket) packet;
            int ebookcount = 0;
            int level = 0;
            int price = 1;
            Enchantment enchantment = null;
            for (TradeOffer offer : tradeOffersS2CPacket.getOffers()) {
                if (EnchantmentHelper.hasEnchantments(offer.getSellItem())) {
                    price = offer.getOriginalFirstBuyItem().getCount();
                    ebookcount++;
                    ItemStack itemStack = offer.getSellItem();
                    Entry<RegistryEntry<Enchantment>> enchantmentEntry = EnchantmentHelper.getEnchantments(itemStack)
                            .getEnchantmentEntries()
                            .iterator().next();
                    enchantment = enchantmentEntry.getKey().value();
                    level = enchantmentEntry.getIntValue();
                }
            }
            if (data.getEnchantedData().containsKey(enchantment) && ebookcount > 0
                    && level == data.getEnchantedData().get(enchantment).getLevel()
                    && data.getEnchantedData().get(enchantment).getPrice() >= price) {
                MinecraftClient client = MinecraftClient.getInstance();

                Text enchant = enchantment.description().copy().append(ScreenTexts.SPACE)
                        .append(Text.translatable("enchantment.level." + level));
                // Notify the player
                client.player.sendMessage(
                        Text.translatable("info.autobooklib.found",
                                enchant, price),
                        false);
                client.player.sendMessage(
                        Text.translatable("info.autobooklib.done",
                                enchant, price),
                        false);
                client.player.playSound(
                        SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
                        1.0f,
                        1.0f
                );
                return new InitialState(data);
            } else {
                MinecraftClient client = MinecraftClient.getInstance();
                BlockPos blockPos = data.getLecternBlock().getBlockPos();
                BlockState state = client.world.getBlockState(blockPos);

                int bestSlot = AutoBookUtils.findBestUsableAxeSlot(
                    client.player.getInventory(),
                    state
                );

                int target = 8;

                if (bestSlot != -1) {
                    if (!AutoBookUtils.isInHotbar(bestSlot)) {
                        AutoBookUtils.placeInHotbar(bestSlot, target);
                        AutoBookUtils.setSelectedSlot(target);
                    } else {
                        AutoBookUtils.setSelectedSlot(bestSlot);
                    }
                }
                // se bestSlot == -1 → bate com a mão

                AutoBookUtils.mineBlock(blockPos);
                return new WaitingLecternState(data);
            }

        }
        return super.onPacket(packet, ci);
    }

}
