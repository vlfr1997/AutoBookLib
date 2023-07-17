package net.vlfr1997.autobooklib.data;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;

public class AutoBookData {
    private VillagerEntity villager;
    private BlockHitResult lecternBlock;
    private Map<Identifier, EnchantedData> eData = new HashMap<Identifier, EnchantedData>();

    public BlockHitResult getLecternBlock() {
        return lecternBlock;
    }

    public void setLecternBlock(BlockHitResult lecternBlock) {
        this.lecternBlock = lecternBlock;
    }

    public void setVillager(VillagerEntity villagerEntity) {
        this.villager = villagerEntity;
    };

    public VillagerEntity getVillager() {
        return villager;
    };

    public Map<Identifier, EnchantedData> getEnchantedData() {
        return eData;
    }

    public void setEnchantedData(Map<Identifier, EnchantedData> eData) {
        this.eData = eData;
    }
}