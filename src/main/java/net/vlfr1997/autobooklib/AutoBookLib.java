package net.vlfr1997.autobooklib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.LecternBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.argument.EnchantmentArgumentType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.village.VillagerProfession;
import net.vlfr1997.autobooklib.data.AutoBookData;
import net.minecraft.enchantment.Enchantment;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoBookLib implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	private static KeyBinding keyBinding;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"Auto Book Librarian", // The translation key of the keybinding's name
			InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
			GLFW.GLFW_KEY_J, // The keycode of the key
			"Auto Book Librarian UI" // The translation key of the keybinding's category.
		));
		
		ClientCommandManager.DISPATCHER.register(
			ClientCommandManager.literal("abl")
			.then(ClientCommandManager.argument("enchantment", EnchantmentArgumentType.enchantment())
			.then(ClientCommandManager.argument("level", IntegerArgumentType.integer(0))
			.executes(context -> {
				AutoBookData.getInfo().setTargetId(EnchantmentHelper.getEnchantmentId((Enchantment)context.getArgument("enchantment", Enchantment.class)));
				AutoBookData.getInfo().setTargetLevel(IntegerArgumentType.getInteger(context, "level"));
				MinecraftClient instance = MinecraftClient.getInstance();
				instance.player.sendMessage(new LiteralText("Enchant Targeted: " + AutoBookData.getInfo().getTargetId().toString() + AutoBookData.getInfo().getTargetLevel()), false);	
				return 1;
			})))
		);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (AutoBookData.getInfo().getWorking() && !AutoBookData.getInfo().getDone() && (((VillagerEntity) client.world.getEntityById(AutoBookData.getInfo().getVillager().getId())).getVillagerData().getProfession() == VillagerProfession.LIBRARIAN)) {
				ClientPlayNetworking.getSender()
					.sendPacket(PlayerInteractEntityC2SPacket.interact(AutoBookData.getInfo().getVillager(),
					client.player.isSneaking(),
					Hand.MAIN_HAND));
				AutoBookData.getInfo().setDone(true);
			}

			while (keyBinding.wasPressed()) {
				var target = client.crosshairTarget;
				if (!AutoBookData.getInfo().getWorking()) {
					if (target instanceof EntityHitResult) {
						if (((EntityHitResult) target).getEntity() instanceof VillagerEntity) {
							AutoBookData.getInfo().setVillager((VillagerEntity)((EntityHitResult) target).getEntity());
							client.player.sendMessage(new LiteralText("Villager Targeted"), false);	
						}
					} else if (target instanceof BlockHitResult) {
						Block block = client.world.getBlockState(((BlockHitResult) target).getBlockPos()).getBlock();
						if (block instanceof LecternBlock) {
							AutoBookData.getInfo().setLecternBlock((BlockHitResult) target);
							client.player.sendMessage(new LiteralText("Lectern Targeted"), false);	
						} else {
							if(AutoBookData.getInfo().getVillager() != null && AutoBookData.getInfo().getLecternBlock() != null){
								try {
									if (((VillagerEntity) client.world.getEntityById(AutoBookData.getInfo().getVillager().getId())).getVillagerData().getProfession() == VillagerProfession.LIBRARIAN) {
										AutoBookData.getInfo().setWorking(true);
									} else{
										client.player.sendMessage(new LiteralText("Please target both librarian and lectern"), false);
										AutoBookData.getInfo().setWorking(false);
									}
								} catch (Exception e) {
									client.player.sendMessage(new LiteralText("Please target both librarian and lectern"), false);
									AutoBookData.getInfo().setWorking(false);
								}
								
							} else {
								client.player.sendMessage(new LiteralText("Please target both librarian and lectern"), false);
								AutoBookData.getInfo().setWorking(false);
							}
						}
					} else {
						AutoBookData.getInfo().setWorking(true);
					}
				} else {
					client.player.sendMessage(new LiteralText("Stopped"), false);
					AutoBookData.getInfo().setWorking(false);
					AutoBookData.getInfo().setDone(false);
				}
			}
		});
	}
}
