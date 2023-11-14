package worldofzuul.domain;

public class CoalPowerPlant extends PowerPlant {
    private static long price = 450000; // den er billigere
    // override constractor fra super klasse
    public CoalPowerPlant() { // den producerer for meget pollution, mellem enrgei
        super(492, 600, price); // override constractor fra super klasse
        setDescription("This is a coal power plant. A coal power plant costs " + getPrice() + " coins " +
                "and produces " + getEnergyProduction() + " MW. The pollution is " + getPollution() + " tonCO2e/turn");
    }

    public static long getPrice() {
        return price;
    }

    @Override// fra superklassse
    public boolean upgrade() { // Changes the power plant's values when you upgrade.
        if (getLevel() < PowerPlant.MAXLEVEL) {
            setLevel(getLevel() + 1);
            setPollution(getPollution()*1.5); // stiger
            setEnergyProduction(getEnergyProduction()*1.5); // stiger
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
