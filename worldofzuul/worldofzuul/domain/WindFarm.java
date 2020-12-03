package worldofzuul.domain;

public class WindFarm extends PowerPlant {
    private static long price = 1240000;

    public WindFarm() {
        super(4400, 400, price);
        setDescription("This is a wind farm consisting of 100 wind turbines. A wind farm costs " + getPrice() +
                " coins and produces " + getEnergyProduction() + " MW. The pollution is " + getPollution() + " kgCO2e/turn");
    }
    public static long getPrice() {
        return price;
    }

    public static void setPrice(long newPrice) {price = newPrice;}

    @Override
    public boolean upgrade() { // Changes the power plant's values when you upgrade.
        if (getLevel() < PowerPlant.MAXLEVEL) {
            setLevel(getLevel() + 1);
            setPollution(getPollution()*0.75);
            setEnergyProduction(getEnergyProduction()*1.5);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "WindFarm (lvl: " + getLevel() + ")";
    }
}
