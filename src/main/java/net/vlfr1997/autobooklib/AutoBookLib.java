package net.vlfr1997.autobooklib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.vlfr1997.autobooklib.core.AutoBookBinds;
import net.vlfr1997.autobooklib.core.AutoBookController;
import net.vlfr1997.autobooklib.gui.AutoBookGuiDescription;
import net.vlfr1997.autobooklib.gui.AutoBookScreen;

public class AutoBookLib implements ModInitializer {
	private static KeyBinding keyBindingJ = AutoBookBinds.keyBindingJ;
	private static KeyBinding keyBindingK = AutoBookBinds.keyBindingK;

	@Override
	public void onInitialize() {
		AutoBookScreen autoBookLibScreen = new AutoBookScreen(new AutoBookGuiDescription());

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyBindingK.wasPressed()) {
				client.setScreen(autoBookLibScreen);
			}

			while (keyBindingJ.wasPressed()) {
				AutoBookController.getInstance().onKey(client);
			}

			AutoBookController.getInstance().onTick(client);
		});
	}
}