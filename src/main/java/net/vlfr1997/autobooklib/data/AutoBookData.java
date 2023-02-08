package net.vlfr1997.autobooklib.data;

import java.util.List;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.village.TradeOffer;

public class AutoBookData {
    private static AutoBookData info;
    private VillagerEntity villager;
	private List<TradeOffer> offers;
    private Boolean HudIsOpen = false;
    private BlockHitResult lecternBlock;
    private int targetLevel = 1;
    private Identifier targetId = new Identifier("minecraft:mending");
    private Boolean working = false;
    private Boolean done = false;

    public static AutoBookData getInfo() {
		if (info == null)
			info = new AutoBookData();
        return info;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Boolean getWorking() {
        return working;
    }

    public void setWorking(Boolean working) {
        this.working = working;
    }

    public Identifier getTargetId() {
        return targetId;
    }

    public void setTargetId(Identifier targetId) {
        this.targetId = targetId;
    }

    public int getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(int targetLevel) {
        this.targetLevel = targetLevel;
    }

    public BlockHitResult getLecternBlock() {
        return lecternBlock;
    }

    public void setLecternBlock(BlockHitResult lecternBlock) {
        this.lecternBlock = lecternBlock;
    }

    public void setOffers(List<TradeOffer> list){
		this.offers = list;
	};

    public List<TradeOffer> getOffers(){	
        if (offers != null) {
            return offers;
        } else {
            return null;
        }
	};
    
    public void setVillager(VillagerEntity villagerEntity){		
		this.villager = villagerEntity;
	};

    public VillagerEntity getVillager(){		
		return villager;
	};

    public void setHud(Boolean HudIsOpen){		
		this.HudIsOpen = HudIsOpen;
	};

    public Boolean getHud(){		
		return HudIsOpen;
	};
}