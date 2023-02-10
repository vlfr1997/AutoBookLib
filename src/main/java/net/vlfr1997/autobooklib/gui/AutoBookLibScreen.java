package net.vlfr1997.autobooklib.gui;

import java.lang.reflect.Field;
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
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.vlfr1997.autobooklib.data.AutoBookData;
import net.vlfr1997.autobooklib.data.EnchantList;
import net.vlfr1997.autobooklib.data.EnchantedData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoBookLibScreen extends LightweightGuiDescription {
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

    public AutoBookLibScreen() {
        BiConsumer<String, EnchantList> configurator = (String s, EnchantList destination) -> {
            Identifier id = new Identifier("minecraft:"+s);

            destination.getButton().setLabel(Text.literal(s));
            Boolean containsKey = AutoBookData.getInfo().getEnchantedData().containsKey(id);
            destination.getButton().setToggle(containsKey);
            Consumer<Boolean> onToggle = value -> {
                if (!destination.getButton().getToggle()) {
                    AutoBookData.getInfo().getEnchantedData().remove(id);
                } else {
                    AutoBookData.getInfo().getEnchantedData().put(id, new EnchantedData(destination.getSliderLevel().getValue(), destination.getSliderPrice().getValue()));
                }
            };
            destination.getButton().setOnToggle(onToggle);

            IntConsumer onSliderLevelChange = value -> {
                if (AutoBookData.getInfo().getEnchantedData().containsKey(id)) {
                    AutoBookData.getInfo().getEnchantedData().get(id).setLevel(value);
                }
            };
            destination.getSliderLevel().setValueChangeListener(onSliderLevelChange);

            IntConsumer onSliderPriceChange = value -> {
                if (AutoBookData.getInfo().getEnchantedData().containsKey(id)) {
                    AutoBookData.getInfo().getEnchantedData().get(id).setPrice(value);   
                }
            };
            destination.getSliderPrice().setValueChangeListener(onSliderPriceChange);

            if(containsKey){
                int level = AutoBookData.getInfo().getEnchantedData().get(id).getLevel();
                destination.getSliderLevel().setValue(level);
                int price = AutoBookData.getInfo().getEnchantedData().get(id).getPrice();
                destination.getSliderPrice().setValue(price);
            }

        };
        
        ArrayList<String> data = new ArrayList<>();

        for (Identifier id : Registries.ENCHANTMENT.getIds()) {
            String name = id.toShortTranslationKey();
            switch (name) {
                //Not possible to get on vanilla.
                case "swift_sneak":                    
                    break;
                case "soul_speed":                    
                    break;
                default:
                    data.add(name);
            }
        }


        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(256, 240);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel label = new WLabel(Text.literal("AutoBookLib GUI"), 0xFFFFFF);
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(label, 0, 0, 16, 1);    
        
        WListPanel<String, EnchantList> list = new WListPanel<>(data, EnchantList::new, configurator);
        list.setListItemHeight(2*18+9);
        root.add(list, 0, 2, 16, 12);

        root.validate(this);
    }
}