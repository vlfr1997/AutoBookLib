package net.vlfr1997.autobooklib.gui;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.vlfr1997.autobooklib.core.AutoBookController;
import net.vlfr1997.autobooklib.data.EnchantedData;
import net.vlfr1997.autobooklib.gui.components.EnchantmentList;

public class AutoBookGuiDescription extends LightweightGuiDescription {

    public AutoBookGuiDescription() {

        BiConsumer<Enchantment, EnchantmentList> configurator = (Enchantment enchantment,
                EnchantmentList destination) -> {
            Identifier id = EnchantmentHelper.getEnchantmentId(enchantment);

            var enchantmentData = AutoBookController.getInstance().getEnchantedData();
            destination.getButton().setLabel(Text.translatable(enchantment.getTranslationKey()));
            Boolean containsKey = enchantmentData.containsKey(id);
            destination.getButton().setToggle(containsKey);

            destination.getSliderLevel().setMaxValue(enchantment.getMaxLevel());
            destination.getSliderLevel().setValue(enchantment.getMaxLevel());
            destination.getSliderPrice().setValue(26);

            Consumer<Boolean> onToggle = value -> {
                if (!destination.getButton().getToggle()) {
                    enchantmentData.remove(id);
                } else {
                    enchantmentData.put(id, new EnchantedData(
                            destination.getSliderLevel().getValue(), destination.getSliderPrice().getValue()));
                }
            };
            destination.getButton().setOnToggle(onToggle);

            IntConsumer onSliderLevelChange = value -> {
                if (enchantmentData.containsKey(id)) {
                    enchantmentData.get(id).setLevel(value);
                }
            };
            destination.getSliderLevel().setValueChangeListener(onSliderLevelChange);

            IntConsumer onSliderPriceChange = value -> {
                if (enchantmentData.containsKey(id)) {
                    enchantmentData.get(id).setPrice(value);
                }
            };
            destination.getSliderPrice().setValueChangeListener(onSliderPriceChange);

            if (containsKey) {
                int level = enchantmentData.get(id).getLevel();
                destination.getSliderLevel().setValue(level);
                int price = enchantmentData.get(id).getPrice();
                destination.getSliderPrice().setValue(price);
            }

        };

        ArrayList<Enchantment> data = new ArrayList<>();

        for (Enchantment enchantment : Registries.ENCHANTMENT) {
            if (enchantment.isAvailableForEnchantedBookOffer()) {
                data.add(enchantment);
            }
        }

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel label = new WLabel(Text.translatable("gui.autobooklib.title"), 0xFFFFFF);
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(label, 0, 0, 16, 1);

        WListPanel<Enchantment, EnchantmentList> list = new WListPanel<>(data, EnchantmentList::new, configurator);
        list.setListItemHeight(2 * 18 + 9);

        root.add(list, 0, 2, 16, 12);

        root.validate(this);
    }
}