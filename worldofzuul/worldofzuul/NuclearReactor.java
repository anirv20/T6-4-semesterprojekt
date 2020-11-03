package worldofzuul;

public class NuclearReactor extends PowerPlant {
    private static long price = 3100000;

    public NuclearReactor() {
        super(12000, 1000);
        setDescription("This is a nuclear reactor. A nuclear reactor costs " + getPrice() + " coins " +
                "and produces " + getEnergyProduction() + " MW. The pollution is " + getPollution() + " kgCO2e/turn");
    }
    public static long getPrice() {
        return price;
    }

    @Override
    public void upgrade() {
        if (getLevel() < PowerPlant.MAXLEVEL) {
            setLevel(getLevel() + 1);
            setPollution(getPollution()-2);
            setEnergyProduction(getEnergyProduction()+10);
        }
    }
    @Override
    public String toString() {
        return "Nuclear reactor (lvl: " + getLevel() + ")";
    }
}
