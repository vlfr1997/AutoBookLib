package net.vlfr1997.autobooklib.data;


import java.util.function.Supplier;

import io.github.cottonmc.cotton.gui.widget.WDynamicLabel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WSlider;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import net.minecraft.text.Text;

public class EnchantList extends WPlainPanel {
    private WToggleButton button;
    private WSlider sliderLevel;
    private WSlider sliderPrice;
    private Supplier<String> supplierPrice;
    private Supplier<String> supplierLevel;
    
    public WToggleButton getButton() {
        return button;
    }

    public void setButton(WToggleButton button) {
        this.button = button;
    }

    public WSlider getSliderLevel() {
        return sliderLevel;
    }

    public void setSliderLevel(WSlider sliderLevel) {
        this.sliderLevel = sliderLevel;
    }

    public WSlider getSliderPrice() {
        return sliderPrice;
    }

    public void setSliderPrice(WSlider sliderPrice) {
        this.sliderPrice = sliderPrice;
    }

    public EnchantList() {        
        setButton(new WToggleButton(Text.literal("MENDING")));
        this.add(getButton(), 18, 0, 14*18, 18);

        
        this.add(new WLabel(Text.literal("Level: ")), 18, 18+1, height, height);        
        setSliderLevel(new WSlider(1, 5, Axis.HORIZONTAL));
        this.add(getSliderLevel(), 5*18-4, 18-4, 10*18, 18);
        supplierLevel = () -> {
            return "" + sliderLevel.getValue();
        };
        WDynamicLabel labelLevel = new WDynamicLabel(supplierLevel);
        this.add(labelLevel, 4*18, 18+1, 4, 4);


        this.add(new WLabel(Text.literal("Max price: ")), 18, 2*18-5, height, height);
        setSliderPrice(new WSlider(1, 64, Axis.HORIZONTAL));      
        this.add(getSliderPrice(), 5*18-4, 18+8, 10*18, 18);
        supplierPrice = () -> {
            return "" + sliderPrice.getValue();
        };
        WDynamicLabel labelPrice = new WDynamicLabel(supplierPrice);
        this.add(labelPrice, 4*18, 2*18-5, 4, 4);



        this.setSize(7*18, 2*18);
    }
}