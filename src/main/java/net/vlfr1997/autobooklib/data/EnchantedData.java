package net.vlfr1997.autobooklib.data;

public class EnchantedData {
    private int level;
    private int price;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public EnchantedData(int level, int price) {
        this.level = level;
        this.price = price;
    }
}
