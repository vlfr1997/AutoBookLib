package net.vlfr1997.autobooklib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.LecternBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
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

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoBookLib implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	private static KeyBinding keyBinding;
	private static KeyBinding keyBinding2;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"Auto Book Librarian", // The translation key of the keybinding's name
			InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
			GLFW.GLFW_KEY_J, // The keycode of the key
			"Target Lectern/Villager" // The translation key of the keybinding's category.
		));

		keyBinding2 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"Open Gui", // The translation key of the keybinding's name
			InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
			GLFW.GLFW_KEY_K, // The keycode of the key
			"Auto Book Librarian UI" // The translation key of the keybinding's category.
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (AutoBookData.getInfo().getWorking() && !AutoBookData.getInfo().getDone() && (((VillagerEntity) client.world.getEntityById(AutoBookData.getInfo().getVillager().getId())).getVillagerData().getProfession() == VillagerProfession.LIBRARIAN)) {
				ClientPlayNetworking.getSender()
					.sendPacket(PlayerInteractEntityC2SPacket.interact(AutoBookData.getInfo().getVillager(),
					client.player.isSneaking(),
					Hand.MAIN_HAND));
				AutoBookData.getInfo().setDone(true);
			}

			while (keyBinding2.wasPressed()) {
				MinecraftClient.getInstance().setScreen(new CottonClientScreen(new AutoBookLibScreen()));
			}

			while (keyBinding.wasPressed()) {
				var target = client.crosshairTarget;
				if (!AutoBookData.getInfo().getWorking()) {
					if (target instanceof EntityHitResult) {
						if (((EntityHitResult) target).getEntity() instanceof VillagerEntity) {
							AutoBookData.getInfo().setVillager((VillagerEntity)((EntityHitResult) target).getEntity());
							client.player.sendMessage(Text.literal("Villager Targeted"), false);	
						}
					} else if (target instanceof BlockHitResult) {
						Block block = client.world.getBlockState(((BlockHitResult) target).getBlockPos()).getBlock();
						if (block instanceof LecternBlock) {
							AutoBookData.getInfo().setLecternBlock((BlockHitResult) target);
							client.player.sendMessage(Text.literal("Lectern Targeted"), false);	
						} else {
							if(AutoBookData.getInfo().getVillager() != null && AutoBookData.getInfo().getLecternBlock() != null){
								try {
									if (((VillagerEntity) client.world.getEntityById(AutoBookData.getInfo().getVillager().getId())).getVillagerData().getProfession() == VillagerProfession.LIBRARIAN) {
										if (AutoBookData.getInfo().getEnchantedData().isEmpty()) {
											client.player.sendMessage(Text.literal("Please select at least one enchant"), false);
											AutoBookData.getInfo().setWorking(false);
										} else {
											AutoBookData.getInfo().setWorking(true);
										}
									} else{
										client.player.sendMessage(Text.literal("Please target both librarian and lectern"), false);
										AutoBookData.getInfo().setWorking(false);
									}
								} catch (Exception e) {
									client.player.sendMessage(Text.literal("Please target both librarian and lectern"), false);
									AutoBookData.getInfo().setWorking(false);
								}
								
							} else {
								client.player.sendMessage(Text.literal("Please target both librarian and lectern"), false);
								AutoBookData.getInfo().setWorking(false);
							}
						}
					} else {
						AutoBookData.getInfo().setWorking(true);
					}
				} else {
					client.player.sendMessage(Text.literal("Stopped"), false);
					AutoBookData.getInfo().setWorking(false);
					AutoBookData.getInfo().setDone(false);
				}
			}
		});
	}
}