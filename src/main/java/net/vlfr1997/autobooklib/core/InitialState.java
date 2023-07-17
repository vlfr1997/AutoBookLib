package net.vlfr1997.autobooklib.core;

import net.minecraft.block.LecternBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.vlfr1997.autobooklib.data.AutoBookData;

public class InitialState extends AutoBookState {

    public InitialState(AutoBookData data) {
        super(data);
    }

    @Override
    public AutoBookState onKey(MinecraftClient client) {
        var target = client.crosshairTarget;

        if (target instanceof EntityHitResult) {
            VillagerEntity villager = (VillagerEntity) ((EntityHitResult) target).getEntity();
            data.setVillager(villager);
            client.player.sendMessage(Text.translatable("info.autobooklib.villagerTargeted"), false);
            return this;
        } else if (target instanceof BlockHitResult && client.world
                .getBlockState(((BlockHitResult) target).getBlockPos()).getBlock() instanceof LecternBlock) {
            data.setLecternBlock((BlockHitResult) target);
            client.player.sendMessage(Text.translatable("info.autobooklib.lecternTargeted"), false);
            return this;
        } else {
            if (data.getVillager() == null || data.getLecternBlock() == null || data.getEnchantedData().isEmpty()) {
                if (data.getEnchantedData().isEmpty()) {
                    client.player.sendMessage(
                            Text.translatable("error.autobooklib.noEnchant"), false);
                } else {
                    client.player.sendMessage(
                            Text.translatable("error.autobooklib.generic"), false);
                }
                return this;
            } else {
                return new WaitingLibrarianState(data);
            }
        }
    }
}