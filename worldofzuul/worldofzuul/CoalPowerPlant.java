package worldofzuul;

public class CoalPowerPlant extends PowerPlant {
    private static long price = 450000;

    public CoalPowerPlant() {
        super(492000, 600);
        setDescription("This is a coal power plant. A coal power plant costs " + getPrice() + " coins " +
                "and produces " + getEnergyProduction() + " MW. The pollution is " + getPollution() + " kgCO2e/turn");
    }

    public static long getPrice() {
        return price;
    }

    @Override
    public void upgrade() {
        if (getLevel() < PowerPlant.MAXLEVEL) {
            setLevel(getLevel() + 1);
            setPollution(getPollution()+10);
            setEnergyProduction(getEnergyProduction()+10);
        }
    }
    @Override
    public String toString() {
        return "Coal power plant (lvl: " + getLevel() + ")";
    }
}
