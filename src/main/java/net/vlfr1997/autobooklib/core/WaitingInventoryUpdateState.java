package net.vlfr1997.autobooklib.core;

import java.util.Arrays;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.vlfr1997.autobooklib.data.AutoBookData;
import net.vlfr1997.autobooklib.util.AutoBookUtils;

public class WaitingInventoryUpdateState extends AutoBookState {

    public WaitingInventoryUpdateState(AutoBookData data) {
        super(data);
    }

    @Override
    public AutoBookState onTick(MinecraftClient client) {
        int position = AutoBookUtils.getItemPosition(Arrays.asList(Items.LECTERN));
        int target = 0;

        if (position > -1) {
            if (!AutoBookUtils.isInHotbar(position)) {
                AutoBookUtils.placeInHotbar(position, target);
            } else {
                AutoBookUtils.setSelectedSlot(position);
            }
            AutoBookUtils.placeBlock(data.getLecternBlock());
            return new WaitingLibrarianState(data);
        }

        return super.onTick(client);
    }

}
