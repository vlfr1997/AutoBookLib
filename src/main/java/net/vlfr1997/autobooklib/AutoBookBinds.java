package net.vlfr1997.autobooklib;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class AutoBookBinds {
    public static KeyBinding keyBindingJ = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.autobooklib.j",
                    InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J,
                    "key.category.autobooklib"));
    public static KeyBinding keyBindingK = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.autobooklib.k",
                    InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K,
                    "key.category.autobooklib"));
}
