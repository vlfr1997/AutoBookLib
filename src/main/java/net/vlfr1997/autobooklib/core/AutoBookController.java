package net.vlfr1997.autobooklib.core;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.vlfr1997.autobooklib.data.AutoBookData;
import net.vlfr1997.autobooklib.data.EnchantedData;

public class AutoBookController {
    private static AutoBookController instance;
    private AutoBookState state;

    private AutoBookController(AutoBookState state) {
        this.state = state;
    }

    public static synchronized AutoBookController getInstance() {
        if (instance == null) {
            instance = new AutoBookController(new InitialState(new AutoBookData()));
        }
        return instance;
    }

    public void onTick(MinecraftClient client) {
        state = state.onTick(client);
    };

    public void onKey(MinecraftClient client) {
        state = state.onKey(client);
    }

    public void onPacket(Packet<ClientPlayPacketListener> packet, CallbackInfo ci) {
        state = state.onPacket(packet, ci);
    }

    public Map<Enchantment, EnchantedData> getEnchantedData() {
        return this.state.data.getEnchantedData();
    }

    public void onStop() {
        this.state.data.setEnchantedData(new HashMap<Enchantment, EnchantedData>());
        this.state.data.setLecternBlock(null);
        this.state.data.setVillager(null);
    }

}
