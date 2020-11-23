package worldofzuul.domain;

public class NuclearReactor extends PowerPlant {
    private static long price = 3100000;

    public NuclearReactor() {
        super(12000, 1000, price);
        setDescription("This is a nuclear reactor. A nuclear reactor costs " + getPrice() + " coins " +
                "and produces " + getEnergyProduction() + " MW. The pollution is " + getPollution() + " kgCO2e/turn");
    }
    public static long getPrice() {
        return price;
    }

    @Override
    public boolean upgrade() {  // Changes the power plant's values when you upgrade.
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
        return "Nuclear reactor (lvl: " + getLevel() + ")";
    }
}
