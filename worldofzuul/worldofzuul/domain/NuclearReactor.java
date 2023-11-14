package worldofzuul.domain;

public class NuclearReactor extends PowerPlant {
    private static long price = 3100000; // den er dyrteste

    public NuclearReactor() { // override constractor fra super klasse
        super(12, 1000, price);
        setDescription("This is a nuclear reactor. A nuclear reactor costs " + getPrice() + " coins " +
                "and produces " + getEnergyProduction() + " MW. The pollution is " + getPollution() + " tonCO2e/turn");
    }
    public static long getPrice() {
        return price;
    }

    @Override // fra superklasse
    public boolean upgrade() {  // Changes the power plant's values when you upgrade.
        if (getLevel() < PowerPlant.MAXLEVEL) {
            setLevel(getLevel() + 1);
            setPollution(getPollution()*0.75); // mindre
            setEnergyProduction(getEnergyProduction()*1.5); // stiger
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
