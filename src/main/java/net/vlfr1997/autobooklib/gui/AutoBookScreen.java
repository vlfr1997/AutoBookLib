package net.vlfr1997.autobooklib.gui;

import org.lwjgl.glfw.GLFW;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.input.KeyInput;

public class AutoBookScreen extends CottonClientScreen {

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.getKeycode() == GLFW.GLFW_KEY_K || input.getKeycode() == GLFW.GLFW_KEY_E) {
            this.close();
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public AutoBookScreen(GuiDescription description) {
        super(description);
    }

}