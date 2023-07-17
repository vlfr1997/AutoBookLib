package net.vlfr1997.autobooklib.gui;

import org.lwjgl.glfw.GLFW;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class AutoBookScreen extends CottonClientScreen {

    @Override
    public boolean keyPressed(int ch, int keyCode, int modifiers) {
        if (ch == GLFW.GLFW_KEY_K || ch == GLFW.GLFW_KEY_E) {
            this.close();
            return true;
        }
        return super.keyPressed(ch, keyCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public AutoBookScreen(GuiDescription description) {
        super(description);
    }

}