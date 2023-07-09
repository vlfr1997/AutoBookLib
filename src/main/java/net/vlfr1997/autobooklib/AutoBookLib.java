package net.vlfr1997.autobooklib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.LecternBlock;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.village.VillagerProfession;
import net.vlfr1997.autobooklib.data.AutoBookData;
import net.vlfr1997.autobooklib.gui.AutoBookLibScreen;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoBookLib implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	private static KeyBinding keyBindingJ = AutoBookBinds.keyBindingJ;
	private static KeyBinding keyBindingK = AutoBookBinds.keyBindingK;

	@Override
	public void onInitialize() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {

			if (AutoBookData.getInstance().getWorking() && !AutoBookData.getInstance().getDone()
					&& (((VillagerEntity) client.world.getEntityById(AutoBookData.getInstance().getVillager().getId()))
							.getVillagerData().getProfession() == VillagerProfession.LIBRARIAN)) {
				ClientPlayNetworking.getSender()
						.sendPacket(PlayerInteractEntityC2SPacket.interact(AutoBookData.getInstance().getVillager(),
								client.player.isSneaking(),
								Hand.MAIN_HAND));
				AutoBookData.getInstance().setDone(true);
			}

			while (keyBindingK.wasPressed()) {
				client.setScreen(new CottonClientScreen(new AutoBookLibScreen()));
			}

			while (keyBindingJ.wasPressed()) {
				var target = client.crosshairTarget;
				if (!AutoBookData.getInstance().getWorking()) {
					if (target instanceof EntityHitResult) {
						if (((EntityHitResult) target).getEntity() instanceof VillagerEntity) {
							AutoBookData.getInstance()
									.setVillager((VillagerEntity) ((EntityHitResult) target).getEntity());
							client.player.sendMessage(Text.translatable("info.autobooklib.villagerTargeted"), false);
						}
					} else if (target instanceof BlockHitResult) {
						Block block = client.world.getBlockState(((BlockHitResult) target).getBlockPos()).getBlock();
						if (block instanceof LecternBlock) {
							AutoBookData.getInstance().setLecternBlock((BlockHitResult) target);
							client.player.sendMessage(Text.translatable("info.autobooklib.lecternTargeted"), false);
						} else {
							if (AutoBookData.getInstance().getVillager() != null
									&& AutoBookData.getInstance().getLecternBlock() != null) {
								try {
									if (((VillagerEntity) client.world
											.getEntityById(AutoBookData.getInstance().getVillager().getId()))
											.getVillagerData().getProfession() == VillagerProfession.LIBRARIAN) {
										if (AutoBookData.getInstance().getEnchantedData().isEmpty()) {
											client.player.sendMessage(
													Text.translatable("error.autobooklib.noEnchant"), false);
											AutoBookData.getInstance().setWorking(false);
										} else {
											AutoBookData.getInstance().setWorking(true);
										}
									} else {
										client.player.sendMessage(
												Text.translatable("error.autobooklib.generic"), false);
										AutoBookData.getInstance().setWorking(false);
									}
								} catch (Exception e) {
									client.player.sendMessage(Text.translatable("error.autobooklib.generic"),
											false);
									AutoBookData.getInstance().setWorking(false);
								}

							} else {
								client.player.sendMessage(Text.translatable("error.autobooklib.generic"),
										false);
								AutoBookData.getInstance().setWorking(false);
							}
						}
					} else {
						AutoBookData.getInstance().setWorking(true);
					}
				} else {
					client.player.sendMessage(Text.translatable("info.autobooklib.stopped"), false);
					AutoBookData.getInstance().setWorking(false);
					AutoBookData.getInstance().setDone(false);
				}
			}
		});
	}
}