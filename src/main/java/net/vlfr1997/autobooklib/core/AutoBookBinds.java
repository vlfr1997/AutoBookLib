package net.vlfr1997.autobooklib.core;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class AutoBookBinds {
        private static Category modCategory =
			KeyBinding.Category.create(Identifier.of("autobooklib"));

        public static KeyBinding keyBindingJ = KeyBindingHelper.registerKeyBinding(
                        new KeyBinding("key.autobooklib.j",
                                        InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J,
                                        modCategory));
        public static KeyBinding keyBindingK = KeyBindingHelper.registerKeyBinding(
                        new KeyBinding("key.autobooklib.k",
                                        InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K,
                                        modCategory));
}