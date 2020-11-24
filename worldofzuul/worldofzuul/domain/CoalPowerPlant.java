package worldofzuul.domain;

public class CoalPowerPlant extends PowerPlant {
    private static long price = 450000;

    public CoalPowerPlant() {
        super(492000, 600, price);
        setDescription("This is a coal power plant. A coal power plant costs " + getPrice() + " coins " +
                "and produces " + getEnergyProduction() + " MW. The pollution is " + getPollution() + " kgCO2e/turn");
    }

    public static long getPrice() {
        return price;
    }

    @Override
    public boolean upgrade() { // Changes the power plant's values when you upgrade.
        if (getLevel() < PowerPlant.MAXLEVEL) {
            setLevel(getLevel() + 1);
            setPollution(getPollution()*1.5);
            setEnergyProduction(getEnergyProduction()*1.5);
            return true;
        } else {
            return false;
        }
    }
    @Override
    public String toString() {
        return "Coal power plant (lvl: " + getLevel() + ")";
    }
}
